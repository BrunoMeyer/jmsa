package br.ufpr.bioinfo.jmsa.model;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.ini4j.Wini;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistInfo;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistPlot;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistTable;

public class OPeaklist
{
    public boolean valid = false;
    //
    public File peaklistFile;
    public File peaklistJMSAINFOFile;
    //Information from original XML file
    public String spectrumid = "";
    public String date = "";
    public String shots = "";
    public String creator = "";
    public String version = "";
    public ArrayList<OPeak> peaks = new ArrayList<OPeak>();
    //Information from JMSA and JMSAINFO file
    public String jmsainfoName = "";
    public String jmsainfoSpecie = "";
    public String jmsainfoStrain = "";
    public String jmsainfoNotes = "";
    //
    //
    //Presentation attributes
    public Boolean selected = new Boolean(false);
    public Boolean reflex = new Boolean(false);
    public Color spectrumForegroundColor = Color.BLACK;
    public Color spectrumBackgroundColor = Color.WHITE;
    private PPeaklistTable peaklistTable;
    private PPeaklistPlot peaklistPlot;
    private PPeaklistInfo peaklistInfo;
    
    public OPeaklist(File peaklistFile) throws ParserConfigurationException, SAXException, IOException
    {
        this.peaklistFile = peaklistFile;
        //
        readXML();
        readJMSAINFO();
        //
        if (valid)
        {
            int hash = spectrumid.hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = hash & 0x0000FF;
            spectrumForegroundColor = new Color(r, g, b);
            spectrumBackgroundColor = new Color(255 - r, 255 - g, 255 - b);
        }
        else
        {
            System.out.println("Peaklist inválido: " + peaklistFile.getAbsolutePath());
        }
    }
    
    public ArrayList<OPeak> getPeaks() {
    	return this.peaks;
    }
    
    public PPeaklistTable getPeaklistTable()
    {
        if (valid)
        {
            if (peaklistTable == null)
            {
                peaklistTable = new PPeaklistTable(this);
            }
        }
        return peaklistTable;
    }
    
    public PPeaklistPlot getPeaklistPlot(boolean intensity)
    {
        if (valid)
        {
                peaklistPlot = new PPeaklistPlot(this,intensity);  
        }
        return peaklistPlot;
    }
    
    public PPeaklistInfo getPeaklistInfo()
    {
        if (valid)
        {
            if (peaklistInfo == null)
            {
                peaklistInfo = new PPeaklistInfo(this);
            }
        }
        return peaklistInfo;
    }
    
    public void reset()
    {
        peaklistPlot = null;
        peaklistTable = null;
        peaklistInfo = null;
    }
    
    public void readXML() throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(peaklistFile);
        //http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        document.getDocumentElement().normalize();
        //
        NodeList nodeListpklist = document.getElementsByTagName("pklist");
        if (nodeListpklist != null)
        {
            try
            {
                for (int i = 0; i < nodeListpklist.getLength(); i++)
                {
                    if (nodeListpklist.item(i).getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element elementpklist = (Element) nodeListpklist.item(i);
                        //
                        spectrumid = elementpklist.getAttributes().getNamedItem("spectrumid").getNodeValue();
                        date = elementpklist.getAttributes().getNamedItem("date").getNodeValue();
                        shots = elementpklist.getAttributes().getNamedItem("shots").getNodeValue();
                        creator = elementpklist.getAttributes().getNamedItem("creator").getNodeValue();
                        version = elementpklist.getAttributes().getNamedItem("version").getNodeValue();
                        //
                        //
                        //TODO MALTON: Todo o conte�do de 'pklist' poderia ser preenchido posteriormente para poupar processamento
                        //TODO MALTON: Posteriormente como, ao selecionar um Peaklist para visualiza��o
                        NodeList nodeListpk = elementpklist.getElementsByTagName("pk");
                        if (nodeListpk != null)
                        {
                            for (int j = 0; j < nodeListpk.getLength(); j++)
                            {
                                if (nodeListpk.item(j).getNodeType() == Node.ELEMENT_NODE)
                                {
                                    peaks.add(new OPeak((Element) nodeListpk.item(j)));
                                }
                            }
                        }
                    }
                }
                if ((spectrumid != null) && !spectrumid.equals("") && !peaks.isEmpty())
                {
                    valid = true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void readJMSAINFO()
    {
        peaklistJMSAINFOFile = new File(peaklistFile.getAbsolutePath() + ".jmsainfo");
        //
        if (peaklistJMSAINFOFile.exists())
        {
            try
            {
                Wini wini = new Wini(peaklistJMSAINFOFile);
                jmsainfoName = wini.get("jmsainfo", "jmsainfoName", String.class);
                jmsainfoSpecie = wini.get("jmsainfo", "jmsainfoSpecie", String.class);
                jmsainfoStrain = wini.get("jmsainfo", "jmsainfoStrain", String.class);
                jmsainfoNotes = wini.get("jmsainfo", "jmsainfoNotes", String.class);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void saveJMSAINFO()
    {
        try
        {
            if (!peaklistJMSAINFOFile.exists())
            {
                peaklistJMSAINFOFile.createNewFile();
            }
            Wini wini = new Wini(peaklistJMSAINFOFile);
            wini.put("jmsainfo", "jmsainfoName", jmsainfoName);
            wini.put("jmsainfo", "jmsainfoSpecie", jmsainfoSpecie);
            wini.put("jmsainfo", "jmsainfoStrain", jmsainfoStrain);
            wini.put("jmsainfo", "jmsainfoNotes", jmsainfoNotes);
            wini.store();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString()
    {
        String title = "";
        if (FMainWindow.getInstance().checkBoxMenuItemPlotTitleName.isSelected() && (jmsainfoName != null) && !jmsainfoName.isEmpty())
        {
            title += " - " + jmsainfoName;
        }
        if (FMainWindow.getInstance().checkBoxMenuItemPlotTitleSpectrumID.isSelected() && (spectrumid != null) && !spectrumid.equals(""))
        {
            title += " - " + spectrumid;
        }
        if (FMainWindow.getInstance().checkBoxMenuItemPlotTitleSpecies.isSelected() && (jmsainfoSpecie != null) && !jmsainfoSpecie.equals(""))
        {
            title += " - " + jmsainfoSpecie;
        }
        if (FMainWindow.getInstance().checkBoxMenuItemPlotTitleStrain.isSelected() && (jmsainfoStrain != null) && !jmsainfoStrain.equals(""))
        {
            title += " - " + jmsainfoStrain;
        }
        //
        //
        if (title.isEmpty())
        {
            title += spectrumid;
        }
        else if (title.startsWith(" - "))
        {
            title = title.substring(3);
        }
        return title;
    }
}
