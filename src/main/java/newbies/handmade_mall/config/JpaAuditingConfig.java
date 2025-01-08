package newbies.handmade_mall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring Data JPA에서 Auditing 기능을 활성화하기 위한 설정
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
