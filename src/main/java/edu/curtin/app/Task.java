/*
 * File     : Task.java
 * Purpose  : Abstract class acting as the base Node (Composite Pattern).
 *            Task node can either be a SingleTask or ParentTask later.
 */

package edu.curtin.app;

public abstract class Task 
{
    protected String id;
    protected String desc;
    protected int level; //number of indentations, i.e. when a task is a subtask within a task

    @Override
    public abstract String toString();
    public abstract int getType();

    public String getID()
    {
        return id;
    }

    public String getDesc()
    {
        return desc;
    }

    public void addLevel()
    {
        level++;
    }

    public void resetLevel()
    {
        level = 0;
    }
}