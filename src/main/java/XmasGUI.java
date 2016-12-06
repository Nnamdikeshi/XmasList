import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Nnamdi on 11/29/2016.
 */
public class XmasGUI extends JFrame implements WindowListener {
    public JPanel rootPanel;
    private JTable needTable;
    private JTextPane NEEDTABLETextPane1;
    private JTextPane WANTTABLETextPane;
    private JTable wantTable;
    private JButton addButton;
    private JButton exitButton;
    private JTextPane welcomeToXmasListTextPane;
    private JButton transferButton;
    private JRadioButton radioNeed;
    private JRadioButton radioWant;
    private JTextField txtNameInput;
    private JSpinner prioritySpinner;
    private JTextField txtPriceInput;
    private JButton deleteButton;

    XmasGUI ( final XmasDataModel xmasDataTableModel ) {

        super ( "Christmas List Database Application" );

        setContentPane ( rootPanel );
        pack ( );
        addWindowListener ( this );
        setVisible ( true );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );


        //Set up JTables
        needTable.setGridColor ( Color.BLACK );
        needTable.setModel ( xmasDataTableModel );
        wantTable.setGridColor ( Color.YELLOW );
        wantTable.setModel ( xmasDataTableModel );

        //Set up the priority spinner.
        //SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.
        prioritySpinner.setModel ( new SpinnerNumberModel ( 1, XmasDB.MOVIE_MIN_RATING, XmasDB.MOVIE_MAX_RATING, 1 ) );

        exitButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                XmasDB.shutdown ( );
                System.exit ( 0 );
            }
        } );
        deleteButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                int currentRow = needTable.getSelectedRow ( );
                int currentrow = wantTable.getSelectedRow ( );

                if ( currentRow == - 1 ) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog ( rootPane, "Please choose a item to delete" );
                }
                boolean deleted = xmasDataTableModel.deleteRow ( currentRow );
                if ( deleted ) {
                    XmasDB.loadAllItems( );
                } else {
                    JOptionPane.showMessageDialog ( rootPane, "Error deleting item" );
                }
            }
        } );
    }


    @Override
    public void windowOpened ( WindowEvent e ) {

    }

    @Override
    public void windowClosing ( WindowEvent e ) {
        System.out.println ( "closing" );
        XmasDB.shutdown ( );
    }

    @Override
    public void windowClosed ( WindowEvent e ) {

    }

    @Override
    public void windowIconified ( WindowEvent e ) {

    }

    @Override
    public void windowDeiconified ( WindowEvent e ) {

    }

    @Override
    public void windowActivated ( WindowEvent e ) {

    }

    @Override
    public void windowDeactivated ( WindowEvent e ) {

    }

}
