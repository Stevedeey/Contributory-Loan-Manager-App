package com.example.contributoryloanapp.utils.mailService;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface MailService {
    JsonNode sendMessage(String to, String subject, String messageBody) throws UnirestException;

}
