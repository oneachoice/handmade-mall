package newbies.handmade_mall.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 고객 정보 수정 dto
 */
@Data
@NoArgsConstructor
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

    /**
     * 고객 휴대전화번호 첫번째 자리
     */
    private String firstPhoneNumber;

    /** 고객 휴대전화번호 두번째 자리
     *
     */
    private String middlePhoneNumber;

    /**
     * 고객 휴대전화번호 세번째 자리
     */
    private String lastPhoneNumber;

}
