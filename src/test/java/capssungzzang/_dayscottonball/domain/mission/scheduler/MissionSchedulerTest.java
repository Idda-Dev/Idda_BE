package capssungzzang._dayscottonball.domain.mission.scheduler;

import capssungzzang._dayscottonball.domain.level.domain.entity.difficulty.Difficulty;
import capssungzzang._dayscottonball.domain.mission.dto.MissionGenerateResponse;
import capssungzzang._dayscottonball.domain.mission.prompt.MissionPromptProvider;
import capssungzzang._dayscottonball.global.openai.client.OpenAiClient;
import capssungzzang._dayscottonball.global.openai.config.OpenAiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(
        classes = { OpenAiConfig.class, JacksonAutoConfiguration.class },
        properties = {
                "spring.autoconfigure.exclude=" +
                        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
                "spring.cloud.aws.s3.enabled=false",
                "spring.cloud.aws.region.static=ap-northeast-2",
                "spring.main.lazy-initialization=true"
        }
)
@Import({ MissionPromptProvider.class, OpenAiClient.class })
class MissionSchedulerTest {

    private static final Logger log = LoggerFactory.getLogger(MissionSchedulerTest.class);

    @Autowired MissionPromptProvider missionPromptProvider;
    @Autowired OpenAiClient openAiClient;
    @Autowired ObjectMapper objectMapper;

    @Test
    void 미션생성테스트() throws Exception {
        int level = 1;
        Difficulty difficulty = Difficulty.EASY;

        String prompt = missionPromptProvider.build(level, difficulty);
        String json = openAiClient.chatJson(prompt);
        MissionGenerateResponse r = objectMapper.readValue(json, MissionGenerateResponse.class);

        log.info("level: {} difficulty: {} content: {} missionComment: {}",
                level, difficulty, r.getContent(), r.getMissionComment());
    }
}
