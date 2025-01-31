package newbies.handmade_mall.product;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.dto.res.ProductImageUrlDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImageManagementService {


    private final ProductImageRepository productImageRepository;

    private final ProductImageManager productImageManager;

    private final ProductImageMapper productImageMapper;


    /**
     * 상품 이미지 업데이트 메서드
     *
     * @return 메인이미지 객체
     */
    public ProductImage update(ProductDto productDto, Product product) {

        // 기존 이미지 모두 삭제
        productImageRepository.deleteByProductId(product.getId());

        //카드 이미지 저장
        save(productDto.getProductCardImages(), product, ProductImageType.CARD);

        //설명 이미지 저장
        save(productDto.getProductDescriptionImages(), product, ProductImageType.DESC);

        // Product에 저장하기 위한 상품 대표이미지
        return save(productDto.getMainProductImage(), product, ProductImageType.MAIN);

    }

    public ProductImage create(ProductDto productDto, Product product) {

        // 카드 이미지 저장
        save(productDto.getProductCardImages(), product, ProductImageType.CARD);

        // 설명 이미지 저장
        save(productDto.getProductDescriptionImages(), product, ProductImageType.DESC);

        // 대표 이미지 저장 후 반환
        return save(productDto.getMainProductImage(), product, ProductImageType.MAIN);
    }

    public List<ProductImage> getProductImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId);
    }

    /**
     * DB에 등록되어 있는 상품별 이미지 정보를 수정 화면에 나타내기 위한 메서드
     *
     * @return 상품 기존 이미지 정보 DTO
     */
    public ProductImageUrlDto getProductImagesDto(Long productId) {

        //제품 FK로 찾은 리스트
        List<ProductImage> productImages = getProductImagesByProductId(productId);

        return productImageMapper.toProductImageUrlDto(productImages);
    }

    /**
     * 이미지 파일 DB에 저장
     */
    private ProductImage save(MultipartFile imageFile, Product product, ProductImageType productImageType) {
        // 이미지 파일 존재 여부 확인
        if (imageFile.isEmpty()) return null;

        UUID presentImageUuid = productImageManager.save(imageFile);

        ProductImage mainProductImage = ProductImage.builder()
                .uuid(presentImageUuid)
                .fileExtension(productImageManager.getExtensionOf(imageFile))
                .productImageType(productImageType)
                .imageName(imageFile.getName())
                .product(product)
                .build();

        return productImageRepository.save(mainProductImage);
    }

    private void save(List<MultipartFile> imageFiles, Product product, ProductImageType productImageType) {
        imageFiles.forEach((imageFile) -> {
            save(imageFile, product, productImageType);
        });
    }
}
