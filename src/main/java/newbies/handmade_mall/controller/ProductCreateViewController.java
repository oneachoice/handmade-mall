package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.service.ProductCreateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class ProductCreateViewController {

    private final ProductCreateService productCreateService;

    /**
     * @return 상품 등록 페이지
     */
    @GetMapping("/product/create")
    public String viewProductCreatePage(){
        return "pages/partner/product/create";
    }


    /**
     * @return 상품 생성 완료, 상품 리스트로 리다이렉트
     */
    @PostMapping("/product/create")
    public String createProduct(ProductCreateDto productCreateDto){

        productCreateService.create(productCreateDto);

        return "redirect:/partner/product/list";
    }
}
