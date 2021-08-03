package com.example.contributoryloanapp.utils.mailService;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImplementation implements MailService {

    @Value("${contributoryapp.app.domain}")
    private String DOMAIN_NAME; // domain for testing

    @Value("${contributoryapp.app.API_KEY}")
    private String API_KEY;

    public  JsonNode sendMessage(String to, String subject, String messageBody) throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
                .basicAuth("api",API_KEY)
                .field("from", "contributoryloanapp.com")
                .field("to", to)
                .field("subject", subject)
                .field("text", messageBody)
                .asJson();
        return request.getBody();

    }
}