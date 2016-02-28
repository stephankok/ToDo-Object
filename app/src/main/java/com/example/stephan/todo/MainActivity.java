package com.example.stephan.todo;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A To Do app created by Stephan Kok.
 * It will create items with an own created adapter MyOwnRowAdapter.
 * You can add items by pressing the button and you can delete items by longclicking on it.
 * Feature: You can make items green by pressing them
 * It will save all your items on a file so you wont lose them! Data is saved per line,
 * so make sure users cannot enter a new line.
 */
public class MainActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                    // Get To Do from user
    ArrayList<String> itemsOnList = new ArrayList<String>();   // Save all items of To Do List
    ListView listView;                                         // Place adapter here
    MyOwnRowAdapter myadepter;                                 // Make adapter
    Boolean saveDataOnFile = true;                             // Ensure data is saved
    ArrayList<Integer> colorData = new ArrayList<Integer>();   // Save the color of To Do list items

    /**
     * Called on create.
     * !Warning Reads all data from STORETEXT.
     * Find all elements. Initialize MyOwnRowAdapter. and add it to listView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find elements
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);

        // read a file and add it to itemsOnList an colorData
        readDataFromFile(itemsOnList, colorData);

        // make adapter
        myadepter = new MyOwnRowAdapter(this, itemsOnList, saveDataOnFile, colorData);

        // add adapter to listview
        listView.setAdapter(myadepter);
    }

    /**
     * Is called when ADD button is clicked.
     * It will add item to adapter and update it.
     */
    public void updateListView(View view) {

        // get item to add
        String itemToAdd = addItemToList.getText().toString();
        if(!itemToAdd.isEmpty()) {
            // make it empty
            addItemToList.setText("");

            // add item
            myadepter.insert(itemToAdd, 0);

            // add color
            myadepter.insertColor();

            // update adapter and listView.
            myadepter.notifyDataSetChanged();
        }
    }

    /**
     * !WARNING Reads all data from STORETEXT. Read apdapter to see how saving works.
     * Get all data in file and add them to placeReadedData.
     */
    public void readDataFromFile(ArrayList<String> placeReadedData, ArrayList<Integer> placeColorData ){
        try {
            // Open File
            Scanner scan = new Scanner(openFileInput("STORETEXT"));

            // Find all To Do items
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // If this line is found you will get color id's
                if(line.compareTo("END OF THE ITEMS NOW COLORS. code:182838") == 0){
                    break;
                }

                // if line is not empty it is data, add it.
                if (!line.isEmpty()){
                    placeReadedData.add(line);
                }
            }

            // Now find all Color's
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.isEmpty()){
                    placeColorData.add(Integer.parseInt(line));
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
