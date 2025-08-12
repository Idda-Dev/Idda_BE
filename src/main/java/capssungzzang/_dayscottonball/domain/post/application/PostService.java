package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.post.dto.PostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.PostResponse;

import java.util.List;

public interface PostService {

    Long createPost(Long memberId, PostCreateRequest request);
    List<PostResponse> getAllPosts();
//    PostResponse getPostById(Long postId);
//    PostResponse updatePost(Long postId, Long memberId, PostUpdateRequest request);
//    void deletePost(Long postId, Long memberId);
}
