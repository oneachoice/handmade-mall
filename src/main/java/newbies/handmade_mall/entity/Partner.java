package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 파트너 엔티티
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "partner")
public class Partner extends BaseTime {

    @Id
    @Column(name = "partner_id", nullable = false, unique = true) // 기본키 설정과 중복되지만 명시적으로 적어둠
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("파트너 기본키")
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    @Comment("파트너 계정의 아이디")
    private String accountId;

    @Column(name = "account_password", nullable = false)
    @Comment("파트너 계정의 패스워드")
    private String accountPassword;
}
