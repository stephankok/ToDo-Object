package com.example.stephan.todo;

import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Stephan on 8-3-2016.
 *
 * The Singleton of a list.
 *
 * This class manages the lists and the save and load procedure.
 *
 * Features:
 * Read & Load procedure. Never load to many items, only load the items that are opened.
 * Items are saved on separate files. These names are always unique. Read FileSavingMethod.
 *
 * FileSavingMethod: The first file will start with 0000000001, then it will count up: 0000000002,
 * enz... There will be a record of the highest number so they will never overlap. This means that
 * an user can create 100 million list.
 */
public class ToDoListSingleton {

    // fields
    private String thisFileLocation;        // filename where all lists are saved.
    private String keepTrackOfFiles;        // Record of highest number inf.: FileSavingMethod.
    private ArrayList<ToDoList> toDoLists;  // All lists.
    Context context;                        // Know where to save and load files.

    // Initialize to null so you can give Context.
    private static ToDoListSingleton instance = null;

    /**
     * Initialize Singleton with context.
     */
    public static ToDoListSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new ToDoListSingleton("PARENTFILE",
                    String.format("%010d",0), new ArrayList<ToDoList>(), context);
        }
        return instance;
    }

    // constructor
    private ToDoListSingleton(String mainFile, String fileCounter, ArrayList<ToDoList> todoLists, Context contextMain){
        thisFileLocation = mainFile;
        keepTrackOfFiles = fileCounter;
        toDoLists = todoLists;
        context = contextMain;
    }

    /**
     * Return all lists.
     */
    public ArrayList<ToDoList> getToDoLists(){

        return toDoLists;
    }

    /**
     * Return one list.
     */
    public ToDoList getToDoList(int pos){
        return toDoLists.get(pos);
    }

    /**
     * Delete one list.
     */
    public void removeList(int pos){
        // delete file
        context.deleteFile(toDoLists.get(pos).getFile());

        // remove list
        toDoLists.remove(pos);
    }

    /**
     * Make a new list and push it to first spot.
     */
    public void addNewList(String name, String time){
        // make the filename.
        String location = String.format("%010d", Integer.parseInt(keepTrackOfFiles) + 1);
        keepTrackOfFiles = location;

        // push the list on first spot.
        ToDoList newList = new ToDoList(name, time, new ArrayList<ToDoItem>(),location);
        toDoLists.add(0, newList);
    }

    /**
     * Read function to read all lists.
     * See writeDataOnFile on how the data is stored.
     */
    public void readDataFromFile() {
        // make sure list is clear
        toDoLists.clear();

        // read file.
        try {
            // Open File.
            Scanner scan = new Scanner(context.openFileInput(thisFileLocation));

            // first get the keepTrackOfFiles.
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(!line.isEmpty()){
                    keepTrackOfFiles = line;
                    break;
                }
            }

            // get all lists.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.isEmpty()) {
                    // In the line first is the listname then the listtime and at last the
                    // filename of where the list items are saved.
                    String[] readList = line.split(",");
                    String name = readList[0];
                    String time = readList[1];
                    String loc = readList[2];

                    // add list.
                    ToDoList newList = new ToDoList(name, time, new ArrayList<ToDoItem>(),loc);
                    toDoLists.add(newList);
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Save all lists on a thisFileLocation.
     * First keepTrackOfFiles is saved with a newline. Then all lists are saved. All properties are
     * separated with a (,). First the list name is saved then the list time and als last the
     * filename where all list items are stored.
     */

    public void writeDataOnFile() {
        try {
            // open/create.
            PrintStream out = new PrintStream(context.getApplicationContext()
                    .openFileOutput(thisFileLocation, Context.MODE_PRIVATE));

            // first print the file recorder.
            out.println(keepTrackOfFiles);

            // add all lists.
            for( int i = 0; i < toDoLists.size(); i++){
                out.println(toDoLists.get(i).getName() + "," + toDoLists.get(i).getTime() + "," +
                        toDoLists.get(i).getFile());
            }

            // close file.
            out.close();

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, "An Error Occurred: " + t.toString(), Toast.LENGTH_LONG)
                    .show();

        }


    }

    /**
     * Read all items on ONE list.
     * See writeItemData to know how it is stored.
     */
    public void readItemData(int position){
        try {
            toDoLists.get(position).deleteAllItems();
            // Open File
            Scanner scan = new Scanner(context.openFileInput(toDoLists.get(position).getFile()));

            // Find all To Do items
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // if line is not empty it is data, add item.
                if (!line.isEmpty()) {
                    // first is the item name and then the item status.
                    String[] readList = line.split(",");
                    String name = readList[0];
                    Boolean check = Boolean.parseBoolean(readList[1]);

                    // add todoitem.
                    toDoLists.get(position).addNewItem(name, check);
                }
            }

            // done
            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Save all the items from ONE list.
     *
     *  All item properties are saved and separated by a (,).
     *  First the item name is saved and then the item status.
     */
    public void writeItemData(int position){
        //When something changed update time
        toDoLists.get(position).updateTime();

        // now write data.
        try {
            // open/create
            PrintStream out = new PrintStream(context.getApplicationContext()
                    .openFileOutput(toDoLists.get(position).getFile(), Context.MODE_PRIVATE));

            // add all items
            for( int i = 0; i < toDoLists.get(position).getItemSize(); i++){
                ToDoItem thisItem = toDoLists.get(position).getItem(i);

                out.println(thisItem.getName() + "," + thisItem.getItemStatus().toString());
            }

            // close file
            out.close();

        } catch (Throwable t) {
            // error happened.
            t.printStackTrace();
            Toast.makeText(context, "An Error Occurred: "+ t.toString(), Toast.LENGTH_LONG).show();

        }
    }
}