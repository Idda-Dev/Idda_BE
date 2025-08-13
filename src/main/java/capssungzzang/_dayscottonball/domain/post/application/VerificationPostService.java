package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;

import java.util.List;

public interface VerificationPostService {
    List<VerificationPostResponse> getAllVerificationPosts();
    VerificationPostResponse getVerificationPost(Long postId);
}
