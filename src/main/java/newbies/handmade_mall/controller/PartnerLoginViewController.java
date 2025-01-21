package newbies.handmade_mall.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.LoginDto;
import newbies.handmade_mall.service.PartnerLoginService;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String login(LoginDto loginDto, RedirectAttributes redirectAttributes) {

        if (partnerLoginService.login(loginDto)) {
            // 로그인 성공
            return "redirect:/partner";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 패스워드를 확인해주세요.");

        // 로그인 실패
        return "redirect:/partner/login";
    }

    /**
     * 로그아웃
     *
     * @return 파트너 로그인 페이지로 이동
     */
    @GetMapping("/logout")
    public String partnerLogout() {
        //세션 삭제
        SessionManager.removeHttpSessionAttribute("partnerId");

        return "redirect:/partner/login";
    }
}
