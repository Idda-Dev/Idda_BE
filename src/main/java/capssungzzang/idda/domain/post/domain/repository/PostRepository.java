package capssungzzang.idda.domain.post.domain.repository;

import capssungzzang.idda.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        select p as post, coalesce(count(h.id), 0) as hearts
        from Post p
        left join Heart h on h.post = p
        where type(p) = Post
        group by p.id
        order by count(h.id) desc, p.createdAt desc
    """)
    List<PostWithHearts> findAllNormalOrderByHeartsDesc();

    interface PostWithHearts {
        Post getPost();
        long getHearts();
    }
}
