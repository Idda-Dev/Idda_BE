package capssungzzang._dayscottonball.domain.member.application;

import capssungzzang._dayscottonball.domain.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse getMember(Long memberId);
}
