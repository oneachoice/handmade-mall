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
public class CheckoutListDto {

    /**
     * 주문자 id
     */
    private String customerId;

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
     * 상품명
     */
    private String productName;

    /**
     * 최종 결제 가격
     */
    private BigDecimal grandTotalPayment;

    /**
     * 대표이미지
     */
    private String presentProductImage;

    /**
     *  주문 수량
     */
    private Long count;

    /**
     * 배송비
     */
    private BigDecimal shippingFee;

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

    /**
     * 주문번호
     */
    private String checkoutCode;


}
