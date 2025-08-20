package capssungzzang.idda.domain.member.api;

import capssungzzang.idda.domain.member.application.MemberService;
import capssungzzang.idda.domain.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
