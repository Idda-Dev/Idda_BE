package capssungzzang._dayscottonball.domain.post.domain.repository;

import capssungzzang._dayscottonball.domain.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
