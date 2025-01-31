package newbies.handmade_mall.customer;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.LoginDto;
import newbies.handmade_mall.common.ResponseApi;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 회원 가입/로그인/로그아웃
 */
@Controller
@RequiredArgsConstructor
public class CustomerAuthController {

    private final CustomerAuthService customerAuthService;


    @GetMapping("/join")
    public String initJoin() {
        return "pages/join";
    }


    @PostMapping("/join")
    public String processJoin(CustomerDto customerDto, RedirectAttributes redirectAttributes) {

        ResponseApi<Void> responseApi = customerAuthService.join(customerDto);

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("errorMessage", responseApi.getMessage());
        return "redirect:/join";
    }


    @GetMapping("/login")
    public String initLogin() {
        return "/pages/login";
    }


    @PostMapping("/login")
    public String processLogin(LoginDto loginDto, RedirectAttributes redirectAttributes) {

        ResponseApi<Void> responseApi = customerAuthService.login(loginDto);

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("errorMessage", responseApi.getMessage());
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String initLogout() {

        ResponseApi<Void> responseApi = customerAuthService.logout();

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/";
        }

        // 로그아웃 실패 시 임시 처리
        return "redirect:/";
    }
}
