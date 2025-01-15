package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.common.ProductImageType;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.dto.req.ProductUpdateDto;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductImageCrudService {


    private final ProductImageRepository productImageRepository;

    private final ProductImageManager productImageManager;


    /**
     * 상품 이미지 업데이트 메서드
     *
     * @return 메인이미지 객체
     */
    public ProductImage update(ProductUpdateDto productUpdateDto, Product product) {

        List<ProductImage> productImages = productImageRepository.findByProductId(product.getId()); //상품FK로 리스트 불러옴

        //리스트 안의 목록 다 삭제
        for (ProductImage productImage : productImages) {
            productImageRepository.deleteById(productImage.getId());
        }

        //새로 입력한 사진 다시 저장
        //카드 이미지 저장
        save(productUpdateDto.getProductCardImages(), ProductImageType.CARD, product);

        //설명 이미지 저장
        save(productUpdateDto.getProductDescriptionImages(), ProductImageType.DESC, product);

        //메인 이미지 저장 후 반환
        return save(productUpdateDto.getMainProductImage(), ProductImageType.MAIN, product);

    }

    public ProductImage create(ProductCreateDto productCreateDto, Product product) {

        // 카드 이미지 저장
        save(productCreateDto.getProductCardImages(), ProductImageType.CARD, product);

        // 설명 이미지 저장
        save(productCreateDto.getProductDescriptionImages(), ProductImageType.DESC, product);

        // 대표 이미지 저장 후 반환
        return save(productCreateDto.getMainProductImage(), ProductImageType.MAIN, product);
    }

    /**
     * 이미지 파일 DB에 저장
     */
    private ProductImage save(MultipartFile imageFile, ProductImageType productImageType, Product product) {
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

    private void save(List<MultipartFile> imageFiles, ProductImageType productImageType, Product product) {
        imageFiles.forEach((imageFile) -> {
            save(imageFile, productImageType, product);
        });
    }
}
