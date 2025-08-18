package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VerificationPostService {
    List<VerificationPostResponse> getAllVerificationPosts();
    VerificationPostResponse getVerificationPost(Long memberId, Long postId);
    Long createVerificationPost(Long memberId, Long missionId,
                                VerificationPostCreateRequest request,
                                MultipartFile file);
}
