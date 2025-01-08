package newbies.handmade_mall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 상속 시 엔티티에 '생성일'과 '수정일'을 컬럼으로 추가해줍니다.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public abstract class BaseTime {

    @Comment("생성일")
    @CreatedDate
    @Column(
            updatable = false,
            name = "created_at",
            nullable = false
    )
    protected LocalDateTime createdAt;

    @Comment("마지막 수정일")
    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    protected LocalDateTime updatedAt;

}
