package com.example.stephan.todo;

/**
 * Created by Stephan on 29-2-2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    Integer fileAmount;

    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnEditAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList, String fileName, Integer amountOfFiles){
        super(contextOfApp, R.layout.single_row_edit_layout, itemsOfToDoList);

        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileToSaveAndLoad = fileName;
        fileAmount = amountOfFiles;
    }

    /**
     * test
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_edit_layout, parent, false);
        }

        // find Views on ListView
        final EditText editview = (EditText) view.findViewById(R.id.nameEditView);

        // add values to Views
        String name = itemOnList.get(position);

        editview.setHint(name);

        return view;
    }


    /**
     * When you update the data also save them on File.
     */
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();
    }

}
