import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDB {

    public static void main(String[] args){
        String url = "jdbc:mysql://freedb.tech:3306/freedbtech_htrackdb?useSSL=false" ;
       String user = "freedbtech_htrack";
       String password = "ganttsucks123";

       try{
           Connection myConn = DriverManager.getConnection(url,user,password);

           Statement myStmt = myConn.createStatement();

           String sql = " INSERT INTO Users (realname)" +"VALUES(JaeJoon)";
           // sql =" UPDATE Users "+ "SET realname = David"+" WHERE realname = JaeJoon ";

           myStmt.executeUpdate(sql);

           System.out.println("Update Complete");

       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
    }


}
