package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
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
import newbies.handmade_mall.util.SessionManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 상품 상세 페이지에서 상품정보와 개수를 dto에 담음
     *
     * @return CheckoutProductViewDto
     */
    public CheckoutProductViewDto viewCheckoutCreateProduct(CheckoutProductDto checkoutProductDto) {

        customerCrudService.getCustomer();

        return checkoutProductMapper.toCheckoutProductViewDto(checkoutProductDto);

    }

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

        Checkout checkout = checkoutRepository.save(checkoutMapper.toCheckoutEntity(checkoutDto));

        Product product = productCrudService.getProduct(checkoutDto.getProductId());

        return CheckoutProductDto.builder()
                                 .productId(product)
                                 .count(checkoutDto.getCount())
                                 .checkoutId(checkout)
                                 .build();
    }

}

