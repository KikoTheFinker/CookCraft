package it.project.cookcraft.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    private static final Dotenv dotenv = Dotenv.load();

    public void sendOrderAcceptedEmail(String recipientEmail, String deliveryPersonName) {
        String sendGripApiKey = dotenv.get("SENDGRID_API_KEY");
        if(sendGripApiKey == null)
        {
            throw new RuntimeException("SENDGRID_API_KEY is not set!");
        }

        Email from = new Email("gorazdbiskoskii@gmail.com");
        String subject = "Your order has been accepted!";
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain", "Your order has been accepted by " + deliveryPersonName + ".");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGripApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
