package com.example.stephan.todo;


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
 * It will save all your items on a file so you wont lose them!
 */
public class MainActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;
    ArrayList<String> itemsOnList = new ArrayList<String>();
    ListView listView;
    MyOwnRowAdapter myadepter;
    Boolean saveDataOnFile = true;

    /**
     * Called on create.
     * !Warning Reads all data from STORETEXT.
     * Find all elements. Initialize MyOwnRowAdapter. and add it to listView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);

        // read a file and add it to itemsOnList.
        readDataFromFile(itemsOnList);

        // make adapter
        myadepter = new MyOwnRowAdapter(this, itemsOnList, saveDataOnFile);

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
            myadepter.insert(itemToAdd,0);

            // update adapter and listView.
            myadepter.notifyDataSetChanged();
        }
    }

    /**
     * !WARNING Reads all data from STORETEXT
     * Get all data in file and add them to placeReadedData
     */
    public void readDataFromFile(ArrayList<String> placeReadedData){
        try {
            // read a file
            Scanner scan = new Scanner(openFileInput("STORETEXT"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()){
                    placeReadedData.add(line);
                }
            }
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
