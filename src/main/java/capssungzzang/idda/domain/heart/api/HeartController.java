package capssungzzang.idda.domain.heart.api;

import capssungzzang.idda.domain.heart.application.HeartService;
import capssungzzang.idda.domain.heart.dto.HeartToggleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/posts/{postId}/hearts/users/{userId}")
    public ResponseEntity<HeartToggleResponse> toggleHeart(
            @PathVariable("userId") Long memberId,
            @PathVariable("postId") Long postId) {
        HeartToggleResponse response = heartService.toggleHeart(memberId, postId);
        return ResponseEntity.ok(response);
    }
}
