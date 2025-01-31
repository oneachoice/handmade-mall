package newbies.handmade_mall.customer;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.LoginDto;
import newbies.handmade_mall.common.ResponseApi;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;


    /**
     * 로그인 성공 시 세션에 고객 아이디 추가
     *
     * @return 로그인 성공 true, 로그인 실패 false
     */
    public ResponseApi<Void> login(LoginDto loginDto) {

        //로그인 시도한 아이디
        String customerAccountId = loginDto.getId();
        //로그인 시도한 비밀번호
        String customerAccountPassword = loginDto.getPassword();

        //해당 아이디 존재 유무 확인
        Optional<Customer> optionalCustomer = customerRepository.findByAccountId(customerAccountId);

        //아이디 존재하지 않으면 종료
        if (optionalCustomer.isEmpty()) return ResponseApi.of(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");

        Customer customer = optionalCustomer.get();

        //비밀번호가 일치 하지 않으면 종료
        if (!customer.getPassword().equals(customerAccountPassword))
            return ResponseApi.of(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");

        //탈퇴한 회원이면 종료
        if (customer.getAccountState().equals(AccountState.INACTIVE))
            return ResponseApi.of(HttpStatus.UNAUTHORIZED, "탈퇴한 회원입니다.");

        // Session에 customerId 저장
        SessionManager.setHttpSessionAttribute("customerId", customer.getAccountId());
        SessionManager.setHttpSessionAttribute("customerName", customer.getName());

        return ResponseApi.of(HttpStatus.OK, "로그인 성공");
    }

    /**
     * 로그아웃
     */
    public ResponseApi<Void> logout() {

        String CUSTOMER_SESSION_KEY = "customerId";

        SessionManager.removeHttpSessionAttribute(CUSTOMER_SESSION_KEY);

        return ResponseApi.of(HttpStatus.OK, "로그아웃 성공");
    }


    /**
     * DB 고객 테이블에 저장
     *
     * @return 가입 성공 시 true, 실패 시 false
     */
    public ResponseApi<Void> join(CustomerDto customerDto) {

        //아이디
        String customerAccountId = customerDto.getAccountId();

        //아이디 중복 체크
        Optional<Customer> optionalCustomer = customerRepository.findByAccountId(customerAccountId);

        // 아이디 중복
        if (optionalCustomer.isPresent()) return ResponseApi.of(HttpStatus.CONFLICT, "이미 존재하는 계정입니다.");

        //dto->엔티티로 변환 후 저장
        customerRepository.save(customerMapper.toEntity(customerDto));

        return ResponseApi.of(HttpStatus.CREATED, "성공적으로 계정이 생성되었습니다.");
    }

}
