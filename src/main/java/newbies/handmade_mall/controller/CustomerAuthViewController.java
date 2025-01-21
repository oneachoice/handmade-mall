package newbies.handmade_mall.controller;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.req.CustomerDto;
import newbies.handmade_mall.dto.req.LoginDto;
import newbies.handmade_mall.service.CustomerAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 회원 가입/로그인/로그아웃
 */
@Controller
@RequiredArgsConstructor
public class CustomerAuthViewController {

    private final CustomerAuthService customerAuthService;

    /**
     * 회원 가입 페이지 생성
     */
    @GetMapping("/join")
    public String viewCustomerJoinPage() {
        return "pages/join";
    }

    /**
     * 회원 가입(DB에 저장)
     *
     * @return 회원가입 성공 시 홈, 실패 시 회원가입 페이지로 리다이렉트
     */
    @PostMapping("/join")
    public String customerJoin(CustomerDto customerDto, RedirectAttributes redirectAttributes) {
        //회원가입 성공
        if (customerAuthService.join(customerDto)) {
            return "redirect:/";
        }
        //회원가입 실패 시 오류 메세지 출력
        redirectAttributes.addFlashAttribute("errorMessage", "아이디가 중복되었습니다.");
        return "redirect:/join";
    }

    /**
     * 로그인 페이지 화면 생성
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        return "/pages/login";
    }


    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(LoginDto loginDto, RedirectAttributes redirectAttributes) {

        //로그인 성공 시
        if (customerAuthService.login(loginDto)) {
            return "redirect:/";
        }

        //로그인 실패 시
        redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 패스워드를 확인해주세요.");

        return "redirect:/login";
    }


    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String Logout() {

        customerAuthService.logout();

        return "redirect:/";
    }
}
