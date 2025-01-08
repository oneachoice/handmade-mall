package newbies.handmade_mall.dto.req;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 전용 DTO
 */
@Data
@NoArgsConstructor
public class LoginDto {

    private String id;

    private String password;

}
