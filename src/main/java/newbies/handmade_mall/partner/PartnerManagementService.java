package newbies.handmade_mall.partner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerManagementService {

    private final PartnerRepository partnerRepository;

    public Partner getByAccountId(String accountId) {
        return partnerRepository.findByAccountId(accountId).orElse(null);
    }

}
