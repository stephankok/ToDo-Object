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
    ListView listView;                                          // Place adapter here
    MyOwnRowAdapter itemAdapter;                                // Make adapter
    TextView nameOfThisList;                                    // display list name
    ToDoListSingleton toDoListManager;                          // Maneger
    ToDoList toDoList;                                          // the whole list
    int listPosition;                                           // position of the list


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

        toDoListManager = ToDoListSingleton.getInstance(this);

        listPosition = getIntent().getExtras().getInt("Position");

        // read a file and add it to itemsOnList an colorData.
        toDoListManager.readItemData(listPosition);

        // Get the ToDoList.
        toDoList = toDoListManager.getToDoList(listPosition);

        // Show name of this list to users.
        nameOfThisList.setText(toDoList.getName());

        // make adapter.
        itemAdapter = new MyOwnRowAdapter(this, toDoListManager.getToDoList(listPosition),
                toDoListManager, listPosition);

        // add adapter to ListView.
        listView.setAdapter(itemAdapter);
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
     * It will add item to adapter and update it.
     */
    public void updateListView(View view) {
        // get item to add
        String itemToAdd = addItemToList.getText().toString();

        // check if not empty
        if (!itemToAdd.isEmpty()) {

            // add item.
            toDoList.pushNewItem(itemToAdd, false);
            toDoList.updateTime();

            // update adapter and listView.
            itemAdapter.notifyDataSetChanged();

            // make EditText empty again.
            addItemToList.setText("");
        }
    }

    /**
     * This will delete all checked items in the list.
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
                        toDoList.deleteAllCheckedItems();

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
