package com.example.stephan.todo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A To Do app created by Stephan Kok.
 * Show all the list the user have created.
 */
public class MainActivity extends AppCompatActivity {

    // Initialize values.
    EditText addItemToList;                                     // Get To Do from user.
    ListView listView;                                          // Place adapter here.
    MyOwnListAdapter listAdapter;                               // Make adapter.
    Calendar calender = Calendar.getInstance();                 // Get the time.
    SimpleDateFormat dateFormat                                 // Specific date format.
            = new SimpleDateFormat("dd-MMM HH:mm");
    ToDoListSingleton toDoListManager;

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

        toDoListManager = ToDoListSingleton.getInstance(this);

        // read data from fileName.
        toDoListManager.readDataFromFile();

        // make adapter.
        listAdapter = new MyOwnListAdapter(this, toDoListManager);

        // add adapter to ListView.
        listView.setAdapter(listAdapter);

        // When editing inside ListView, the EditText loses focusability. This was the fix.
        listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }


    /**
     * When resume make sure adapter is up-to-date.
     */
    public void onResume(){
        super.onResume();
        listAdapter.notifyDataSetChanged();
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

            // add item
            toDoListManager.addNewList(itemToAdd, formattedDate);

            // update adapter and listView.
            listAdapter.notifyDataSetChanged();

            // make it empty
            addItemToList.setText("");
        }
    }

}
