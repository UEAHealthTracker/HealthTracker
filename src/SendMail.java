import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {
    public static boolean sendMail(String receipient, String groupName, String groupPassword) throws MessagingException {
        String appSender = "team1.5healthtracker@gmail.com";
        String password = "ganttsucks123";
        //String Usersender=User.INSTANCE.getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(appSender, password);
                    }
                });
        //compose message

        Message message = prepareMessage(session, appSender, receipient, groupName, groupPassword);
        Transport.send(message);

        return true;
    }
  private static Message prepareMessage(Session session, String appSender, String receipient,String groupName, String groupPassword){
        try{
            //String groupMessage="<h1> Invitation to join group </h1> <p> You are  invited to join the group of <p1> "+ User.INSTANCE.getUsername()+"<p1> If you wish to join the group kindly click <a>Yes</a>. If not don't click anythting and ignore the message</p1>";
            Message message = new MimeMessage(session);    //Create a message object
            message.setFrom(new InternetAddress(appSender));    //Set the sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receipient));  //Set the recepient
            message.setSubject("Invitation to "+ User.INSTANCE.getUsername()+"'s Group");  //Create the subject
            //message.setContent(message, groupMessage);
            //message.setText("You are invited to join "+ User.INSTANCE.getUsername()+"'s Group"+" called: " +groupName+" If you accept to join please send a reply to this mail, confirming so other wise ignore this message");  //Create the message
            // Send the actual HTML message, as big as you like
            message.setContent(
                    "<h1> Invitation to join group </h1> <p1> You are  invited to join the group of </p1> <i>"+ User.INSTANCE.getUsername()+ "</i> namely: <i> " + groupName + "  </i> If you wish to join the group kindly go the  join the group section of the application and type the group name and  the following password: <b><i> " + groupPassword +  "</i></b>",
                    "text/html");
            System.out.println("Email has been sent successfully");
            System.out.println(message.getReplyTo().toString());
            return message;

        }catch(Exception ex){
            ex.printStackTrace();


        }

return null;
  }





    public static boolean sendGoalMail(String receipient, String groupName, String goalCode,String Date,String desc) throws MessagingException {
        String appSender = "team1.5healthtracker@gmail.com";
        String password = "ganttsucks123";
        //String Usersender=User.INSTANCE.getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(appSender, password);
                    }
                });
        //compose message

        Message message = prepareGoalMessage(session, appSender, receipient, groupName, goalCode,Date,desc);
        Transport.send(message);

        return true;
    }
    private static Message prepareGoalMessage(Session session, String appSender, String receipient,String groupName, String goalCode,String date,String desc){
        try{
            //String groupMessage="<h1> Invitation to join group </h1> <p> You are  invited to join the group of <p1> "+ User.INSTANCE.getUsername()+"<p1> If you wish to join the group kindly click <a>Yes</a>. If not don't click anythting and ignore the message</p1>";
            Message message = new MimeMessage(session);    //Create a message object
            message.setFrom(new InternetAddress(appSender));    //Set the sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receipient));  //Set the recepient
            message.setSubject("New goal added to "+ groupName+"'s Group");  //Create the subject
            //message.setContent(message, groupMessage);
            //message.setText("You are invited to join "+ User.INSTANCE.getUsername()+"'s Group"+" called: " +groupName+" If you accept to join please send a reply to this mail, confirming so other wise ignore this message");  //Create the message
            // Send the actual HTML message, as big as you like
            message.setContent(
                    "<h3> A new goal with code:  <b>"+goalCode+"</b></h3><h3>With date: <i>"+date+"</i> was added</h3><br> <b>Goal Desciption: </b><h2>"+desc+"</h2><h1> Add the alphanumeric code to your Health Tracker if you want to be the first to complete it!!! ",
                    "text/html");
            System.out.println("Email has been sent successfully");
            System.out.println(message.getReplyTo().toString());
            return message;

        }catch(Exception ex){
            ex.printStackTrace();


        }

        return null;
    }















    public static boolean sendGoalCompletationMail(String receipient, String groupName,String desc,String username) throws MessagingException {
        String appSender = "team1.5healthtracker@gmail.com";
        String password = "ganttsucks123";
        //String Usersender=User.INSTANCE.getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(appSender, password);
                    }
                });
        //compose message

        Message message = prepareGoaCompletelMessage(session, appSender, receipient, groupName,desc,username);
        Transport.send(message);

        return true;
    }
    private static Message prepareGoaCompletelMessage(Session session, String appSender, String receipient,String groupName,String desc,String username){
        try{
            //String groupMessage="<h1> Invitation to join group </h1> <p> You are  invited to join the group of <p1> "+ User.INSTANCE.getUsername()+"<p1> If you wish to join the group kindly click <a>Yes</a>. If not don't click anythting and ignore the message</p1>";
            Message message = new MimeMessage(session);    //Create a message object
            message.setFrom(new InternetAddress(appSender));    //Set the sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receipient));  //Set the recepient
            message.setSubject("Goal completed in: "+ groupName+" Group");  //Create the subject
            //message.setContent(message, groupMessage);
            //message.setText("You are invited to join "+ User.INSTANCE.getUsername()+"'s Group"+" called: " +groupName+" If you accept to join please send a reply to this mail, confirming so other wise ignore this message");  //Create the message
            // Send the actual HTML message, as big as you like
            message.setContent(
                    "<h3>Congratulations to User:<b>"+username+"</b></h3><br><b>Completed Goal Desciption: </b><h2>"+desc+"</h2><h1> If you want to challange this user with a new goal share yours with the group: "+groupName+"!!!",
                    "text/html");
            System.out.println("Email has been sent successfully");
            System.out.println(message.getReplyTo().toString());
            return message;

        }catch(Exception ex){
            ex.printStackTrace();


        }

        return null;
    }








    public static boolean sendPassword(String receipient, String email, String pass) throws MessagingException {
        String appSender = "team1.5healthtracker@gmail.com";
        String password = "ganttsucks123";
        //String Usersender=User.INSTANCE.getEmail();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(appSender, password);
                    }
                });
        //compose message

        Message message = preparepassword(session, appSender, receipient,password);
        Transport.send(message);

        return true;
    }
    private static Message preparepassword(Session session, String appSender, String receipient,String password){
        try{
            //String groupMessage="<h1> Invitation to join group </h1> <p> You are  invited to join the group of <p1> "+ User.INSTANCE.getUsername()+"<p1> If you wish to join the group kindly click <a>Yes</a>. If not don't click anythting and ignore the message</p1>";
            Message message = new MimeMessage(session);    //Create a message object
            message.setFrom(new InternetAddress(appSender));    //Set the sender
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receipient));  //Set the recepient
            message.setSubject("Forgotten Password:"+ receipient+" Group");  //Create the subject
            //message.setContent(message, groupMessage);
            //message.setText("You are invited to join "+ User.INSTANCE.getUsername()+"'s Group"+" called: " +groupName+" If you accept to join please send a reply to this mail, confirming so other wise ignore this message");  //Create the message
            // Send the actual HTML message, as big as you like
            message.setContent(
                    "<h3>Password was requested for this email:<b>"+receipient+"</b></h3><br><b>Password: </b><h2>"+password+"</h2>",
                    "text/html");
            System.out.println("Email has been sent successfully");
            System.out.println(message.getReplyTo().toString());
            return message;

        }catch(Exception ex){
            ex.printStackTrace();


        }

        return null;
    }




}



