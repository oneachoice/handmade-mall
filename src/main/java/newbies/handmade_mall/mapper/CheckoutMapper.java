package newbies.handmade_mall.mapper;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.CheckoutProductState;
import newbies.handmade_mall.dto.req.CheckoutDto;
import newbies.handmade_mall.entity.Checkout;
import newbies.handmade_mall.entity.Customer;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.service.CustomerCrudService;
import newbies.handmade_mall.service.ProductCrudService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CheckoutMapper {

    private final CustomerCrudService customerCrudService;

    private final ProductCrudService productCrudService;
    /**
     * checkout 엔티티 생성
     */
    public Checkout toCheckoutEntity(CheckoutDto checkoutDto) {

        //세션 정보
        Customer customer = customerCrudService.getCustomer();

        if (customer == null) throw new RuntimeException("로그인 되지 않음");

        Product product = productCrudService.getProduct(checkoutDto.getProductId());

        //배송 주소(도로명주소 + 상세주소 + (우편주소))
        String shippingAddress = checkoutDto.getRoadAddress() + " " + checkoutDto.getDetailAddress() + "(" + checkoutDto.getPostCode() + ")";

        //count (Long 타입 -> BigDecimal 타입 변환)
        BigDecimal count = new BigDecimal(checkoutDto.getCount());

        //판매가 합계
        BigDecimal grandTotalSellingPrice = product.getSellingPrice().multiply(count);

        //할인가 합계
        BigDecimal grandTotalDiscountedPrice = product.getDiscountPrice().multiply(count);

        //최종 결제 가격
        BigDecimal grandTotalPayment = grandTotalDiscountedPrice.add(product.getShippingFee());


        return Checkout.builder()
                       .customer(customer)
                       .shippingAddress(shippingAddress)
                       .recipient(checkoutDto.getRecipientName())
                       .grandTotalSellingPrice(grandTotalSellingPrice)
                       .grandTotalDiscountedPrice(grandTotalDiscountedPrice)
                       .grandTotalShippingFee(product.getShippingFee())
                       .grandTotalPayment(grandTotalPayment)
                       .build();
    }

    /**
     * 주문 상태 ENUM -> String 으로 변경
     */
    public String getCheckoutState(CheckoutProductState checkoutProductState){
       String statement = "";
        switch (checkoutProductState){
            case CANCEL -> statement = "주문취소";
            case WAIT -> statement = "주문완료";
            case CONFIRM -> statement = "주문확인";
        }
    return statement;
    }


}
