import java.sql.*;

public class DBsession {
    public final static DBsession INSTANCE = new DBsession();


    public Statement Stmt() {
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://freedb.tech:3306/freedbtech_htrackdb?useSSL=false", "freedbtech_htrack", "ganttsucks123");
            stmt = con.createStatement();


        } catch (Exception e) {
            System.out.println(e);
        }
        return stmt;
    }

    public Connection OpenConnection() {
        Connection con=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://freedb.tech:3306/freedbtech_htrackdb?useSSL=false", "freedbtech_htrack", "ganttsucks123");

        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    //prepered statements example functions
 /**   public void Login(ActionEvent actionEvent) {
        passwordtf.setStyle("-fx-text-fill:white");
        String userdb,passdb;

        try{
            ResultSet rs=DBsession.INSTANCE.Stmt().executeQuery("select username,password,realname from Users ");
            while(rs.next()) {
                userdb=rs.getString("username");
                passdb=rs.getString("password");
                User.INSTANCE.setUsername(usernametf.getText());
                User.INSTANCE.setPassword(passwordtf.getText());

                if (userdb.equals(User.INSTANCE.getUsername())&&passdb.equals(User.INSTANCE.getPassword()) ) {
                    User.INSTANCE.setRealName(rs.getString("realname"));
                    closewindow(actionEvent);
                    userController.userwindow();
                } else {
                    logbtn.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
                    logbtn.setText("Wrong Username/Password");
                    thread.start();
                }
            }
        DBsession.INSTANCE.OpenConnection().close();
        }catch(Exception e){ System.out.println(e);}
    }

    private static final String SQL_INSERT="INSERT INTO Users(  RealName, email, username, password, weight, height, activity,age) VALUES (?,?,?,?,?,?,?,?)";
    public void CreateAccount(ActionEvent event){
        String userdb,emaildb,usnm,pass,rname,email,mass;
        float wght;
        int ht,aty,ag3;
        usnm=usernametf.getText();
        pass=passwordtf.getText();
        rname=realnametf.getText();
        email=emailtf.getText();
        mass=weight.getSelectionModel().getSelectedItem().toString()+"."+ gr.getSelectionModel().getSelectedItem().toString();;
        wght=Float.parseFloat(mass);
        ht=Integer.parseInt(height.getSelectionModel().getSelectedItem().toString());
        aty=activity.getSelectionModel().getSelectedIndex();
        ag3=Integer.parseInt(Age.getSelectionModel().getSelectedItem().toString());

        if (email.matches(EMAIL_PATTERN)) {
            try {
                PreparedStatement pst = DBsession.INSTANCE.OpenConnection().prepareStatement(SQL_INSERT);
                pst.setString(1, rname);
                pst.setString(2, email);
                pst.setString(3, usnm);
                pst.setString(4, pass);
                pst.setFloat(5, wght);
                pst.setFloat(6, ht);
                pst.setInt(7, aty);
                pst.setInt(8, ag3);

                if (CheckCredentials() > 0) {
                    createacc.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
                    createacc.setText("Username/Password already exists");
                    thread.start();
                    DBsession.INSTANCE.OpenConnection().close();
                } else {
                    thread.start();
                    pst.executeUpdate();
                    User.INSTANCE.setUsername(usernametf.getText());
                    User.INSTANCE.setPassword(passwordtf.getText());
                    User.INSTANCE.setRealName(realnametf.getText());
                    UserController.INSTANCE.userwindow();
                    closewindowlog();
                }

            DBsession.INSTANCE.OpenConnection().close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            createacc.setStyle("-fx-background-color:transparent;-fx-text-fill: red");
            createacc.setText("Wrong Email address");
            thread.start();

        }
    }
  */
}
