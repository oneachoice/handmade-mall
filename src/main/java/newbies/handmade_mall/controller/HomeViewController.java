package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.service.ProductCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 홈 화면 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class HomeViewController {

    private final ProductCrudService productCrudService;




    /**
     * 홈 화면 생성
     */
    @GetMapping("/")
    public String ViewHomePage(Model model) {

        //최신등록 순 8개의 상품 목록 불러옴

        Page<ProductListItemDto> productListViewDtoPage = productCrudService.getProductListItemDtoPage(PageRequest.of(0, 8, Sort.Direction.DESC, "createdAt"));

        model.addAttribute("product", productListViewDtoPage.getContent());

        return "pages/home";
    }

}
