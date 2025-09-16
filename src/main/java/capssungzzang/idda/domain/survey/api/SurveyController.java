package capssungzzang.idda.domain.survey.api;

import capssungzzang.idda.domain.survey.application.SurveyService;
import capssungzzang.idda.domain.survey.dto.SurveyResponse;
import capssungzzang.idda.domain.survey.dto.SurveyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PatchMapping("/users/{userId}/surveys/{question-number}")
    public ResponseEntity<Void> updateSurvey(@PathVariable("userId") Long memberId,
                                          @PathVariable("question-number") int questionNumber,
                                          @RequestBody SurveyUpdateRequest request) {
        surveyService.updateSurvey(request, questionNumber, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/surveys/{question-number}")
    public ResponseEntity<SurveyResponse> getSurvey(@PathVariable("userId") Long memberId,
                                                    @PathVariable("question-number") int questionNumber){
        SurveyResponse response = surveyService.getSurvey(questionNumber, memberId);
        return ResponseEntity.ok(response);
    }
}
