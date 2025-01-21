package newbies.handmade_mall.repository;

import newbies.handmade_mall.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    List<ProductImage> findByProductId(Long productId);

    void deleteByProductId(Long productId);
}
