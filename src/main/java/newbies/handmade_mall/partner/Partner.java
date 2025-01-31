package newbies.handmade_mall.partner;

import jakarta.persistence.*;
import lombok.*;
import newbies.handmade_mall.common.BaseTime;
import newbies.handmade_mall.product.Product;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 파트너 엔티티
 */
@Entity
@Table(name = "partner")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Partner extends BaseTime {

    @Id
    @Column(name = "partner_id", nullable = false, unique = true) // 기본키 설정과 중복되지만 명시적으로 적어둠
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("파트너 기본키")
    private Long id;

    @Column(name = "account_id", nullable = false, unique = true)
    @Comment("파트너 계정의 아이디")
    @NonNull // Null 값 허용 안함
    private String accountId;

    @Column(name = "account_password", nullable = false)
    @Comment("파트너 계정의 패스워드")
    @NonNull // Null 값 허용 안함
    private String accountPassword;

    //soft delete 시 불러옴
    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Product> products = new ArrayList<>();

}
