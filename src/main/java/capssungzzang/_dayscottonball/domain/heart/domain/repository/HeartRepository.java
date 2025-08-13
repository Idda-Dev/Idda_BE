package capssungzzang._dayscottonball.domain.heart.domain.repository;

import capssungzzang._dayscottonball.domain.heart.domain.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
    long deleteByPostIdAndMemberId(Long postId, Long memberId);
}
