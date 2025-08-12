package capssungzzang._dayscottonball.domain.comment.api;

import capssungzzang._dayscottonball.domain.comment.application.CommentService;
import capssungzzang._dayscottonball.domain.comment.dto.CommentCreateRequest;
import capssungzzang._dayscottonball.domain.comment.dto.CommentResponse;
import capssungzzang._dayscottonball.domain.comment.dto.CommentUpdateRequest;
import capssungzzang._dayscottonball.domain.post.dto.PostResponse;
import capssungzzang._dayscottonball.domain.post.dto.PostUpdateRequest;
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
    public ResponseEntity<List<CommentResponse>> getAllPosts(@PathVariable Long postId) {
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

}
