package capssungzzang.idda.domain.member.domain.entity.profileimage;

import lombok.Getter;

@Getter
public enum ProfileImage {
    LV1("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%EC%9D%80%EB%91%94%EC%9D%B4+%ED%94%84%EB%A1%9C%ED%95%84/1%E1%84%83%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A8.png"),
    LV2("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%EC%9D%80%EB%91%94%EC%9D%B4+%ED%94%84%EB%A1%9C%ED%95%84/2%E1%84%83%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A8.png"),
    LV3("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%EC%9D%80%EB%91%94%EC%9D%B4+%ED%94%84%EB%A1%9C%ED%95%84/3%E1%84%83%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A8.png"),
    LV4("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%EC%9D%80%EB%91%94%EC%9D%B4+%ED%94%84%EB%A1%9C%ED%95%84/4%E1%84%83%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A8%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC%E1%84%87%E1%85%A9%E1%86%AB.png"),
    LV5("https://capssungzzang-bucket.s3.ap-northeast-2.amazonaws.com/%EC%9D%80%EB%91%94%EC%9D%B4+%ED%94%84%EB%A1%9C%ED%95%84/5%E1%84%83%E1%85%A1%E1%86%AB%E1%84%80%E1%85%A8.png");
    private final String url;

    ProfileImage(String url) {
        this.url = url;
    }
}
