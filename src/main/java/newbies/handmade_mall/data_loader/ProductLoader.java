package newbies.handmade_mall.data_loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductImageRepository;
import newbies.handmade_mall.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@Component
@Log4j2
@RequiredArgsConstructor
public class ProductLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (productRepository.count() > 1000) return;

        Optional<Product> optionalProduct = productRepository.findFirstBy();

        if (optionalProduct.isEmpty()) return;

        Product firstProduct = optionalProduct.get();


        IntStream.rangeClosed(0, 1000).forEach((i) -> {
            copyProduct(firstProduct);
        });
    }

    private void copyProduct(Product product) {
        Product newProduct = Product.builder()
                                    .category(product.getCategory())
                                    .count(product.getCount())
                                    .shippingFee(product.getShippingFee())
                                    .marginRate(product.getMarginRate())
                                    .margin(product.getMargin())
                                    .discountRate(product.getDiscountRate())
                                    .discountPrice(product.getDiscountPrice())
                                    .productName(product.getProductName())
                                    .costPrice(product.getCostPrice())
                                    .sellingPrice(product.getSellingPrice())
                                    .productImage(copyProductImage(product.getProductImage()))
                                    .partner(product.getPartner())
                                    .build();

        productRepository.save(newProduct);
    }

    private ProductImage copyProductImage(ProductImage productImage) {

        ProductImage newProductImage = ProductImage.builder()
                           .productImageType(productImage.getProductImageType())
                           .imageName(productImage.getImageName())
                           .uuid(productImage.getUuid())
                           .product(productImage.getProduct())
                           .fileExtension(productImage.getFileExtension())
                           .build();

        return productImageRepository.save(newProductImage);
    }
}
