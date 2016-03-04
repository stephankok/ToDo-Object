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
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * An adapter Created By Stephan Kok.
 *
 */
public class MyOwnListAdapter extends ArrayAdapter<String> {

    Context context;                // Activity to display the adapter
    ArrayList<String> itemOnList;   // Items of the to do list
    String fileToSaveAndLoad;       // Store data here
    ArrayList<String> timeList;     // Store the data of when the list is added
    ArrayList<String> fileSaveLocation;
    String fileCounter;

    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnListAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList,
                            String fileName,
                            ArrayList<String> currentTime, ArrayList<String> fileNameList,
                            String fileRecord){
        super(contextOfApp, R.layout.single_row_lists_layout, itemsOfToDoList);

        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileToSaveAndLoad = fileName;
        timeList = currentTime;
        fileSaveLocation = fileNameList;
        fileCounter = fileRecord;
    }

    /**
     *
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_lists_layout, parent, false);
        }

        // find Views on ListView
        final TextView textview = (TextView) view.findViewById(R.id.nameTextView);
        final TextView timetextview = (TextView) view.findViewById(R.id.timeTextView);

        // add values to Views
        final String name = itemOnList.get(position);
        final String time = timeList.get(position);

        textview.setText(name);
        timetextview.setText(time);

        // If the list is longpressed delete it, first give a warning.
        View.OnLongClickListener longclicklistener = new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {

                // Create a warning
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm delete");
                alertDialogBuilder.setMessage("Are you sure you want to delete: " + name + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If you wanted to delete it, delete it

                                // make message
                                String text = context.getString(R.string.youDeleted) + name;
                                // make popup
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                // delete item
                                itemOnList.remove(position);
                                // delete file
                                context.deleteFile(fileSaveLocation.get(position));
                                fileSaveLocation.remove(position);
                                timeList.remove(position);
                                // update listview
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // end it
                                dialog.cancel();
                            }
                        });

                // make warning
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show warning
                alertDialog.show();
                return true;
            }
        };

        // When this list is clicked open it in a new activity
        View.OnClickListener shortlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create activity
                Intent loadItems = new Intent(context, ToDoActivity.class);

                // add the name of the file, so you will now what file to load.
                Bundle extras = new Bundle();
                extras.putString("listName", name);
                extras.putString("fileSaveLocation", fileSaveLocation.get(position));
                loadItems.putExtras(extras);

                // start
                context.startActivity(loadItems);

            }
        };

        // add all listeners to View
        view.setOnLongClickListener(longclicklistener);
        view.setOnClickListener(shortlistener);

        return view;
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
     *
     * first save all listsnames, seperated by a newline. When finished write a line: "Done Now Time"
     * then save all listdata seperated by a newline
     */
    public void updateAllData(){
        try {
            // open/create
            PrintStream out = new PrintStream(context.getApplicationContext().openFileOutput(fileToSaveAndLoad,context.MODE_PRIVATE));

            // first print the file recorder.
            out.println(fileCounter);

            // add all lists
            for( int i = 0; i < itemOnList.size(); i++){
                out.println(itemOnList.get(i) + "\n");
            }

            // write serparation line
            out.println("Done Now Time\n");

            // add all listdata
            for( int i = 0; i < timeList.size(); i++){
                out.println(timeList.get(i) + "\n");
            }

            // write serparation line
            out.println("Done Now FileSaveLocations");

            for(int i = 0; i < fileSaveLocation.size(); i++){
                out.println(fileSaveLocation.get(i) + "\n");
            }

            // close file
            out.close();

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, R.string.standardError + t.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
