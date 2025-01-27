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
public class CheckoutProductViewDto {

    /**
     * 상품 PK
     */
    private Long productId;

    /**
     * 상품명
     */
    private String productName;

    /**
     * 구매할 개수
     */
    private Long count;

    /**
     * 할인가
     */
    private BigDecimal discountedPrice; 

    /**
     * 총 할인가 (할인가X개수)
     */
    private BigDecimal totalDiscountedPrice; 

    /**
     * 총 판매가
     */
    private BigDecimal totalSellingPrice; 

    /**
     * 총 할인금액
     */
    private BigDecimal totalDiscount;

    /**
     * 배송비
     */
    private BigDecimal shippingFee;

    /**
     * 총 결제가격
     */
    private BigDecimal totalPrice;

    /**
     * 생성일
     */
    private String createdAt;

    /**
     * 대표이미지 경로
     */
    private String mainImagePath;

    /**
     * 주문코드
     */
    private String checkoutCode;

    /**
     * 상품코드
     */
    private String productCode;

    /**
     * 받는 분
     */
    private String recipient;

    /**
     * 받는 분 휴대전화번호
     */
    private String recipientPhoneNumber;

    /**
     * 배송 주소
     */
    private String shippingAddress;

    /**
     * 주문상태
     */
    private String checkoutProductState;




}
