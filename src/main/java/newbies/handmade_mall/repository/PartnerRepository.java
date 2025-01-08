package newbies.handmade_mall.repository;

import newbies.handmade_mall.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    public Optional<Partner> findByAccountId(String accountId);
}
