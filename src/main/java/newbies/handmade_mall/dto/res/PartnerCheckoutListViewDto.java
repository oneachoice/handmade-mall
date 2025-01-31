package newbies.handmade_mall.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerCheckoutListViewDto {

    /**
     * 대표이미지
     */
    private String presentProductImage;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 주문상세 PK
     */
    private Long checkoutProductId;

    /**
     * 주문 PK
     */
    private Long checkoutId;

    /**
     * 주문날짜
     */
    private String createdAt;

    /**
     *  주문 수량
     */
    private Long count;

    /**
     * 배송비
     */
    private BigDecimal shippingFee;

    /**
     * 판매금액
     */
    private BigDecimal totalDiscountedPrice;

    /**
     * 마진
     */
    private BigDecimal margin;

    /**
     * 마진율
     */
    private BigDecimal marginRate;

    /**
     * 주문확인여부
     */
    private String checkoutProductState;


}
