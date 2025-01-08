package newbies.handmade_mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * form 태그에서 GET, POST 외에 추가적인 메소드를 지원하기 위한 설정 클래스입니다.
 */
@Configuration
public class HttpMethodConfig {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
