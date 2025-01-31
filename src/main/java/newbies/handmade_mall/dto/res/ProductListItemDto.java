package newbies.handmade_mall.dto.res;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ProductListItemDto {

    /**
     * 상품 고유 아이디
     */
    private Long productId;

    /**
     * 상품 코드
     */
    private String productCode;

    /**
     * 메인 상품 이미지
     */
    private MultipartFile mainProductImage;

    /**
     * 상품 카테고리
     */
    private String category;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 상품 판매가
     */
    private BigDecimal sellingPrice;

    /**
     * 상품 할인가
     */
    private BigDecimal discountPrice;

    /**
     * 상품 할인율
     */
    private BigDecimal discountRate;

    /**
     * 상품 재고 수량
     */
    private Long count;
    /**
     * 상품 마진
     */
    private BigDecimal margin;

    /**
     * 상품 마진율
     */
    private BigDecimal marginRate;

    /**
     * 상품 등록 일자
     */
    private String createdAt;

    /**
     * UUID 와 확장자 조합
     */
    private String mainImagePath;
}
