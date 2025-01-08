package newbies.handmade_mall.controller;


import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.LoginDto;
import newbies.handmade_mall.service.PartnerLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 파트너 경로의 로그인을 담당하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerLoginViewController {

    private final PartnerLoginService partnerLoginService;

    /**
     * @return 파트너 로그인 페이지
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "pages/partner/login";
    }

    /**
     * @param loginDto 로그인용 DTO
     * @return 성공 시 '/partner', 실패 시 '/partner/login'
     */
    @PostMapping("/login")
    public String login(LoginDto loginDto) {

        if (partnerLoginService.login(loginDto)) {
            // 로그인 성공
            return "redirect:/partner";
        }

        // 로그인 실패
        return "redirect:/partner/login";
    }
}
