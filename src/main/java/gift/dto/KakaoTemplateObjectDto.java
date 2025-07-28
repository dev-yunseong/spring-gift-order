package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTemplateObjectDto {

    @JsonProperty("object_type")
    private String objectType = "text";

    @JsonProperty("text")
    private String text;

    @JsonProperty("link")
    private Links link = new Links();

    @JsonProperty("button_title")
    private String buttonTitle = "spring gift로";

    public KakaoTemplateObjectDto(String message) {
        text = message;
    }

    public static class Links {
        @JsonProperty("web_url")
        private String webUrl = "http://localhost:8080";

        @JsonProperty("mobile_web_url")
        private String mobileWebUrl = "http://localhost:8080";
    }
}
