/*
 * File     : App.java
 * Purpose  : The 'main' file. Will read WBS data from .txt save file, run a menu to manipulate WBS data,
 *            and save changes to the same file.
 */

package edu.curtin.app;
import java.io.*;
import java.util.logging.*;
import java.util.*;

public class App
{
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static void main(String[] args)
    {
        String filename = null;

        if(args.length < 1) //determine is user passed filename argument
        {
            System.out.println("Please enter a filename!");
        }
        else //assign filename
        {
            filename = args[0];
            System.out.println("File entered: " + filename);
        }

        if (filename != null)
        {
            try
            {
                logger.info("Reading WBS file");
                WBS wbs = readFile(filename);
                logger.info("Running Menu");
                wbs = menu(wbs);
                logger.info("Save WBS to file");
                saveFile(filename, wbs);
            }
            catch(IOException e)
            {
                System.out.println("Could not read/write file: " + e.getMessage());
                logger.log(Level.SEVERE, "Could not read/write file", e);
            }
        }
    }

    private static WBS readFile(String filename) throws IOException
    {   
        WBS wbs = new WBS();
        Set<String> parentTasks = new HashSet<>(); //record which task is a parent task

        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {   //collect and store parent tasks into parentTasks HashSet
            String line1 = reader.readLine();
            while(line1 != null) 
            {
                String[] parts = line1.split(";");
                parts[0] = parts[0].replaceAll("\\s", ""); //remove whitespace from parent task ID
                if (parts[0].length() > 0)
                {
                    parentTasks.add(parts[0]); //record parent task ID
                }

                line1 = reader.readLine();
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {   //fill up the WBS
            String line2 = reader.readLine();
            while(line2 != null)
            {
                String[] parts = line2.split(";");

                // Remove whitespace
                parts[0] = parts[0].replaceAll("\\s", ""); //remove whitespace from parent task ID
                parts[1] = parts[1].replaceAll("\\s", ""); //remove whitespace from current task ID

                if (parts[2].charAt(0) == ' ') //remove whitespace at front of description
                {
                    parts[2] = parts[2].substring(0, 0) + "" + parts[2].substring(0 + 1);
                }
                int lastIndex = (parts[2].length()-1);
                if (parts[2].charAt(lastIndex) == ' ') //remove whitespace at end of description
                {
                    parts[2] = parts[2].substring(0, lastIndex) + "" + parts[2].substring(lastIndex + 1);
                }
                if (parts.length == 4)  //remove whitespace from effort
                {
                    parts[3] = parts[3].replaceAll("\\s", "");
                }
                // Remove whitespace


                String ctID = parts[1]; //assign current task ID
                String desc = parts[2]; //assign task description

                if (parts[0].length() == 0) //no parent ID, must be either single or parent task
                {
                    if (parts.length == 4) //single task with effort
                    {
                        SingleTask newTask = new SingleTask(ctID, desc, Integer.parseInt(parts[3]),0);
                        wbs.addTask(newTask);
                    }
                    else if (parentTasks.contains(parts[1])) //check if is a parent task
                    {
                        ParentTask newTask = new ParentTask(ctID, desc, 0);
                        wbs.addTask(newTask);
                    }
                    else //is just a single task without effort
                    {
                        SingleTask newTask = new SingleTask(ctID, desc, 0, 0);
                        wbs.addTask(newTask);
                    }
                }
                else //parent ID is present
                {
                    String ptID = parts[0];
                    if (parts.length == 4) //single task with effort
                    {
                        SingleTask newTask = new SingleTask(ctID, desc, Integer.parseInt(parts[3]),0);
                        wbs.addSubTask(ptID, newTask);
                    }
                    else if (parentTasks.contains(parts[1]))// if current task is a parent itself
                    {
                        ParentTask newTask = new ParentTask(ctID, desc, 0);
                        wbs.addSubTask(ptID,newTask);
                    }
                    else //just a single task without effort
                    {
                        SingleTask newTask = new SingleTask(ctID, desc, 0, 0);
                        wbs.addSubTask(ptID, newTask);
                    }
                }
                logger.info(() -> "Added Task (" + ctID + ") to WBS");
                line2 = reader.readLine();
            }
        }
        
        return wbs;
    }

    private static WBS menu(WBS wbs)
    {
        String option = "";
        String taskEsti; //task for estimation
        int config = 3; //reconciliation approach configuration, 3 by default
        int noEsti = 3; //number of estimators, 3 by default
        Ops[] operation = new Ops[4];
        operation[1] = new OpsHigh();
        operation[2] = new OpsMedi();
        operation[3] = new OpsSingle();

        Scanner sc = new Scanner(System.in);  //create scanner object

        while (!option.equals("3")) //menu loop
        {
            System.out.println("\n" + wbs.toString());

            System.out.println("(1) Estimate Effort");
            System.out.println("(2) Configure");
            System.out.println("(3) Quit");

            System.out.print("\nEnter option: ");
            option = sc.nextLine();  // Read user input

            if (option.equals("1")) //Estimate effort
            {
                logger.info("Estimation Option Selected");
                System.out.print("Enter Task ID to estimate effort: ");
                taskEsti = sc.nextLine();

                if (wbs.hasTask(taskEsti)) //check if Task actually exists
                {
                    operation[config].estimate(wbs, taskEsti, noEsti, sc);
                    sc.nextLine();
                }
                else //Error if task does not exist
                {
                    System.out.println("Error: Task (" + taskEsti + ") does not exist!");
                }
            }
            else if (option.equals("2")) //Configure
            {
                logger.info("Configuration Option Selected");
                System.out.print("Set number of estimators: ");
                try
                { //accept number of estimators
                    int inputNoEsti = sc.nextInt();
                    if (inputNoEsti < 1)
                    { //error if user inputs invalid configuration
                        System.out.println("Error: Please enter a valid number of estimators!");
                    }
                    else
                    {
                        noEsti = inputNoEsti;
                    }
                } 
                catch (InputMismatchException e) 
                { //error if user inputs invalid configuration
                    System.out.println("Error: Please enter a valid number of estimators!");
                }
                sc.nextLine();
                
                System.out.println("Number of estimators now set to " + noEsti);

                System.out.println("\nConfiguration (1): Take the highest estimate");
                System.out.println("Configuration (2): Take the median estimate");
                System.out.println("Configuration (3): Re-enter a single revised estimate\n");

                System.out.print("Enter configuration: ");
                try
                { //accept configutation
                    int inputConfig = sc.nextInt();
                    if ( (inputConfig < 0) || (inputConfig > 3) )
                    { //error if user inputs invalid configuration
                        System.out.println("Error: Please enter a valid configuration! (1,2,3)");
                    }
                    else
                    {
                        config = inputConfig;
                    }

                } 
                catch (InputMismatchException e) 
                { //error if user inputs invalid configuration
                    System.out.println("Error: Please enter a valid configuration! (1,2,3)");
                }
                
                System.out.println("Now set to configuration (" + config + ")");
                sc.nextLine();
            }
            else if (option.equals("3")) //Quit
            {
                logger.info("Quit from menu");
                System.out.println("\nYou have quit the program.");
            }
            else //Error if user inputs invalid option
            {
                System.out.println("Error: Please enter a valid option! (1,2,3)");
            }
        }
        sc.close();
        return wbs;
    }

    private static void saveFile(String filename, WBS wbs) throws IOException
    {
        @SuppressWarnings("PMD") //Suppress LooseCoupling warning, ArrayList container is appropriate here
        ArrayList<String> toSave = new ArrayList<>(); //records new lines of updated wbs

        try(BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            String line = reader.readLine();
            String newLine; //save new line after update effort value

            while(line != null) 
            { //read each line from file, update the effort value, then save as String
                String[] parts = line.split(";");
                String taskID = parts[1].replaceAll("\\s", ""); 
                //save taskID and remove whitespace
                
                if (wbs.getTask(taskID).getType() == 1) //if task is a single task, save with effort
                {
                    //get effort
                    String effort = Integer.toString(((SingleTask) wbs.getTask(taskID)).getEffort());
                
                    newLine = parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + effort;

                }
                else  //otherwise is a parent task, save as string with no changes
                {
                    newLine = parts[0] + ";" + parts[1] + ";" + parts[2];
                }

                toSave.add(newLine); //save the new line
                line = reader.readLine();
            }
        }

        try (FileWriter myWriter = new FileWriter(filename))
        { //write updated WBS strings to the file
            for (String i: toSave)
            {
                myWriter.write(i + "\n");
            }
        }
        
    }
}