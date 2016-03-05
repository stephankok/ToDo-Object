package com.example.stephan.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * An adapter Created By Stephan Kok.
 *
 *  !WARNING! When u set saveOnFile to true it will save all items on STORETEXT. First all
 *  data is saved then the line: END OF THE ITEMS NOW COLORS. code:182838 is written en then all
 *  the colors are saved.
 *  Set saveOnFile false to disable saving.
 *
 *  Input:
 *  an ArrayList<String> of items to add. You can dynamically add or remove.
 *  an ArrayList<Integer> of color id's. When you add an item it is automatic black.
 *      You must make all color Color.BLACK or Color.Green.
 *
 *  Your layout wil need:
 *  TextView with id: nameTextView
 *
 *  Your strings xml will need:
 *  EMColorsAdapter: error message when collors cant load.
 *  standardError: standard error it will add the problem to string.
 *
 *  Features:
 *  It will delete an item that is long pressed.
 *  Items will turn green when pressing them
 */
public class MyOwnRowAdapter extends ArrayAdapter<String> {

    Context context;                // Activity to display the adapter
    ArrayList<String> itemOnList;   // Items of the to do list
    Boolean fileSaving;             // Set true to save items on STORETEXT
    ArrayList<Integer> itemColor;   // color of the items
    String fileToSaveAndLoad;

    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnRowAdapter(Context contextOfApp, ArrayList<String> itemsOfToDoList, Boolean saveOnFile, ArrayList<Integer> colorData, String fileName){
        super(contextOfApp, R.layout.single_row_items_layout, itemsOfToDoList);

        context = contextOfApp;
        itemOnList = itemsOfToDoList;
        fileSaving = saveOnFile;
        itemColor = colorData;
        fileToSaveAndLoad = fileName;
    }

    /**
     * Add the picture and itemtoadd to the Listview.
     *
     * Set a onlongclick listener that will delete an item when it is long clicked
     *
     * set onclick listener that will turn items green.
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_items_layout, parent, false);
        }

        // find Views on ListView
        final TextView textview = (TextView) view.findViewById(R.id.nameTextView);
        final ImageButton imageDeleteButton = (ImageButton)
                view.findViewById(R.id.deleteButton);

        // add values to Views
        final String name = itemOnList.get(position);

        // set text on textview
        textview.setText(name);
        textview.setTextColor(itemColor.get(position));


        imageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // The longClickListener, delete items
        View.OnLongClickListener longclicklistener = new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {

                // create dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // set info
                builder.setTitle("Rename")
                        .setMessage("What will be the new name for: " + itemOnList.get(position) + "?");

                // make user able to give new name
                final EditText input = new EditText(context);
                input.setText(itemOnList.get(position));
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "you changed it to " + input.getText(), Toast.LENGTH_SHORT).show();
                        itemOnList.set(position, input.getText().toString());
                        notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // show dialog
                builder.show();;
                return true;
            }
        };

        // the onclick listener, turn items green
        View.OnClickListener shortlistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if green, make black
                if(textview.getTextColors() == ColorStateList.valueOf(Color.GREEN)){
                    textview.setTextColor(Color.BLACK);
                    itemColor.set(position,Color.BLACK);
                }
                // else it is black, make it green
                else {
                    textview.setTextColor(Color.GREEN);
                    itemColor.set(position, Color.GREEN);
                }

                // save data, you wont need to update listview and adapter
                if(fileSaving){
                    updateAllData();
                }
            }
        };

        // add all listeners to View
        view.setOnLongClickListener(longclicklistener);
        view.setOnClickListener(shortlistener);

        return view;
    }


    /**
     *  Add color in front.
     *  It must be black so requires no input
     */
    public void insertColor(){
        itemColor.add(0,Color.BLACK);
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

    public void clearlist(){
        itemColor.clear();
        itemOnList.clear();
    }


    /**
     * When fileSaving is set true, this function will be called.
     * It will open/create a file STORETEXT.
     * It will save all itemsOnList and separate them by a newline.
     *
     * To seperate data from colors there will be writen a special line:
     * END OF THE ITEMS NOW COLORS. code:182838
     *
     * Then all colors id will be added in string format and will be seperated by a newline.
     */
    public void updateAllData(){
        try {
            // open/create
            PrintStream out = new PrintStream(context.getApplicationContext().openFileOutput(fileToSaveAndLoad,context.MODE_PRIVATE));

            // add all items
            for( int i = 0; i < itemOnList.size(); i++){
                out.println(itemOnList.get(i) + "\n");
            }

            // add special line.
            out.println("END OF THE ITEMS NOW COLORS. code:182838\n");

            // add all colors
            for (int i = 0; i < itemColor.size(); i++){
                out.println(itemColor.get(i).toString() + "\n");
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
