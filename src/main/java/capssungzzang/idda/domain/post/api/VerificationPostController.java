package capssungzzang.idda.domain.post.api;

import capssungzzang.idda.domain.post.application.VerificationPostService;
import capssungzzang.idda.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang.idda.domain.post.dto.VerificationPostResponse;
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
    public ResponseEntity<List<VerificationPostResponse>> getAllVerificationPosts(@RequestParam("location") String location) {
        List<VerificationPostResponse> responses = verificationPostService.getAllVerificationPosts(location);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/missions/users/{userId}/posts/{postId}")
    public ResponseEntity<VerificationPostResponse> getVerificationPost(@PathVariable("userId") Long memberId,
                                                                        @PathVariable("postId") Long postId) {
        VerificationPostResponse response = verificationPostService.getVerificationPost(memberId, postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/users/{userId}/missions/{missionId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> createVerificationPost(
            @PathVariable("userId") Long memberId,
            @PathVariable("missionId") Long missionId,
            @ModelAttribute VerificationPostCreateRequest request,
            @RequestPart("file") MultipartFile file) {
        Long postId = verificationPostService.createVerificationPost(
                memberId, missionId, request, file);

        return ResponseEntity
                .created(URI.create("/api/missions/posts/" + postId))
                .build();
    }
}
