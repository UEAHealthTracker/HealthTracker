import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {

    //Method to connect to the sqlite db
    public static Connection connect() {

        //Create a new connection
        Connection conn = null;

        //Try/catch block to catch any db errors
        try {

            //db parameters
            String url = "jdbc:sqlite:users.db";

            //create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //Return the connection for the db
        return conn;
    }

    //Test harness to connect to the db
    public static void main(String[] args) {
        connect();
    }

}
