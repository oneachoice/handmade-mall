package newbies.handmade_mall.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import newbies.handmade_mall.checkout.Checkout;
import newbies.handmade_mall.product.Product;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutProductDto {

    private Checkout checkoutId;

    private Product productId;

    private Long count;

}
