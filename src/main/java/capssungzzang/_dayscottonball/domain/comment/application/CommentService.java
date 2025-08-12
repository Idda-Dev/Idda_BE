package capssungzzang._dayscottonball.domain.comment.application;

import capssungzzang._dayscottonball.domain.comment.dto.CommentCreateRequest;

public interface CommentService {
    Long createComment(Long memberId, Long PostId, CommentCreateRequest request);
//    List<CommentResponse> getAllComments(Long postId);
//    CommentResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest request);
//    void deleteComment(Long memberId, Long commentId);
}
