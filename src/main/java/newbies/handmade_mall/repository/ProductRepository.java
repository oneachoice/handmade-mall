package newbies.handmade_mall.repository;

import newbies.handmade_mall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
