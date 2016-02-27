package com.example.stephan.todo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * An adapter Created By Stephan Kok.
 *
 *  !WARNING! When u set saveOnFile to true it will save all items on STORETEXT
 *  set false to disable
 *
 *  Input:
 *  an ArrayList<String> of items to add. You can dynamically add or remove.
 *
 *  Your layout wil need:
 *  TextView with id: nameTextView
 *
 *  Features:
 *  It will delete an item that is long pressed.
 */
public class MyOwnRowAdapter extends ArrayAdapter<String> {

    Context context;                // Activity to display the adapter
    ArrayList<String> itemOnList;   // Items of the to do list
    Boolean fileSaving;             // Set true to save items on STORETEXT

    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnRowAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList, Boolean saveOnFile){
        super(contextOfApp, R.layout.single_row_layout, itemsOfToDoList);

        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileSaving = saveOnFile;
    }

    /**
     * Add the picture and itemtoadd to the Listview.
     *
     * Set a onlongclick listener that will delete an item when it is long clicked
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_layout, parent, false);
        }

        // find Views on ListView
        final TextView textview = (TextView) view.findViewById(R.id.nameTextView);

        // add values to Views
        final String name = itemOnList.get(position);

        // set text on textview
        textview.setText(name);

        // The longClickListener
        View.OnLongClickListener longclicklistener = new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                // make anousment
                String text = context.getString(R.string.youDeleted) + name;

                // make popup
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                // delete item
                itemOnList.remove(position);

                // update listview
                notifyDataSetChanged();

                return true;
            }
        };

        View.OnClickListener shortlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textview.getTextColors() == ColorStateList.valueOf(Color.GREEN)){
                    textview.setTextColor(Color.BLACK);
                }
                else {
                    textview.setTextColor(Color.GREEN);
                }
            }
        };

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
        if(fileSaving){
            updateAllData();
        }
    }

    /**
     * When fileSaving is set true, this function will be called.
     * It will open/create a file STORETEXT.
     * It will save all itemsOnList and separate them by a newline.
     */
    public void updateAllData(){
        try {
            // open/create
            PrintStream out = new PrintStream(context.getApplicationContext().openFileOutput("STORETEXT",context.MODE_PRIVATE));

            // add all items
            for( int i = 0; i < itemOnList.size(); i++){
                out.println(itemOnList.get(i) + "\n");
            }

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, "An Error Occurred: " + t.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
