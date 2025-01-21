package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.*;
import newbies.handmade_mall.common.AccountState;
import org.hibernate.annotations.Comment;

/**
 * 회원 엔티티
 */
@Entity
@Table(name = "customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseTime {

    /**
     * 고객 PK
     */
    @Id
    @Column(name = "customer_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고객 PK")
    private Long id;

    /**
     * 고객 id
     */
    @Column(name = "account_id", nullable = false)
    @Comment("아이디")
    private String accountId;

    /**
     * 고객 비밀번호
     */
    @Column(name = "account_password", nullable = false)
    @Comment("비밀번호")
    private String password;

    /**
     * 고객 이름
     */
    @Column(name = "account_name", nullable = false)
    @Comment("이름")
    private String name;

    /**
     * 고객 이메일
     */
    @Column(name="account_email", nullable = false)
    @Comment("이메일")
    private String email;

    /**
     * 고객 휴대전화번호
     */
    @Column(name="phone", nullable = false)
    @Comment("휴대전화번호")
    private String phoneNumber;

    /**
     * 고객 계정상태
     */
    @Column(name="account_state",nullable = false)
    @Comment("계정상태")
    @Enumerated(EnumType.STRING)
    private AccountState accountState;


}
