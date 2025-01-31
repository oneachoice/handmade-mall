package newbies.handmade_mall.customer;


import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ResponseApi;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class CustomerMyPageController {


    private final CustomerManagementService customerManagementService;


    @GetMapping
    public String initMyPage(Model model) {

        if (SessionManager.getHttpSessionAttribute("customerId") == null) {
            return "redirect:/login";
        }

        return "pages/my-page/index";
    }


    @GetMapping("/account/update")
    public String initAccountUpdate(Model model) {

        //로그인 된 회원의 정보 불러옴
        ResponseApi<CustomerDto> responseApi = customerManagementService.getCustomerDto();

        CustomerDto customerDto = responseApi.getOptData().orElseThrow(() -> new RuntimeException("계정 정보가 존재하지 않음"));

        //DB에 ***-****-**** 형식으로 저장된 휴대전화번호 분리
        String[] phoneNumbers = customerDto.getPhoneNumber().split(",");

        model.addAttribute("loginCustomer", customerDto);
        model.addAttribute("firstPhoneNumber", phoneNumbers[0]);
        model.addAttribute("middlePhoneNumber", phoneNumbers[1]);
        model.addAttribute("lastPhoneNumber", phoneNumbers[2]);

        return "pages/my-page/account/update";
    }


    @PutMapping("/account/update")
    public String processAccountUpdate(CustomerDto customerDto) {

        ResponseApi<Void> responseApi = customerManagementService.update(customerDto);

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/my-page";
        }

        // 임시 처리
        return "redirect:/my-page";
    }



    @GetMapping("/account/cancel")
    public String initAccountCancel() {
        return "pages/my-page/account/cancel";
    }


    @DeleteMapping("/account/cancel")
    public String processAccountCancel() {

        ResponseApi<Void> responseApi = customerManagementService.cancel();

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/logout";
        }

        // 삭제 실패
        return "redirect:/my-page/account/cancel";
    }
}
