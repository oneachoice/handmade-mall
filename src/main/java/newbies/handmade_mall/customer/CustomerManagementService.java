package newbies.handmade_mall.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.common.ResponseApi;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerManagementService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    /**
     * 세션을 사용해서 Customer 엔티티를 불러옴
     */
    public Customer getCustomer() {
        String customerId = (String) SessionManager.getHttpSessionAttribute("customerId");

        return customerRepository.findByAccountId(customerId).orElseThrow(() -> new RuntimeException("로그인 되지 않은 사용자"));
    }


    /**
     * 계정 삭제
     */
    public ResponseApi<Void> cancel() {
        Customer customer = getCustomer();

        // SOFT DELETE
        customer.setAccountState(AccountState.INACTIVE);

        return ResponseApi.of(HttpStatus.NO_CONTENT, "계정이 성공적으로 삭제되었습니다.");
    }


    /**
     * 상단바에 로그인유무를 나타내주기 위한 dto 생성
     *
     * @return 로그인 시 navigationViewDto 반환
     */
    public ResponseApi<CustomerDto> getCustomerDto() {
        Customer customer = getCustomer();

        return ResponseApi.of(HttpStatus.OK, "계정 정보를 성공적으로 불러왔습니다.", customerMapper.toCustomerDto(customer));
    }


    public ResponseApi<Void> update(CustomerDto customerDto) {
        Customer customer = getCustomer();


        //휴대 전화번호 ***-****-**** 형식으로 저장
        String phoneNumber = customerDto.getPhoneNumber();

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

        return ResponseApi.of(HttpStatus.CREATED, "계정이 성공적으로 수정되었습니다.");
    }

}
