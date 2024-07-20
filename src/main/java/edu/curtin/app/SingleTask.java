/*
 * File     : SingleTask.java
 * Purpose  : The leaf-node class (Composite Pattern) extending from Task.java
 *            Represents a single task that has an effort
 */

package edu.curtin.app;

public class SingleTask extends Task
{
    private int effort;

    public SingleTask(String inID, String inDesc, int inEffort, int inLevel)
    {
        this.id = inID;
        this.desc = inDesc;
        this.effort = inEffort;
        this.level = inLevel;
    }

    public void setEffort(int newEffort)
    {
        this.effort = newEffort;
    }

    public int getEffort()
    {
        return effort;
    }

    @Override
    public int getType()
    {
        return 1; // 1 represents single task
    }

    @Override
    public String toString()
    {
        String toPrint = "";
        for (int i=0; i<level; i++)
        { //identation
            toPrint += "\t";
        }
        toPrint += id + ": ";
        toPrint += desc + ", effort = ";
        toPrint += effort + "\n";
        return toPrint;
    }
}
