package com.example.stephan.todo;


import android.content.Intent;
import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * A To Do app created by Stephan Kok.
 * Show all the list the user have created.
 */
public class MainActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                     // Get To Do from user.
    ArrayList<String> itemsOnList = new ArrayList<String>();    // Save all items of To Do List.
    ArrayList<String> timeList = new ArrayList<String>();       // Save all items of To Do List.
    ArrayList<String> fileNameList = new ArrayList<String>();   // Save all filenames.
    String fileCounter;                                         // Keep track of files used.
    ListView listView;                                          // Place adapter here.
    MyOwnListAdapter listAdapter;                               // Make adapter.
    String fileName;                                            // Main save file.
    Calendar calender = Calendar.getInstance();                 // Get the time.
    SimpleDateFormat dateFormat                                 // Specific date format.
            = new SimpleDateFormat("dd-MMM HH:mm");

    /**
     * Called on create.
     * Make adapter, read data and set ListView.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find elements.
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);

        // Initialize fileCounter to 0.
        fileCounter = String.format("%010d",0);

        // read data from fileName.
        fileName = "PARENTFILE";
        readDataFromFile();

        // make adapter.
        listAdapter = new MyOwnListAdapter(this, itemsOnList, fileName, timeList, fileNameList, fileCounter);

        // add adapter to ListView.
        listView.setAdapter(listAdapter);

        // When editing inside ListView, the EditText loses focusability. This was the fix.
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }


    /**
     * Set Menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
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
                Intent helpWindow = new Intent(this, HelpActivity.class);
                startActivity(helpWindow);
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

        // if not empty add.
        if (!itemToAdd.isEmpty()) {
            // get date
            String formattedDate = dateFormat.format(calender.getTime());

            // make fileSaveLocation
            Integer fileCount = Integer.parseInt(listAdapter.fileCounter) + 1;
            String fileNumber = String.format("%010d", fileCount);

            Log.v("FileCounterUpdate", fileNumber);

            // add item
            listAdapter.itemOnList.add(0,itemToAdd);
            listAdapter.timeList.add(0,formattedDate);
            listAdapter.fileSaveLocation.add(0,fileNumber);
            listAdapter.fileCounter = fileNumber;

            // update adapter and listView.
            listAdapter.notifyDataSetChanged();

            // make it empty
            addItemToList.setText("");
        }
    }

    /**
     * Read the data on fileName.
     */
    public void readDataFromFile() {
        // make sure list is clear
        itemsOnList.clear();
        timeList.clear();
        fileNameList.clear();

        // read file
        try {
            // Open File.
            Scanner scan = new Scanner(openFileInput(fileName));

            // first get the fileCounter.
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(!line.isEmpty()){
                    fileCounter = line;
                    break;
                }
            }

            // get all list names.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("Done Now Time") == 0) {
                    break;
                }
                if (!line.isEmpty()) {
                    itemsOnList.add(line);
                }
            }

            // get all dates.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("Done Now FileSaveLocations") == 0) {
                    break;
                }
                if (!line.isEmpty()) {
                    timeList.add(line);
                }
            }

            // get all File save locations.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    fileNameList.add(line);
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
