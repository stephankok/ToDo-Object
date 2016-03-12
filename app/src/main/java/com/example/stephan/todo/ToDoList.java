package com.example.stephan.todo;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Stephan on 7-3-2016.
 *
 * The class of a list. There are supposed to be items on such list.
 *
 * Features:
 * String list name.
 * String list time the list or items in it are last modified.
 * ArrayList items on the list.
 * String filename where all items are stored.
 */
public class ToDoList implements Serializable{

    // fields
    private String title;                   // name of the list.
    private String time;                    // last time changes where made.
    SimpleDateFormat dateFormat                  // Date format to show user.
            = new SimpleDateFormat("dd-MMM HH:mm");
    private ArrayList<ToDoItem> toDoItems;  // the items
    private String saveLocation;            // the location where the items of the list are saved.

    // constructor
    public ToDoList(String name, String timeModified, ArrayList<ToDoItem> todoItems,
                    String fileLocation){
        title = name;
        time = timeModified;
        toDoItems = todoItems;
        saveLocation = fileLocation;
    }
    // when extend use super

    // methods

    /**
     * Return all items.
     */
    public ArrayList<ToDoItem> getAllToDoItems(){
        return toDoItems;
    }

    /**
     * Return one item.
     */
    public ToDoItem getItem(int pos){
        return toDoItems.get(pos);
    }

    /**
     * Return name of the list.
     */
    public String getName(){
        return title;
    }

    /**
     * Change the name of the list.
     */
    public void setName(String newName){
        title = newName;
    }

    /**
     * Return the amount of items.
     */
    public int getItemSize(){
        return toDoItems.size();
    }

    /**
     * Return the time of the list.
     */
    public String getTime(){
        return time;
    }

    /**
     * Change the time of the list.
     */
    public void updateTime(){
        // get the time right now
        Calendar calender = Calendar.getInstance();
        time = dateFormat.format(calender.getTime());
    }

    /**
     * Return the filename where all list items are stored.
     */
    public String getFile(){
        return saveLocation;
    }

    /**
     * Add a new item at the end of the array.
     */
    public void addNewItem(String itemName, Boolean itemChecked){
        toDoItems.add(new ToDoItem(itemName, itemChecked));
    }

    /**
     * Push a new item at the beginning of the array.
     */
    public void pushNewItem(String itemName, Boolean itemChecked){
        toDoItems.add(0, new ToDoItem(itemName, itemChecked));
    }

    /**
     * Remove one item.
     */
    public void removeItem(int position){
        toDoItems.remove(position);
    }

    /**
     * WARNING! Delete all items on the list!
     */
    public void deleteAllItems(){
        toDoItems.clear();
    }

    /**
     * Delete all checked items.
     */
    public void deleteAllCheckedItems(){
        // start at the end so you wont get out of index
        for(int i = toDoItems.size() - 1; i >= 0; i--){
            if(toDoItems.get(i).getItemStatus()){
                toDoItems.remove(i);
            }
        }
    }
}
