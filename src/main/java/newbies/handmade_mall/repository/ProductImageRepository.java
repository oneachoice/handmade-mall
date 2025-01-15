package newbies.handmade_mall.repository;

import newbies.handmade_mall.common.ProductImageType;
import newbies.handmade_mall.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {

    ProductImage findByProductIdAndProductImageType(Long productId, ProductImageType Main);

    List<ProductImage> findByProductId(Long productId);
}
