package server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class JavaMail {
    static final String ENCODING = "UTF-8";

    public static int registration(String email, String reg_token){
        String subject = "Confirm registration";
        String content = "Random Password: " + reg_token +". Don't show it to anyone!";
        String smtpHost="mail.99cows.com";
        String from= "carrissa.makailyn@99cows.com";
        String login="carrissa.makailyn";
        String password="";
        String smtpPort="25";
        try {
            sendSimpleMessage(login, password, from, email, content, subject, smtpPort, smtpHost);
            return 1;
        } catch (Exception e) {
            e.getMessage();
            System.out.println("Error whilst sending email");
            return -1;
        }
    }

    public static void sendSimpleMessage(String login, String password, String from, String to, String content, String subject, String smtpPort, String smtpHost)
            throws MessagingException {
        Authenticator auth = new Auth(login, password);

        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setText(content);
        Transport.send(msg);
    }
}