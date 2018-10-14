package com.aegeanflow.essentials;

import com.aegeanflow.core.ioc.AegeanFlowCoreModule;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static org.testng.Assert.*;

@Guice(modules = AegeanFlowCoreModule.class)
public class EmailSenderNodeTest {

    @Inject
    private Provider<Session> sessionProvider;

    @Test
    public void test() {
        Session session = sessionProvider.get();

        Node emailSender = session.newNode(UUID.randomUUID(), EmailSenderNode.class);

        Map<String, String> props = new HashMap<>();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        session.setInput(emailSender.getId(), EmailSenderNode.IN_PROPERTIES, props);
        session.setInput(emailSender.getId(), EmailSenderNode.IN_RECIPIENT_LIST, "gorkemgok@gmail.com");
        session.setInput(emailSender.getId(), EmailSenderNode.IN_SUBJECT, "Email Sender Node Test");
        session.setInput(emailSender.getId(), EmailSenderNode.IN_CONTENT_TYPE, MediaType.TEXT_HTML);
        session.setInput(emailSender.getId(), EmailSenderNode.IN_CONTENT,
                "<html>\n" +
                "\t<header></header>\n" +
                "\t<body>\n" +
                "\t\tCegid-Corebi Entegrasyonu farkı tablodadir:\n" +
                "\t\t<table>\n" +
                "\t\t\t<thead>\n" +
                "\t\t\t\t<th>Day</th>\n" +
                "\t\t\t\t<th>Quantity</th>\n" +
                "\t\t\t\t<th>Price</th>\n" +
                "\t\t\t</thead>\n" +
                "\t\t\t<tr>2017-01-01</tr>\n" +
                "\t\t\t<tr>2</tr>\n" +
                "\t\t\t<tr>3</tr>\n" +
                "\t\t</table>\n" +
                "\t</body>\n" +
                "</html>");

        session.run();
        session.awaitCompletion();
    }

}