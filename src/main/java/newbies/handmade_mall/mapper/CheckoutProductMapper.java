package newbies.handmade_mall.mapper;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.entity.Checkout;
import newbies.handmade_mall.entity.CheckoutProduct;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductRepository;
import newbies.handmade_mall.util.Formatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CheckoutProductMapper {

    private final ProductRepository productRepository;

    private final ProductImageManager productImageManager;


    /**
     * 주문목록 dto 생성
     */
    public CheckoutListDto toCheckoutListDto(Checkout checkout) {

        // Product Names 리스트 생성
        List<String> productNames = new ArrayList<>();
        List<String> mainImages = new ArrayList<>(); // 메인 이미지 리스트
        for (CheckoutProduct product : checkout.getCheckoutProductList()) {
            productNames.add(product.getProduct().getProductName());

            // Product Image 가져오기
            ProductImage productImage = product.getPresentProductImage();
            if (productImage != null) {
                String uuid = productImage.getUuid().toString();
                String fileExtension = productImage.getFileExtension();
                mainImages.add(uuid + fileExtension); // 이미지 정보 추가
            }
        }
        // DTO 생성 및 반환
        return CheckoutListDto.builder()
                              .checkoutProductId(checkout.getId())
                              .checkoutId(checkout.getId())
                              .createdAt(Formatter.formatForCheckout())
                              .grandTotalPayment(checkout.getGrandTotalPayment())
                              .presentProductImage(String.join(", ", mainImages)) // 이미지들을 연결
                              .productName(String.join(", ", productNames)) // 상품 이름들을 연결
                              .build();

    }

    /**
     * 상품 상세 페이지에서 상품정보와 개수를 dto에 담음
     *
     * @return CheckoutProductViewDto
     */
    public CheckoutProductViewDto toCheckoutProductViewDto(CheckoutProductDto checkoutProductDto) {

        //상품 정보 가져옴
        Optional<Product> optionalProduct = productRepository.findById(checkoutProductDto.getProductId().getId());

        if (optionalProduct.isEmpty()) throw new RuntimeException("상품을 찾을 수 없음");

        Product product = optionalProduct.get();

        //Long타입의 count를 bigdecimal로 변환
        BigDecimal count = new BigDecimal(checkoutProductDto.getCount());

        //총 판매가 = 판매가*개수
        BigDecimal totalSellingPrice = product.getSellingPrice().multiply(count);

        //총 할인가 = 할인가*개수
        BigDecimal totalDiscountedPrice = product.getDiscountPrice().multiply(count);

        //총 결제 금액 = 총 할인가+배송비
        BigDecimal totalPrice = totalDiscountedPrice.add(product.getShippingFee());

        //메인이미지 경로
        String mainImagePath = productImageManager.createImageUrl(product.getProductImage().getUuid() + product.getProductImage().getFileExtension());

        return CheckoutProductViewDto.builder()
                                     .productId(product.getId())
                                     .productName(product.getProductName())
                                     .count(checkoutProductDto.getCount())
                                     .discountedPrice(product.getDiscountPrice())
                                     .totalDiscountedPrice(totalDiscountedPrice)
                                     .totalSellingPrice(totalSellingPrice)
                                     .totalDiscount(totalSellingPrice.subtract(totalDiscountedPrice))
                                     .shippingFee(product.getShippingFee())
                                     .totalPrice(totalPrice)
                                     .mainImagePath(mainImagePath)
                                     .build();
    }

}
