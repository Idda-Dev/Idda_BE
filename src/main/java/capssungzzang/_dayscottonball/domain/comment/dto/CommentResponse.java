package capssungzzang._dayscottonball.domain.comment.dto;

import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Long memberId;
    private Long postId;
    private Long commentId;
    private String profileImageUrl;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
