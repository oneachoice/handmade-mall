package newbies.handmade_mall.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer();

        //저장할 휴대전화번호(처음,중간,끝까지 합치고 '-'로 구분)
        String savePhoneNumber = customerDto.getPhoneNumber();

        customer.setAccountId(customerDto.getAccountId());
        customer.setPassword(customerDto.getPassword());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(savePhoneNumber);
        customer.setAccountState(AccountState.ACTIVE); //가입할 때 계정상태 'ACTIVE'로 고정

        return customer;
    }

    public CustomerDto toCustomerDto(Customer customer) {

        return CustomerDto.builder()
                          .id(customer.getId())
                          .phoneNumber(customer.getPhoneNumber())
                          .accountId(customer.getAccountId())
                          .email(customer.getEmail())
                          .name(customer.getName())
                          .password(customer.getPassword())
                          .build();

    }
}
