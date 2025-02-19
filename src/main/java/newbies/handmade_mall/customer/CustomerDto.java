package newbies.handmade_mall.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 고객 정보 수정 dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    /**
     * 고객 PK
     */
    private Long id;

    /**
     * 고객 id
     */
    private String accountId;

    /**
     * 고객 비밀번호
     */
    private String password;

    /**
     * 고객 이름
     */
    private String name;

    /**
     * 고객 이메일
     */
    private String email;

    private String phoneNumber;
}
