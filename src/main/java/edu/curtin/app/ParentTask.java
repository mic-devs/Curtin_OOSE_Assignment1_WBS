/*
 * File     : ParentTask.java
 * Purpose  : The composite-node class (Composite Pattern) extending from Task.java
 *            Represents a parent task that consists of multiple Tasks (either Single or Parent themselves)
 *            Each subtask can be accessed, along with its own subtasks via recursion, and so on...
 */

 package edu.curtin.app;
 import java.util.ArrayList;
 
 public class ParentTask extends Task
 {
     @SuppressWarnings("PMD") //Suppress LooseCoupling warning, ArrayList container is appropriate here
     public ArrayList<Task> subTasks;
 
     public ParentTask(String inID, String inDesc, int inLevel)
     {
         this.id = inID;
         this.desc = inDesc;
         this.level = inLevel;
         this.subTasks = new ArrayList<>();
     }
 
     @Override
     public int getType()
     {
         return 2; // 2 represents parent task
     }
 
     public Task getTask(String searchID)
     {
         Task foundTask = null;
 
         for (Task i: subTasks) //search within this ParentTask's subtasks
         {
             if (i.getID().equals(searchID))
             {
                 foundTask = i;
             }
 
             if ( (i.getType() == 2) && (foundTask == null)) //if one of the subtasks is a parent, search within it too
             {
                 foundTask = ((ParentTask) i).getTask(searchID);
             }
         }
 
         return foundTask;
     }
 
     @SuppressWarnings("PMD") //Suppress LooseCoupling warning, ArrayList container is appropriate here
     public ArrayList<Task> getSubTasks()
     {
         return subTasks;
     }
 
     public boolean addToParentTask(String inID, Task inTask) 
     {   //attempting to add a subtask into a parentTask that's within this parentTask's subtasks
 
         boolean found = false;
         //we never really know if the ParentTask we are looking for is actually inside this ParentTask,
         // so we keep track whether we have found it, and successfully added the subtask
 
         for (Task i: subTasks)
         {
             if (i.getType() == 2)
             {
                 if (i.getID().equals(inID))
                 {
                     inTask.addLevel();
                     ((ParentTask) i).addSubTask(inTask);
                     found = true;
                 }
                 else
                 {
                     if ( !((ParentTask) i).isEmpty() )
                     {
                         inTask.addLevel();
                         found = ((ParentTask) i).addToParentTask(inID, inTask); //recurse into the subtasks
                         
                     }
                 }
             }
         }
 
         return found;
     }
 
     public void addSubTask(Task inTask) //adds a task to subTasks
     { 
         inTask.addLevel();
         subTasks.add(inTask);
     }
 
     public boolean isEmpty() //if subTasks is empty
     {
         boolean ans = true;
         if (subTasks.size() > 0)
         {
             ans = false;
         }
         return ans;
     }
 
     @Override
     public String toString() //toStrings this parent task and its subtasks
     {
         String toPrint = "";
         for (int i=0; i<level; i++)
         { //identation
             toPrint += "\t";
         }
         toPrint += id + ": ";
         toPrint += desc + "\n";
 
         for (Task i : subTasks)
         {
             toPrint += i.toString();
         }
 
         return toPrint;
     }
 }