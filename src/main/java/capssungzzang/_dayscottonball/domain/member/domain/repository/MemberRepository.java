package capssungzzang._dayscottonball.domain.member.domain.repository;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
}
