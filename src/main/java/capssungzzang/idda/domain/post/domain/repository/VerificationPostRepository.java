package capssungzzang.idda.domain.post.domain.repository;

import capssungzzang.idda.domain.post.domain.entity.VerificationPost;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationPostRepository extends JpaRepository<VerificationPost, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"member", "mission"})
    Optional<VerificationPost> findById(@NonNull Long id);

    @Query("""
    select vp as post,
           coalesce(count(distinct h.id), 0) as hearts,
           coalesce(count(distinct c.id), 0) as comments
    from VerificationPost vp
    left join Heart h on h.post = vp
    left join Comment c on c.post = vp
    where vp.location = :location
      and vp.isPublic = true
    group by vp
    order by count(distinct h.id) desc, vp.createdAt desc
""")
    List<PostWithStats> findAllByLocationAndIsPublicTrueOrderByHeartsDesc(String location);


    interface PostWithStats {
        VerificationPost getPost();
        long getHearts();
        long getComments();
    }

    boolean existsByMissionId(Long missionId);

    Optional<VerificationPost> findFirstByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtAsc(
            Long memberId, LocalDateTime startUtc, LocalDateTime endUtc);
}

