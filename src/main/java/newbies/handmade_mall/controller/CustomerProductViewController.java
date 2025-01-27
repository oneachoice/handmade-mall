package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.ProductDto;
import newbies.handmade_mall.dto.res.ProductImageUrlDto;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.service.ProductCrudService;
import newbies.handmade_mall.service.ProductImageCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerProductViewController {

    private final ProductCrudService productCrudService;

    private final ProductImageCrudService productImageCrudService;

    /**
     * @return 고객에게 보여지는 페이지
     */
    @GetMapping("/product/list")
    public String viewList(Model model, @PageableDefault(size = 20, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductListItemDto> productListViewDtoPage = productCrudService.getProductListItemDtoPage(pageable);

        model.addAttribute("productList", productListViewDtoPage.getContent());
        model.addAttribute("paging", productListViewDtoPage);
        model.addAttribute("currentPage", productListViewDtoPage.getNumber());

        return "pages/product/list";
    }

    @GetMapping("/product/details/{productId}")
    public String viewDetail(@PathVariable("productId") Long productId, Model model) {
        //상품 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductDto productDto = productCrudService.getProductDto(productId);

        //상품 이미지 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductImageUrlDto productImageUrlDto = productImageCrudService.getProductImagesDto(productId);


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
