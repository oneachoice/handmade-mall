package newbies.handmade_mall.product;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.ProductImageUrlDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final ProductImageManager productImageManager;

    public ProductImageUrlDto toProductImageUrlDto(List<ProductImage> productImages) {
        String productMainImageUrl = null;
        List<String> productCardImageUrls = new ArrayList<>();
        List<String> productDescriptionImageUrls = new ArrayList<>();

        //이미지 타입에 따른 분류
        for (ProductImage productImage : productImages) {
            switch (productImage.getProductImageType()) {
                case MAIN -> productMainImageUrl = productImageManager.createImageUrl(productImage.getImageFullName());
                case CARD ->
                        productCardImageUrls.add(productImageManager.createImageUrl(productImage.getImageFullName()));
                case DESC ->
                        productDescriptionImageUrls.add(productImageManager.createImageUrl(productImage.getImageFullName()));
            }
        }

        return ProductImageUrlDto.builder()
                                 .mainImageUrl(productMainImageUrl)
                                 .cardImageUrls(productCardImageUrls)
                                 .descriptionImageUrls(productDescriptionImageUrls)
                                 .build();
    }
}
