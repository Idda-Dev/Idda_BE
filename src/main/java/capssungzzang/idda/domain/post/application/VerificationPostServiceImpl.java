package capssungzzang.idda.domain.post.application;

import capssungzzang.idda.domain.comment.domain.repository.CommentRepository;
import capssungzzang.idda.domain.heart.domain.repository.HeartRepository;
import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.mission.domain.entity.Mission;
import capssungzzang.idda.domain.mission.domain.repository.MissionRepository;
import capssungzzang.idda.domain.post.domain.entity.VerificationPost;
import capssungzzang.idda.domain.post.domain.repository.VerificationPostRepository;
import capssungzzang.idda.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang.idda.domain.post.dto.VerificationPostDailyResponse;
import capssungzzang.idda.domain.post.dto.VerificationPostResponse;
import capssungzzang.idda.global.s3.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationPostServiceImpl implements VerificationPostService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final VerificationPostRepository verificationPostRepository;
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final S3StorageService s3StorageService;
    private final CommentRepository commentRepository;

    @Override
    public List<VerificationPostResponse> getAllVerificationPosts(String location) {

        List<VerificationPostRepository.PostWithStats> rows =
                verificationPostRepository.findAllByLocationAndIsPublicTrueOrderByHeartsDesc(location);

        return rows.stream().map(row -> {
            VerificationPost verificationPost = row.getPost();

            VerificationPostResponse response = new VerificationPostResponse();
            response.setPostId(verificationPost.getId());
            response.setMemberId(verificationPost.getMember().getId());
            response.setMissionId(verificationPost.getMission().getId());
            response.setProfileImageUrl(verificationPost.getMember().getProfileImageUrl());
            response.setNickname(verificationPost.getMember().getNickname());
            response.setTitle(verificationPost.getTitle());
            response.setContent(verificationPost.getContent());
            response.setPhotoUrl(verificationPost.getPhotoUrl());
            response.setLocation(verificationPost.getLocation());
            response.setHeartCount(row.getHearts());
            response.setCommentCount(row.getComments());
            response.setCreatedAt(verificationPost.getCreatedAt());
            response.setUpdatedAt(verificationPost.getUpdatedAt());
            return response;
        }).toList();
    }



    @Override
    public VerificationPostResponse getVerificationPost(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
        VerificationPost verificationPost = verificationPostRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "인증글이 존재하지 않습니다."));

        VerificationPostResponse response = new VerificationPostResponse();
        response.setPostId(verificationPost.getId());
        response.setMemberId(verificationPost.getMember().getId());
        response.setMissionId(verificationPost.getMission().getId());
        response.setProfileImageUrl(member.getProfileImageUrl());
        response.setNickname(member.getNickname());
        response.setTitle(verificationPost.getTitle());
        response.setContent(verificationPost.getContent());
        response.setPhotoUrl(verificationPost.getPhotoUrl());
        response.setLocation(verificationPost.getLocation());
        response.setHeartCount(heartRepository.countByPostId(postId));
        response.setCommentCount(commentRepository.countByPostId(postId));
        response.setCreatedAt(verificationPost.getCreatedAt());
        response.setUpdatedAt(verificationPost.getUpdatedAt());
        return response;
    }

    @Override
    public Long createVerificationPost(Long memberId, Long missionId,
                                           VerificationPostCreateRequest request,
                                           MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사진은 필수입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "미션이 존재하지 않습니다."));

        String photoUrl = s3StorageService.uploadImage(
                file, String.format("verification/missions/%d/users/%d", missionId, memberId));

        VerificationPost verificationPost = VerificationPost.verificationBuilder()
                .member(member)
                .mission(mission)
                .title(mission.getContent())
                .content(request.getContent())
                .location(member.getLocation())
                .isPublic(request.isPublic())
                .photoUrl(photoUrl)
                .build();

        verificationPostRepository.save(verificationPost);

        return verificationPost.getId();
    }

    public VerificationPostDailyResponse getDailyVerificationPost(Long memberId, LocalDate kstDate) {

        if (kstDate == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date는 필수입니다.");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        LocalDateTime startUtc = kstDate.atStartOfDay(KST).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endUtc   = startUtc.plusDays(1);

        VerificationPost verificationPost = verificationPostRepository.findFirstByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanOrderByCreatedAtAsc(memberId, startUtc, endUtc)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 날짜의 인증글이 없습니다."));

        VerificationPostDailyResponse response = new VerificationPostDailyResponse();
        response.setPostId(verificationPost.getId());
        response.setTitle(verificationPost.getTitle());
        response.setContent(verificationPost.getContent());
        response.setPhotoUrl(verificationPost.getPhotoUrl());
        return response;
    }
}
