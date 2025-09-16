package capssungzzang.idda.domain.member.application;

import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.repository.MemberMissionProgressRepository;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.member.dto.MemberRegisterRequest;
import capssungzzang.idda.domain.member.dto.MemberRegisterResponse;
import capssungzzang.idda.domain.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMissionProgressRepository memberMissionProgressRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        MemberResponse response = new MemberResponse();

        response.setMemberId(member.getId());
        response.setNickname(member.getNickname());
        response.setCandy(member.getCandy());
        response.setProfileImageUrl(member.getProfileImageUrl());
        response.setLocation(member.getLocation());
        response.setLevel(memberMissionProgressRepository.findCurrentLevel(memberId));
        response.setSuccessCount(memberMissionProgressRepository.getCurrentSuccessCount(memberId));
        response.setTotalCount(memberMissionProgressRepository.getCurrentTotalCount(memberId));
        response.setCreatedAt(member.getCreatedAt());

        return response;
    }

    @Override
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        if (memberRepository.existsByNickname(request.getNickname())) {
            Member member = memberRepository.findByNickname(request.getNickname()).get();

            MemberRegisterResponse response = new MemberRegisterResponse();
            response.setMemberId(member.getId());
            response.setNickname(member.getNickname());
            response.setNewMember(false);

            return response;
        }

        Member newMember = Member.builder()
                .nickname(request.getNickname())
                .candy(15)
                .location("동작구")
                .profileImageUrl("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF+%E1%84%89%E1%85%A1%E1%84%8C%E1%85%B5%E1%86%AB+%E1%84%86%E1%85%A9%E1%84%8B%E1%85%B3%E1%86%B7/%E1%84%8B%E1%85%B3%E1%86%AB%E1%84%83%E1%85%AE%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%91%E1%85%B5%E1%86%AF%E1%84%89%E1%85%A1%E1%84%8C%E1%85%B5%E1%86%AB.png")
                .build();

        memberRepository.save(newMember);

        MemberRegisterResponse response = new MemberRegisterResponse();
        response.setMemberId(newMember.getId());
        response.setNickname(newMember.getNickname());
        response.setNewMember(true);

        return response;
    }
}
