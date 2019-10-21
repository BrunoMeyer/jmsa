package br.ufpr.bioinfo.jmsa.model.event.useraction;


public interface OEvent
{
    public void executeEvent();
    
    public interface CallBackEvent{
        void callback();
    }
}
