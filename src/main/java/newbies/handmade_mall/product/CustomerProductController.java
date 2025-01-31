package newbies.handmade_mall.product;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.ProductImageUrlDto;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.dto.res.SearchListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class CustomerProductController {

    private final ProductManagementService productManagementService;

    private final ProductImageManagementService productImageManagementService;


    /**
     * @return 고객에게 보여지는 페이지
     */
    @GetMapping("/list")
    public String viewList(@RequestParam("category") String category, Model model, @PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductListItemDto> productListItemDtoPage = null;

        if (!category.isBlank()) {
            ProductCategory productCategory = ProductCategory.getEnum(category);
            productListItemDtoPage = productManagementService.getCategoryProductPage(productCategory, pageable);
        } else {
            productListItemDtoPage = productManagementService.getProductListItemDtoPage(pageable);
        }


        model.addAttribute("productList", productListItemDtoPage.getContent());
        model.addAttribute("paging", productListItemDtoPage);
        model.addAttribute("currentPage", productListItemDtoPage.getNumber());

        return "pages/product/list";
    }

    /**
     * @return 검색 및 페이지네이션
     */
    @GetMapping("/search")
    public String viewSearchProductNamePage(@RequestParam(value = "search", required = false, defaultValue = "")String productName, @PageableDefault(size = 20)Pageable pageable, Model model) {

        Page<SearchListDto> findProduct = productManagementService.searchByProductName(productName, pageable);

        model.addAttribute("products", findProduct);
        model.addAttribute("productPage", findProduct.getContent());
        model.addAttribute("currentPage", findProduct.getNumber());
        model.addAttribute("totalPages", findProduct.getTotalPages());
        model.addAttribute("searchQuery", productName);  // 검색어

        return "/pages/product/search";
    }

    @GetMapping("/details/{productId}")
    public String viewDetail(@PathVariable("productId") Long productId, Model model) {
        //상품 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductDto productDto = productManagementService.getProductDto(productId);

        //상품 이미지 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductImageUrlDto productImageUrlDto = productImageManagementService.getProductImagesDto(productId);


        //모델에 담기
        model.addAttribute("mainImagePath", productImageUrlDto.getMainImageUrl());
        model.addAttribute("cardImagePath", productImageUrlDto.getCardImageUrls());
        model.addAttribute("descriptionImagePath", productImageUrlDto.getDescriptionImageUrls()); //설명이미지 경로
        model.addAttribute("productId", productDto.getProductId()); //상품 아이디
        model.addAttribute("productName", productDto.getProductName()); //상품명
        model.addAttribute("category", productDto.getCategory()); //카테고리
        model.addAttribute("sellingPrice", productDto.getSellingPrice()); //판매가
        model.addAttribute("costPrice", productDto.getCostPrice()); //원가
        model.addAttribute("discountRate", productDto.getDiscountRate()); //할인율
        model.addAttribute("discountedPrice", productDto.getDiscountedPrice()); //할인가
        model.addAttribute("margin", productDto.getMargin()); //마진
        model.addAttribute("marginRate", productDto.getMarginRate()); //마진율
        model.addAttribute("shippingFee", productDto.getShippingFee()); //배송비
        model.addAttribute("count", productDto.getCount()); //재고 수량

        return "pages/product/details";
    }
}
