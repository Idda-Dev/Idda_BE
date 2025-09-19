package capssungzzang.idda.domain.survey.domain.repository;

import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.survey.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findByMemberId(Long memberId);
}
