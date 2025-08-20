package capssungzzang.idda.domain.comment.domain.repository;

import capssungzzang.idda.domain.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
    select c
    from Comment c
    join fetch c.member m
    where c.post.id = :postId
    order by c.createdAt asc, c.id asc
    """)
    List<Comment> findAllByPostIdWithMember(@Param("postId") Long postId);

    long countByPostId(Long postId);

}
