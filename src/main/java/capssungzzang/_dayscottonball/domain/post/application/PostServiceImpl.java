package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.heart.domain.repository.HeartRepository;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import capssungzzang._dayscottonball.domain.post.domain.repository.PostRepository;
import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.PostResponse;
import capssungzzang._dayscottonball.domain.post.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<PostResponse> getAllPosts() {

        List<Post> posts = postRepository.findAll();

        List<Long> postIds = posts.stream().map(Post::getId).toList();

        final Map<Long, Long> likeCountMap =
                postIds.isEmpty()
                        ? Map.of()
                        : heartRepository.countGroupByPostIds(postIds).stream()
                        .collect(Collectors.toMap(
                                HeartRepository.PostLikeCount::getPostId,
                                HeartRepository.PostLikeCount::getLikeCount
                        ));

        return posts.stream()
                .map(post -> {
                    PostResponse response = new PostResponse();
                    response.setMemberId(post.getMember().getId());
                    response.setPostId(post.getId());
                    response.setTitle(post.getTitle());
                    response.setContent(post.getContent());
                    response.setLikes(likeCountMap.getOrDefault(post.getId(), 0L)); // ★ 좋아요 수 세팅
                    response.setCreatedAt(post.getCreatedAt());
                    response.setUpdatedAt(post.getUpdatedAt());
                    return response;
                })
                .toList();
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
        response.setLikes(heartRepository.countByPostId(postId));
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
        response.setLikes(heartRepository.countByPostId(postId));
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
