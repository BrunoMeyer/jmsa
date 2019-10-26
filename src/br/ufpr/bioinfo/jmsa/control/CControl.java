package br.ufpr.bioinfo.jmsa.control;

import java.awt.EventQueue;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class CControl
{
    private static CControl myself;
    //
    public volatile CThreadUserActionsQueue threadUserActionsPool;
    
    public static CControl getInstance()
    {
        if (myself == null)
        {
            myself = new CControl();
        }
        return myself;
    }
    
    private CControl()
    {
        CConfig.getInstance().getLookAndFeel();
        threadUserActionsPool = new CThreadUserActionsQueue();
        threadUserActionsPool.start();
    }
    
    public void exit()
    {
        try
        {
            if (threadUserActionsPool != null)
            {
                threadUserActionsPool.setRunning(false);
                threadUserActionsPool.join(5000);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        CControl.getInstance();
        //
        //
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    FMainWindow.getInstance().setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        //
        //
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
