package capssungzzang._dayscottonball.domain.member.application;

import capssungzzang._dayscottonball.domain.member.domain.entity.Member;
import capssungzzang._dayscottonball.domain.member.domain.entity.MemberMissionProgress;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberMissionProgressRepository;
import capssungzzang._dayscottonball.domain.member.domain.repository.MemberRepository;
import capssungzzang._dayscottonball.domain.member.dto.MemberResponse;
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
        response.setLevel(memberMissionProgressRepository.findCurrentLevel(memberId));
        response.setSuccessCount(memberMissionProgressRepository.getCurrentSuccessCount(memberId));
        response.setTotalCount(memberMissionProgressRepository.getCurrentTotalCount(memberId));

        return response;
    }
}
