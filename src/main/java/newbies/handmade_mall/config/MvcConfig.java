package newbies.handmade_mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 일반 Mvc 설정 클래스
 */
@Configuration
public class MvcConfig {

    /**
     * RequestContextHolder를 사용하기 위해 등록한 RequestContextListener Bean입니다.
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
