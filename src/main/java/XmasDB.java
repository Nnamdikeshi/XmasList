/**
 * Created by Nnamdi on 11/29/2016.
 * */

import java.sql.*;




public class XmasDB {
    public final static String WANT_TABLE_NAME = "want_list";
    public final static String NEED_TABLE_NAME = "need_list";
    // Each solver will have a unique ID
    public final static String PK_COLUMN = "id";
    // A primary key is needed to allow updates to the database on modifications to ResultSet
    public final static String NAME_COLUMN = "name";
    public final static String PRICE_COLUMN = "price";
    public final static String PRIORITY_COLUMN = "riority";
    public final static int ITEM_MIN_PRIORITY = 1;
    public final static int ITEM_MAX_PRIORITY = 10;
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";   //TODO replace with your username
    static final String PASS = "*****";   //TODO replace with your password
    static private final String DB_NAME = "xmas";


    // Name our database
    static Statement statement = null;
    static Connection conn = null;
    static ResultSet nrs = null;
    static ResultSet wrs = null;

    // Create out data model
    private static XmasDataWantModel xmasDataWantModel;
    private static XmasDataNeedModel xmasDataNeedModel;


    public static void main ( String args[] ) throws SQLException {


        //setup creates database (if it doesn't exist), opens connection, and adds sample data


        if ( ! setup ( ) ) {
            System.exit ( - 1 );
        }

        if ( ! loadAllItems( ) ) {
            System.exit ( - 1 );
        }

        //If no errors, then start GUI
        XmasGUI tableGUI;
        tableGUI = new XmasGUI ( xmasDataNeedModel, xmasDataWantModel);

    }

    public static boolean loadAllItems() {

        try {

            if ( nrs != null ) {
                nrs.close ( );
            }
            if ( wrs !=null ) {
                wrs.close ();
            }

            String getSomeData = "SELECT * FROM " + WANT_TABLE_NAME;
            String getRestData = "SELECT * FROM " + NEED_TABLE_NAME;

            nrs = statement.executeQuery ( getSomeData );
            wrs = statement.executeQuery ( getRestData );


            if ( xmasDataNeedModel == null ) {
                xmasDataNeedModel = new XmasDataNeedModel ( nrs );
            }
            if ( xmasDataWantModel == null ) {
                xmasDataWantModel = new XmasDataWantModel ( wrs );
            }
            else {
                //Or, if one already exists, update its ResultSet
                xmasDataNeedModel.updateResultSet ( nrs );
                xmasDataWantModel.updateResultSet ( wrs );
            }

            return true;

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

            statement = conn.createStatement ( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );


            //Does the table exist? If not, create it.
        if ( ! xmasTableTwoExists ( ) ) {
            String createWantTableSQL = "CREATE TABLE " + WANT_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " int, " + PRIORITY_COLUMN + " int, PRIMARY KEY(" + PK_COLUMN + "))";
            System.out.println ( createWantTableSQL );
            statement.executeUpdate ( createWantTableSQL );
            System.out.println ( "Created Want Table..." );

            String addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Gaming PC', 500, 3)";
            statement.executeUpdate ( addMoreSQL );
            addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES('Astrology Book', 150, 2)";
            statement.executeUpdate ( addMoreSQL );
            addMoreSQL = "INSERT INTO " + WANT_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Hover Board', 300, 1)";
            statement.executeUpdate ( addMoreSQL );
        }
            if ( ! xmasTableOneExists ( ) ) {

                //Create a table
                String createNeedTableSQL = "CREATE TABLE " + NEED_TABLE_NAME + " (" + PK_COLUMN + " int NOT NULL AUTO_INCREMENT, " + NAME_COLUMN + " varchar(50), " + PRICE_COLUMN + " int, " + PRIORITY_COLUMN + " int, PRIMARY KEY(" + PK_COLUMN + "))";
                System.out.println ( createNeedTableSQL );


                statement.executeUpdate ( createNeedTableSQL );

                System.out.println ( "Created Need table..." );

                String addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('New Winter Boots', 85, 3)";
                statement.executeUpdate ( addDataSQL );
                addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES('New Car', 3000, 2)";
                statement.executeUpdate ( addDataSQL );
                addDataSQL = "INSERT INTO " + NEED_TABLE_NAME + "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ", " + PRIORITY_COLUMN + ")" + " VALUES ('Gift Card to Grocery Store', 300, 1)";
                statement.executeUpdate ( addDataSQL );
            }
            return true;

        } catch (SQLException se) {
            System.out.println ( se );
            se.printStackTrace ( );
            return false;
        }
    }

    private static boolean xmasTableOneExists () throws SQLException {

        String checkNeedTablePresentQuery = "SHOW TABLES LIKE '" + NEED_TABLE_NAME + "'";   //Can query the database schema
        ResultSet tablesRS = statement.executeQuery ( checkNeedTablePresentQuery );

        return tablesRS.next ( );

    }

    private static boolean xmasTableTwoExists () throws SQLException {

        String checkWantTablePresentQuery = "SHOW TABLES LIKE '" + WANT_TABLE_NAME + "'";

        ResultSet tableTwoRS = statement.executeQuery ( checkWantTablePresentQuery );

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
            if ( statement != null ) {
                statement.close ( );
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
