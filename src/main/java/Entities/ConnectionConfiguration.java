package Entities;

/**
 * Created by Nnamdi on 12/4/2016.
 */

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionConfiguration {
    public static Connection getConnection () {
        Connection connection = null;

        try {
            Class.forName ( "com.mysql.jdbc.Driver" );
            connection = DriverManager.getConnection ( "jdbc:mysql://localhost:3306/test", "root", "11271994" );
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        return connection;
    }

}
