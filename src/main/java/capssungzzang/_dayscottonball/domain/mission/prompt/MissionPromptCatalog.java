package capssungzzang._dayscottonball.domain.mission.prompt;

import capssungzzang._dayscottonball.domain.level.domain.entity.difficulty.Difficulty;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class MissionPromptCatalog {

    private MissionPromptCatalog() {}

    // 프롬프트 공통 부분
    public static final String COMMON_RULES = """
        공통 포맷
        """;

    private static final Map<Integer, EnumMap<Difficulty, String>> bodyByLevel = new HashMap<>();

    private static void put(int level, Difficulty difficulty, String body) {
        bodyByLevel.computeIfAbsent(level, k -> new EnumMap<>(Difficulty.class))
                .put(difficulty, body);
    }

    // 난이도별 본문
    public static String bodyOf(int level, Difficulty difficulty) {
        EnumMap<Difficulty, String> bodyByDifficulty = bodyByLevel.get(level);
        if (bodyByDifficulty == null || !bodyByDifficulty.containsKey(difficulty)) {
            throw new IllegalArgumentException("No prompt body for level=" + level + ", diff=" + difficulty);
        }
        return bodyByDifficulty.get(difficulty);
    }

    static {
        // ===== Level 1 =====
        put(1, Difficulty.EASY, """
        레벨, 난이도별 포맷
        """);

        put(1, Difficulty.NORMAL, """
        레벨, 난이도별 포맷
        """);

        put(1, Difficulty.HARD, """
        레벨, 난이도별 포맷
        """);

        // ===== Level 2 =====
        put(2, Difficulty.EASY, """
        레벨, 난이도별 포맷
        """);
        put(2, Difficulty.NORMAL, """
        레벨, 난이도별 포맷
        """);
        put(2, Difficulty.HARD, """
        레벨, 난이도별 포맷
        """);

        // ===== Level 3 =====
        put(3, Difficulty.EASY, """
        레벨, 난이도별 포맷
        """);
        put(3, Difficulty.NORMAL, """
        레벨, 난이도별 포맷
        """);
        put(3, Difficulty.HARD, """
        레벨, 난이도별 포맷
        """);

        // ===== Level 4 =====
        put(4, Difficulty.EASY, """
        레벨, 난이도별 포맷
        """);
        put(4, Difficulty.NORMAL, """
        레벨, 난이도별 포맷
        """);
        put(4, Difficulty.HARD, """
        레벨, 난이도별 포맷
        """);
    }
}

