package newbies.handmade_mall.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.req.ProductDto;
import newbies.handmade_mall.dto.res.ProductListViewDto;
import newbies.handmade_mall.dto.res.ProductUpdateImageViewDto;
import newbies.handmade_mall.entity.Partner;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductImageRepository;
import newbies.handmade_mall.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCrudService {

    private final ProductRepository productRepository;

    private final ProductImageCrudService productImageCrudService;

    private final PartnerReadService partnerReadService;

    private final ProductImageRepository productImageRepository;

    private final ProductImageManager productImageManager;


    /**
     * @return 뷰페이지에 th:each 문으로 보여줄 상품 리스트
     */
    public List<ProductListViewDto> viewDto() {

        List<Product> products = productRepository.findAll();

        // 각각의 Product를 ProductListViewDto로 변환
        return products.stream().map(this::getProductListViewDtoOf).toList();
    }

    /**
     * @return 페이지 설정 (페이지 사이즈, 정렬순서, 페이지에 들어갈 항목)
     */
    public Page<ProductListViewDto> getPagedProductList(int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("createdAt")));

        Page<Product> productPage = productRepository.findAll(pageable);

        // 각각의 Product를 ProductListViewDto로 변환
        return productPage.map(this::getProductListViewDtoOf);
    }


    /**
     * DB에 등록되어 있는 상품별 이미지 정보를 수정 화면에 나타내기 위한 메서드
     *
     * @return 상품 기존 이미지 정보 DTO
     */
    public ProductUpdateImageViewDto viewImage(Long productId) {

        //제품 FK로 찾은 리스트
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);

        ProductImage productImagesMain = null; //메인이미지 정보 담을 그릇 초기화

        List<ProductImage> productCardImages = new ArrayList<>(); //카드 이미지 리스트 생성
        List<ProductImage> productDescriptionImages = new ArrayList<>(); //설명 이미지 리스트 생성

        //이미지 타입에 따른 분류
        for (ProductImage productImage : productImages) {
            switch (productImage.getProductImageType()) {
                case MAIN -> productImagesMain = productImage;
                case CARD -> productCardImages.add(productImage);
                case DESC -> productDescriptionImages.add(productImage);
            }
        }

        // DTO 생성
        ProductUpdateImageViewDto productUpdateImageViewDto = new ProductUpdateImageViewDto();

        productUpdateImageViewDto.setMainProductImage(productImagesMain);
        productUpdateImageViewDto.setProductCardImages(productCardImages);
        productUpdateImageViewDto.setProductDescriptionImages(productDescriptionImages);

        //모델에 담을 제품 기존 상품 이미지 정보를 dto에 담아 보내줌
        return productUpdateImageViewDto;
    }


    /**
     * DB에 등록되어 있는 상품별 정보(이미지 외)를 수정 화면에 나타내기 위한 메서드
     *
     * @return 상품 기존 정보 DTO
     */
    public ProductDto view(Long productId) {

        //상품 PK로 찾은 상품 객체
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("상품이 존재하지 않음"));

        return getProductDtoOf(product);
    }

    /**
     * 상품 업데이트 사항을 DB에 저장하기 위한 메서드
     */
    public void update(ProductDto productDto) {

        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(false);

        if (httpSession == null) throw new RuntimeException("파트너 로그인이 안돼있음");

        Partner aPartner = partnerReadService.getByAccountId(httpSession.getAttribute("partnerId").toString());

        //상품 수정사항 저장
        Product updateProduct = productRepository.save(getProductOf(productDto, aPartner));

        //상품 이미지 수정사항 저장
        ProductImage mainProductImage = productImageCrudService.update(productDto, updateProduct);

        //상품 엔티티에서 메인 이미지 컬럼 수정
        updateProduct.setProductImage(mainProductImage);

    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없음 ID = " + id));

    }

    /**
     * @param id soft delete 할 id
     */
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없음 ID = " + id));

//        product.setPartner(null);

        productRepository.delete(product);
    }


    public void create(ProductDto productDto) {

        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(false);

        if (httpSession == null) throw new RuntimeException("파트너 로그인이 안돼있음");

        Partner aPartner = partnerReadService.getByAccountId(httpSession.getAttribute("partnerId").toString());

        Product savedProduct = productRepository.save(getProductOf(productDto, aPartner));

        ProductImage mainProductImage = productImageCrudService.create(productDto, savedProduct);

        savedProduct.setProductImage(mainProductImage);
    }


    /**
     * 문자열을 알 맞은 카테고리로 변환합니다.
     *
     * @param category 카테고리 문자열
     */
    private ProductCategory getProductCategoryOf(String category) {
        try {
            return ProductCategory.valueOf(category);
        } catch (IllegalArgumentException ex) {
            return ProductCategory.GENERAL;
        }
    }

    /**
     * From ProductDto to Product using Partner
     */
    private Product getProductOf(ProductDto productDto, Partner partner) {

        Product product = new Product();

        product.setPartner(partner);
        product.setId(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setCostPrice(productDto.getCostPrice());
        product.setDiscountPrice(productDto.getDiscountedPrice());
        product.setDiscountRate(productDto.getDiscountRate());
        product.setMargin(productDto.getMargin());
        product.setMarginRate(productDto.getMarginRate());
        product.setShippingFee(productDto.getShippingFee());
        product.setCount(productDto.getCount());
        product.setCategory(getProductCategoryOf(productDto.getCategory()));

        return product;

    }

    /**
     * From Product to ProductDto
     */
    private ProductDto getProductDtoOf(Product product) {

        ProductDto productDto = new ProductDto();

        productDto.setProductId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setSellingPrice(product.getSellingPrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setDiscountedPrice(product.getDiscountPrice());
        productDto.setDiscountRate(product.getDiscountRate());
        productDto.setMargin(product.getMargin());
        productDto.setMarginRate(product.getMarginRate());
        productDto.setShippingFee(product.getShippingFee());
        productDto.setCategory(String.valueOf(product.getCategory()));
        productDto.setCount(product.getCount());

        return productDto;
    }

    /**
     * From Product to ProductListViewDto
     */
    private ProductListViewDto getProductListViewDtoOf(Product product) {

        ProductImage productMainImage = product.getProductImage();

        // ProductListViewDto 생성
        ProductListViewDto productListViewDto = new ProductListViewDto();

        String productMainImagePath = null;

        // 이미지가 존재하지 않으면 이미지 추가 안함
        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getUuid() + productMainImage.getFileExtension());
        }

        // 상품 메인 이미지 경로 생성

        // ProductListViewDto 데이터 주입
        productListViewDto.setProductId(product.getId());
        productListViewDto.setProductCode(product.getProductCode());
        productListViewDto.setCount(product.getCount());
        productListViewDto.setCategory(product.getCategory().toString());
        productListViewDto.setCreatedAt(product.getCreatedAt());
        productListViewDto.setDiscountPrice(product.getDiscountPrice());
        productListViewDto.setDiscountRate(product.getDiscountRate());
        productListViewDto.setMainImagePath(productMainImagePath);
        productListViewDto.setProductName(product.getProductName());
        productListViewDto.setMargin(product.getMargin());
        productListViewDto.setMarginRate(product.getMarginRate());
        productListViewDto.setSellingPrice(product.getSellingPrice());

        return productListViewDto;
    }
}
