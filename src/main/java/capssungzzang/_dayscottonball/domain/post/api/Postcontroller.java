package capssungzzang._dayscottonball.domain.post.api;

import capssungzzang._dayscottonball.domain.post.application.PostService;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
}
