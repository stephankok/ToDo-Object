package com.example.stephan.todo;

import android.content.Context;
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
 *  Picture give the id of the picture u want to add.
 *  an ArrayList<String> of items to add. You can dynamically add or remove.
 *
 *  Your layout wil need:
 *  ImageView with id: imageView
 *  TextView with id: nameTextView
 *
 *  Features:
 *  It will delete an item that is long pressed.
 */
public class MyOwnRowAdapter extends ArrayAdapter<String> {

    Context context;           // Activity to display the adapter
    int picture;              // The ID of the picture to add
    ArrayList<String> itemOnList;   // Items of the to do list
    Boolean fileSaving;        // Set true to save items on STORETEXT

    /**
     * Initialize
     */
    public MyOwnRowAdapter(Context contextOfApp, int pictureOfAllItems, ArrayList<String> itemsOfToDoList, Boolean saveOnFile){
        super(contextOfApp, R.layout.single_row_layout, itemsOfToDoList);

        context = contextOfApp;
        picture = pictureOfAllItems;
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
        final ImageView imageview = (ImageView) view.findViewById(R.id.imageView);
        final TextView textview = (TextView) view.findViewById(R.id.nameTextView);

        // add values to Views
        final String name = itemOnList.get(position);
        int pictures = picture;


        textview.setText(name);
        imageview.setImageResource(pictures);


        // The longClickListener
        View.OnLongClickListener listener1 = new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                // make anousment
                String text = "You deleted: " + name;

                // make popup
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();

                // delete item
                itemOnList.remove(position);

                // update listview
                notifyDataSetChanged();

                if(fileSaving){
                    updateAllData();
                }
                return true;
            }
        };

        view.setOnLongClickListener(listener1);

        return view;
    }

    /**
     * Add item to list.
     * Save on file.
     */
    public void add(String itemToAdd){
        super.add(itemToAdd);

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
