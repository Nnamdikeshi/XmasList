/**
 * Created by Nnamdi on 12/4/2016.
 */

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class XmasDataNeedModel extends AbstractTableModel {
    ResultSet needResultSet;
    private int rowCount = 0;
    private int colCount = 0;

    public XmasDataNeedModel ( ResultSet nrs ) {
        this.needResultSet = nrs;
        setup ( );
    }

    private void setup () {

        countRows ( );

        try {
            colCount = needResultSet.getMetaData ( ).getColumnCount ( );

        } catch (SQLException se) {
            System.out.println ( "Error counting Need columns" + se );
        }

    }


    public void updateResultSet ( ResultSet newNRS ) {
        needResultSet = newNRS;
        setup ( );
    }


    private void countRows () {
        rowCount = 0;
        try {
            //Move cursor to the start...
            needResultSet.beforeFirst ( );
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while ( needResultSet.next ( ) ) {
                rowCount++;

            }
            needResultSet.beforeFirst ( );

        } catch (SQLException se) {
            System.out.println ( "Error counting Need rows " + se );
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
            needResultSet.absolute ( row + 1 );
            Object o = needResultSet.getObject ( col + 1 );
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

        int newNeedPriority;

        try {
            newNeedPriority = Integer.parseInt ( newValue.toString ( ) );

            if ( newNeedPriority < XmasDB.ITEM_MIN_PRIORITY || newNeedPriority > XmasDB.ITEM_MAX_PRIORITY ) {
                throw new NumberFormatException ( "You have to choose an appropriate priority level." );
            }
        } catch (NumberFormatException ne) {

            JOptionPane.showMessageDialog ( null, "Try entering a number between " + XmasDB.ITEM_MIN_PRIORITY + " " + XmasDB.ITEM_MAX_PRIORITY );
            //return prevents the following database update code happening...
            return;
        }

        //This only happens if the new priority is valid
        try {
            needResultSet.absolute ( row + 1 );
            needResultSet.updateInt ( XmasDB.PRIORITY_COLUMN, newNeedPriority );
            needResultSet.updateRow ( );
            fireTableDataChanged ( );
        } catch (SQLException e) {
            System.out.println ( "error changing priority on need table" + e );
        }

    }


    @Override
    public boolean isCellEditable ( int row, int col ) {
        return col == 3;
    }

    //Delete row, return true if successful, false otherwise
    public boolean deleteRow ( int row ) {
        try {
            needResultSet.absolute ( row + 1 );
            needResultSet.deleteRow ( );
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
            needResultSet.moveToInsertRow ( );
            needResultSet.updateString ( XmasDB.NAME_COLUMN, name );
            needResultSet.updateInt ( XmasDB.PRICE_COLUMN, price );
            needResultSet.updateInt ( XmasDB.PRIORITY_COLUMN, priority );
            needResultSet.insertRow ( );
            needResultSet.moveToCurrentRow ( );
            fireTableDataChanged ( );
            return true;

        } catch (SQLException e) {
            System.out.println ( "Error adding row" );
            System.out.println ( e );
            return false;
        }

    }

    @Override
    public String getColumnName ( int col ) {
        try {
            return needResultSet.getMetaData ( ).getColumnName ( col + 1 );
        } catch (SQLException se) {
            System.out.println ( "Error fetching column names" + se );
            return "?";
        }
    }

}
