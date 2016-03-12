package com.example.stephan.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * An item adapter for To DO app created By Stephan Kok.
 *
 * With this adapter you can create items for inside an list.
 *
 * Features:
 * You can edit items that are long pressed.
 * Items got a checkbox to check when they are finished.
 * You can delete items by pressing the thrash button.
 */
public class MyOwnRowAdapter extends ArrayAdapter<ToDoItem> {

    Context context;                // Activity to display the adapter
    ToDoList toDoList;
    ToDoListSingleton toDoListManeger;
    int listPosition;

    /**
     * Initialize MMyOwnRowAdapter
     */
    public MyOwnRowAdapter(Context contextOfApp, ToDoList todoList, ToDoListSingleton singleton,
                           int posOfList){
        super(contextOfApp, R.layout.single_row_items_layout, todoList.getAllToDoItems());

        context = contextOfApp;
        toDoList = todoList;
        toDoListManeger = singleton;
        listPosition = posOfList;
    }

    /**
     * Initialize View.
     * Set an OnClick on ImageButton and an OnLongClick on the View.
     */
    public View getView(final int position, View view, ViewGroup parent){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_row_items_layout, parent, false);
        }

        // find Views on ListView
        final TextView itemTextView = (TextView) view.findViewById(R.id.nameTextView);
        final ImageButton imageDeleteButton = (ImageButton)
                view.findViewById(R.id.deleteButton);
        final CheckBox itemChecked = (CheckBox) view.findViewById(R.id.itemChecked);

        // add values to Views
        final ToDoItem thisItem = toDoList.getItem(position);

        final String name = thisItem.getName();
        boolean checked = thisItem.getItemStatus();

        // Set text on itemTextView.
        itemTextView.setText(name);

        // Set checked.
        itemChecked.setChecked(checked);

        // save when checked changed.
        itemChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisItem.changeItemStatus(itemChecked.isChecked());

                toDoListManeger.writeItemData(listPosition);
            }
        });

        // Set delete listener.
        imageDeleteButton.setOnClickListener(onDeleteButtonPressed(position));

        // Set edit listener.
        view.setOnLongClickListener(setOnLongClickListener(position));

        // Done.
        return view;
    }

    /**
     * Return an onClickListener that will create a AlertDialog where users can delete their
     * list.
     */
    public ImageButton.OnClickListener onDeleteButtonPressed(final int position){
        // create OnClickListener below.
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a AlertDialog.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set info.
                alertDialogBuilder.setTitle("Confirm delete")
                        .setMessage("Are you sure you want to delete: " +
                                toDoList.getItem(position).getName() + "?")
                        .setCancelable(false)

                        // set buttons.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // on yes delete item.

                                // Make Toast popup that you deleted list.
                                String text = "You deleted: " +
                                        toDoList.getItem(position).getName();
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                                // delete item.
                                toDoList.removeItem(position);

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
        return new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                // Create a AlertDialog.
                AlertDialog.Builder alertEditItemName = new AlertDialog.Builder(context);

                // set info.
                alertEditItemName.setTitle("Rename")
                        .setMessage("What will be the new name for: " +
                                toDoList.getItem(position).getName() + "?");

                // make EditText where users can give input.
                final EditText input = new EditText(context);
                input.setText(toDoList.getItem(position).getName());

                // set cursor at the end.
                input.setSelection(input.getText().length());

                // put it in AlertDialog.
                alertEditItemName.setView(input);

                // Set up the buttons.
                alertEditItemName.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On yes change name of the item.
                        toDoList.getItem(position).changeName(input.getText().toString());
                        notifyDataSetChanged();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On no cancel.
                        dialog.cancel();
                    }
                });

                // show dialog
                alertEditItemName.show();;
                return true;
            }
        };
    }

    /**
     * When you update the data also save them on File.
     */
    public void notifyDataSetChanged(){
        super.notifyDataSetChanged();

        // save on file
        toDoListManeger.writeItemData(listPosition);
    }
}