package newbies.handmade_mall.checkout;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import newbies.handmade_mall.dto.res.CheckoutProductDto;
import newbies.handmade_mall.dto.res.CheckoutProductViewDto;
import newbies.handmade_mall.product.ProductManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 주문 생성 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CustomerCheckoutCrudController {

    private final CheckoutCrudService checkoutCrudService;

    private final CheckoutProductCrudService checkoutProductCrudService;

    private final ProductManagementService productManagementService;

    /**
     * 주문페이지 생성
     */
    @GetMapping("/create")
    public String viewCheckoutCreatePage(){

        return "pages/checkout/create";
    }

    /**
     * 주문페이지에 상품정보와 상품 수량을 넘겨주기 위한 메서드
     */
    @PostMapping("/create")
    public String checkout(CheckoutProductDto checkoutProductDto, RedirectAttributes redirectAttributes) {

        //로그인 실패
        if(!checkoutCrudService.checkLogin()){
            redirectAttributes.addFlashAttribute("errorMessage","로그인 되지 않았습니다");
            return "redirect:/login";
        }

        CheckoutProductViewDto checkoutProductViewDto = checkoutCrudService.viewCheckoutCreateProduct(checkoutProductDto);

        redirectAttributes.addFlashAttribute("productId",checkoutProductViewDto.getProductId());
        redirectAttributes.addFlashAttribute("productName", checkoutProductViewDto.getProductName());
        redirectAttributes.addFlashAttribute("count",checkoutProductViewDto.getCount());
        redirectAttributes.addFlashAttribute("discountedPrice",checkoutProductViewDto.getDiscountedPrice());
        redirectAttributes.addFlashAttribute("totalDiscountedPrice",checkoutProductViewDto.getTotalDiscountedPrice());
        redirectAttributes.addFlashAttribute("totalSellingPrice",checkoutProductViewDto.getTotalSellingPrice());
        redirectAttributes.addFlashAttribute("totalDiscount",checkoutProductViewDto.getTotalDiscount());
        redirectAttributes.addFlashAttribute("shippingFee",checkoutProductViewDto.getShippingFee());
        redirectAttributes.addFlashAttribute("totalPrice",checkoutProductViewDto.getTotalPrice());
        redirectAttributes.addFlashAttribute("mainImagePath",checkoutProductViewDto.getMainImagePath());

        return "redirect:/checkout/create";
    }

    /**
     * @return 주문성공 후 페이지 리다이렉트
     */
    @PostMapping("")
    public String checkout(CheckoutDto checkoutDto, RedirectAttributes redirectAttributes){
        //주문 테이블 생성
        CheckoutProductDto checkoutProductDto = checkoutCrudService.createCheckout(checkoutDto);

        //주문 상세 테이블 생성
        checkoutProductCrudService.toCheckoutProductEntity(checkoutProductDto);

        //주문 성공 화면에 보여줄 dto 불러오기
        CheckoutProductViewDto  checkoutProductViewDto = productManagementService.viewCheckoutSuccessPage(checkoutProductDto.getCheckoutId(),checkoutDto.getProductId());

        redirectAttributes.addFlashAttribute("checkoutDate",checkoutProductViewDto.getCreatedAt());
        redirectAttributes.addFlashAttribute("productName",checkoutProductViewDto.getProductName());
        redirectAttributes.addFlashAttribute("checkoutCode",checkoutProductViewDto.getCheckoutCode());

        return "redirect:/checkout/success";
    }

    /**
     * 주문 성공 페이지 생성
     */
    @GetMapping("/success")
    public String viewCheckoutSuccessPage(){
        return "pages/checkout/success";
    }

    /**
     * 주문 상세 페이지 생성
     */
    @GetMapping("/details/{checkoutId}")
    public String viewCheckoutDetailsPage(@PathVariable Long checkoutId, Model model) {

        CheckoutProductViewDto checkoutProductViewDto = checkoutProductCrudService.getCheckoutProductViewDto(checkoutId);

        model.addAttribute("productId", checkoutProductViewDto.getProductId());
        model.addAttribute("productName", checkoutProductViewDto.getProductName());
        model.addAttribute("totalSellingPrice", checkoutProductViewDto.getTotalSellingPrice());
        model.addAttribute("totalDiscountedPrice", checkoutProductViewDto.getTotalDiscountedPrice());
        model.addAttribute("shippingFee", checkoutProductViewDto.getShippingFee());
        model.addAttribute("count", checkoutProductViewDto.getCount());
        model.addAttribute("recipient", checkoutProductViewDto.getRecipient());
        model.addAttribute("recipientPhoneNumber", checkoutProductViewDto.getRecipientPhoneNumber());
        model.addAttribute("shippingAddress", checkoutProductViewDto.getShippingAddress());
        model.addAttribute("totalPrice", checkoutProductViewDto.getTotalPrice());
        model.addAttribute("checkoutProductState", checkoutProductViewDto.getCheckoutProductState());
        model.addAttribute("mainImagePath", checkoutProductViewDto.getMainImagePath());

        return "pages/checkout/details";
    }


    /**
     * @return 주문 목록 상세 페이지
     */
    @GetMapping("/details")
    public String viewCheckoutDetailsPage() {
        return "pages/checkout/details";
    }


    /**
     * @return 주문 목록 리스트 페이지
     */
    @GetMapping("/list")
    public String viewCheckoutListPage(@PageableDefault(size = 10,
            sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable , Model model,RedirectAttributes redirectAttributes) {

        Page<CheckoutListDto> checkoutList = checkoutCrudService.getCheckoutListDtoPage(pageable);

        if(checkoutList == null) {
            redirectAttributes.addFlashAttribute("errorMessage","주문 상품이 없습니다.");
            return "redirect:/my-page";
        }

        model.addAttribute("checkouts", checkoutList.getContent());
        model.addAttribute("currentPage", checkoutList.getNumber());
        model.addAttribute("totalPages", checkoutList.getTotalPages());
        model.addAttribute("totalElements", checkoutList.getTotalElements());
        model.addAttribute("paging", checkoutList);

        return "pages/checkout/checkoutList";
    }
}
