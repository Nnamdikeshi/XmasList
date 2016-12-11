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

    XmasGUI ( final XmasDataNeedModel xmasDataNeedModel, final XmasDataWantModel xmasDataWantModel ) {

        super ( "Christmas List Database Application" );

        setContentPane ( rootPanel );
        pack ( );
        addWindowListener ( this );
        setVisible ( true );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );


        //Set up JTables
        needTable.setGridColor ( Color.BLUE );
        needTable.setModel ( xmasDataNeedModel );

        wantTable.setGridColor ( Color.YELLOW );
        wantTable.setModel ( xmasDataWantModel );

        //Set up the priority spinner.
        //SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.
        prioritySpinner.setModel ( new SpinnerNumberModel ( 1, XmasDB.ITEM_MIN_PRIORITY, XmasDB.ITEM_MAX_PRIORITY, 10 ) );

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
                int currentNeedRow = needTable.getSelectedRow ( );
                int currentWantRow = wantTable.getSelectedRow ( );

                if ( currentNeedRow == - 1 && currentWantRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog ( rootPane, "Please choose an item to delete" );
                }
                if (currentNeedRow == 1) {
                    boolean deletedOne = xmasDataWantModel.deleteRow ( currentNeedRow );

                    if ( currentWantRow == 1 ) {
                        boolean deletedTwo = xmasDataNeedModel.deleteRow ( currentWantRow );

                        if ( deletedOne || deletedTwo ) {
                            XmasDB.loadAllItems ( );
                        } else {
                            JOptionPane.showMessageDialog ( rootPane, "Error deleting item" );
                        }
                    }
                }}});
        addButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                //Get Movie title, make sure it's not blank
                String nameData = txtNameInput.getText();

                if (nameData == null || nameData.trim().equals("")) {
                    JOptionPane.showMessageDialog(rootPane, "Please enter a title for the new movie");
                    return;
                }

                //Get movie year. Check it's a number between 1900 and present year
                int priceData;

                try {
                    priceData = Integer.parseInt(txtPriceInput.getText());
                    if (priceData < 5000){
                        throw new NumberFormatException("This Christmas list will only include prices below $5,000.00");
                    }
                } catch (NumberFormatException ne) {
                    JOptionPane.showMessageDialog(rootPane,
                            "Change your price to be between 0 & 5,000...");
                    return;
                }

                //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
                int priorityData = (Integer)(prioritySpinner.getValue());

                System.out.println("Adding " + nameData + " " + priceData + " " + priorityData);
                if (radioNeed.isSelected ()) {
                    boolean insertedRow = xmasDataNeedModel.insertRow ( nameData, priceData, priorityData );

                    if ( ! insertedRow ) {
                        JOptionPane.showMessageDialog ( rootPane, "Error adding new Item to  Need list" );
                    }}
                    if ( radioWant.isSelected ( ) ) {
                        boolean insertedrow = xmasDataWantModel.insertRow ( nameData, priceData, priorityData );
                        if ( ! insertedrow ) {
                            JOptionPane.showMessageDialog ( rootPane, "Error adding new Item to Want list" );
                        }}
                // If insertedRow is true and the data was added, it should show up in the table, so no need for confirmation message.
            }

        });
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
