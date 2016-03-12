package com.example.stephan.todo;

/**
 * Created by Stephan on 7-3-2016.
 *
 * The class of an item. It is meant as a single item for on a list.
 *
 * Features:
 * String Item name.
 * Boolean Item Status.
 */
public class ToDoItem {

    // fields
    private String itemName;    // Name of the item.
    private Boolean itemDone;   // Status of the item.

    // constructor
    public ToDoItem(String name, Boolean done){
        itemName = name;
        itemDone = done;
    }

    // methods

    /**
     * Return name of the item.
     */
    public String getName(){
        return itemName;
    }

    /**
     * Change the name of the item.
     */
    public void changeName(String newName){
        itemName = newName;
    }

    /**
     * Return the status of the item.
     */
    public Boolean getItemStatus(){
        return itemDone;
    }

    /**
     * Change the status of the item.
     */
    public void changeItemStatus(Boolean done){
        itemDone = done;
    }
}
