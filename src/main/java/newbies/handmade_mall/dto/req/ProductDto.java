package newbies.handmade_mall.dto.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    /**
     * 상품 PK키
     */
    private Long productId;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 상품 카테고리
     */
    private String category;

    /**
     * 상품 판매가
     */
    private BigDecimal sellingPrice;

    /**
     * 상품 원가
     */
    private BigDecimal costPrice;

    /**
     * 상품 할인가
     */
    private BigDecimal discountedPrice;

    /**
     * 상품 할인율
     */
    private BigDecimal discountRate;

    /**
     * 상품 마진
     */
    private BigDecimal margin;

    /**
     * 상품 마진율
     */
    private BigDecimal marginRate;

    /**
     * 상품 배송비
     */
    private BigDecimal shippingFee;

    /**
     * 상품 재고 수량
     */
    private Long count;

    /**
     * 상품 대표 이미지
     */
    private MultipartFile mainProductImage;

    /**
     * 상품 카드 이미지
     */
    private List<MultipartFile> productCardImages;

    /**
     * 상품 설명 이미지
     */
    private List<MultipartFile> productDescriptionImages;
}
