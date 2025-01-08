package newbies.handmade_mall.data_loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import newbies.handmade_mall.entity.Partner;
import newbies.handmade_mall.repository.PartnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 서버 부팅 시 파트너 계정을 생성해주는 데이터로더 컴포넌트
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class PartnerLoader implements CommandLineRunner {

    private final PartnerRepository partnerRepository;

    @Transactional // 트랜잭션, 메서드 단위 적용
    @Override
    public void run(String... args) throws Exception {

        // 접속 계정
        final String ACCOUNT_ID = "admin";
        final String ACCOUNT_PASSWORD = "123456";

        // DB로부터 계정 가져오기 시도
        Optional<Partner> optionalPartner = partnerRepository.findByAccountId(ACCOUNT_ID);

        // 이미 DB에 해당 계정이 존재하는지 확인, 계정이 있다면 메서드 종료
        if(optionalPartner.isPresent()) return;

        // 파트너 엔티티 생성
        Partner newPartner = Partner.builder()
                .accountId(ACCOUNT_ID)
                .accountPassword(ACCOUNT_PASSWORD)
                .build();

        // 생성된 파트너 엔티티 영속화
        Partner createdPartner = partnerRepository.save(newPartner);

        // 성공적으로 생성 시 로그 출력, Reflection API를 사용하여 동적으로 클래스 이름을 가져옵니다.
        log.info("{}의 {} 계정이 성공적으로 생성됨.", createdPartner.getClass().getName() ,ACCOUNT_ID);
    }
}
