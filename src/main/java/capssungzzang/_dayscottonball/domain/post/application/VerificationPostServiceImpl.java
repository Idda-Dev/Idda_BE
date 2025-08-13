package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.heart.domain.repository.HeartRepository;
import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.mission.domain.entity.Mission;
import capssungzzang._dayscottonball.domain.mission.domain.repository.MissionRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.VerificationPost;
import capssungzzang._dayscottonball.domain.post.domain.repository.VerificationPostRepository;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostCreateRequest;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;
import capssungzzang._dayscottonball.global.s3.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationPostServiceImpl implements VerificationPostService {

    private final VerificationPostRepository verificationPostRepository;
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final MissionRepository missionRepository;
    private final S3StorageService s3StorageService;

    @Override
    public List<VerificationPostResponse> getAllVerificationPosts() {

        List<VerificationPostRepository.PostWithLikes> rows =
                verificationPostRepository.findAllOrderByLikesDesc();

        return rows.stream().map(row -> {
            VerificationPost verificationPost = row.getPost();

            VerificationPostResponse response = new VerificationPostResponse();
            response.setPostId(verificationPost.getId());
            response.setMemberId(verificationPost.getMember().getId());
            response.setMissionId(verificationPost.getMission().getId());
            response.setTitle(verificationPost.getTitle());
            response.setContent(verificationPost.getContent());
            response.setPhotoUrl(verificationPost.getPhotoUrl());
            response.setLocation(verificationPost.getLocation());
            response.setLikes(row.getLikes());
            response.setCreatedAt(verificationPost.getCreatedAt());
            response.setUpdatedAt(verificationPost.getUpdatedAt());
            return response;
        }).toList();
    }



    @Override
    public VerificationPostResponse getVerificationPost(Long postId) {
        VerificationPost verificationPost = verificationPostRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "인증글이 존재하지 않습니다."));

        VerificationPostResponse response = new VerificationPostResponse();
        response.setPostId(verificationPost.getId());
        response.setMemberId(verificationPost.getMember().getId());
        response.setMissionId(verificationPost.getMission().getId());
        response.setTitle(verificationPost.getTitle());
        response.setContent(verificationPost.getContent());
        response.setPhotoUrl(verificationPost.getPhotoUrl());
        response.setLocation(verificationPost.getLocation());
        response.setLikes(heartRepository.countByPostId(postId));
        response.setCreatedAt(verificationPost.getCreatedAt());
        response.setUpdatedAt(verificationPost.getUpdatedAt());
        return response;
    }

    @Override
    public Long createVerificationPost(Long userId, Long missionId,
                                           VerificationPostCreateRequest request,
                                           MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사진은 필수입니다.");
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "미션이 존재하지 않습니다."));

        String photoUrl = s3StorageService.uploadImage(
                file, String.format("verification/missions/%d/users/%d", missionId, userId));

        VerificationPost verificationPost = VerificationPost.verificationBuilder()
                .member(member)
                .mission(mission)
                .title(mission.getContent())
                .content(request.getContent())
                .location(request.getLocation())
                .photoUrl(photoUrl)
                .build();

        verificationPostRepository.save(verificationPost);

        return verificationPost.getId();
    }
}
