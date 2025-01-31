package newbies.handmade_mall.partner;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.common.LoginDto;
import newbies.handmade_mall.common.ResponseApi;
import newbies.handmade_mall.util.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 파트너의 로그인을 담당하는 서비스
 */
@Service
@RequiredArgsConstructor
public class PartnerAuthService {

    private final PartnerRepository partnerRepository;

    /**
     * 로그인 성공 시 세션에 파트너 아이디를 추가합니다.
     * @return 로그인 성공 true, 로그인 실패 false
     */
    public ResponseApi<Void> login(LoginDto loginDto) {

        // 아이디, 비밀번호
        String partnerAccountId = loginDto.getId();
        String partnerAccountPassword = loginDto.getPassword();

        // DB로부터 계정 가져오기 시도
        Optional<Partner> optionalPartner = partnerRepository.findByAccountId(partnerAccountId);

        // 존재하지 않는 계정이면 메서드 종료
        if(optionalPartner.isEmpty()) return ResponseApi.of(HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다.");

        // 엔티티 얻기
        Partner foundPartner = optionalPartner.get();

        // 비밀번호가 일치하지 않으면 메서드 종료
        if(!foundPartner.getAccountPassword().equals(partnerAccountPassword)) return ResponseApi.of(HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다.");

        SessionManager.setHttpSessionAttribute("partnerId",  partnerAccountId);

        return ResponseApi.of(HttpStatus.OK, "로그인 성공");
    }

    public ResponseApi<Void> logout() {
        SessionManager.removeHttpSessionAttribute("partnerId");

        return ResponseApi.of(HttpStatus.OK, "로그아웃 성공");
    }
}
