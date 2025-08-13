package capssungzzang._dayscottonball.domain.post.domain.repository;

import capssungzzang._dayscottonball.domain.post.domain.entity.VerificationPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface VerificationPostRepository extends JpaRepository<VerificationPost, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"member", "mission"})
    Optional<VerificationPost> findById(@NonNull Long id);

    @Query("""
        select vp as post, coalesce(count(h), 0) as likes
        from VerificationPost vp
        left join Heart h on h.post = vp
        group by vp
        order by count(h) desc, vp.createdAt desc
    """)
    List<PostWithLikes> findAllOrderByLikesDesc();

    interface PostWithLikes {
        VerificationPost getPost();
        long getLikes();
    }
}
