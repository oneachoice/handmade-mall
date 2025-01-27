package newbies.handmade_mall.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name="checkout")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Checkout extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="checkout_id", nullable = false,unique = true)
    @Comment("주문 PK")
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    @Comment("고객 PK")
    private Customer customer;

    @Column(name="shipping_address", nullable = false)
    @Comment("배송주소")
    private String shippingAddress;

    @Column(name="recipient", nullable = false)
    @Comment("받는 분")
    private String recipient;

    @Column(name="grand_total_selling_price",nullable = false)
    @Comment("판매가 합계")
    private BigDecimal grandTotalSellingPrice;

    @Column(name="grand_total_discounted_price",nullable = false)
    @Comment("할인가 합계")
    private BigDecimal grandTotalDiscountedPrice;

    @Column(name="grand_total_shipping_fee",nullable = false)
    @Comment("배송비 합계")
    private BigDecimal grandTotalShippingFee;

    @Column(name="grand_total_payment",nullable = false)
    @Comment("최종 결제 가격")
    private BigDecimal grandTotalPayment;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL)
    private List<CheckoutProduct> checkoutProductList;

}
