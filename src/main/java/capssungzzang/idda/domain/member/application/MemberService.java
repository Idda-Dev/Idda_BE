package capssungzzang.idda.domain.member.application;

import capssungzzang.idda.domain.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse getMember(Long memberId);
}
