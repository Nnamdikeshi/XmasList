import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Nnamdi on 12/6/2016.
 */

public class XmasDataWantModel extends AbstractTableModel{
    ResultSet wantResultSet;
    private int rowCount = 0;
    private int colCount = 0;

    public XmasDataWantModel ( ResultSet wrs ) {
        this.wantResultSet = wrs;
        setup ( );
    }

    private void setup () {

        countRows ( );

        try {
            colCount = wantResultSet.getMetaData ( ).getColumnCount ( );

        } catch (SQLException se) {
            System.out.println ( "Error counting columns" + se );
        }

    }


    public void updateResultSet ( ResultSet newWRS ) {
        wantResultSet = newWRS;
        setup ( );
    }


    private void countRows () {
        rowCount = 0;
        try {
            //Move cursor to the start...
            wantResultSet.beforeFirst ( );
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while ( wantResultSet.next ( ) ) {
                rowCount++;

            }
            wantResultSet.beforeFirst ( );

        } catch (SQLException se) {
            System.out.println ( "Error counting rows " + se );
        }

    }

    @Override
    public int getRowCount () {
        countRows ( );
        return rowCount;
    }

    @Override
    public int getColumnCount () {
        return colCount;
    }

    @Override
    public Object getValueAt ( int row, int col ) {
        try {
            //  System.out.println("get value at, row = " +row);
            wantResultSet.absolute ( row + 1 );
            Object o = wantResultSet.getObject ( col + 1 );
            return o.toString ( );
        } catch (SQLException se) {
            System.out.println ( se );
            //se.printStackTrace();
            return se.toString ( );

        }
    }

    @Override
    //This is called when user edits an editable cell
    public void setValueAt ( Object newValue, int row, int col ) {

        //Make sure newValue is an integer AND that it is in the range of valid ratings

        int newWantPriority;

        try {
            newWantPriority = Integer.parseInt ( newValue.toString ( ) );

            if ( newWantPriority < XmasDB.ITEM_MAX_PRIORITY ) {
                throw new NumberFormatException ( "This list is only for Item's priced below $5,000.00" );
            }
        } catch (NumberFormatException ne) {

            JOptionPane.showMessageDialog ( null, "Try entering a number between " + XmasDB.ITEM_MAX_PRIORITY );
            //return prevents the following database update code happening...
            return;
        }

        //This only happens if the new rating is valid
        try {
            wantResultSet.absolute ( row + 1 );
            wantResultSet.updateInt ( XmasDB.PRIORITY_COLUMN, newWantPriority );
            wantResultSet.updateRow ( );
            fireTableDataChanged ( );
        } catch (SQLException e) {
            System.out.println ( "error changing priority " + e );
        }

    }


    @Override
    public boolean isCellEditable ( int row, int col ) {
        return col == 3;
    }

    //Delete row, return true if successful, false otherwise
    public boolean deleteRow ( int row ) {
        try {
            wantResultSet.absolute ( row + 1 );
            wantResultSet.deleteRow ( );
            //Tell table to redraw itself
            fireTableDataChanged ( );
            return true;
        } catch (SQLException se) {
            System.out.println ( "Delete row error " + se );
            return false;
        }
    }

    //returns true if successful, false if error occurs
    public boolean insertRow ( String name, int price, int priority ) {

        try {
            //Move to insert row, insert the appropriate data in each column, insert the row, move cursor back to where it was before we started

            wantResultSet.moveToInsertRow ( );
            wantResultSet.updateString ( XmasDB.NAME_COLUMN, name );
            wantResultSet.updateInt ( XmasDB.PRICE_COLUMN, price );
            wantResultSet.updateInt ( XmasDB.PRIORITY_COLUMN, priority );
            wantResultSet.insertRow ( );
            wantResultSet.moveToCurrentRow ( );
            fireTableDataChanged ( );
            return true;

        } catch (SQLException e) {
            System.out.println ( "Error adding row" );
            System.out.println ( e );
            return false;
        }

    }

    public boolean transferRow ( String name, int price, int priority ) {

        try {
            wantResultSet.moveToInsertRow ( );
            wantResultSet.updateString ( XmasDB.NAME_COLUMN, name );
            wantResultSet.updateInt ( XmasDB.PRICE_COLUMN, price );
            wantResultSet.updateInt ( XmasDB.PRIORITY_COLUMN, priority );
            wantResultSet.insertRow ( );
            wantResultSet.moveToCurrentRow ( );
            fireTableDataChanged ( );
            return true;

        } catch (SQLException e) {
            System.out.println ( "Error Trsnfering row" );
            System.out.println ( e );
            return false;
        }

    }


    @Override
    public String getColumnName ( int col ) {
        try {
            return wantResultSet.getMetaData ( ).getColumnName ( col + 1 );
        } catch (SQLException se) {
            System.out.println ( "Error fetching column names" + se );
            return "?";
        }
    }

}


