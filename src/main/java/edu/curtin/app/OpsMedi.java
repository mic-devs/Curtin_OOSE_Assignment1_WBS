/*
 * File     : OpsMedi.java
 * Purpose  : Act as a hook (template pattern) for Ops, does estimation based on configuration 2.
 *            Receive estimates, return the median estimate
 */

package edu.curtin.app;
import java.util.Arrays;
import java.util.Scanner;

public class OpsMedi extends Ops
{
    @Override
    public int finalEffort(int[] nums, Scanner sc)
    {
        Arrays.sort(nums);
        int median;

        if (nums.length % 2 == 0)
        {
            median = (nums[nums.length/2] + nums[nums.length/2 - 1])/2;
        }
        else
        {
            median = nums[nums.length/2];
        }
            
        return median;
    }
}
