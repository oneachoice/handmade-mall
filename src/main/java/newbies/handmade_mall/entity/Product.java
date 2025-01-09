package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.*;
import newbies.handmade_mall.common.ProductCategory;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseTime {

    @Id
    @Column(name = "product_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 기본키")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "partner_id")
    @Comment("파트너 외래키")
    private Partner partner;


    @Column(name = "product_name", nullable = false)
    @Comment("상품명")
    private String productName;


    @Column(name = "selling_price", nullable = false)
    @Comment("판매가")
    private BigDecimal sellingPrice;


    @Column(name = "cost_price", nullable = false)
    @Comment("원가")
    private BigDecimal costPrice;


    @Column(name = "discount_price", nullable = false)
    @Comment("할인가")
    private BigDecimal discountPrice;


    @Column(name = "discount_rate", nullable = false)
    @Comment("할인율")
    private BigDecimal discountRate;


    @Column(name = "margin", nullable = false)
    @Comment("마진")
    private BigDecimal margin;


    @Column(name = "margin_rate", nullable = false)
    @Comment("마진율")
    private BigDecimal marginRate;


    @Column(name = "shipping_fee", nullable = false)
    @Comment("배송비")
    private BigDecimal shippingFee;


    @Column(name = "count", nullable = false)
    @Comment("재고 수량")
    private Long count;


    @OneToOne
    @JoinColumn(name = "present_product_image")
    @Comment("대표 이미지")
    private ProductImage productImage;


    @Column(name = "category", nullable = false)
    @Comment("카테고리")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;


    @Transient
    public String getProductCode() {
        String categoryName = category.toString();

        return categoryName.substring(0, Math.min(2, categoryName.length())) + id;
    }

}
