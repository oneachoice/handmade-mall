package newbies.handmade_mall.service;

import lombok.RequiredArgsConstructor;
import newbies.handmade_mall.entity.Partner;
import newbies.handmade_mall.repository.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerReadService {

    private final PartnerRepository partnerRepository;

    public Partner getByAccountId(String accountId) {
        return partnerRepository.findByAccountId(accountId).orElse(null);
    }
}
