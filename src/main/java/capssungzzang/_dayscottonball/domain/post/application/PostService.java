package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.PostResponse;
import capssungzzang._dayscottonball.domain.post.dto.PostUpdateRequest;

import java.util.List;

public interface PostService {

    Long createPost(Long memberId, PostCreateRequest request);
    List<PostResponse> getAllPosts();
    PostResponse getPostById(Long memberId, Long postId);
    PostResponse updatePost(Long memberId, Long postId, PostUpdateRequest request);
//    void deletePost(Long postId, Long memberId);
}
