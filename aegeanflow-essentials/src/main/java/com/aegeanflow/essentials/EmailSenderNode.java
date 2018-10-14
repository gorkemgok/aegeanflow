package com.aegeanflow.essentials;

import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class EmailSenderNode extends AbstractSynchronizedNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderNode.class);

    public static final Input<Map<String, String>> IN_PROPERTIES = Parameter.input("PROPERTIES", new TypeLiteral<Map<String, String>>(){});
    public static final Input<String> IN_USERNAME = Parameter.stringInput("USERNAME");
    public static final Input<String> IN_PASSWORD = Parameter.stringInput("PASSWORD");
    public static final Input<String> IN_SENDER = Parameter.stringInput("SENDER");
    public static final Input<String> IN_RECIPIENT_LIST = Parameter.stringInput("RECIPIENT_LIST");
    public static final Input<String> IN_SUBJECT = Parameter.stringInput("SUBJECT");
    public static final Input<String> IN_CONTENT_TYPE = Parameter.stringInput("CONTENT_TYPE");
    public static final Input<String> IN_CONTENT = Parameter.stringInput("CONTENT");

    private Map<String, String> properties;
    private String username;
    private String password;
    private String sender;
    private String recipientList;
    private String subject;
    private String contentType;
    private String content;

    @Override
    protected void run() throws Exception {
        Session session = getSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientList));
        message.setSubject(subject);
        message.setContent(content, contentType);
        Transport.send(message);
    }
    
    private Session getSession() {
        Properties properties = new Properties();
        properties.putAll(this.properties);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }


    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(IN_PROPERTIES)) {
            properties = (Map<String, String>) value;
        } else if (input.equals(IN_USERNAME)) {
            username = (String) value;
        } else if (input.equals(IN_PASSWORD)) {
            password = (String) value;
        } else if (input.equals(IN_SENDER)) {
            sender = (String) value;
        } else if (input.equals(IN_RECIPIENT_LIST)) {
            recipientList = (String) value;
        } else if (input.equals(IN_SUBJECT)) {
            subject = (String) value;
        } else if (input.equals(IN_CONTENT_TYPE)) {
            contentType = (String) value;
        } else if (input.equals(IN_CONTENT)) {
            content = (String) value;
        }
    }

    @Override
    public String getName() {
        return "Email Sender";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(
                IN_CONTENT,
                IN_CONTENT_TYPE,
                IN_PASSWORD,
                IN_PROPERTIES,
                IN_RECIPIENT_LIST,
                IN_SENDER,
                IN_SUBJECT,
                IN_USERNAME
        );
    }
}
