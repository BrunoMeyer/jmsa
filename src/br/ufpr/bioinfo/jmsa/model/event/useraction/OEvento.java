package br.ufpr.bioinfo.jmsa.model.event.useraction;


public interface OEvento
{
    public void executarEvento();
    
    public interface CallBackEvent{
        void callback();
    }
}
