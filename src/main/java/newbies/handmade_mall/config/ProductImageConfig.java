package newbies.handmade_mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 저장된 이미지 파일을 URL주소로 가져오기 위한 설정 클래스입니다.
 */
@Configuration
public class ProductImageConfig implements WebMvcConfigurer {

    @Value("${config.product-image-location}")
    private String productImageLocation;

    @Value("${config.product-image-resource-handler}")
    private String productImageResourceHandler;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(productImageResourceHandler + "**")
                .addResourceLocations("file:///" + productImageLocation);
    }
}