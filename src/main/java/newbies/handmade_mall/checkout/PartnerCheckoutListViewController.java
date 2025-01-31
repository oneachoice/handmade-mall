package newbies.handmade_mall.checkout;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.CheckoutListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PartnerCheckoutListViewController {

    private final CheckoutProductCrudService checkoutProductCrudService;

    @GetMapping("/partner/checkout/list")
    public String viewPartnerCheckoutListPage(Model model, @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable){

        Page<CheckoutListDto> checkoutList = checkoutProductCrudService.getPartnerCheckoutListViewDto(pageable);

        model.addAttribute("checkouts", checkoutList.getContent());
        model.addAttribute("currentPage", checkoutList.getNumber());
        model.addAttribute("totalPages", checkoutList.getTotalPages());
        model.addAttribute("totalElements", checkoutList.getTotalElements());
        model.addAttribute("paging", checkoutList);

        return "pages/partner/checkout/list";
    }
}
