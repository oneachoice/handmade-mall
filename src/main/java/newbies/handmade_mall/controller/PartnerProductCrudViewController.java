package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.req.ProductDto;
import newbies.handmade_mall.dto.res.ProductListViewDto;
import newbies.handmade_mall.dto.res.ProductUpdateImageViewDto;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.service.ProductCrudService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerProductCrudViewController {

    private final ProductCrudService productCrudService;

    private final ProductImageManager productImageManager;
    

    /**
     * @param id 삭제 할 id
     * @return soft delete 완료 후 리다이텍트
     */
    @DeleteMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productCrudService.delete(id);

        return "redirect:/partner/product/list";
    }


    /**
     * @return 파트너 상품 리스트 페이지
     */
    @GetMapping("/product/list")
    public String viewProductListPage(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        List<ProductListViewDto> productListViewDtoList = productCrudService.viewDto();

        model.addAttribute("productList", productListViewDtoList);

        Page<ProductListViewDto> productListViewDtoPage = productCrudService.getPagedProductList(page);

        model.addAttribute("paging", productListViewDtoPage);
        model.addAttribute("currentPage", page);

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
        ProductDto productDto = productCrudService.view(productId);

        //상품 이미지 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductUpdateImageViewDto productUpdateImageViewDto = productCrudService.viewImage(productId);


        ProductImage productMainImage = productUpdateImageViewDto.getMainProductImage();

        String mainImagePath = null;

        if (productMainImage != null) {
            String mainImageUuid = productMainImage.getUuid().toString();
            String mainImageExtension = productMainImage.getFileExtension();

            //메인이미지 경로(위치+UUID+확장자)
            mainImagePath = productImageManager.createImageUrl(mainImageUuid + mainImageExtension);
        }

        model.addAttribute("mainImagePath", mainImagePath);


        //카드이미지 경로를 담을 리스트 생성
        List<String> newCardImageList = new ArrayList<>();

        //카드이미지 경로(위치+UUID+확장자)
        for (int i = 0; i < productUpdateImageViewDto.getProductCardImages().size(); i++) {
            ProductImage productCardImage = productUpdateImageViewDto.getProductCardImages().get(i);

            newCardImageList.add(productImageManager.createImageUrl(productCardImage.getUuid() + productCardImage.getFileExtension()));
        }

        model.addAttribute("cardImagePath", newCardImageList);

        //설명이미지 경로를 담을 리스트 생성
        List<String> newDescriptionImageList = new ArrayList<>();

        //설명이미지 경로(위치+UUID+확장자)
        for (int i = 0; i < productUpdateImageViewDto.getProductDescriptionImages().size(); i++) {
            ProductImage productDescriptionImage = productUpdateImageViewDto.getProductDescriptionImages().get(i);

            newDescriptionImageList.add(productImageManager.createImageUrl(productDescriptionImage.getUuid() + productDescriptionImage.getFileExtension()));
        }


        //모델에 담기
        model.addAttribute("descriptionImagePath", newDescriptionImageList); //설명이미지 경로
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

        productCrudService.create(productDto);

        return "redirect:/partner/product/list";
    }
}
