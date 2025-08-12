package capssungzzang._dayscottonball.domain.comment.api;

import capssungzzang._dayscottonball.domain.comment.application.CommentService;
import capssungzzang._dayscottonball.domain.comment.dto.CommentCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/posts/{postId}/comments")
    public ResponseEntity<Void> createComment(@RequestBody CommentCreateRequest request,
                                              @PathVariable("userId") Long memberId,
                                              @PathVariable("postId") Long postId) {
        Long commentId = commentService.createComment(memberId, postId, request);
        URI location = URI.create("/api/users/" + memberId +
                "/posts/" + postId + "/comments/" + commentId);
        return ResponseEntity.created(location).build();
    }

}
