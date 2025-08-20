package capssungzzang.idda.domain.heart.application;

import capssungzzang.idda.domain.heart.domain.entity.Heart;
import capssungzzang.idda.domain.heart.domain.repository.HeartRepository;
import capssungzzang.idda.domain.heart.dto.HeartToggleResponse;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.post.domain.entity.Post;
import capssungzzang.idda.domain.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartServiceImpl implements HeartService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final HeartRepository heartRepository;

    @Override
    public HeartToggleResponse toggleHeart(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (heartRepository.existsByPostIdAndMemberId(postId, memberId)) {
            heartRepository.deleteByPostIdAndMemberId(postId, memberId);
        } else {
            try {
                heartRepository.save(Heart.builder()
                        .post(post)
                        .member(member)
                        .build());
            } catch (DataIntegrityViolationException ignored) {
            }
        }

        HeartToggleResponse response = new HeartToggleResponse();
        response.setLiked(heartRepository.existsByPostIdAndMemberId(postId, memberId));
        return response;
    }
}
