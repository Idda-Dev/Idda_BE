package capssungzzang.idda.domain.member.api;

import capssungzzang.idda.domain.member.application.MemberService;
import capssungzzang.idda.domain.member.dto.MemberRegisterRequest;
import capssungzzang.idda.domain.member.dto.MemberRegisterResponse;
import capssungzzang.idda.domain.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable("userId") Long memberId) {
        MemberResponse response = memberService.getMember(memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/register")
    public ResponseEntity<MemberRegisterResponse> registerMember(@RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.registerMember(request);
        return ResponseEntity.ok(response);
    }
}
