package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductCategory;
import newbies.handmade_mall.dto.req.CheckoutDto;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.entity.Checkout;
import newbies.handmade_mall.entity.Customer;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.mapper.CheckoutMapper;
import newbies.handmade_mall.mapper.CheckoutProductMapper;
import newbies.handmade_mall.repository.CheckoutRepository;
import newbies.handmade_mall.util.Formatter;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutCrudService {

    private final ProductCrudService productCrudService;

    private final CustomerCrudService customerCrudService;

    private final CheckoutRepository checkoutRepository;

    private final CheckoutProductMapper checkoutProductMapper;

    private final CheckoutMapper checkoutMapper;


    /**
     * 로그인한 회원의 주문이 맞는지 체크
     */
    public Checkout toCheckout(Long checkoutId){
        String customerId = (String) SessionManager.getHttpSessionAttribute("customerId");

        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutId);

        if (optionalCheckout.isEmpty()) throw new RuntimeException("주문 정보 없음");

        Checkout checkout = optionalCheckout.get();

        //로그인한 회원이 주문한 게 맞는지 체크
        if (!checkout.getCustomer().getAccountId().equals(customerId)) throw new RuntimeException("주문을 불러올 수 없습니다.");

        return checkout;
    }

    /**
     * 상품 상세 페이지에서 상품정보와 개수를 dto에 담음
     *
     * @return CheckoutProductViewDto
     */
    public CheckoutProductViewDto viewCheckoutCreateProduct(CheckoutProductDto checkoutProductDto) {

        customerCrudService.getCustomer();

        Product product = productCrudService.getProduct(checkoutProductDto.getProductId().getId());

        return checkoutProductMapper.toCheckoutProductViewDto(checkoutProductDto, product);

    }


    /**
     * 고객 주문 목록
     */
    public Page<CheckoutListDto> getCheckoutListDtoPage(Pageable pageable) {

        Customer customer = customerCrudService.getCustomer();

        if (customer == null) throw new RuntimeException("로그인이 안돼있음");

        Page<Checkout> checkoutPage = checkoutRepository.findByCustomerId(customer.getId(), pageable);

        return checkoutPage.map(checkoutProductMapper::toCheckoutListDto);
    }


    /**
     * 로그인 체크
     */
    public boolean checkLogin() {
        return SessionManager.getHttpSessionAttribute("customerId") != null;
    }

    /**
     * 주문 테이블 생성 및 주문상세 테이블을 만들기 위한 DTO생성
     */
    public CheckoutProductDto createCheckout(CheckoutDto checkoutDto) {

        //세션 정보
        Customer customer = customerCrudService.getCustomer();

        if (customer == null) throw new RuntimeException("로그인 되지 않음");

        Product product = productCrudService.getProduct(checkoutDto.getProductId());

        Checkout checkout = checkoutRepository.save(checkoutMapper.toCheckoutEntity(customer, checkoutDto, product));

        return CheckoutProductDto.builder()
                                 .productId(product)
                                 .count(checkoutDto.getCount())
                                 .checkoutId(checkout)
                                 .build();
    }

    /**
     * 주문번호 생성
     */
    public String getCheckoutCode(ProductCategory productCategory, LocalDateTime localDateTime) {
        return productCategory.toString().substring(0, 2) + Formatter.formatForCheckoutCode(localDateTime);
    }


}

