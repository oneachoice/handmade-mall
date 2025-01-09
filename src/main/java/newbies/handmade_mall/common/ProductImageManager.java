package newbies.handmade_mall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 상품 이미지 파일을 관리하는 컴포넌트 클래스
 */
@Component
public class ProductImageManager {

    // 이미지 저장 위치
    @Value("${config.product-image-location}")
    private String location;

    // 이미지 리소스 핸들러 경로
    @Value("{config.product-image-resource-handler}")
    private String resourceHandler;

    /**
     * @param imageFile 저장할 이미지 파일
     * @return 저장된 이미지 파일의 이름으로 사용된 uuid
     */
    public UUID save(MultipartFile imageFile) {

        try {
            UUID uuid = UUID.randomUUID();

            String originalFilename = imageFile.getOriginalFilename();
            
            if(originalFilename == null || originalFilename.isBlank()) throw new RuntimeException("이미지 파일의 이름이 존재하지 않음");

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 고유한 파일의 이름 생성
            String uniqueFilename = uuid.toString() + fileExtension;

            // 지정된 경로에 파일 저장
            Path filePath = Paths.get(location, uniqueFilename);
            Files.write(filePath, imageFile.getBytes());

            return uuid;
        } catch (IOException ex) {
            throw new RuntimeException("상품 이미지 파일 저장 실패", ex);
        }
    }


    /**
     * @param imageFileName 확장자를 포함한 이미지 파일의 이름
     * @return 이미지 파일의 URL
     */
    public String createImageUrl(String imageFileName) {
        return resourceHandler + imageFileName;
    }


    /**
     * @param imageFile 이미지 파일
     * @return 이미지 파일의 확장자
     */
    public String getExtensionOf(MultipartFile imageFile) {
        String originalFilename = imageFile.getOriginalFilename();

        if(originalFilename == null || originalFilename.isBlank()) throw new RuntimeException("유효하지 않은 이미지 파일명");

        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}
