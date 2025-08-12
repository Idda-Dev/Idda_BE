package capssungzzang._dayscottonball.domain.comment.application;

import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
import capssungzzang._dayscottonball.domain.comment.domain.repository.CommentRepository;
import capssungzzang._dayscottonball.domain.comment.dto.CommentCreateRequest;
import capssungzzang._dayscottonball.domain.comment.dto.CommentResponse;
import capssungzzang._dayscottonball.domain.comment.dto.CommentUpdateRequest;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @Override
    public List<CommentResponse> getAllComments(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");
        }

        List<Comment> comments = commentRepository.findAllByPostIdWithMember(postId);

        return comments.stream()
                .map(comment -> {
                    CommentResponse response = new CommentResponse();
                    response.setMemberId(comment.getMember().getId());
                    response.setPostId(comment.getPost().getId());
                    response.setCommentId(comment.getId());
                    response.setContent(comment.getContent());
                    response.setCreatedAt(comment.getCreatedAt());
                    response.setUpdatedAt(comment.getUpdatedAt());
                    return response;
                })
                .toList();
    }

    @Override
    public CommentResponse updateComment(Long memberId, Long postId, Long commentId, CommentUpdateRequest request) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다.");
        }

        if (request.getContent() != null) {
            comment.updateContent(request.getContent());
        }

        commentRepository.flush();

        CommentResponse response = new CommentResponse();
        response.setMemberId(comment.getMember().getId());
        response.setPostId(comment.getPost().getId());
        response.setCommentId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        return response;
    }

    @Override
    public void deleteComment(Long memberId, Long postId, Long commentId) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        if (!postRepository.existsById(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

}
