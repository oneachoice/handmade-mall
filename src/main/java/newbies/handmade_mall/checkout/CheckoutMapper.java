package newbies.handmade_mall.checkout;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.product.ProductImageManager;
import newbies.handmade_mall.dto.res.SearchListDto;
import newbies.handmade_mall.customer.Customer;
import newbies.handmade_mall.product.Product;
import newbies.handmade_mall.product.ProductImage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class CheckoutMapper {

    private final ProductImageManager productImageManager;

    /**
     * checkout 엔티티 생성
     */
    public Checkout toCheckoutEntity(Customer customer, CheckoutDto checkoutDto,Product product) {


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
                       .grandTotalSellingPrice(grandTotalSellingPrice.setScale(0, RoundingMode.FLOOR))
                       .grandTotalDiscountedPrice(grandTotalDiscountedPrice.setScale(0,RoundingMode.FLOOR))
                       .grandTotalShippingFee(product.getShippingFee().setScale(0,RoundingMode.FLOOR))
                       .grandTotalPayment(grandTotalPayment.setScale(0,RoundingMode.FLOOR))
                       .build();
    }




}
