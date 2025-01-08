package newbies.handmade_mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 서버 최초 설정 테스트용 컨트롤러입니다.
 * 삭제하시고 개발하시면됩니다.
 */
@Controller
public class ServerInitViewController {

    @GetMapping("/")
    public String helloServer() {
        return "pages/index";
    }
}
