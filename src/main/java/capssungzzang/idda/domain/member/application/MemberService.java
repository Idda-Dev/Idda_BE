package capssungzzang.idda.domain.member.application;

import capssungzzang.idda.domain.member.dto.MemberRegisterRequest;
import capssungzzang.idda.domain.member.dto.MemberRegisterResponse;
import capssungzzang.idda.domain.member.dto.MemberResponse;

public interface MemberService {
    MemberResponse getMember(Long memberId);
    MemberRegisterResponse registerMember(MemberRegisterRequest request);
}
