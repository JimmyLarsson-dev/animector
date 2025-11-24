package org.animector.animectorbe.image;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String getAiMessage(String message) {
        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        ResponseCreateParams params = ResponseCreateParams.builder()
                .input("Say this is a test")
                .model("gpt-5-nano")
                .build();

        Response response = client.responses().create(params);
        System.out.println("*************" + response._text());
        return response._output().toString();
    }
}
