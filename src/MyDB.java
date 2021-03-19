import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDB {

    public static void main(String[] args){
        String url = "jdbc:mysql://freedb.tech:3306/freedbtech_htrackdb?useSSL=false" ;
       // String url = "jdbc:mysql://freedb.tech:3306/freedbtech_htrackdb?useSSL=false";
        String username = "freedbtech_htrack";
        String password = "ganttsucks123";

       try{
           Connection myConn = DriverManager.getConnection(url,username,password);

           Statement myStmt = myConn.createStatement();

           String sql = " INSERT INTO Users (realname)" +"VALUES(JaeJoon)";
           // sql =" UPDATE Users "+ "SET realname = David"+" WHERE realname = JaeJoon ";

           myStmt.executeUpdate(sql);

           System.out.println("Query Complete");

       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }


}
//db - execute sql statements
// INSERT INTO Users (userid,realname,username,password,email,weight,height,acitivty,age)VALUES('1','Jaejoon','Ja3oon','softEngineer','Jae@hotmail.com', '85','180','1','32')

