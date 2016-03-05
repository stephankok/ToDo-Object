package com.example.stephan.todo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * A To Do app created by Stephan Kok.
 *
 * This is the main activity here you will be able to create lists. For this MyOwnListAdapter is
 * used.
 * You can delete items by longpressing them. You will get a warning first.
 * It will save all data in PARENTFILE
 *
 */
public class MainActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                    // Get To Do from user
    ArrayList<String> itemsOnList = new ArrayList<String>();   // Save all items of To Do List
    ArrayList<String> timeList = new ArrayList<String>();   // Save all items of To Do List
    ArrayList<String> fileNameList = new ArrayList<String>(); // save all filenames
    String fileCounter;                                         // keep track of files used
    ListView listView;                                         // Place adapter here
    MyOwnListAdapter myadepter;                                 // Make adapter
    String fileName = "PARENTFILE";                        // main save file
    Calendar time;                                               // geth the time
    Boolean editable = false;                                    // check wether u are in edit mode or not

    /**
     * Called on create.
     * !Warning Reads all data from STORETEXT.
     * Find all elements. Initialize MyOwnRowAdapter. and add it to listView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // stop keyboard from popping up
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // find elements
        addItemToList = (EditText) findViewById(R.id.addItemEditText);
        listView = (ListView) findViewById(R.id.listView);

        // read a file and add it to itemsOnList an colorData
        fileCounter = String.format("%010d",0);
        Log.v("fileCounter", fileCounter);
        readDataFromFile();

        // make adapter
        myadepter = new MyOwnListAdapter(this, itemsOnList, fileName, timeList, fileNameList, fileCounter);

        // add adapter to listview
        listView.setAdapter(myadepter);

        // When editting inside listview, the EditText loses focusablity. This was the fix.
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

    }


    // menu button testgit
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

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

        if (!itemToAdd.isEmpty()) {
            // get date
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-H:m");
            String formattedDate = df.format(time.getInstance().getTime());

            // make fileSaveLocation
            Integer fileCount = Integer.parseInt(myadepter.fileCounter) + 1;
            String fileNumber = String.format("%010d", fileCount);

            Log.v("FileCounterUpdate", fileNumber);

            // add item
            myadepter.itemOnList.add(0,itemToAdd);
            myadepter.timeList.add(0,formattedDate);
            myadepter.fileSaveLocation.add(0,fileNumber);
            myadepter.fileCounter = fileNumber;

            // update adapter and listView.
            myadepter.notifyDataSetChanged();

            // make it empty
            addItemToList.setText("");
        }
    }

    /**
     * !WARNING Reads all data from STORETEXT. Read apdapter to see how saving works.
     * Get all data in file and add them to placeReadedData.
     */
    public void readDataFromFile() {
        itemsOnList.clear();
        try {
            // Open File
            Scanner scan = new Scanner(openFileInput(fileName));

            // first get the filecounter
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(!line.isEmpty()){
                    fileCounter = line;
                    break;
                }
            }

            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // If this line is found you will get color id's
                if (line.compareTo("Done Now Time") == 0) {
                    break;
                }

                // if line is not empty it is data, add it.
                if (!line.isEmpty()) {
                    itemsOnList.add(line);
                }
            }

            // Now find all Color's
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.compareTo("Done Now FileSaveLocations") == 0) {
                    break;
                }

                if (!line.isEmpty()) {
                    timeList.add(line);
                }
            }

            // Now find all Filesavelocations
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
