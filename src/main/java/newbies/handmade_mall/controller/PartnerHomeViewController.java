package newbies.handmade_mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partner")
public class PartnerHomeViewController {

    /**
     * @return 파트너 홈 페이지
     */
    @GetMapping
    public String viewHomePage(){
        return "pages/partner/index";
    }
}
