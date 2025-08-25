package capssungzzang.idda.domain.post.application;

import capssungzzang.idda.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang.idda.domain.post.dto.VerificationPostDailyResponse;
import capssungzzang.idda.domain.post.dto.VerificationPostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface VerificationPostService {
    List<VerificationPostResponse> getAllVerificationPosts(String location);
    VerificationPostResponse getVerificationPost(Long memberId, Long postId);
    Long createVerificationPost(Long memberId, Long missionId,
                                VerificationPostCreateRequest request,
                                MultipartFile file);
    VerificationPostDailyResponse getDailyVerificationPost(Long memberId, LocalDate kstDate);
}
