package Helpers;

import java.sql.*;

public class MySQLConnection {
    public static Connection getConnection () {
        String url       = "jdbc:mysql://localhost:3306/ehealth?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user      = "mergim";
        String password  = "mergim";
        Connection conn = null;

        try {
            // create a connection to the e-health database
            conn = DriverManager.getConnection(url, user, password);
        } catch( SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
