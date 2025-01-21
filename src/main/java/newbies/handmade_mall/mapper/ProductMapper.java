package newbies.handmade_mall.mapper;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.req.ProductDto;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.entity.Partner;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.util.Formatter;
import org.springframework.stereotype.Component;

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
                                 .discountRate(product.getDiscountRate())
                                 .discountPrice(product.getDiscountPrice())
                                 .mainImagePath(productMainImagePath)
                                 .productName(product.getProductName())
                                 .margin(product.getMargin())
                                 .marginRate(product.getMarginRate())
                                 .sellingPrice(product.getSellingPrice())
                                 .build();
    }

    public ProductDto toProductDto(Product product) {

        return ProductDto.builder()
                         .productId(product.getId())
                         .productName(product.getProductName())
                         .sellingPrice(product.getSellingPrice())
                         .costPrice(product.getCostPrice())
                         .discountedPrice(product.getDiscountPrice())
                         .discountRate(product.getDiscountRate())
                         .margin(product.getMargin())
                         .marginRate(product.getMarginRate())
                         .shippingFee(product.getShippingFee())
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

}
