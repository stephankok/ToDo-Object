package com.example.stephan.todo;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stephan on 7-3-2016.
 *
 * To Do Lists
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
    public ArrayList<ToDoItem> getAllToDoItems(){
        return toDoItems;
    }

    public ToDoItem getItem(int pos){
        return toDoItems.get(pos);
    }

    public String getName(){
        return title;
    }

    public void setName(String newName){
        title = newName;
    }

    public int getItemSize(){
        return toDoItems.size();
    }

    public String getTime(){
        return time;
    }

    public void setTime(String newTime){
        time = newTime;
    }

    public String getFile(){
        return saveLocation;
    }

    public void addNewItem(String itemName, Boolean itemChecked){
        toDoItems.add(new ToDoItem(itemName, itemChecked));
    }

    public void removeItem(int position){
        toDoItems.remove(position);
    }
    public void deleteAllItems(){
        toDoItems.clear();
    }
}
