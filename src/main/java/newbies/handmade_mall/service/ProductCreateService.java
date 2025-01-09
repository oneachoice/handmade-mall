package newbies.handmade_mall.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.entity.Partner;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCreateService {

    private final ProductRepository productRepository;

    private final ProductImageCreateService productImageCreateService;

    private final PartnerReadService partnerReadService;


    public void create(ProductCreateDto productCreateDto) {

        // RequestContextHolder에서 ServletRequestAttributes 얻기
        ServletRequestAttributes servletRequestAttribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // Session 가져오기
        HttpSession httpSession = servletRequestAttribute.getRequest().getSession(false);

        if (httpSession == null) throw new RuntimeException("파트너 로그인이 안돼있음");

        Partner aPartner = partnerReadService.getByAccountId(httpSession.getAttribute("partnerId").toString());

        Product savedProduct = productRepository.save(getProductOf(productCreateDto, aPartner));

        ProductImage mainProductImage = productImageCreateService.create(productCreateDto, savedProduct);

        savedProduct.setProductImage(mainProductImage);
    }


    private Product getProductOf(ProductCreateDto productCreateDto, Partner partner) {

        return Product.builder()
                .partner(partner)
                .productName(productCreateDto.getProductName())
                .sellingPrice(productCreateDto.getSellingPrice())
                .costPrice(productCreateDto.getCostPrice())
                .discountPrice(productCreateDto.getDiscountedPrice())
                .discountRate(productCreateDto.getMarginRate())
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
