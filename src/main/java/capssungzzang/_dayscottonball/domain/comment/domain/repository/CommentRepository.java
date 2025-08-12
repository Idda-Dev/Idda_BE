package capssungzzang._dayscottonball.domain.comment.domain.repository;

import capssungzzang._dayscottonball.domain.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
