/*
 * File     : Ops.java
 * Purpose  : An abstract class to act as a template (template pattern) for WBS manipulation operations.
 *            With each manipulation operation defined as a hook.
 */

package edu.curtin.app;
import java.util.*;
import java.util.logging.*;

public abstract class Ops 
{
    private static final Logger logger = Logger.getLogger(Ops.class.getName());
    public abstract int finalEffort(int[] nums, Scanner sc); //hook

    public void estimate(WBS wbs, String id, int noEsti, Scanner sc)
    { //Receives a Task ID and searches for it in the wbs, and recurse through its subtasks if any
        Task toEsti = wbs.getTask(id);

        if (toEsti.getType() == 1) //is a single task
        {
            estiTask((SingleTask) toEsti, noEsti, sc);
        }
        else //is a parent task, recruse through its subtasks
        {
            @SuppressWarnings("PMD") //Suppress LooseCoupling warning, ArrayList container is appropriate here
            ArrayList<Task> subTasks = ((ParentTask) toEsti).getSubTasks();
            for (Task i: subTasks)
            {
                if (i.getType() == 1) //is a single task
                {
                    estiTask((SingleTask) i, noEsti, sc);
                }
                else //is another parent task, recurse through its subtasks, and again, and again...
                {
                    estimate(wbs, i.getID(), noEsti, sc);
                }
            }
        }
    }

    //This is a template!
    private void estiTask(SingleTask toEsti, int noEsti, Scanner sc)
    { //Estimate efforts for a single task. 
        int effort;
        String allEfs = "All estimates: ";
        int[] efs = new int[noEsti];
        System.out.println("Now estimating for Task (" + toEsti.getID() + ")");

        for (int i=0; i<noEsti; i++)
        { //collect all estimates
            boolean done = false;

            while (!done)
            {
                System.out.print("Estimate " + (i+1) + " = ");
                try 
                {
                    int inEsti = sc.nextInt();
                    done = true;
                    efs[i] = inEsti;
                } 
                catch (InputMismatchException e) 
                {
                    System.out.println("Error: Please input a valid effort estimate (integer)!");
                }
                sc.nextLine();
            }
            
        }

        for (int i: efs)
        { //print all estimates to the screen
            allEfs += i + ", ";
        }
        System.out.println(allEfs);

        effort = finalEffort(efs, sc); //hook here, pass in estimates and get back the final effort value
        toEsti.setEffort(effort);
        System.out.println("Task (" + toEsti.getID() + ") effort set to " + effort);
        logger.info(() -> "Task (" + toEsti.getID() + ") effort set to " + effort);
    }
}