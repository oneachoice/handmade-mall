package newbies.handmade_mall.partner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partner")
public class PartnerHomeController {

    /**
     * @return 파트너 홈 페이지
     */
    @GetMapping
    public String initHome(){
        return "pages/partner/index";
    }
}
