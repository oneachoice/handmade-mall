package newbies.handmade_mall.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductImageUrlDto {
    /** 상품 대표 이미지 Url */
    private final String mainImageUrl;

    /** 상품 카드 이미지 Url 리스트 */
    private final List<String> cardImageUrls;

    /** 상품 설명 이미지 Url 리스트 */
    private final List<String> descriptionImageUrls;

}
