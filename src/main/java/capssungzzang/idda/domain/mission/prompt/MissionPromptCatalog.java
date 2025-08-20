package capssungzzang.idda.domain.mission.prompt;

import capssungzzang.idda.domain.level.domain.entity.difficulty.Difficulty;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class MissionPromptCatalog {

    private MissionPromptCatalog() {}

    // 프롬프트 공통 부분
    public static final String COMMON_RULES = """
            #입력
            # 입력 [톤] 미션 본문 : 깔끔한 톤 , 억지표현 없음
            - 부정적 단어(한숨 등) 금지
            - 거실 조명 켜고 오기처럼 나중에 불 끄러 다시 나가야 되는 행동의 미션 금지
            - 사용자 집에 없을 수 있는 물건 금지(예: 거실시계, 부엌냅킨, 가습기, 화분, 컴퓨터 등)
            - 조리대 대신 싱크대처럼 사용자에게 친숙한 단어 사용하기
            # 입력 [톤] 마무리문구 : 활기차게 동기부여하는 톤(‘팡팡’, ‘리듬’ 같은 유아스러운 단어 제외) 또는
            - 할 수 있다고 응원해주는 톤(‘지금도 괜찮다’, ‘중간에 포기해도 괜찮다’ 같은 문구 제외)
            
            #처리
            당신은 고립은둔청년을 위한 따뜻하고 다정한 미션 안내 문구를 작성하는 AI입니다.
            입력과 예시를 반영해, 사용자에게 맞는 미션 1개를 생성하세요.
            - 미션 본문: 미션 제목(간결)
            - 마무리 문구: 활동의 즐거움·의미 전달 / 관련된 짧은 설명 / 동기부여 표현 / 미션 효과 중
              미션 내용과 어울리는 문구로 작성
            
            #출력(반드시 JSON만 출력)
            {
              "content": "<미션 본문 텍스트만>",
              "missionComment": "<마무리 문구 텍스트만>"
            }
            
            #제약
            - content: 여백포함 22자 이내, ‘기’로 끝나기, 방 안 전용
            - timed 미션은 5~10분 가정(본문에 ‘5분’ 또는 ‘10분’ 표기), 일회성(one-off)은 시간 표기 없음
            - missionComment: 여백포함 10~15자, ‘요’로 끝나기
            - JSON 외의 텍스트(예: ‘미션 본문:’, ‘마무리 문구:’ 라벨, 설명 문장, 코드블록) 출력 금지
            - 값은 모두 한국어 평문으로, 따옴표/이모지 과다 사용 금지
            ""\"
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
        #입력
        [사용자 레벨] = Level 1
        [미션 난이도] = 하
        [조건] = 방 안 전용
        - timed: 5~10분(본문에 표기)
        - 일회성(one-off): 시간 표기 없음
        - 금지: 사용자에게 없을 법한 물건 사용 금지
        #예시
        - 창문 열고 환기하기
        - 책상 5분 정리하기
        - 가볍게 5분 스트레칭하기
        """);

        put(1, Difficulty.NORMAL, """
        #입력
        [사용자 레벨] = Level 1
        [미션 난이도] = 중
        [조건] = 화장실 전용 행동
        - timed: 5~10분(본문에 표기)
        - 일회성(one-off): 시간 표기 없음
        - 제외: 손잡이/문틀 닦기 같은 지나치게 사소한 부위 청소
        - 제외: 세탁기 관련 미션
        - 수건모서리 정리해걸기 대신 수건 정리해서 걸기처럼 넓은 범위로 미션 추천
        #예시
        - 양치 후 세면대 물기 닦기
        - 10분간 샤워하기
        """);

        put(1, Difficulty.HARD, """
        #입력
        [사용자 레벨] = Level 1
        [미션 난이도] = 상
        [조건] = 화장실 제외, 부엌/거실/현관으로 이동하는 일회성 행동 위주
        - 일회성(one-off): 시간 표기 없음
        #예시
        - 부엌에서 간식 챙겨오기
        """);

        // ===== Level 2 =====
        put(2, Difficulty.EASY, """
        #입력
        [사용자 레벨] = Level 2
        [미션 난이도] = 하
        [조건] = 집 안 전용(방 제외, 부엌/거실), 최대 반경은 현관문 열고 닫기
        - timed: 10분(본문에 표기)
        - 일회성(one-off): 현관문 열기만 허용(시간 표기 없음)
        #예시
        - 현관문 열었다 닫기
        - 부엌에서 어지러운 공간 찾아서 10분간 청소하기
        - 거실에서 10분간 스트레칭하기
        """);
        put(2, Difficulty.NORMAL, """
        #입력
        [사용자 레벨] = Level 2
        [미션 난이도] = 중
        [조건] = 건물 안 전용(현관문에서 건물 1층까지의 범위)
        - timed 가능
        - 일회성(one-off): 우리집 1층까지 왕복, 우편함 확인, 분리수거/쓰레기 버리기 등(시간 표기 없음)
        #예시
        - 우리집 우편함 확인하고 오기
        """);
        put(2, Difficulty.HARD, """
        #입력
        [사용자 레벨] = Level 2
        [미션 난이도] = 상
        [조건] = 건물 밖 초근거리(맞은편 가게 ‘밖’까지, 내부 진입 없음)
        - 일회성(one-off) 위주(시간 표기 없음)
        - timed 예: 건물 앞 벤치에 5분 앉아있기(본문에 ‘5분’ 표기)
        #예시
        - 집 건너편 가게 밖에서 구경하기
        """);

        // ===== Level 3 =====
        put(3, Difficulty.EASY, """
        #입력
        [사용자 레벨] = Level 3
        [미션 난이도] = 하
        [조건] = 건물 외부 근거리, 일회성(one-off) 위주, 가능하면 소비 지양
        #예시
        - 집 맞은편 가게 간판 찍어오기
        - 근처 우체통 사진 찍고 오기
        """);
        put(3, Difficulty.NORMAL, """
        #입력
        [사용자 레벨] = Level 3
        [미션 난이도] = 중
        [조건] = 건물 외부 근거리 탐색 중심
        - 옵션A timed: 편의점 들린 뒤 주변 길목 10분 걷기(본문에 ‘10분’ 표기)
        - 옵션B one-off: 가까운 공원/놀이터 입구 찍고 오기(시간 표기 없음)
        - 마무리 문구: 호기심/탐색 톤, 반드시 ‘요?’로 끝나기(10~15자)
        #예시
        - 편의점 들리고 주변 길목 10분 걷기
        - 가까운 공원/놀이터 입구 찍고 오기
        """);
        put(3, Difficulty.HARD, """
        #입력
        [사용자 레벨] = Level 3
        [미션 난이도] = 상
        [조건] = 동네 범위로 확장
        - timed: 15~20분 활동(본문에 ‘분’ 표기)
        - one-off: 편의점 제외 일반 가게 밖에서 구경 또는 짧게 방문(시간 표기 없음)
        #예시
        - 집 근처 공원에서 20분 산책하기
        """);

        // ===== Level 4 =====
        put(4, Difficulty.EASY, """
        #입력
        [사용자 레벨] = Level 4
        [미션 난이도] = 하
        [조건] = 동네 실외 활동
        - 운동형 timed: 달리기/빠르게 걷기 등 일반 걷기보다 강도 높임(본문에 ‘분’ 표기)
        - 또는 일반 가게 방문 one-off(시간 표기 없음, 편의점 제외)
        #예시
        - 집 근처 공원에서 20분 빠르게 걷기
        - 집 근처 카페에서 음료 마시기
        """);
        put(4, Difficulty.NORMAL, """
        #입력
        [사용자 레벨] = Level 4
        [미션 난이도] = 중
        [조건] = 동네 실외, 대중적 만남/군중 환경의 일회성 행동(시간 표기 없음)
        #예시
        - 시장 골목 한 바퀴 구경하기
        """);
        put(4, Difficulty.HARD, """
        #입력
        [사용자 레벨] = Level 4
        [미션 난이도] = 상
        [조건] = 일대일 상호작용이 발생하는 일회성 행동(시간 표기 없음)
        #예시
        - 주민센터 방문해서 상담받기
        - 카페 직원에게 먼저 인사하기
        """);
    }
}

