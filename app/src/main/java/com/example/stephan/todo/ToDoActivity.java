package com.example.stephan.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This activity is called when u want to show an item.
 *
 * What file to load and save items will need to be in extras.
 *
 */
public class ToDoActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                    // Get To Do from user
    ArrayList<String> itemsOnList = new ArrayList<String>();   // Save all items of To Do List
    ListView listView;                                         // Place adapter here
    MyOwnRowAdapter myadepter;                                 // Make adapter
    Boolean saveDataOnFile = true;                             // Ensure data is saved
    ArrayList<Integer> colorData = new ArrayList<Integer>();   // Save the color of To Do list items
    String fileName;                                    // file where items are stored


    /**
     * Called on create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        // stop keyboard from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // find elements
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);

        // get filename to save and load, if not exist give error
        try {
            // get filename
            fileName = getIntent().getExtras().getString("fileName");

            // read a file and add it to itemsOnList an colorData
            readDataFromFile(itemsOnList, colorData, fileName);

            // make adapter
            myadepter = new MyOwnRowAdapter(this, itemsOnList, saveDataOnFile, colorData, fileName);

            // add adapter to listview
            listView.setAdapter(myadepter);
        }
        catch (Throwable error){
            Toast.makeText(ToDoActivity.this,
                    "An error happend please try again", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add actionbar.
     * There will be a back button to return to the mainscreen
     * There will be a edit button and when u press settings a help file.
     * There will be a help option when settings is pressed, here you will get information about the
     * app
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actionbar, menu);

        // Make a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

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
        if (!itemToAdd.isEmpty()) {
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
     * !WARNING Reads all data from fileName. Read apdapter to see how saving works.
     * Get all data in file and add them to placeReadedData.
     */
    public void readDataFromFile(ArrayList<String> placeReadedData, ArrayList<Integer> placeColorData, String fileName) {
        placeColorData.clear();
        placeReadedData.clear();
        try {
            // Open File
            Scanner scan = new Scanner(openFileInput(fileName));

            // Find all To Do items
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // If this line is found you will get color id's
                if (line.compareTo("END OF THE ITEMS NOW COLORS. code:182838") == 0) {
                    break;
                }

                // if line is not empty it is data, add it.
                if (!line.isEmpty()) {
                    placeReadedData.add(line);
                }
            }

            // Now find all Color's
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.isEmpty()) {
                    placeColorData.add(Integer.parseInt(line));
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearTheCurrentList(View view){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Confirm delete");
        alertDialogBuilder.setMessage("Are you sure you want to clear this list?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myadepter.clearlist();
                myadepter.notifyDataSetChanged();
            }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do this
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
