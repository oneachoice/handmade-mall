package newbies.handmade_mall.repository;

import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findFirstBy();

    Page<Product> findAllByCategory(ProductCategory productCategory, Pageable pageable);

    Page<Product> findByProductNameContaining(String productName, Pageable pageable);
}
