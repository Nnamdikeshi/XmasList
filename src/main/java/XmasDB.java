/**
 * Created by Nnamdi on 11/29/2016.
 * */

import java.sql.*;

public class XmasDB {
    // Name our tables
    public final static String WANT_TABLE_NAME = "want_list";
    public final static String NEED_TABLE_NAME = "need_list";

    // Each Product will have a unique ID
    public final static String PK_COLUMN = "ID";

    // A primary key is needed to allow updates to the database on modifications to ResultSet
    public final static String NAME_COLUMN = "Product_Name";
    public final static String PRICE_COLUMN = "Price";
    public final static String PRIORITY_COLUMN = "Priority";
    public final static int ITEM_MIN_PRIORITY = 1;
    public final static int ITEM_MAX_PRIORITY = 10;
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";   //TODO replace with your username
    static final String PASS = "*****";   //TODO replace with your password
    static private final String DB_NAME = "xmas";
    // create our statements
    static Statement statementWant = null;
    static Statement statementNeed = null;
    // create connection and resultSets
    static Connection conn = null;
    static ResultSet nrs = null;
    static ResultSet wrs = null;

    // Create our data model(s)
    private static XmasDataWantModel xmasDataWantModel;
    private static XmasDataNeedModel xmasDataNeedModel;

    // Main method
    public static void main ( String args[] ) throws SQLException {


        //setup creates database (if it doesn't exist), opens connection, and adds sample data
        if ( ! setup ( ) ) {
            System.exit ( - 1 );
        }

        if ( ! loadAllItems( ) ) {
            System.exit ( - 1 );
        }

        //If no errors, then start GUI
        XmasGUI tableGUI = new XmasGUI ( xmasDataNeedModel, xmasDataWantModel );

    }

    public static boolean loadAllItems() {

        try {
            // SQL statements to select table info for use
            String getRestData = "SELECT * FROM " + WANT_TABLE_NAME;
            wrs = statementWant.executeQuery ( getRestData );
            String getSomeData = "SELECT * FROM " + NEED_TABLE_NAME;
            nrs = statementNeed.executeQuery ( getSomeData );

            // If the models are null then create them
            if ( xmasDataNeedModel == null ) {
                xmasDataNeedModel = new XmasDataNeedModel ( nrs );
            }
            else {
                xmasDataNeedModel.updateResultSet ( nrs );
            }
            if ( xmasDataWantModel == null ) {
                xmasDataWantModel = new XmasDataWantModel ( wrs );
            } else {
                xmasDataWantModel.updateResultSet ( wrs );
            }

            return true;
            // Catch exception and show error
        } catch (Exception e) {
            System.out.println ( "Error loading or reloading lists" );
            System.out.println ( e );
            e.printStackTrace ( );
            return false;
        }

    }

    public static boolean setup () {
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName ( Driver );
            } catch (ClassNotFoundException cnfe) {
                System.out.println ( "No database drivers found. Quitting" );
                return false;
            }

            conn = DriverManager.getConnection ( DB_CONNECTION_URL + DB_NAME, USER, PASS );

            //Create two statements, and then the results sets from each can be used independently.
            statementWant = conn.createStatement ( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );
            statementNeed = conn.createStatement ( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );


            // Do the tables exist? If not, create them
            if ( ! xmasTableTwoExists ( ) ) {
                String createWantTableSQL = "CREATE TABLE " + WANT_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " int, " + PRIORITY_COLUMN + " int, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println ( createWantTableSQL );
                statementWant.executeUpdate ( createWantTableSQL );
                System.out.println ( "Created Want Table..." );
                // Insert our default data into Table
                String addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Gaming PC', 500, 3)";
                statementWant.executeUpdate ( addMoreSQL );
                addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES('Astrology Book', 150, 2)";
                statementWant.executeUpdate ( addMoreSQL );
                addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Hover Board', 300, 1)";
                statementWant.executeUpdate ( addMoreSQL );
            }
            if ( ! xmasTableOneExists ( ) ) {
                String createNeedTableSQL = "CREATE TABLE " + NEED_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " int, " + PRIORITY_COLUMN + " int, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println ( createNeedTableSQL );
                statementNeed.executeUpdate ( createNeedTableSQL );
                System.out.println ( "Created Need table..." );
                // Insert our default data into Table
                String addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('New Winter Boots', 85, 3)";
                statementNeed.executeUpdate ( addDataSQL );
                addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES('New Car', 3000, 2)";
                statementNeed.executeUpdate ( addDataSQL );
                addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Gift Card to Grocery Store', 300, 1)";
                statementNeed.executeUpdate ( addDataSQL );
            }
            return true;

        } catch (SQLException se) {
            System.out.println ( se );
            se.printStackTrace ( );
            return false;
        }
    }

    // Methods that check to see if the tables exist
    private static boolean xmasTableOneExists () throws SQLException {

        String checkNeedTablePresentQuery = "SHOW TABLES LIKE '" + NEED_TABLE_NAME + "'";   //Can query the database schema
        ResultSet tablesRS = statementNeed.executeQuery ( checkNeedTablePresentQuery );

        return tablesRS.next ( );

    }

    private static boolean xmasTableTwoExists () throws SQLException {

        String checkNeedTablePresentQuery = "SHOW TABLES LIKE '" + WANT_TABLE_NAME + "'";

        ResultSet tableTwoRS = statementNeed.executeQuery ( checkNeedTablePresentQuery );

        return tableTwoRS.next ( );

    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown () {
        try {
            if ( nrs != null && wrs != null) {
                wrs.close ( );
                nrs.close ( );
                System.out.println ( "Result sets closed" );
            }
        } catch (SQLException se) {
            se.printStackTrace ( );
        }

        try {
            if ( statementWant != null ) {
                statementWant.close ( );
                System.out.println ( "Statement closed" );
            }
        } catch (SQLException se) {
            //Closing the connection could throw an exception too
            se.printStackTrace ( );
        }
        try {
            if ( statementNeed != null ) {
                statementNeed.close ( );
                System.out.println ( "Statement closed" );
            }
        } catch (SQLException se) {
            //Closing the connection could throw an exception too
            se.printStackTrace ( );
        }

        try {
            if ( conn != null ) {
                conn.close ( );
                System.out.println ( "Database connection closed" );
            }
        } catch (SQLException se) {
            se.printStackTrace ( );
        }
    }
}