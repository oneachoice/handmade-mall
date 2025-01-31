package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.SearchListDto;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.mapper.CheckoutMapper;
import newbies.handmade_mall.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductService {

    private final ProductRepository productRepository;

    private final CheckoutMapper checkoutMapper;



    public Page<SearchListDto> searchByProductName(String productName, Pageable pageable) {

        //상품 이름 기준
        if (productName == null || productName.isBlank()) {
            return Page.empty(pageable);
        }

        Page<Product> products = productRepository.findByProductNameContaining(productName.trim(),pageable);

        return products.map(checkoutMapper::toSearchListDto);
    }

}