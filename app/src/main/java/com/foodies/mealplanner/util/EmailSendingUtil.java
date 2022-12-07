package com.foodies.mealplanner.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility for sending email.
 * @author herje
 * @version 1
 */
public class EmailSendingUtil extends AsyncTask<Void, Void, Void>{

    private Context context;
    private Session session;
    private InternetAddress[] email;
    private String subject;
    private String message;
    private ProgressDialog progressDialog;


    public EmailSendingUtil(Context context, InternetAddress[] email, String subject, String message){
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Setting up session for email.
     *
     */
    @Override
    protected Void doInBackground(Void... voids) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        Log.d("EMAIL UTIL", "PROPS SET");
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConfigForGoogle.EMAIL, EmailConfigForGoogle.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(EmailConfigForGoogle.EMAIL));
            mm.setRecipients(Message.RecipientType.TO, email);
            mm.setSubject(subject);
            mm.setContent(message, "text/html");
            Log.d("EMAIL UTIL", "BEFORE SEND");
            Transport.send(mm);
        }
        catch (MessagingException e) {
            Log.e("EMAIL SENDING UTILITY", "ERROR: " + e.toString());
        }
        return null;
    }

    /**
     * Progress dialog before sending message.
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("EMAIL UTIL", "BEFORE SEND");
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    /**
     * After sending the email.
     */
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Log.d("EMAIL UTIL", "MESSAGE SENT");
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }


}
