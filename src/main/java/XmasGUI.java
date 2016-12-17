import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Created by Nnamdi on 11/29/2016.
 */
public class XmasGUI extends JFrame implements WindowListener {
    public JPanel rootPanel;
    private JTable needTable;
    private JTextPane NEEDTABLETextPane1;
    private JButton addButton;
    private JButton exitButton;
    private JButton transferNeedButton;
    private JRadioButton radioNeed;
    private JRadioButton radioWant;
    private JTextField txtNameInput;
    private JSpinner prioritySpinner;
    private JTextField txtPriceInput;
    private JButton deleteButton;
    private JLabel dateTime;
    private JTabbedPane needWantPanes;
    private JTable wantTablePaned;
    private JPanel needTab;
    private JPanel wantPaneTabbed;
    private JTextField wantNameIn;
    private JTextField wantPriceIn;
    private JSpinner wantPriorityIn;
    private JButton addWantButton;
    private JButton exitTwo;
    private JButton deleteWant;
    private JButton transferWantButton;
    private JButton helpButton1;
    private JButton helpButton;
    private JLabel dateTime1;
    private JTabbedPane needWantTabbed;


    XmasGUI ( final XmasDataNeedModel xmasDataNeedModel, final XmasDataWantModel xmasDataWantModel ) {

        setContentPane ( needWantPanes );

        validate ( );
        pack ( );
        addWindowListener ( this );
        setVisible ( true );
        setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE );
        // Call our music class to play theme
        Music.music ( );
        //Set up JTables
        needTable.setGridColor ( Color.GREEN );
        needTable.setModel ( xmasDataNeedModel );

        wantTablePaned.setGridColor ( Color.RED );
        wantTablePaned.setModel ( xmasDataWantModel );

        //Set up priority spinners.
        //SpinnerNumberModel constructor arguments: spinner's initial value, min, max, step.
        prioritySpinner.setModel ( new SpinnerNumberModel ( 1, XmasDB.ITEM_MIN_PRIORITY, XmasDB.ITEM_MAX_PRIORITY, 1 ) );
        wantPriorityIn.setModel ( new SpinnerNumberModel ( 1, XmasDB.ITEM_MIN_PRIORITY, XmasDB.ITEM_MAX_PRIORITY, 1 ) );

