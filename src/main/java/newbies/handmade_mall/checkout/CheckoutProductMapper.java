package newbies.handmade_mall.checkout;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.product.ProductImageManager;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.product.Product;
import newbies.handmade_mall.product.ProductImage;
import newbies.handmade_mall.util.Formatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CheckoutProductMapper {

    private final ProductImageManager productImageManager;

    public CheckoutListDto toCheckoutListDtos(CheckoutProduct checkoutProduct, String checkoutCode) {


        ProductImage productImage = checkoutProduct.getPresentProductImage();

        String mainImagePath = productImageManager.createImageUrl(productImage.getUuid() + productImage.getFileExtension());

        //수량 타입 변경
        BigDecimal count = new BigDecimal(checkoutProduct.getCount());

        //총 마진
        BigDecimal margin = count.multiply(checkoutProduct.getProduct().getMargin());

        Checkout checkout = checkoutProduct.getCheckout();

        // DTO 생성 및 반환
        return CheckoutListDto.builder()
                              .checkoutCode(checkoutCode)
                              .checkoutProductId(checkoutProduct.getId())
                              .checkoutId(checkout.getId())
                              .customerId(checkout.getCustomer().getAccountId())
                              .createdAt(Formatter.formatForViewCheckout(checkout.getCreatedAt()))
                              .grandTotalPayment(checkout.getGrandTotalPayment().setScale(0, RoundingMode.FLOOR))
                              .presentProductImage(mainImagePath) // 이미지들을 연결
                              .productName(checkoutProduct.getProduct().getProductName()) // 상품 이름들을 연결
                              .count(checkoutProduct.getCount())
                              .shippingFee(checkoutProduct.getTotalShippingFee().setScale(0,RoundingMode.FLOOR))
                              .margin(margin.setScale(0,RoundingMode.FLOOR))
                              .marginRate(checkoutProduct.getProduct().getMarginRate().setScale(0,RoundingMode.FLOOR))
                              .checkoutProductState(Formatter.getCheckoutState(checkoutProduct.getCheckoutProductState()))
                              .build();

    }

    /**
     * @Return CheckoutProduct
     */
    public CheckoutProduct toCheckoutProduct(CheckoutProductDto checkoutProductDto, Product product) {
        //count 타입 변경
        BigDecimal count = new BigDecimal(checkoutProductDto.getCount());

        //총 판매가
        BigDecimal totalSellingPrice = product.getSellingPrice().multiply(count);

        //총 할인가
        BigDecimal totalDiscountedPrice = product.getDiscountPrice().multiply(count);


        // 주문상세 엔티티로 변환
        return CheckoutProduct.builder()
                              .checkout(checkoutProductDto.getCheckoutId())
                              .product(checkoutProductDto.getProductId())
                              .totalSellingPrice(totalSellingPrice.setScale(0,RoundingMode.FLOOR))
                              .totalDiscountedPrice(totalDiscountedPrice.setScale(0,RoundingMode.FLOOR))
                              .count(checkoutProductDto.getCount())
                              .totalShippingFee(product.getShippingFee().setScale(0,RoundingMode.FLOOR))
                              .presentProductImage(product.getProductImage())
                              .checkoutProductState(CheckoutProductState.WAIT)
                              .build();
    }



    /**
     * 주문목록 dto 생성
     */
    public CheckoutListDto toCheckoutListDto(Checkout checkout) {


        List<String> mainImages = new ArrayList<>(); // 메인 이미지 리스트
        List<String> productNames = new ArrayList<>(); //상품명


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
                              .customerId(checkout.getCustomer().getAccountId())
                              .createdAt(Formatter.formatForViewCheckout(checkout.getCreatedAt()))
                              .grandTotalPayment(checkout.getGrandTotalPayment().setScale(0,RoundingMode.FLOOR))
                              .presentProductImage(String.join(", ", mainImages)) // 이미지들을 연결
                              .productName(String.join(", ", productNames)) // 상품 이름들을 연결
                              .build();

    }

    /**
     * 상품 상세 페이지에서 상품정보와 개수를 dto에 담음
     *
     * @return CheckoutProductViewDto
     */
    public CheckoutProductViewDto toCheckoutProductViewDto(CheckoutProductDto checkoutProductDto, Product product) {

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
                                     .discountedPrice(product.getDiscountPrice().setScale(0,RoundingMode.FLOOR))
                                     .totalDiscountedPrice(totalDiscountedPrice.setScale(0,RoundingMode.FLOOR))
                                     .totalSellingPrice(totalSellingPrice.setScale(0,RoundingMode.FLOOR))
                                     .totalDiscount(totalSellingPrice.subtract(totalDiscountedPrice).setScale(0,RoundingMode.FLOOR))
                                     .shippingFee(product.getShippingFee().setScale(0,RoundingMode.FLOOR))
                                     .totalPrice(totalPrice.setScale(0,RoundingMode.FLOOR))
                                     .mainImagePath(mainImagePath)
                                     .build();
    }

    /**
     * 주문상세 페이지 dto
     */
    public CheckoutProductViewDto toCheckoutProductDto(Checkout checkout, CheckoutProduct checkoutProduct) {

        //주문 상태 enum -> string
        String checkoutState = Formatter.getCheckoutState(checkoutProduct.getCheckoutProductState());

        //총 할인금액
        BigDecimal totalDiscount = checkoutProduct.getTotalSellingPrice().subtract(checkoutProduct.getTotalDiscountedPrice());

        ProductImage productImage = checkoutProduct.getPresentProductImage();
        //대표 이미지
        String mainImagePath = productImageManager.createImageUrl(productImage.getUuid() + productImage.getFileExtension());

        return CheckoutProductViewDto.builder()
                                     .productId(checkoutProduct.getProduct().getId())
                                     .productName(checkoutProduct.getProduct().getProductName())
                                     .count(checkoutProduct.getCount())
                                     .discountedPrice(checkoutProduct.getProduct().getDiscountPrice().setScale(0,RoundingMode.FLOOR))
                                     .totalDiscountedPrice(checkoutProduct.getTotalDiscountedPrice().setScale(0,RoundingMode.FLOOR))
                                     .totalSellingPrice(checkoutProduct.getTotalSellingPrice().setScale(0,RoundingMode.FLOOR))
                                     .totalDiscount(totalDiscount.setScale(0,RoundingMode.FLOOR))
                                     .shippingFee(checkoutProduct.getTotalShippingFee().setScale(0,RoundingMode.FLOOR))
                                     .totalPrice(checkout.getGrandTotalPayment().setScale(0,RoundingMode.FLOOR))
                                     .createdAt(Formatter.format(checkout.getCreatedAt()))
                                     .mainImagePath(mainImagePath)
                                     .recipient(checkout.getRecipient())
                                     .shippingAddress(checkout.getShippingAddress())
                                     .recipientPhoneNumber(checkout.getCustomer().getPhoneNumber())
                                     .checkoutProductState(checkoutState)
                                     .build();

    }

}
