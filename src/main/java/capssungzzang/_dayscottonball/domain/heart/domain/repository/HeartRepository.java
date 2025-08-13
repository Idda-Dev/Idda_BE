package capssungzzang._dayscottonball.domain.heart.domain.repository;

import capssungzzang._dayscottonball.domain.heart.domain.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);
    long countByPostId(Long postId);
    @Query("""
           select h.post.id as postId, count(h) as likeCount
           from Heart h
           where h.post.id in :postIds
           group by h.post.id
           """)
    List<PostLikeCount> countGroupByPostIds(@Param("postIds") Collection<Long> postIds);

    interface PostLikeCount {
        Long getPostId();
        long getLikeCount();
    }
}
