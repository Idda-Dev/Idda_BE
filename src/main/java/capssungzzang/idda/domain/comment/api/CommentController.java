package capssungzzang.idda.domain.comment.api;

import capssungzzang.idda.domain.comment.application.CommentService;
import capssungzzang.idda.domain.comment.dto.CommentCreateRequest;
import capssungzzang.idda.domain.comment.dto.CommentResponse;
import capssungzzang.idda.domain.comment.dto.CommentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.getAllComments(postId);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/users/{userId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("userId") Long memberId,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.updateComment(memberId, postId, commentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("userId") Long memberId,
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(memberId, postId, commentId);
        return ResponseEntity.noContent().build();
    }

}
