package capssungzzang.idda.domain.heart.application;

import capssungzzang.idda.domain.heart.dto.HeartToggleResponse;

public interface HeartService {
    HeartToggleResponse toggleHeart(Long memberId, Long postId);
}