        //Set up our Hint messages
        helpButton.setToolTipText ( "Need Help? Click here!" );
        helpButton1.setToolTipText ( "Need Help? Click here!" );
        addButton.setToolTipText ( "Click this button after you've entered the data you want to add" );
        addWantButton.setToolTipText ( "Click this button after you've entered the data you want to add" );
        transferNeedButton.setToolTipText ( "Click this button once you have a radio button and a row selected to transfer" );
        transferWantButton.setToolTipText ( "Click this button once you have a radio button and a row selected to transfer" );
        deleteButton.setToolTipText ( "Click this after you've selected a row to delete" );
        deleteWant.setToolTipText ( "Click this after you've selected a row to delete" );
        // Set our Jlabel to show date and time started program
        dateTime.setText ( "Started program: " + DateFormat.getDateTimeInstance ( ).format ( new Date ( ) ) );
        dateTime1.setText ( "Started program: " + DateFormat.getDateTimeInstance ( ).format ( new Date ( ) ) );
        // Action listener for delete button on need tab
        deleteButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // IF need tab is showing then do action
                if ( needTab.isShowing ( ) ) {
                    int currentNeedRow = needTable.getSelectedRow ( );

                    if ( currentNeedRow == - 1 ) {      // -1 means no row is selected. Display error message.
                        showMessageDialog ( rootPane, "Please choose an item to delete" );
                    }
                    boolean deletedOne = xmasDataNeedModel.deleteRow ( currentNeedRow );

                    // If delete successful, load items from DB
                    if ( deletedOne ) {
                        XmasDB.loadAllItems ( );
                        // If unsuccessful, show error message
                    } else {
                        showMessageDialog ( rootPane, "Error deleting item" );
                    }
                }
            }
        } );

        // Action Listener for Delete button on want tab
        deleteWant.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {

                // IF want tab showing perform action
                if ( wantPaneTabbed.isShowing ( ) ) {
                    // Get selected row
                    int currentWantRow = wantTablePaned.getSelectedRow ( );

                    if ( currentWantRow == - 1 ) {      // -1 means no row is selected. Display error message.
                        showMessageDialog ( rootPane, "Please choose an item to delete" );
                    }
                    // Delete row
                    boolean deletedOne = xmasDataWantModel.deleteRow ( currentWantRow );

                    // If delete successful, load items from DB
                    if ( deletedOne ) {
                        XmasDB.loadAllItems ( );
                        // If unsuccessful, show error message
                    } else {
                        showMessageDialog ( rootPane, "Error deleting item" );
                    }
                }
            }
        } );
        // Action listener for add button on need tab
        addButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // If need tab is showing then perform action
                if ( needTab.isShowing ( ) ) {
                    //Get name, make sure it's not blank
                    String nameData = txtNameInput.getText ( );
                    // If nothing was put into the txt box then show error message
                    if ( nameData == null || nameData.trim ( ).equals ( "" ) ) {
                        showMessageDialog ( rootPane, "Please enter a name for your item" );
                        return;
                    }

                    //get price
                    int priceData;
                    // Try catch for our list budget
                    try {
                        priceData = Integer.parseInt ( txtPriceInput.getText ( ) );
                        if ( priceData > 5000 ) {
                            throw new NumberFormatException ( "This Christmas list will only include prices below $5,000.00" );
                        }
                    } catch (NumberFormatException ne) {
                        showMessageDialog ( rootPane,
                                "Change your price to be between 0 & 5,000..." );
                        return;
                    }

                    //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
                    int priorityData = (Integer) ( prioritySpinner.getValue ( ) );

                    // Print what we are adding
                    System.out.println ( "Adding " + nameData + " " + priceData + " " + priorityData );
                    // Insert our new row based on the data
                    boolean insertedNeedRow = xmasDataNeedModel.insertRow ( nameData, priceData, priorityData );
                    // IF adding fails then throw error message
                    if ( ! insertedNeedRow ) {
                        showMessageDialog ( rootPane, "Error adding new Item to Need list" );
                    }


                }
            }

        });
        // Action listener for add button on want tab
        addWantButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                // If want tab is showing then perform action
                if ( wantPaneTabbed.isShowing ( ) ) {
                    // Get our name Data from txt box
                    String nameData = wantNameIn.getText ( );
                    // If nothing is in txt box after button click then throw error
                    if ( nameData == null || nameData.trim ( ).equals ( "" ) ) {
                        showMessageDialog ( rootPane, "Please enter a title for the new Item" );
                        return;
                    }

                    //get price
                    int priceData;
                    // Try catch for our list budget
                    try {
                        priceData = Integer.parseInt ( wantPriceIn.getText ( ) );
                        if ( priceData > 5000 ) {
                            throw new NumberFormatException ( "This Christmas list will only include prices below $5,000.00" );
                        }
                    } catch (NumberFormatException ne) {
                        showMessageDialog ( rootPane,
                                "Change your price to be between 0 & 5,000..." );
                        return;
                    }

                    //Using a spinner means we are guaranteed to get a number in the range we set, so no validation needed.
                    int priorityData = (Integer) ( wantPriorityIn.getValue ( ) );
                    // Print what we are adding
                    System.out.println ( "Adding " + nameData + " " + priceData + " " + priorityData );
                    // insert our row
                    boolean insertedWantRow = xmasDataWantModel.insertRow ( nameData, priceData, priorityData );
                    // IF failed to insert then throw error
                    if ( ! insertedWantRow ) {
                        showMessageDialog ( rootPane, "Error adding new Item to Want list" );
                    }
                }
            }
        } );
        // Action listener for Transfer button on need tab
        transferNeedButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {

                int currentNeedRow = needTable.getSelectedRow ( );
                String transName = xmasDataNeedModel.getColumnName ( 0 );
                int transPrice = Integer.parseInt ( xmasDataNeedModel.getColumnName ( 1 ) );
                int transPriority = Integer.parseInt ( xmasDataNeedModel.getColumnName ( 2 ) );
                if ( currentNeedRow == - 1 ) {      // -1 means no row is selected. Display error message.
                    showMessageDialog ( rootPane, "Please choose an item to transfer" );
                }
                boolean insertedWantRow = xmasDataWantModel.insertRow ( transName, transPrice, transPriority );
                if ( insertedWantRow ) {
                    // Delete row
                    boolean deletedOne = xmasDataNeedModel.deleteRow ( currentNeedRow );

                    // If delete successful, load items from DB
                    if ( deletedOne ) {
                        XmasDB.loadAllItems ( );
                        // If unsuccessful, show error message
                    } else {
                        showMessageDialog ( rootPane, "Error deleting item" );
                    }
                }
                if ( ! insertedWantRow ) {
                    showMessageDialog ( rootPane, "Error adding new Item to Want list" );
                }

            }
        } );
        // Action listener for Transfer button on want tab
        transferWantButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                int currentWantRow = wantTablePaned.getSelectedRow ( );
                String transName = xmasDataWantModel.getColumnName ( 0 );
                int transPrice = Integer.parseInt ( xmasDataWantModel.getColumnName ( 1 ) );
                int transPriority = Integer.parseInt ( xmasDataWantModel.getColumnName ( 2 ) );
                if ( currentWantRow == - 1 ) {      // -1 means no row is selected. Display error message.
                    showMessageDialog ( rootPane, "Please choose an item to transfer" );
                }
                boolean insertedWantRow = xmasDataNeedModel.insertRow ( transName, transPrice, transPriority );
                if ( insertedWantRow ) {
                    // Delete row
                    boolean deletedOne = xmasDataWantModel.deleteRow ( currentWantRow );

                    // If delete successful, load items from DB
                    if ( deletedOne ) {
                        XmasDB.loadAllItems ( );
                        // If unsuccessful, show error message
                    } else {
                        showMessageDialog ( rootPane, "Error deleting item" );
                    }
                }
                if ( ! insertedWantRow ) {
                    showMessageDialog ( rootPane, "Error adding new Item to Want list" );
                }

            }
        } );
        // Our action listeners for our exit buttons
        exitButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                XmasDB.shutdown ( );
                System.exit ( 0 );
            }
        } );
        exitTwo.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                XmasDB.shutdown ( );
                System.exit ( 0 );
            }
        } );

        // Action listener for help button on need tab
        helpButton.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                showMessageDialog ( null, "This program is a Christmas list developer with two tables (Want & Need)\nTo add a row to this table, insert some data into the name & price field then select what priority you need and then click the add button.\nIf you are trying to delete a row then select one and click the Delete button.\nIf you are trying to transfer a row then select one and click the Transfer button.\nIf you are done with this program click the Exit button. \n\n Thank you for using this application!\nDeveloper: Nnamdi Keshi - Software Developer Undergrad\n " );
            }
        } );
        // Action listener for help button on want tab
        helpButton1.addActionListener ( new ActionListener ( ) {
            @Override
            public void actionPerformed ( ActionEvent e ) {
                showMessageDialog ( null, "This program is a Christmas list developer with two tables (Want & Need)\nTo add a row to this table, insert some data into the name & price field then select what priority you need and then click the add button.\nIf you are trying to delete a row then select one and click the Delete button.\nIf you are trying to transfer a row then select one and click the Transfer button.\nIf you are done with this program click the Exit button. \n\n Thank you for using this application!\nDeveloper: Nnamdi Keshi - Software Developer Undergrad\n " );

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