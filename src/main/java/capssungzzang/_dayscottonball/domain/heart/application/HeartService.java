package capssungzzang._dayscottonball.domain.heart.application;

import capssungzzang._dayscottonball.domain.heart.dto.HeartToggleResponse;

public interface HeartService {
    HeartToggleResponse toggleHeart(Long memberId, Long postId);
}
