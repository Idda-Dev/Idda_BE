package capssungzzang.idda.domain.member.application;

import capssungzzang.idda.domain.member.domain.entity.Member;
import capssungzzang.idda.domain.member.domain.entity.profileimage.ProfileImage;
import capssungzzang.idda.domain.member.domain.repository.MemberMissionProgressRepository;
import capssungzzang.idda.domain.member.domain.repository.MemberRepository;
import capssungzzang.idda.domain.member.dto.MemberRegisterRequest;
import capssungzzang.idda.domain.member.dto.MemberRegisterResponse;
import capssungzzang.idda.domain.member.dto.MemberResponse;
import capssungzzang.idda.domain.survey.domain.entity.Survey;
import capssungzzang.idda.domain.survey.domain.repository.SurveyRepository;
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
    private final SurveyRepository surveyRepository;

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
            response.setLevelAssigned(memberMissionProgressRepository.existsByMemberId(member.getId()));

            return response;
        }

        Member newMember = Member.builder()
                .nickname(request.getNickname())
                .candy(0)
                .location("동작구")
                .profileImageUrl(ProfileImage.LV1.getUrl())
                .build();

        memberRepository.save(newMember);

        Survey survey = Survey.builder()
                .member(newMember)
                .build();

        surveyRepository.save(survey);

        MemberRegisterResponse response = new MemberRegisterResponse();
        response.setMemberId(newMember.getId());
        response.setNickname(newMember.getNickname());
        response.setNewMember(true);

        return response;
    }
}
