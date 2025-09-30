
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Conexao {
    private Connection con;
    
    public Conexao() {
        String driver = "org.postgresql.Driver";
        String user = "postgres";
        String senha = "root";
        String url = "jdbc:postgresql://localhost:5432/academia_relacional";

        // String driver = "com.mysql.cj.jdbc.Driver";
        // String user = "root";
        // String senha = "root";
        // String url = "jdbc:mysql://localhost:3306/academia_relacional";

        try {
            Class.forName(driver);
            this.con = (Connection) DriverManager.getConnection(url, user, senha);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.exit(1);
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.exit(1);
        }  
    }
    
    public Connection getConnection() {
        return con;
    }
    
    public void closeConnection(){
        try {
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
