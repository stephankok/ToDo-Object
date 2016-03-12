package com.example.stephan.todo;

/**
 * Created by Stephan on 29-2-2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * An list adapter for the To Do app created By Stephan Kok.
 *
 * With this adapter you can create lists, where inside the list u can create items to do.
 *
 * Features:
 * Edit list name by long pressing it.
 * Open list by pressing on it.
 * Delete a list by pressing the thrash button.
 *
 */
public class MyOwnListAdapter extends ArrayAdapter<ToDoList> {

    Context context;                    // Activity to display the adapter.
    Calendar calender                   // Get time.
            = Calendar.getInstance();
    SimpleDateFormat dateFormat         // Specific date format.
            = new SimpleDateFormat("dd-MMM HH:mm");
    ToDoListSingleton toDoLists;


    /**
     * Initialize MMyOwnRowAdapter.
     */
    public MyOwnListAdapter(Context contextOfApp, ToDoListSingleton todoManger){
        super(contextOfApp, R.layout.single_row_lists_layout, todoManger.getToDoLists());

        context = contextOfApp;
        toDoLists = todoManger;

    }

    /**
     * Initialize View.
     * Set an OnClick on ImageButton, an OnClick on the View and an OnLongClick on the View.
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_lists_layout, parent, false);
        }

        // find Views on ListView.
        final TextView textview = (TextView) view.findViewById(R.id.nameTextView);
        final TextView timetextview = (TextView) view.findViewById(R.id.timeTextView);
        final ImageButton imageDeleteButton = (ImageButton)
                view.findViewById(R.id.deleteButton);

        // add values to Views.
        ToDoList currentList = toDoLists.getToDoList(position);

        final String name = currentList.getName();
        final String time = currentList.getTime();

        textview.setText(name);

        timetextview.setText(time);

        // Set delete listener.
        imageDeleteButton.setOnClickListener(onDeleteButtonPressed(position));

        // Set open list listener.
        textview.setOnClickListener(setOnClickListener(position));

        // Set edit listener.
        textview.setOnLongClickListener(setOnLongClickListener(position));

        // Done.
        return view;
    }

    /**
     * Return an onClickListener that will create a AlertDialog where users can delete their
     * list.
     */
    public View.OnClickListener onDeleteButtonPressed(final int position){
        // create OnClickListener below.
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a AlertDialog.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set info.
                alertDialogBuilder.setTitle("Confirm delete")
                        .setMessage("Are you sure u want to delete: " +
                                toDoLists.getToDoList(position).getName() + "?")
                        .setCancelable(false)

                        // set buttons.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on yes delete list.

                                // Make Toast popup that you deleted list.
                                String text = "You deleted: " +
                                        toDoLists.getToDoList(position).getName();
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                                // remove list and items.
                                toDoLists.removeList(position);

                                // update.
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // end it.
                                dialog.cancel();
                            }
                        });

                // make AlertDialog popup.
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        };
    }

    /**
     * Return an onLongClickListener that will create a AlertDialog where users can edit their
     * list name.
     */
    public View.OnLongClickListener setOnLongClickListener(final int position){
        // create OnLongClickListener below.
        return new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                // create AlertDialog.
                AlertDialog.Builder alertEditListName = new AlertDialog.Builder(context);

                // set info.
                alertEditListName.setTitle("Rename")
                        .setMessage("What will the new name be?");

                // make EditText where users can give input.
                final EditText input = new EditText(context);
                input.setText(toDoLists.getToDoList(position).getName());

                // put it in AlertDialog.
                alertEditListName.setView(input);

                // Set up the buttons.
                alertEditListName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On yes change name
                        toDoLists.getToDoList(position).setName(input.getText().toString());

                        // update time
                        String formattedDate = dateFormat.format(calender.getTime());
                        toDoLists.getToDoList(position).setTime(formattedDate);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On no cancel
                        dialog.cancel();
                    }
                });

                // show AlertDialog
                alertEditListName.show();;
                return true;
            }
        };
    }


    /**
     * Return an onClickListener that will open a list in a new activity.
     * As extra a Bundle, with a string containing list name: listName and a string
     * containing what file to save and load: fileSaveLocation.
     */
    public View.OnClickListener setOnClickListener(final int position){
        // return the onClickListener below.
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create activity.
                Intent loadItems = new Intent(context, ToDoActivity.class);

                // add the name of the file, so you will now what file to load.
                Bundle extras = new Bundle();
                extras.putInt("Position", position);
                loadItems.putExtras(extras);

                // start.
                context.startActivity(loadItems);
            }
        };
    }

    /**
     * When you update the data also save them on File.
     */
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();

        toDoLists.writeDataOnFile();
    }
}
