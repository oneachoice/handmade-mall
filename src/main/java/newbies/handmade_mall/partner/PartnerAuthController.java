package newbies.handmade_mall.partner;


import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.LoginDto;
import newbies.handmade_mall.common.ResponseApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 파트너 경로의 로그인을 담당하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerAuthController {

    private final PartnerAuthService partnerAuthService;


    @GetMapping("/login")
    public String initLogin() {

        return "pages/partner/login";
    }


    @PostMapping("/login")
    public String processLogin(LoginDto loginDto, RedirectAttributes redirectAttributes) {

        ResponseApi<Void> responseApi = partnerAuthService.login(loginDto);

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            // 로그인 성공
            return "redirect:/partner";
        }

        redirectAttributes.addFlashAttribute("errorMessage", responseApi.getMessage());

        // 로그인 실패
        return "redirect:/partner/login";
    }

    /**
     * 로그아웃
     *
     * @return 파트너 로그인 페이지로 이동
     */
    @GetMapping("/logout")
    public String initLogout() {
        ResponseApi<Void> responseApi = partnerAuthService.logout();

        if (responseApi.getHttpStatus().is2xxSuccessful()) {
            return "redirect:/partner/login";
        }

        return "redirect:/partner/login";
    }
}
