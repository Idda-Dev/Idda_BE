package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createPost(Long memberId, PostCreateRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        Post post = Post.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return postRepository.save(post).getId();
    }
}
