package newbies.handmade_mall.product;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.dto.res.SearchListDto;
import newbies.handmade_mall.partner.Partner;
import newbies.handmade_mall.util.Formatter;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductImageManager productImageManager;

    public ProductListItemDto toProductListItemDto(Product product) {

        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        // 이미지가 존재하지 않으면 이미지 추가 안함
        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        // 상품 메인 이미지 경로 생성

        // ProductListViewDto 데이터 주입
        return ProductListItemDto.builder()
                                 .productId(product.getId())
                                 .productCode(product.getProductCode())
                                 .count(product.getCount())
                                 .category(product.getCategory().toString())
                                 .createdAt(Formatter.format(product.getCreatedAt()))
                                 .discountRate(product.getDiscountRate().setScale(0,RoundingMode.FLOOR)) // 소수점이하 절사
                                 .discountPrice(product.getDiscountPrice().setScale(0, RoundingMode.FLOOR))
                                 .mainImagePath(productMainImagePath)
                                 .productName(product.getProductName())
                                 .margin(product.getMargin().setScale(0, RoundingMode.FLOOR))
                                 .marginRate(product.getMarginRate().setScale(0, RoundingMode.FLOOR))
                                 .sellingPrice(product.getSellingPrice().setScale(0, RoundingMode.FLOOR))
                                 .build();
    }

    public ProductDto toProductDto(Product product) {

        return ProductDto.builder()
                         .productId(product.getId())
                         .productName(product.getProductName())
                         .sellingPrice(product.getSellingPrice().setScale(0, RoundingMode.FLOOR))
                         .costPrice(product.getCostPrice().setScale(0, RoundingMode.FLOOR))
                         .discountedPrice(product.getDiscountPrice().setScale(0, RoundingMode.FLOOR))
                         .discountRate(product.getDiscountRate().setScale(0, RoundingMode.FLOOR))
                         .margin(product.getMargin().setScale(0, RoundingMode.FLOOR))
                         .marginRate(product.getMarginRate().setScale(0, RoundingMode.FLOOR))
                         .shippingFee(product.getShippingFee().setScale(0, RoundingMode.FLOOR))
                         .category(String.valueOf(product.getCategory()))
                         .count(product.getCount())
                         .build();
    }

    public Product toEntity(ProductDto productDto, Partner partner) {

        ProductCategory productCategory = ProductCategory.getEnum(productDto.getCategory());

        return Product.builder()
                      .partner(partner)
                      .id(productDto.getProductId())
                      .productName(productDto.getProductName())
                      .sellingPrice(productDto.getSellingPrice())
                      .costPrice(productDto.getCostPrice())
                      .discountPrice(productDto.getDiscountedPrice())
                      .discountRate(productDto.getDiscountRate())
                      .margin(productDto.getMargin())
                      .marginRate(productDto.getMarginRate())
                      .shippingFee(productDto.getShippingFee())
                      .count(productDto.getCount())
                      .category(productCategory)
                      .build();
    }

    /**
     * @return 상풍명 기준으로 상품 검색 , 페이지네이션 포함
     */
    public SearchListDto toSearchListDto(Product product) {

        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        // 이미지가 존재하지 않으면 이미지 추가 안함
        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return SearchListDto.builder()
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .sellingPrice(product.getSellingPrice())
                            .discountPrice(product.getDiscountPrice())
                            .discountRate(product.getDiscountRate())
                            .mainImagePath(productMainImagePath)
                            .build();
    }
}
