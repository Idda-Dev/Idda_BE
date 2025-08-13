package capssungzzang._dayscottonball.domain.post.api;

import capssungzzang._dayscottonball.domain.post.application.VerificationPostService;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationPostController {

    private final VerificationPostService verificationPostService;

    @GetMapping("/missions/posts")
    public ResponseEntity<List<VerificationPostResponse>> getAllVerificationPosts() {
        List<VerificationPostResponse> responses = verificationPostService.getAllVerificationPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/missions/posts/{postId}")
    public ResponseEntity<VerificationPostResponse> getVerificationPost(@PathVariable("postId") Long postId) {
        VerificationPostResponse response = verificationPostService.getVerificationPost(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/users/{userId}/missions/{missionId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> createVerificationPost(
            @PathVariable Long userId,
            @PathVariable Long missionId,
            @ModelAttribute VerificationPostCreateRequest request,
            @RequestPart("file") MultipartFile file) {
        Long postId = verificationPostService.createVerificationPost(
                userId, missionId, request, file);

        return ResponseEntity
                .created(URI.create("/api/missions/posts/" + postId))
                .build();
    }
}
