package capssungzzang.idda.domain.survey.application;

import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.survey.domain.entity.Survey;
import capssungzzang.idda.domain.survey.domain.repository.SurveyRepository;
import capssungzzang.idda.domain.survey.dto.SurveyResponse;
import capssungzzang.idda.domain.survey.dto.SurveyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void updateSurvey(SurveyUpdateRequest request, int questionNumber, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        Survey survey = surveyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "설문이 존재하지 않습니다."));

        survey.updateQuestion(questionNumber, request.getAnswer());
    }

    @Override
    public SurveyResponse getSurvey(int questionNumber, Long memberId) {

        if (questionNumber < 1 || questionNumber > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "questionNumber index 가 유효하지 않습니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        Survey survey = surveyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "설문이 존재하지 않습니다."));

        SurveyResponse response = new SurveyResponse();
        response.setQuestionNumber(questionNumber);

        switch (questionNumber) {
            case 1 -> response.setAnswer(survey.getQuestion1());
            case 2 -> response.setAnswer(survey.getQuestion2());
            case 3 -> response.setAnswer(survey.getQuestion3());
            case 4 -> response.setAnswer(survey.getQuestion4());
            case 5 -> response.setAnswer(survey.getQuestion5());
            case 6 -> response.setAnswer(survey.getQuestion6());
        }

        return response;
    }


}
