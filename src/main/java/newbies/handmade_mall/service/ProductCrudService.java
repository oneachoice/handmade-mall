package newbies.handmade_mall.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.common.ProductImageType;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.dto.req.ProductUpdateDto;
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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
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

        List<Product> product = productRepository.findAll();


        List<ProductListViewDto> productListViewDtos = new ArrayList<>();

        for (Product value : product) {
            ProductImage productImage = null;
            productImage = productImageRepository.findByProductIdAndProductImageType(value.getId(), ProductImageType.MAIN);

            ProductListViewDto productListViewDto = ProductListViewDto.builder()
                    .productId(value.getId())
                    .productCode(value.getProductCode())
                    .count(value.getCount())
                    .category(value.getCategory().toString())
                    .createdAt(value.getCreatedAt())
                    .discountPrice(value.getDiscountPrice())
                    .discountRate(value.getDiscountRate())
                    .mainImagePath(productImageManager.createImageUrl(productImage.getUuid() + productImage.getFileExtension()))
                    .productName(value.getProductName())
                    .margin(value.getMargin())
                    .marginRate(value.getMarginRate())
                    .sellingPrice(value.getSellingPrice())
                    .build();
            productListViewDtos.add(productListViewDto);
        }

        return productListViewDtos;
    }

    /**
     * @return 페이지 설정 (페이지 사이즈, 정렬순서, 페이지에 들어갈 항목)
     */
    public Page<ProductListViewDto> getPagedProductList(int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("createdAt")));
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> {
            ProductImage productImage = productImageRepository.findByProductIdAndProductImageType(product.getId(), ProductImageType.MAIN);

            return ProductListViewDto.builder()
                    .productId(product.getId())
                    .productCode(product.getProductCode())
                    .category(product.getCategory().toString())
                    .productName(product.getProductName())
                    .sellingPrice(product.getSellingPrice())
                    .discountPrice(product.getDiscountPrice())
                    .discountRate(product.getDiscountRate())
                    .count(product.getCount())
                    .margin(product.getMargin())
                    .marginRate(product.getMarginRate())
                    .createdAt(product.getCreatedAt())
                    .mainImagePath(productImageManager.createImageUrl(
                            productImage.getUuid() + productImage.getFileExtension()))
                    .build();
        });
    }

    /**
     * DB에 등록되어 있는 상품별 이미지 정보를 수정 화면에 나타내기 위한 메서드
     * @return 상품 기존 이미지 정보 DTO
     */
    public ProductUpdateImageViewDto viewImage(Long productId) {

        //제품 FK로 찾은 리스트
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);

        ProductImage productImagesMain = null; //메인이미지 정보 담을 그릇 초기화
        List<ProductImage> productCardImages = new ArrayList<>(); //카드 이미지 리스트 생성
        List<ProductImage> productDescriptionImages = new ArrayList<>(); //설명 이미지 리스트 생성

        //이미지 타입에 따른 분류
        try {
            for (ProductImage productImage : productImages) {
                if (productImage.getProductImageType() == ProductImageType.MAIN) { //메인 이미지
                    productImagesMain = productImage; //그릇에 넣기
                } else if (productImage.getProductImageType() == ProductImageType.CARD) { //카드 이미지
                    productCardImages.add(productImage); //리스트에 추가
                } else if (productImage.getProductImageType() == ProductImageType.DESC) { //설명 이미지
                    productDescriptionImages.add(productImage); //리스트에 추가
                }
            }
        } catch (NullPointerException e) {
            log.error("productImage is null");
        }

        //모델에 담을 제품 기존 상품 이미지 정보를 dto에 담아 보내줌
        return ProductUpdateImageViewDto.builder()
                .mainProductImage(productImagesMain) //메인이미지
                .productCardImages(productCardImages) //카드이미지
                .productDescriptionImages(productDescriptionImages) //설명이미지
                .build();
    }

    /**
     * DB에 등록되어 있는 상품별 정보(이미지 외)를 수정 화면에 나타내기 위한 메서드
     * @return 상품 기존 정보 DTO
     */
    public ProductUpdateDto view(Long productId) {

        //상품 PK로 찾은 상품 객체
        Product product = productRepository.findById(productId).orElse(null);

        //모델에 담을 기존 상품 정보를 dto에 담아 보내줌
        return ProductUpdateDto.builder()
                .productId(product.getId()) //기존 상품을 수정하므로 id값을 보내줌
                .productName(product.getProductName()) //제품명
                .sellingPrice(product.getSellingPrice()) //판매가
                .costPrice(product.getCostPrice()) //원가
                .discountedPrice(product.getDiscountPrice()) //할인가
                .discountRate(product.getDiscountRate()) //할인율
                .margin(product.getMargin()) //마진
                .marginRate(product.getMarginRate()) //마진율
                .shippingFee(product.getShippingFee()) //배송비
                .count(product.getCount()) //재고 수량
                .category(String.valueOf(product.getCategory())) //카테고리
                .build();
    }

    /**
     * 상품 업데이트 사항을 DB에 저장하기 위한 메서드
     */
    public void update(ProductUpdateDto productUpdateDto) {

        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(false);

        if (httpSession == null) throw new RuntimeException("파트너 로그인이 안돼있음");

        Partner aPartner = partnerReadService.getByAccountId(httpSession.getAttribute("partnerId").toString());

        //상품 수정사항 저장
        Product updateProduct = productRepository.save(getProductOf(productUpdateDto, aPartner));

        //상품 이미지 수정사항 저장
        ProductImage mainProductImage = productImageCrudService.update(productUpdateDto, updateProduct);

        //상품 엔티티에서 메인 이미지 컬럼 수정
        updateProduct.setProductImage(mainProductImage);

    }

    //엔티티로 변환
    private Product getProductOf(ProductUpdateDto productUpdateDto, Partner partner) {

        AbstractButton productCreateDto;
        return Product.builder()
                .partner(partner)
                .id(productUpdateDto.getProductId())
                .productName(productUpdateDto.getProductName())
                .sellingPrice(productUpdateDto.getSellingPrice())
                .costPrice(productUpdateDto.getCostPrice())
                .discountPrice(productUpdateDto.getDiscountedPrice())
                .discountRate(productUpdateDto.getDiscountRate())
                .margin(productUpdateDto.getMargin())
                .marginRate(productUpdateDto.getMarginRate())
                .shippingFee(productUpdateDto.getShippingFee())
                .count(productUpdateDto.getCount())
                .category(getProductCategoryOf(productUpdateDto.getCategory()))
                .build();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("찾을 수 없음 ID = " + id));

    }

    /**
     *
     * @param id soft delete 할 id
     */
    @Transactional
    public void delete (Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("찾을 수 없음 ID = " + id));

//        product.setPartner(null);

        productRepository.delete(product);
    }


    public void create(ProductCreateDto productCreateDto) {

        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(false);

        if (httpSession == null) throw new RuntimeException("파트너 로그인이 안돼있음");

        Partner aPartner = partnerReadService.getByAccountId(httpSession.getAttribute("partnerId").toString());

        Product savedProduct = productRepository.save(getProductOf(productCreateDto, aPartner));

        ProductImage mainProductImage = productImageCrudService.create(productCreateDto, savedProduct);

        savedProduct.setProductImage(mainProductImage);
    }

    private Product getProductOf(ProductCreateDto productCreateDto, Partner partner) {

        return Product.builder()
                .partner(partner)
                .productName(productCreateDto.getProductName())
                .sellingPrice(productCreateDto.getSellingPrice())
                .costPrice(productCreateDto.getCostPrice())
                .discountPrice(productCreateDto.getDiscountedPrice())
                .discountRate(productCreateDto.getDiscountRate())
                .margin(productCreateDto.getMargin())
                .marginRate(productCreateDto.getMarginRate())
                .shippingFee(productCreateDto.getShippingFee())
                .count(productCreateDto.getCount())
                .category(getProductCategoryOf(productCreateDto.getCategory()))
                .build();
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
}
