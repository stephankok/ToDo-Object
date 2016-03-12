package com.example.stephan.todo;

import java.util.ArrayList;

/**
 * Created by Stephan on 7-3-2016.
 *
 * The items on the To Do list.
 */
public class ToDoItem {
    // fields
    private String itemName;
    private Boolean itemDone;

    // constructor
    public ToDoItem(String name, Boolean done){
        itemName = name;
        itemDone = done;
    }

    // methods
    public String getName(){
        return itemName;
    }

    public void changeName(String newName){
        itemName = newName;
    }

    public Boolean getItemStatus(){
        return itemDone;
    }

    public void changeItemStatus(Boolean done){
        itemDone = done;
    }
}
