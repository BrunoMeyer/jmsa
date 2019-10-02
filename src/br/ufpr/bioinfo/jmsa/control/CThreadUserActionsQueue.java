package br.ufpr.bioinfo.jmsa.control;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Queue;
import br.ufpr.bioinfo.jmsa.model.event.useraction.OEvento;

public class CThreadUserActionsQueue extends Thread
{
    private boolean running = true;
    private Queue<Object> events = new LinkedList<Object>();
    
    public CThreadUserActionsQueue()
    {
        setName("CThreadUserActionsQueue");
    }
    
    @Override
    public void run()
    {
        while (this.isRodando())
        {
            if (this.events.isEmpty())
            {
                try
                {
                    sleep(0, 1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                continue;
            }
            try
            {
                OEvento obj = (OEvento) this.events.poll();
                obj.executarEvento();
            }
            catch (Exception e)
            {
                StringBuffer sBuffer = new StringBuffer();
                sBuffer.append("Exception founded when event was executed:\n");
                StringWriter sWriter = new StringWriter();
                PrintWriter pWriter = new PrintWriter(sWriter, true);
                e.printStackTrace(pWriter);
                pWriter.flush();
                sWriter.flush();
                sBuffer.append(sWriter);
                System.out.println(sBuffer.toString());
                //                CCommons.AdicionarLog(Statics.habilitarLog, Level.SEVERE, sBuffer.toString());
            }
        }
    }
    
    public synchronized void setRodando(boolean isAlive)
    {
        this.running = isAlive;
    }
    
    public synchronized boolean isRodando()
    {
        return this.running;
    }
    
    public synchronized void addEvento(Object evento)
    {
        this.events.add(evento);
    }
}
