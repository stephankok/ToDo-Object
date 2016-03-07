package com.example.stephan.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This activity is called when u want to show all items of the list.
 * What file to load and save items will need to be saved in extras.
 */
public class ToDoActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                     // Get To Do from user
    ArrayList<String> itemsOnList = new ArrayList<String>();    // Save all items of To Do List
    ListView listView;                                          // Place adapter here
    MyOwnRowAdapter itemAdapter;                                // Make adapter
    Boolean saveDataOnFile = true;                              // Ensure data is saved
    ArrayList<Boolean> itemCheckedArray                         // Save the color of To Do list items
            = new ArrayList<Boolean>();
    String listName;                                            // list name
    String fileSaveLocation;                                    // where the items are stored
    TextView nameOfThisList;                                    // display list name


    /**
     * Called on create.
     * Get data from previous activity, make adapter, read data and set ListView.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        // find elements.
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);
        nameOfThisList = (TextView) findViewById(R.id.nameOfThisList);

        // stop keyboard from popping up.

        // get filename to save and load, if not exist give error.
        try {
            // get filename.
            listName = getIntent().getExtras().getString("listName");
            fileSaveLocation = getIntent().getExtras().getString("fileSaveLocation");

            // set name of this list.
            nameOfThisList.setText(listName);

            // read a file and add it to itemsOnList an colorData.
            readDataFromFile();

            // make adapter.
            itemAdapter = new MyOwnRowAdapter(this, itemsOnList, saveDataOnFile,
                    itemCheckedArray, fileSaveLocation);

            // add adapter to ListView.
            listView.setAdapter(itemAdapter);

        }
        catch (Throwable error){
            Toast.makeText(ToDoActivity.this,
                    "An Error Occurred: ", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);

        // Make a back button
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }
    /**
     * Add a listener on action bar.
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        // check witch item is pressed.
        switch (item.getItemId()){
            case R.id.help:
                Intent settingsWindows = new Intent(this, HelpActivity.class);
                startActivity(settingsWindows);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Is called when ADD button is clicked.
     * It will add item to adapter and update it.
     */
    public void updateListView(View view) {
        // get item to add
        String itemToAdd = addItemToList.getText().toString();

        // check if not empty
        if (!itemToAdd.isEmpty()) {
            // add item.
            itemAdapter.insert(itemToAdd, 0);

            // update adapter and listView.
            itemAdapter.notifyDataSetChanged();

            // make EditText empty again.
            addItemToList.setText("");
        }
    }

    /**
     * Read he file fileName
     */
    public void readDataFromFile() {
        // make sure lists are clear.
        itemCheckedArray.clear();
        itemsOnList.clear();

        try {
            // Open File
            Scanner scan = new Scanner(openFileInput(fileSaveLocation));

            // Find all To Do items
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // If this line is found you will get color id's
                if (line.compareTo("END OF THE ITEMS NOW CHECKED") == 0) {
                    break;
                }

                // if line is not empty it is data, add it.
                if (!line.isEmpty()) {
                    itemsOnList.add(line);
                }
            }

            // Now find all checkboxes.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.isEmpty()) {
                    itemCheckedArray.add(Boolean.parseBoolean(line));
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will delete all items in the list.
     */
    public void clearTheCurrentList(View view){

        // Make an AlertDialog that will delete all items in list.
        AlertDialog.Builder alertDeleteAllItems = new AlertDialog.Builder(this);

        // set info.
        alertDeleteAllItems.setTitle(R.string.confirmDelete)
                .setMessage(R.string.deleteAllMessage)
                .setCancelable(false);

        // set buttons.
        alertDeleteAllItems.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On yes delete all.
                itemAdapter.clearList();

                // update.
                itemAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On no cancel.
                        dialog.cancel();
                    }
                });

        // Make AlertDialog popup.
        AlertDialog alertDialog = alertDeleteAllItems.create();
        alertDialog.show();
    }

}
