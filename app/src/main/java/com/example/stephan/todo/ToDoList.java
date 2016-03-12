package com.example.stephan.todo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stephan on 7-3-2016.
 *
 * The class of a list. There are supposed to be items on such list.
 *
 * Features:
 * String list name.
 * String list time.
 * ArrayList items on the list.
 * String filename where all items are stored.
 */
public class ToDoList implements Serializable{

    // fields
    private String title;                   // name of the list.
    private String time;                    // time list added or modified.
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
    public void setTime(String newTime){
        time = newTime;
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
}
