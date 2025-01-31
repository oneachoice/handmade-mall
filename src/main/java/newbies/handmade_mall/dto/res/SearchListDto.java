package newbies.handmade_mall.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchListDto {

    private Long productId;

    private String productName;

    private BigDecimal sellingPrice;

    private BigDecimal discountPrice;

    private BigDecimal discountRate;

    private String mainImagePath;
}
