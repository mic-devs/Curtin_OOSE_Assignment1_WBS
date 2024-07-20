/*
 * File     : WBS.java
 * Purpose  : holds the WBS data read from the .txt save file in main
 *            consists of one ArrayList that holds first-level tasks, and their respective subtasks within
 */

package edu.curtin.app;
import java.util.logging.*;
import java.util.ArrayList;

public class WBS 
{
    private static final Logger logger = Logger.getLogger(WBS.class.getName());
    @SuppressWarnings("PMD") //Suppress LooseCoupling warning, ArrayList container is appropriate here
    public ArrayList<Task> wbs; //holds all first-level tasks, and their respective subtasks within

    public WBS()
    {
        this.wbs = new ArrayList<>();
    }

    public Task getTask(String searchID)
    {
        Task foundTask = null;
        for (Task i: wbs)
        {
            if (i.getID().equals(searchID))
            {
                foundTask = i;
            }

            if ((i.getType() == 2) && (foundTask == null))
            {
                foundTask = ((ParentTask) i).getTask(searchID); //search within the parent via recursion
            }
            
        }
        return foundTask;
    }
    
    public boolean hasTask(String searchID)
    {
        boolean exists;
        if (getTask(searchID) == null) 
        { //using getTask on a non-existant task will simply return null
            exists = false;
        }
        else
        {
            exists = true;
        }
        
        return exists;
    }
    
    public void addTask(Task inTask) //stores a first-level task
    {
        wbs.add(inTask);
        logger.info(() -> "Task (" + inTask.getID() + ") added");
    }

    public void addSubTask(String pID, Task inTask) //stores a subTask
    {
        boolean found = false;

        for (Task i: wbs)
        {
            if (i.getType() == 2)
            {
                if (i.getID().equals(pID))
                {
                    ((ParentTask) i).addSubTask(inTask);
                    found = true;
                    logger.info(() -> "Task (" + inTask.getID() + ") added");
                }
                else
                {
                    found = ((ParentTask) i).addToParentTask(pID, inTask); //attempt to add the subTask into a parentTask's withins
                }
            }

            if (!found)
            { //if never successfully added on this Task (i), reset indentation level to zero for next attempt
                inTask.resetLevel(); 
            }
        }
    }

    @Override
    public String toString() //returns a String of the entire WBS
    {
        String toPrint = "";
        for(Task i : wbs)
        {
            toPrint += i.toString();
            
        }
        return toPrint;
    }
}
