package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.ProductDto;
import newbies.handmade_mall.dto.res.ProductImageUrlDto;
import newbies.handmade_mall.dto.res.ProductListItemDto;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.service.ProductCrudService;
import newbies.handmade_mall.service.ProductImageCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerCrudViewController {

    private final ProductCrudService productCrudService;

    private final ProductImageCrudService productImageCrudService;

    /**
     * @param id 삭제 할 id
     * @return soft delete 완료 후 리다이텍트
     */
    @DeleteMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productCrudService.removeProductById(id);

        return "redirect:/partner/product/list";
    }


    /**
     * @return 파트너 상품 리스트 페이지
     */
    @GetMapping("/product/list")
    public String viewProductListPage(Model model, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC, size = 5, page = 0) Pageable pageable) {
        Page<ProductListItemDto> productListViewDtoPage = productCrudService.getProductListItemDtoPage(pageable);

        model.addAttribute("productList", productListViewDtoPage.getContent());
        model.addAttribute("paging", productListViewDtoPage);
        model.addAttribute("currentPage", productListViewDtoPage.getNumber());

        return "/pages/partner/product/list";
    }

    /**
     * 상품 수정 페이지 호출
     *
     * @return 상품 수정 페이지
     */
    @GetMapping("/product/update/{productId}")
    public String viewProductUpdatePage(Model model, @PathVariable Long productId) {

        //상품 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductDto productDto = productCrudService.getProductDto(productId);

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

        return "pages/partner/product/update";

    }

    /**
     * 상품 수정
     *
     * @return 상품 수정 완료 ,리다이렉트 : 상품 목록
     */
    @PutMapping("/product/update")
    public String updateProduct(ProductDto productDto, Product product, ProductImage productImage) {
        //상품 수정 서비스 호출
        productCrudService.update(productDto);

        return "redirect:/partner/product/list";
    }

    /**
     * @return 상품 등록 페이지
     */
    @GetMapping("/product/create")
    public String viewProductCreatePage() {
        return "pages/partner/product/create";
    }


    /**
     * @return 상품 생성 완료, 상품 리스트로 리다이렉트
     */
    @PostMapping("/product/create")
    public String createProduct(ProductDto productDto) {

        productCrudService.createByProductDto(productDto);

        return "redirect:/partner/product/list";
    }
}
