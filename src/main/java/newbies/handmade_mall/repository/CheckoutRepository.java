package newbies.handmade_mall.repository;

import newbies.handmade_mall.entity.Checkout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Page<Checkout> findByCustomerId(Long customerId, Pageable pageable);
}
