package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.*;
import newbies.handmade_mall.common.CheckoutProductState;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Data
@Table(name="checkout_product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CheckoutProduct extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checkout_product_id",nullable = false,unique = true)
    @Comment("주문 상품 PK")
    private Long id;

    @ManyToOne
    @JoinColumn(name="checkout_id",nullable = false)
    @Comment("주문 PK")
    private Checkout checkout;

    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    @Comment("상품 PK")
    private Product product;

    @Column(name="total_selling_price",nullable = false)
    @Comment("총 판매가")
    private BigDecimal totalSellingPrice;

    @Column(name="total_discounted_price",nullable = false)
    @Comment("총 할인가")
    private BigDecimal totalDiscountedPrice;

    @Column(name="count",nullable = false)
    @Comment("개수")
    private Long count;

    @Column(name="total_shipping_fee",nullable = false)
    @Comment("배송비")
    private  BigDecimal totalShippingFee;

    @ManyToOne
    @JoinColumn(name = "present_product_image", nullable = true)
    @Comment("대표 이미지")
    private ProductImage presentProductImage;

    @Column(name="checkout_product_state")
    @Comment("주문상태")
    @Enumerated(EnumType.STRING)
    private CheckoutProductState checkoutProductState;


}
