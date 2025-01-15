package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.*;
import newbies.handmade_mall.common.ProductImageType;
import org.hibernate.annotations.Comment;

import java.util.UUID;

/**
 * 제품 이미지 엔티티
 */
@Entity
@Table(name = "product_image")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductImage extends BaseTime {


    @Id
    @Column(name = "product_image_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 이미지 기본키")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @Comment("상품 외래키")
    private Product product;


    @Column(name = "uuid", nullable = false)
    @Comment("고유 값")
    private UUID uuid;


    @Column(name = "image_name", nullable = false)
    @Comment("이미지 이름")
    private String imageName;


    @Column(name = "file_extension", nullable = false)
    @Comment("파일 확장자")
    private String fileExtension;


    @Column(name = "image_type", nullable = false)
    @Comment("이미지 타입")
    @Enumerated(EnumType.STRING)
    private ProductImageType productImageType;


}
