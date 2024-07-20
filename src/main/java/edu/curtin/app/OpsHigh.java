/*
 * File     : OpsHigh.java
 * Purpose  : Act as a hook (template pattern) for Ops, does estimation based on configuration 1.
 *            Receive estimates, return the highest estimate
 */

package edu.curtin.app;
import java.util.Scanner;

public class OpsHigh extends Ops
{
    @Override
    public int finalEffort(int[] nums, Scanner sc)
    {
        int max = nums[0];

        for (int i: nums)
        {
            if (i > max)
            {
                max = i;
            }
        }

        return max;
    }
}
