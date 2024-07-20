/*
 * File     : OpsSingle.java
 * Purpose  : Act as a hook (template pattern) for Ops, does estimation based on configuration 3.
 *            Receive estimates, check if all are same, if yes, return it.
 *            Otherwise, request user to enter a single revised value, to be returned as final effort.
 */

package edu.curtin.app;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OpsSingle extends Ops
{
    @Override
    public int finalEffort(int[] nums, Scanner sc)
    {
        int finalNum = 0;

        //check if all estimates are the same
        boolean match = Arrays.stream(nums).allMatch(s -> s == nums[0]);

        if (match) //all estimates are the same, return it
        {
            finalNum = nums[0];
        }
        else //estimates not the same, ask for a single revised number
        {
            System.out.println("Different estimates detected. Create a single revised estimate.");
            boolean done = false;
            while (!done)
            {
                System.out.print("Revised estimate: ");
                try 
                {
                    finalNum = sc.nextInt();
                    done = true;
                } 
                catch (InputMismatchException e) 
                {
                    System.out.println("Error: Please input a valid effort estimate (integer)!");
                }
                sc.nextLine();
            }
        }

        return finalNum;
    }
}
