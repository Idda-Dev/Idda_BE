package capssungzzang._dayscottonball.domain.post.api;

import capssungzzang._dayscottonball.domain.post.application.PostService;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class Postcontroller {

    private final PostService postService;

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostCreateRequest request,
                                           @PathVariable("userId") Long memberId) {
        Long postId = postService.createPost(memberId, request);
        URI location = URI.create("/api/users/" + memberId + "/posts/" + postId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{userId}/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts(@PathVariable("userId") Long memberId) {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("{userId}/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("userId") Long memberId,
                                                    @PathVariable("postId") Long postId) {
        PostResponse response = postService.getPostById(memberId, postId);
        return ResponseEntity.ok(response);
    }
}
