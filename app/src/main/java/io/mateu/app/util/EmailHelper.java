package io.mateu.app.util;

import com.google.common.base.Strings;
import io.mateu.app.modelo.config.AppConfig;
import io.mateu.erp.model.util.Helper;
import io.mateu.erp.model.util.JPATransaction;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;

public class EmailHelper {

    public static void sendEmail(String toEmail, String subject, String text, boolean noCC) throws Throwable {

        Helper.transact(new JPATransaction() {
            @Override
            public void run(EntityManager em) throws Throwable {

                AppConfig c = AppConfig.get(em);

                Email email = new HtmlEmail();
                email.setHostName(c.getAdminEmailSmtpHost());
                email.setSmtpPort(c.getAdminEmailSmtpPort());
                email.setAuthenticator(new DefaultAuthenticator(c.getAdminEmailUser(), c.getAdminEmailPassword()));
                //email.setSSLOnConnect(true);
                email.setFrom(c.getAdminEmailFrom());
                if (!noCC && !Strings.isNullOrEmpty(c.getAdminEmailCC())) email.getCcAddresses().add(new InternetAddress(c.getAdminEmailCC()));

                email.setSubject(subject);
                //email.setMsg(io.mateu.ui.mdd.server.util.Helper.freemark(template, getData()));
                email.setMsg(text);
                email.addTo(toEmail);
                email.send();
            }
        });
    }


}
