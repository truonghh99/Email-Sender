import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public static void sendEmail(String port, String host, String to, final String from, final String pass, String subject,
            String bodyText) throws MessagingException {

        // create properties field
        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");


        // Setup authentication, get session
        Authenticator authenticator = new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(from, pass);
            }   
        };
        Session session = Session.getInstance(properties, authenticator);
        
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Set From: header field of the header.
        message.setFrom(new InternetAddress(from));

        // Set To: header field of the header.
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Set Subject: header field
        message.setSubject(subject);

        // Now set the actual message
        message.setText(bodyText);

        // Send message
        Transport.send(message);
        System.out.println("Sent message successfully");
        
        return;
    }
}
