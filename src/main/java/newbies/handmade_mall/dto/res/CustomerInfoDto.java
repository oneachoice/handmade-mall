package newbies.handmade_mall.dto.res;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoDto {

    /**
     * 고객 pk
     */
    private Long id;

    /**
     * 고객 아이디
     */
    private String accountId;

    /**
     * 고객 이름
     */
    private String name;

    /**
     * 고객 이메일
     */
    private String email;

    /**
     * 고객 휴대전화번호
     */
    private String phoneNumber;

}
