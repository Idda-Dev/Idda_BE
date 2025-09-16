package capssungzzang.idda.domain.survey.domain.repository;

import capssungzzang.idda.domain.survey.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
