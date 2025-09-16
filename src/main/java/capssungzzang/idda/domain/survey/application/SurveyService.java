package capssungzzang.idda.domain.survey.application;

import capssungzzang.idda.domain.survey.domain.entity.Survey;
import capssungzzang.idda.domain.survey.dto.SurveyResponse;
import capssungzzang.idda.domain.survey.dto.SurveyUpdateRequest;

public interface SurveyService {
    void updateSurvey(SurveyUpdateRequest request, int questionNumber, Long memberId);
    SurveyResponse getSurvey(int questionNumber, Long memberId);
}
