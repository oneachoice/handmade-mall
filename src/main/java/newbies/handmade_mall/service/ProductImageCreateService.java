package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.ProductImageManager;
import newbies.handmade_mall.common.ProductImageType;
import newbies.handmade_mall.dto.req.ProductCreateDto;
import newbies.handmade_mall.entity.Product;
import newbies.handmade_mall.entity.ProductImage;
import newbies.handmade_mall.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImageCreateService {

    private final ProductImageRepository productImageRepository;

    private final ProductImageManager productImageManager;


    public ProductImage create(ProductCreateDto productCreateDto, Product product) {

        // 카드 이미지 저장
        save(productCreateDto.getProductCardImages(), ProductImageType.CARD, product);

        // 설명 이미지 저장
        save(productCreateDto.getProductDescriptionImages(), ProductImageType.DESC, product);

        // 대표 이미지 저장 후 반환
        return save(productCreateDto.getMainProductImage(), ProductImageType.MAIN, product);
    }


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
