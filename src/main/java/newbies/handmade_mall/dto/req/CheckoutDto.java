package newbies.handmade_mall.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDto {

    /**
     * 받는 분 이름
     */
    private String recipientName;

    /**
     * 휴대전화번호 첫번째 자리
     */
    private String firstPhoneNumber;

    /**
     * 휴대전화번호 가운데 자리
     */
    private String middlePhoneNumber;

    /**
     * 휴대전화번호 마지막 자리
     */
    private String lastPhoneNumber;

    /**
     * 우편 주소
     */
    private String postCode;

    /**
     * 도로명 주소
     */
    private String roadAddress;

    /**
     * 상세 주소
     */
    private String detailAddress;

    /**
     * 상품 PK
     */
    private Long productId;

    /**
     * 주문 수량
     */
    private Long count;



}
