package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.SearchListDto;
import newbies.handmade_mall.service.SearchProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductSearchController {

    private final SearchProductService searchProductService;

    /**
     * @return 검색 및 페이지네이션
     */
    @GetMapping("/search")
    public String viewSearchProductNamePage(@RequestParam(value = "search", required = false, defaultValue = "")String productName, @PageableDefault(size = 20)Pageable pageable, Model model) {

        Page<SearchListDto> findProduct = searchProductService.searchByProductName(productName, pageable);

        model.addAttribute("products", findProduct);
        model.addAttribute("productPage", findProduct.getContent());
        model.addAttribute("currentPage", findProduct.getNumber());
        model.addAttribute("totalPages", findProduct.getTotalPages());
        model.addAttribute("searchQuery", productName);  // 검색어

        return "/pages/product/search";
    }


}
