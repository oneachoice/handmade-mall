package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.dto.req.ProductUpdateDto;
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
public class PartnerCrudViewController {

    private final ProductCrudService productCrudService;

    private final ProductImageManager productImageManager;

    /**
     * @return 모달창 페이지
     */
    @GetMapping("/product/delete/{id}")
    public String viewProductDeletePage(@PathVariable Long id, Model model){

        Product product = productCrudService.findById(id);

        model.addAttribute("product", product);

        return "pages/partner/product/delete";
    }

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
     *
     * @return 파트너 상품 리스트 페이지
     */
    @GetMapping("/product/list")
    public String viewProductListPage(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        List<ProductListViewDto> productListViewDtos = productCrudService.viewDto();

        model.addAttribute("productList", productListViewDtos);

        Page<ProductListViewDto> paging = productCrudService.getPagedProductList(page);
        model.addAttribute("paging", paging);
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
        ProductUpdateDto productUpdateDto = productCrudService.view(productId);

        //상품 이미지 정보 나타내주기 위한 서비스 호출(모델에 넣을 DTO 불러오기)
        ProductUpdateImageViewDto productUpdateImageViewDto = productCrudService.viewImage(productId);


        //모델에 담기
        if (productUpdateDto != null) {
            model.addAttribute("productId", productUpdateDto.getProductId()); //상품 아이디
            model.addAttribute("productName", productUpdateDto.getProductName()); //상품명
            model.addAttribute("category", productUpdateDto.getCategory()); //카테고리
            model.addAttribute("sellingPrice", productUpdateDto.getSellingPrice()); //판매가
            model.addAttribute("costPrice", productUpdateDto.getCostPrice()); //원가
            model.addAttribute("discountRate", productUpdateDto.getDiscountRate()); //할인율
            model.addAttribute("discountedPrice", productUpdateDto.getDiscountedPrice()); //할인가
            model.addAttribute("margin", productUpdateDto.getMargin()); //마진
            model.addAttribute("marginRate", productUpdateDto.getMarginRate()); //마진율
            model.addAttribute("shippingFee", productUpdateDto.getShippingFee()); //배송비
            model.addAttribute("count", productUpdateDto.getCount()); //재고 수량
        }

        //메인 이미지 null 체크
        if (productUpdateImageViewDto.getMainProductImage() != null) {
            //메인이미지 경로(위치+UUID+확장자)
            String mainImagePath = productImageManager.createImageUrl(productUpdateImageViewDto.getMainProductImage().getUuid() + productUpdateImageViewDto.getMainProductImage().getFileExtension());
            model.addAttribute("mainImagePath", mainImagePath );
        }
        //카드 이미지 null 체크
        if (productUpdateImageViewDto.getProductCardImages() != null) {
            //카드이미지 경로를 담을 리스트 생성
            List<String> newCardImageList = new ArrayList<>();

            //카드이미지 경로(위치+UUID+확장자)
            for (int i = 0; i < productUpdateImageViewDto.getProductCardImages().size(); i++) {
                newCardImageList.add(productImageManager.createImageUrl(productUpdateImageViewDto.getProductCardImages().get(i).getUuid() + productUpdateImageViewDto.getProductCardImages().get(i).getFileExtension()));
            }

            model.addAttribute("cardImagePath",newCardImageList);
        }

        //설명 이미지 null 체크
        if (productUpdateImageViewDto.getProductDescriptionImages() != null) {
            //설명이미지 경로를 담을 리스트 생성
            List<String> newDescriptionImageList = new ArrayList<>();

            //설명이미지 경로(위치+UUID+확장자)
            for(int i = 0; i< productUpdateImageViewDto.getProductDescriptionImages().size() ; i++) {
                newDescriptionImageList.add(productImageManager.createImageUrl(productUpdateImageViewDto.getProductDescriptionImages().get(i).getUuid()+ productUpdateImageViewDto.getProductDescriptionImages().get(i).getFileExtension()));
            }


            model.addAttribute("descriptionImagePath", newDescriptionImageList); //설명이미지 경로
        }
        return "pages/partner/product/update";

    }

    /**
     * 상품 수정
     *
     * @return 상품 수정 완료 ,리다이렉트 : 상품 목록
     */
    @PutMapping("/product/update")
    public String updateProduct(ProductUpdateDto productUpdateDto, Product product, ProductImage productImage) {

        productCrudService.update(productUpdateDto); //상품 수정 서비스 호출

        return "redirect:/partner/product/list";
    }

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

        productCrudService.create(productCreateDto);

        return "redirect:/partner/product/list";
    }
}
