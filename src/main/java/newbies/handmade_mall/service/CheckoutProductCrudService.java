package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.entity.Checkout;
import newbies.handmade_mall.entity.CheckoutProduct;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.mapper.CheckoutProductMapper;
import newbies.handmade_mall.repository.CheckoutProductRepository;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutProductCrudService {

    private final ProductCrudService productCrudService;

    private final CheckoutProductRepository checkoutProductRepository;

    private final CheckoutProductMapper checkoutProductMapper;

    private final CheckoutCrudService checkoutCrudService;


    /**
     * 파트너 주문 목록
     */

    public Page<CheckoutListDto> getPartnerCheckoutListViewDto(Pageable pageable) {

        if (SessionManager.getHttpSessionAttribute("partnerId") == null) throw new RuntimeException("파트너 로그인 안됨");

        Page<CheckoutProduct> checkoutProductPage = checkoutProductRepository.findAll(pageable);

        return checkoutProductPage.map((checkoutProduct) -> {
            return checkoutProductMapper.toCheckoutListDtos(checkoutProduct, checkoutCrudService.getCheckoutCode(checkoutProduct.getProduct().getCategory(), checkoutProduct.getCreatedAt()));
        });
    }


    /**
     * checkoutProductViewDto 생성
     */
    public CheckoutProductViewDto getCheckoutProductViewDto(Long checkoutId) {

        Checkout checkout = checkoutCrudService.toCheckout(checkoutId);

        Optional<CheckoutProduct> optionalCheckoutProduct = checkoutProductRepository.findByCheckoutId(checkoutId);

        if (optionalCheckoutProduct.isEmpty()) throw new RuntimeException("주문 상품 없음");

        CheckoutProduct checkoutProduct = optionalCheckoutProduct.get();

        return checkoutProductMapper.toCheckoutProductDto(checkout, checkoutProduct);
    }


    /**
     * CheckoutProduct 엔티티 생성
     */
    public void toCheckoutProductEntity(CheckoutProductDto checkoutProductDto) {

        Product product = productCrudService.getProduct(checkoutProductDto.getProductId().getId());

        CheckoutProduct checkoutProduct = checkoutProductMapper.toCheckoutProduct(checkoutProductDto, product);

        //DB에 저장
        checkoutProductRepository.save(checkoutProduct);

    }
}
