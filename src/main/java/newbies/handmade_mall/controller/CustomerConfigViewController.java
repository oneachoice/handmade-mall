package newbies.handmade_mall.controller;


import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.CustomerDto;
import newbies.handmade_mall.dto.res.CustomerInfoDto;
import newbies.handmade_mall.service.CustomerCrudService;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class CustomerConfigViewController {


    private final CustomerCrudService customerCrudService;

    /**
     * 회원 정보 수정 페이지 생성
     */
    @GetMapping("/account/update")
    public String viewAccountUpdatePage(Model model) {

        //로그인 된 회원의 정보 불러옴
        CustomerInfoDto customerInfoDto = customerCrudService.getCustomerInfoDto();

        //DB에 ***-****-**** 형식으로 저장된 휴대전화번호 분리
        String firstPhoneNumber = customerInfoDto.getPhoneNumber().substring(0, 2);
        String middlePhoneNumber = customerInfoDto.getPhoneNumber().substring(4, customerInfoDto.getPhoneNumber().lastIndexOf("-"));
        String lastPhoneNumber = customerInfoDto.getPhoneNumber().substring(customerInfoDto.getPhoneNumber().lastIndexOf("-") + 1);

        model.addAttribute("loginCustomer", customerInfoDto);
        model.addAttribute("firstPhoneNumber", firstPhoneNumber);
        model.addAttribute("middlePhoneNumber", middlePhoneNumber);
        model.addAttribute("lastPhoneNumber", lastPhoneNumber);

        return "pages/my-page/account/update";
    }

    /**
     * 회원 정보 수정 (DB 변경)
     *
     * @return 마이페이지로 리다이렉트
     */
    @PutMapping("/account/update")
    public String updateCustomerInfo(CustomerDto customerDto) {

        customerCrudService.update(customerDto);

        return "redirect:/my-page";
    }


    /**
     * 회원 탈퇴 페이지 생성
     */
    @GetMapping("/account/cancel")
    public String viewCancelPage(Model model) {
        return "pages/my-page/account/cancel";
    }

    /**
     * 회원 탈퇴(DB 변경)
     */
    @DeleteMapping("/account/cancel")
    public String cancelCustomerAccount() {

        customerCrudService.cancelByCustomerId();

        return "redirect:/logout";
    }


    /**
     * 마이페이지 화면 생성
     */
    @GetMapping
    public String ViewMyPage(Model model) {

        if (SessionManager.getHttpSessionAttribute("customerId") == null) {
            return "redirect:/login";
        }

        return "pages/my-page/index";
    }
}
