package newbies.handmade_mall.mapper;

import newbies.handmade_mall.common.AccountState;
import newbies.handmade_mall.dto.req.CustomerDto;
import newbies.handmade_mall.dto.res.CustomerInfoDto;
import newbies.handmade_mall.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerDto customerDto) {
        Customer customer = new Customer();

        //저장할 휴대전화번호(처음,중간,끝까지 합치고 '-'로 구분)
        String savePhoneNumber = customerDto.getFirstPhoneNumber() + "-" + customerDto.getMiddlePhoneNumber() + "-" + customerDto.getLastPhoneNumber();

        customer.setAccountId(customerDto.getAccountId());
        customer.setPassword(customerDto.getPassword());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(savePhoneNumber);
        customer.setAccountState(AccountState.ACTIVE); //가입할 때 계정상태 'ACTIVE'로 고정

        return customer;
    }

    public CustomerInfoDto toCustomerInfoDto(Customer customer){

            return CustomerInfoDto.builder()
                                  .id(customer.getId())
                                  .accountId(customer.getAccountId())
                                  .name(customer.getName())
                                  .email(customer.getEmail())
                                  .phoneNumber(customer.getPhoneNumber())
                                  .build();

    }

}
