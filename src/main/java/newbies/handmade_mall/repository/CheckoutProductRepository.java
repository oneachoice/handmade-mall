package newbies.handmade_mall.repository;

import newbies.handmade_mall.entity.CheckoutProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutProductRepository  extends JpaRepository<CheckoutProduct,Long> {

    Optional<CheckoutProduct> findByCheckoutId(Long checkoutId);
}
