package newbies.handmade_mall.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newbies.handmade_mall.entity.ProductImage;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateImageViewDto {
    /**
     * 상품 대표 이미지
     */
    private ProductImage mainProductImage;

    /**
     * 상품 카드 이미지
     */
    private List<ProductImage> productCardImages;

    /**
     * 상품 설명 이미지
     */
    private List<ProductImage> productDescriptionImages;
}
