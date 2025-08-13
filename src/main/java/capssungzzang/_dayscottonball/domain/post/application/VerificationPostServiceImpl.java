package capssungzzang._dayscottonball.domain.post.application;

import capssungzzang._dayscottonball.domain.heart.domain.repository.HeartRepository;
import capssungzzang._dayscottonball.domain.post.domain.entity.VerificationPost;
import capssungzzang._dayscottonball.domain.post.domain.repository.VerificationPostRepository;
import capssungzzang._dayscottonball.domain.post.dto.VerificationPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationPostServiceImpl implements VerificationPostService {

    private final VerificationPostRepository verificationPostRepository;
    private final HeartRepository heartRepository;

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
}
