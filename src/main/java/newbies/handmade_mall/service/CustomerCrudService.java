package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.common.AccountState;
import newbies.handmade_mall.dto.req.CustomerDto;
import newbies.handmade_mall.dto.res.CustomerInfoDto;
import newbies.handmade_mall.entity.Customer;
import newbies.handmade_mall.mapper.CustomerMapper;
import newbies.handmade_mall.repository.CustomerRepository;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerCrudService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    /**
     * 회원 탈퇴
     */
    public void cancelByCustomerId() {
        Customer customer = getCustomer();

        // SOFT DELETE
        customer.setAccountState(AccountState.INACTIVE);
    }


    /**
     * 세션을 사용해서 Customer 엔티티를 불러옴
     */
    public Customer getCustomer() {
        String customerId = (String) SessionManager.getHttpSessionAttribute("customerId");

        return customerRepository.findByAccountId(customerId).orElseThrow(() -> new RuntimeException("로그인 되지 않은 사용자"));
    }

    /**
     * 상단바에 로그인유무를 나타내주기 위한 dto 생성
     *
     * @return 로그인 시 navigationViewDto 반환
     */
    public CustomerInfoDto getCustomerInfoDto() {
        Customer customer = getCustomer();
        return customerMapper.toCustomerInfoDto(customer);
    }


    public void update(CustomerDto customerDto) {
        Customer customer = getCustomer();


        //휴대 전화번호 ***-****-**** 형식으로 저장
        String phoneNumber = customerDto.getFirstPhoneNumber() + "-" + customerDto.getMiddlePhoneNumber() + "-" + customerDto.getLastPhoneNumber();

        if (!customerDto.getName().isBlank()) {
            customer.setName(customerDto.getName());
        }

        if (!customerDto.getPassword().isBlank()) {
            customer.setPassword(customerDto.getPassword());
        }

        if (!customerDto.getEmail().isBlank()) {
            customer.setEmail(customerDto.getEmail());
        }

        customer.setPhoneNumber(phoneNumber);
    }

}
