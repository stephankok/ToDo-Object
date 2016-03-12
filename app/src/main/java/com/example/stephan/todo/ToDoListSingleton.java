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
 */
public class ToDoListSingleton {

    // fields
    private String thisFileLocation;
    private String keepTrackOfFiles;
    private ArrayList<ToDoList> toDoLists;
    Context context;

    private static ToDoListSingleton instance = null;

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

    public ArrayList<ToDoList> getToDoLists(){

        return toDoLists;
    }

    public ToDoList getToDoList(int pos){
        return toDoLists.get(pos);
    }

    public void removeList(int pos){
        // delete file
        context.deleteFile(toDoLists.get(pos).getFile());

        // TODO do i need to clear the items?

        // remove list
        toDoLists.remove(pos);
    }

    public void addNewList(String name, String time){
        String location = String.format("%010d", Integer.parseInt(keepTrackOfFiles) + 1);
        keepTrackOfFiles = location;

        ToDoList newList = new ToDoList(name, time, new ArrayList<ToDoItem>(),location);
        toDoLists.add(0, newList);
    }

    public void readDataFromFile() {
        // make sure list is clear
        toDoLists.clear();

        // read file.
        try {
            // Open File.
            Scanner scan = new Scanner(context.openFileInput(thisFileLocation));

            // first get the fileCounter.
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(!line.isEmpty()){
                    keepTrackOfFiles = line;
                    break;
                }
            }

            // get all list names.
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (!line.isEmpty()) {
                    String[] readList = line.split(",");
                    String name = readList[0];
                    String time = readList[1];
                    String loc = readList[2];

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
     * Save the data of in the file given.
     * first save all list names, separated by a newline.
     * When finished write a line: "Done Now Time".
     * Then save all list date, separated by a newline.
     * When finished write a line: "Done Now FileSaveLocations".
     * Then save all file locations.
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

    public void readItemData(int position){
        try {
            toDoLists.get(position).deleteAllItems();
            // Open File
            Scanner scan = new Scanner(context.openFileInput(toDoLists.get(position).getFile()));

            // Find all To Do items
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                // if line is not empty it is data, add it.
                if (!line.isEmpty()) {
                    String[] readList = line.split(",");
                    String name = readList[0];
                    Boolean check = Boolean.parseBoolean(readList[1]);

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
     *  Save data on file.
     *  First all items, separated by newline.
     *  Then a separation line "END OF THE ITEMS NOW CHECKED".
     *  Then all check statuses separated by a newline.
     */
    public void writeItemData(int position){
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