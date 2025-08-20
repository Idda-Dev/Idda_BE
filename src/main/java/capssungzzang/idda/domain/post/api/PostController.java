package capssungzzang.idda.domain.post.api;

import capssungzzang.idda.domain.post.application.PostService;
import capssungzzang.idda.domain.post.dto.PostCreateRequest;
import capssungzzang.idda.domain.post.dto.PostResponse;
import capssungzzang.idda.domain.post.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostCreateRequest request,
                                           @PathVariable("userId") Long memberId) {
        Long postId = postService.createPost(memberId, request);
        URI location = URI.create("/api/users/" + memberId + "/posts/" + postId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("userId") Long memberId,
                                                    @PathVariable("postId") Long postId) {
        PostResponse response = postService.getPostById(memberId, postId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("userId") Long memberId,
            @PathVariable("postId") Long postId,
            @RequestBody PostUpdateRequest request) {
        PostResponse response = postService.updatePost(memberId, postId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("userId") Long memberId,
            @PathVariable("postId") Long postId) {
        postService.deletePost(memberId, postId);
        return ResponseEntity.noContent().build();
    }
}
