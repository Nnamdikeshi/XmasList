package Entities;

/**
 * Created by Nnamdi on 12/4/2016.
 */

import java.util.*;
import java.text.SimpleDateFormat;

public class Items {
    private int itemID;
    private String name;
    private String price;
    private int priority;
    private Date dateEntered;

    public Items ( int itemID, String name, String price, int priority ) {
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.dateEntered = new Date ( );
    }

    public int getItemID () {
        return itemID;
    }

    public String getName () {
        return name;
    }

    public String getPrice () {
        return price;
    }

    public int getPriority () {
        return priority;
    }

    @Override
    public String toString () {
        String datePattern = "HH:mm 'on' MM/dd/yy";
        SimpleDateFormat format = new SimpleDateFormat ( datePattern );
        String dateEnteredString = format.format ( dateEntered );

        return ( "ID: " + this.itemID + " Name: " + this.name + " Price: " + this.price + " Priority Level: "
                + this.priority + " Reported on: " + dateEnteredString );
    }

}
