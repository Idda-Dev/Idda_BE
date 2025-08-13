package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VerificationPostService {
    List<VerificationPostResponse> getAllVerificationPosts();
    VerificationPostResponse getVerificationPost(Long postId);
    Long createVerificationPost(Long userId, Long missionId,
                                VerificationPostCreateRequest request,
                                MultipartFile file);
}
