package newbies.handmade_mall.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductUpdateViewDto {

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


}

