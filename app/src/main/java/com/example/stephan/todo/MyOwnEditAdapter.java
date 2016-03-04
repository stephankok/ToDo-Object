package com.example.stephan.todo;

/**
 * Created by Stephan on 29-2-2016.
 */

import android.content.Context;
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
public class MyOwnEditAdapter extends ArrayAdapter<String> {

    Context context;                // Activity to display the adapter
    ArrayList<String> itemOnList;   // Items of the to do list
    String fileToSaveAndLoad;       // Store data here
    ArrayList<String> timeList;     // Store the data of when the list is added
    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnEditAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList, String fileName, ArrayList<String> currentTime){
        super(contextOfApp, R.layout.single_row_lists_layout, itemsOfToDoList);
        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileToSaveAndLoad = fileName;
        timeList = currentTime;
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

            // close file
            out.close();

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, R.string.standardError + t.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
