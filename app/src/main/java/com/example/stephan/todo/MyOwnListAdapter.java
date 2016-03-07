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
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class MyOwnListAdapter extends ArrayAdapter<String> {

    Context context;                    // Activity to display the adapter.
    ArrayList<String> itemOnList;       // Items of the to do list.
    String fileToSaveAndLoad;           // Store data here.
    ArrayList<String> timeList;         // Store the data of when the list is added.
    ArrayList<String> fileSaveLocation; // Store the location of where the file is saved.
    String fileCounter;                 // Keep track of files.
    Calendar calender                   // Get time.
            = Calendar.getInstance();
    SimpleDateFormat dateFormat         // Specific date format.
            = new SimpleDateFormat("dd-MMM HH:mm");

    /**
     * Initialize MMyOwnRowAdapter.
     */
    public MyOwnListAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList,
                            String fileName, ArrayList<String> currentTime,
                            ArrayList<String> fileNameList, String fileRecord){
        super(contextOfApp, R.layout.single_row_lists_layout, itemsOfToDoList);

        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileToSaveAndLoad = fileName;
        timeList = currentTime;
        fileSaveLocation = fileNameList;
        fileCounter = fileRecord;
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
        final String name = itemOnList.get(position);
        final String time = timeList.get(position);

        // set text.
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
                                itemOnList.get(position) + "?")
                        .setCancelable(false)

                        // set buttons.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on yes delete list.

                                // Make Toast popup that you deleted list.
                                String text = "You deleted: " +
                                        itemOnList.get(position);
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                                // delete list.
                                itemOnList.remove(position);

                                // delete date.
                                timeList.remove(position);

                                // delete file.
                                context.deleteFile(fileSaveLocation.get(position));
                                fileSaveLocation.remove(position);

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
                input.setText(itemOnList.get(position));

                // put it in AlertDialog.
                alertEditListName.setView(input);

                // Set up the buttons.
                alertEditListName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On yes change name
                        itemOnList.set(position, input.getText().toString());

                        // update time
                        String formattedDate = dateFormat.format(calender.getTime());
                        timeList.set(position,formattedDate);
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
                extras.putString("listName", itemOnList.get(position));
                extras.putString("fileSaveLocation", fileSaveLocation.get(position));
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

        // save on file
        updateAllData();
    }

    /**
     * Save the data of in the file given.
     * first save all list names, separated by a newline.
     * When finished write a line: "Done Now Time".
     * Then save all list date, separated by a newline.
     * When finished write a line: "Done Now FileSaveLocations".
     * Then save all file locations.
     */
    public void updateAllData(){
        try {
            // open/create.
            PrintStream out = new PrintStream(context.getApplicationContext()
                            .openFileOutput(fileToSaveAndLoad,Context.MODE_PRIVATE));

            // first print the file recorder.
            out.println(fileCounter);

            // add all lists.
            for( int i = 0; i < itemOnList.size(); i++){
                out.println(itemOnList.get(i) + "\n");
            }

            // write separation line.
            out.println("Done Now Time\n");

            // add all list names.
            for( int i = 0; i < timeList.size(); i++){
                out.println(timeList.get(i) + "\n");
            }

            // write separation line.
            out.println("Done Now FileSaveLocations\n");

            // write all file locations.
            for(int i = 0; i < fileSaveLocation.size(); i++){
                out.println(fileSaveLocation.get(i) + "\n");
            }

            // close file.
            out.close();

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, "An Error Occurred: " + t.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }
}
