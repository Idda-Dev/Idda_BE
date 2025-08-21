package capssungzzang.idda.domain.mission.prompt;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MissionPromptProvider {

    public String build(int level, Difficulty difficulty, String location) {
        int effectiveLevel = level;
        Difficulty effectiveDifficulty = difficulty;

        // Lv5는 레벨, 난이도 랜덤
        if (level == 5) {
            effectiveLevel = ThreadLocalRandom.current().nextInt(1, 5); // 1~4
            Difficulty[] difficulties = Difficulty.values();
            effectiveDifficulty = difficulties[ThreadLocalRandom.current().nextInt(difficulties.length)];
        }

        // 공통 + 난이도별 본문 결합
        String prompt =
                MissionPromptCatalog.bodyOf(effectiveLevel, effectiveDifficulty)
                        + "\n\n"
                        + MissionPromptCatalog.COMMON_RULES;

        // 플레이스홀더 치환
        return substitute(prompt, effectiveLevel, effectiveDifficulty, location);
    }

    private String substitute(String prompt, int level, Difficulty difficulty, String location) {
        return prompt.replace("{level}", String.valueOf(level))
                .replace("{difficulty}", difficulty == null ? "" : difficulty.name().toLowerCase(Locale.ROOT))
                .replace("{location}", location == null ? "" : location);
    }
}
