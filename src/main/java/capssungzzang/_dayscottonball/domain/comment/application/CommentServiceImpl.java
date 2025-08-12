package capssungzzang._dayscottonball.domain.comment.application;

import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
import capssungzzang._dayscottonball.domain.comment.domain.repository.CommentRepository;
import capssungzzang._dayscottonball.domain.comment.dto.CommentCreateRequest;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public Long createComment(Long memberId, Long postId, CommentCreateRequest request) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(request.getContent())
                .build();

        return commentRepository.save(comment).getId();
    }
}
