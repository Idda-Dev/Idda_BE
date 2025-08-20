package capssungzzang.idda.domain.post.application;

import capssungzzang.idda.domain.heart.domain.repository.HeartRepository;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.post.domain.entity.Post;
import capssungzzang.idda.domain.post.domain.repository.PostRepository;
import capssungzzang.idda.domain.post.dto.PostCreateRequest;
import capssungzzang.idda.domain.post.dto.PostResponse;
import capssungzzang.idda.domain.post.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

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

    public List<PostResponse> getAllPosts() {

        List<PostRepository.PostWithHearts> rows = postRepository.findAllNormalOrderByHeartsDesc();

        return rows.stream().map(row -> {
            var p = row.getPost();
            PostResponse res = new PostResponse();
            res.setMemberId(p.getMember().getId());
            res.setPostId(p.getId());
            res.setTitle(p.getTitle());
            res.setContent(p.getContent());
            res.setHearts(row.getHearts());
            res.setCreatedAt(p.getCreatedAt());
            res.setUpdatedAt(p.getUpdatedAt());
            return res;
        }).toList();
    }

    @Override
    public PostResponse getPostById(Long memberId,  Long postId) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        PostResponse response = new PostResponse();

        response.setMemberId(post.getMember().getId());
        response.setPostId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setHearts(heartRepository.countByPostId(postId));
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return response;
    }

    @Override
    public PostResponse updatePost(Long memberId, Long postId, PostUpdateRequest request) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다.");
        }

        if (request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }

        if (request.getContent() != null) {
            post.updateContent(request.getContent());
        }

        postRepository.flush();

        PostResponse response = new PostResponse();
        response.setMemberId(post.getMember().getId());
        response.setPostId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setHearts(heartRepository.countByPostId(postId));
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return response;
    }

    @Override
    public void deletePost(Long memberId, Long postId) {

        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (!post.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시글 삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

}
