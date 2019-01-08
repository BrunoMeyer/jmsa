package br.ufpr.bioinfo.jmsa.model;

import org.w3c.dom.Element;

public class OPeak
{
    public double absi = 0;
    public double area = 0;
    public double ind = 0;
    public double lind = 0;
    public double mass = 0;
    public double meth = 0;
    public double reso = 0;
    public double rind = 0;
    public double s2n = 0;
    public double type = 0;
    
    public OPeak(Element elementpk)
    {
        absi = Double.parseDouble(elementpk.getElementsByTagName("absi").item(0).getTextContent());
        area = Double.parseDouble(elementpk.getElementsByTagName("area").item(0).getTextContent());
        ind = Double.parseDouble(elementpk.getElementsByTagName("ind").item(0).getTextContent());
        mass = Double.parseDouble(elementpk.getElementsByTagName("mass").item(0).getTextContent());
        meth = Double.parseDouble(elementpk.getElementsByTagName("meth").item(0).getTextContent());
        reso = Double.parseDouble(elementpk.getElementsByTagName("reso").item(0).getTextContent());
        rind = Double.parseDouble(elementpk.getElementsByTagName("rind").item(0).getTextContent());
        s2n = Double.parseDouble(elementpk.getElementsByTagName("s2n").item(0).getTextContent());
        //type = Double.parseDouble(elementpk.getElementsByTagName("type").item(0).getTextContent());
    }
    public OPeak(OPeak opeak)
    {
        absi = opeak.absi;
        area = opeak.area;
        ind = opeak.ind;
        ind = opeak.lind;
        mass = opeak.mass;
        meth = opeak.meth;
        reso = opeak.reso;
        rind = opeak.rind;
        s2n = opeak.s2n;
        type = opeak.type;
    }
    
    public void add(OPeak opeak)
    {
        absi += opeak.absi;
        area += opeak.area;
        ind += opeak.ind;
        ind += opeak.lind;
        mass += opeak.mass;
        meth += opeak.meth;
        reso += opeak.reso;
        rind += opeak.rind;
        s2n += opeak.s2n;
    }
    
    public void divide(double factor) {
    	absi /= factor;
        area /= factor;
        ind /= factor;
        ind /= factor;
        mass /= factor;
        meth /= factor;
        reso /= factor;
        rind /= factor;
        s2n /= factor;
    }
    public OPeak()
    {
        
    }
}
