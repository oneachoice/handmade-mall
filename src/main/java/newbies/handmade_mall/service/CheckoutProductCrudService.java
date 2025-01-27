package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.CheckoutProductState;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.entity.Checkout;
import newbies.handmade_mall.entity.CheckoutProduct;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.mapper.CheckoutMapper;
import newbies.handmade_mall.repository.CheckoutProductRepository;
import newbies.handmade_mall.repository.CheckoutRepository;
import newbies.handmade_mall.util.Formatter;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutProductCrudService {

    private final ProductCrudService productCrudService;

    private final CheckoutProductRepository checkoutProductRepository;

    private final CheckoutRepository checkoutRepository;

    private final ProductImageManager productImageManager;

    private final CheckoutMapper checkoutMapper;


    /**
     * checkoutProductViewDto 생성
     */
    public CheckoutProductViewDto getCheckoutProductViewDto(Long checkoutId) {

        String customerId = (String) SessionManager.getHttpSessionAttribute("customerId");

        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutId);

        if (optionalCheckout.isEmpty()) throw new RuntimeException("주문 정보 없음");

        Checkout checkout = optionalCheckout.get();

        //로그인한 회원이 주문한 게 맞는지 체크
        if(!checkout.getCustomer().getAccountId().equals(customerId)) throw new RuntimeException("주문을 불러올 수 없습니다.");

        Optional<CheckoutProduct> optionalCheckoutProduct = checkoutProductRepository.findByCheckoutId(checkoutId);

        if (optionalCheckoutProduct.isEmpty()) throw new RuntimeException("주문 상품 없음");

        CheckoutProduct checkoutProduct = optionalCheckoutProduct.get();

        //주문 상태 enum -> string
        String checkoutState = checkoutMapper.getCheckoutState(checkoutProduct.getCheckoutProductState());

        //총 할인금액
        BigDecimal totalDiscount = checkoutProduct.getTotalSellingPrice().subtract(checkoutProduct.getTotalDiscountedPrice());

        ProductImage productImage = checkoutProduct.getPresentProductImage();
        //대표 이미지
        String mainImagePath = productImageManager.createImageUrl(productImage.getUuid() + productImage.getFileExtension());

        return CheckoutProductViewDto.builder()
                                     .productId(checkoutProduct.getProduct().getId())
                                     .productName(checkoutProduct.getProduct().getProductName())
                                     .count(checkoutProduct.getCount())
                                     .discountedPrice(checkoutProduct.getProduct().getDiscountPrice())
                                     .totalDiscountedPrice(checkoutProduct.getTotalDiscountedPrice())
                                     .totalSellingPrice(checkoutProduct.getTotalSellingPrice())
                                     .totalDiscount(totalDiscount)
                                     .shippingFee(checkoutProduct.getTotalShippingFee())
                                     .totalPrice(checkout.getGrandTotalPayment())
                                     .createdAt(Formatter.format(checkout.getCreatedAt()))
                                     .mainImagePath(mainImagePath)
                                     .recipient(checkout.getRecipient())
                                     .shippingAddress(checkout.getShippingAddress())
                                     .recipientPhoneNumber(checkout.getCustomer().getPhoneNumber())
                                     .checkoutProductState(checkoutState)
                                     .build();
    }



    /**
     * CheckoutProduct 엔티티 생성
     */
    public void toCheckoutProductEntity(CheckoutProductDto checkoutProductDto) {

       Product product = productCrudService.getProduct(checkoutProductDto.getProductId().getId());

        //count 타입 변경
        BigDecimal count = new BigDecimal(checkoutProductDto.getCount());

        //총 판매가
        BigDecimal totalSellingPrice = product.getSellingPrice().multiply(count);

        //총 할인가
        BigDecimal totalDiscountedPrice = product.getDiscountPrice().multiply(count);



        // 주문상세 엔티티로 변환
        CheckoutProduct checkoutProduct = CheckoutProduct.builder()
                                                         .checkout(checkoutProductDto.getCheckoutId())
                                                         .product(checkoutProductDto.getProductId())
                                                         .totalSellingPrice(totalSellingPrice)
                                                         .totalDiscountedPrice(totalDiscountedPrice)
                                                         .count(checkoutProductDto.getCount())
                                                         .totalShippingFee(product.getShippingFee())
                                                         .presentProductImage(product.getProductImage())
                                                         .checkoutProductState(CheckoutProductState.WAIT)
                                                         .build();

        //DB에 저장
        checkoutProductRepository.save(checkoutProduct);

    }



}
