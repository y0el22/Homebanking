package utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static utils.APIK.API_KEY;


public class SendEmail {
    private static final String YOUR_DOMAIN_NAME = "sandbox1d7eb0caf4f54b47bdc884fac189823d.mailgun.org";


    public static JsonNode sendSimpleMessage(String from, String to, String subject, String text) throws UnirestException {

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
                .basicAuth("api", API_KEY)
                .field("from", from)
                .field("to", to)
                .field("subject", subject)
                .field("text", text)
                .asJson();

        return request.getBody();
    }

}
