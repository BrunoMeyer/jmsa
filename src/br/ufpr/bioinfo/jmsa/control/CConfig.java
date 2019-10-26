package br.ufpr.bioinfo.jmsa.control;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.ini4j.Wini;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class CConfig
{
    private static CConfig myself;
    //
    public ResourceBundle rb = ResourceBundle.getBundle("br.ufpr.bioinfo.jmsa.resources.properties.MESSAGES");
    //
    //
    public int rawDataMergeSize = 10;
    public String lookAndFeel = "";
    public boolean showMSName = true;
    public boolean showMSSpectrumID = true;
    public boolean showMSSpecies = true;
    public boolean showMSStrain = true;
    public boolean showFilePath = true;
    public boolean showSuperSpectreSize = true;
    public boolean plotTitleName = true;
    public boolean plotTitleSpectrumID = true;
    public boolean plotTitleSpecies = true;
    public boolean plotTitleStrain = true;
    public boolean plotEnableIntensity = true;
    public boolean plotEnableRawSpectre = false;
    
    //
    //OS Info
    public String osname = "";
    public String osvardir = "";
    public String jmsadir = "";
    //
    public String loadingPath = "D:\\DADOSMALDITOF";
    
    public static CConfig getInstance()
    {
        if (myself == null)
        {
            myself = new CConfig();
        }
        return myself;
    }
    
    private CConfig()
    {
        initOS();
        readConfig();
    }
    
    private void initOS()
    {
        //TODO MALTON: Any OS Specific should be here
        osname = System.getProperty("os.name");
        if (osname.toLowerCase().indexOf("windows") != -1)
        {
            osvardir = System.getenv("APPDATA");
        }
        else if (osname.toLowerCase().indexOf("linux") != -1)
        {
            osvardir = System.getenv("HOME");
        }
        else if (osname.toLowerCase().indexOf("mac") != -1)
        {
            osvardir = System.getenv("HOME");
        }
        else
        {
            osvardir = System.getenv("HOME");
        }
        //TODO MALTON: Sets a path at user home to storage JMSA Things
        //        jmsadir = osvardir + File.separator + rb.getString("ABOUT.NAME") + File.separator;
        File jmsadirFile = new File(jmsadir);
        File configFile = new File(jmsadir + rb.getString("CONFIG.FILENAME"));
        if (!configFile.exists())
        {
            try
            {
                jmsadirFile.mkdirs();
                configFile.createNewFile();
                saveConfig();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void readConfig()
    {
        try
        {
            Wini wini = new Wini(new File(jmsadir + rb.getString("CONFIG.FILENAME")));
            
            loadingPath = wini.get(rb.getString("CONFIG.SECTION"), "loadingPath", String.class);
            lookAndFeel = wini.get(rb.getString("CONFIG.SECTION"), "lookAndFeel", String.class);
            if(wini.get(rb.getString("CONFIG.SECTION")).containsKey("RawDataMergeSize")) {            	
            	rawDataMergeSize = (int)wini.get(rb.getString("CONFIG.SECTION"), "RawDataMergeSize", int.class);
            }
            showMSName = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowMSName", Boolean.class);
            showMSSpectrumID = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowMSSpectrumID", Boolean.class);
            showMSSpecies = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowMSSpecies", Boolean.class);
            showMSStrain = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowMSStrain", Boolean.class);
            plotTitleName = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotTitleName", Boolean.class);
            plotTitleSpectrumID = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotTitleSpectrumID", Boolean.class);
            plotTitleSpecies = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotTitleSpecies", Boolean.class);
            plotTitleStrain = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotTitleStrain", Boolean.class);
            plotEnableIntensity = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotEnableIntensity", Boolean.class);
            plotEnableRawSpectre = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "PlotEnableRawSpectre", Boolean.class);
            
            if(wini.get(rb.getString("CONFIG.SECTION")).containsKey("ShowFilePath")) {
            	showFilePath = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowFilePath", Boolean.class);
            }
            if(wini.get(rb.getString("CONFIG.SECTION")).containsKey("ShowSuperSpectreSize")) {
            	showSuperSpectreSize = (Boolean)wini.get(rb.getString("CONFIG.SECTION"), "ShowSuperSpectreSize", Boolean.class);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void saveConfig()
    {
        try
        {
            Wini wini = new Wini(new File(jmsadir + rb.getString("CONFIG.FILENAME")));
            wini.put(rb.getString("CONFIG.SECTION"), "loadingPath", loadingPath);
            wini.put(rb.getString("CONFIG.SECTION"), "lookAndFeel", lookAndFeel);
            if((int)wini.get(rb.getString("CONFIG.SECTION"), "RawDataMergeSize", int.class) != rawDataMergeSize
            	&& plotEnableRawSpectre) {
            	FMainWindow.getInstance().resetPlots();
            }
            wini.put(rb.getString("CONFIG.SECTION"), "RawDataMergeSize", rawDataMergeSize);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowMSName", showMSName);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowMSSpectrumID", showMSSpectrumID);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowMSSpecies", showMSSpecies);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowMSStrain", showMSStrain);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotTitleName", plotTitleName);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotTitleSpectrumID", plotTitleSpectrumID);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotTitleSpecies", plotTitleSpecies);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotTitleStrain", plotTitleStrain);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotEnableIntensity", plotEnableIntensity);
            wini.put(rb.getString("CONFIG.SECTION"), "PlotEnableRawSpectre", plotEnableRawSpectre);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowFilePath", showFilePath);
            wini.put(rb.getString("CONFIG.SECTION"), "ShowSuperSpectreSize", showSuperSpectreSize);
            
            wini.store();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void setLookAndFeel(String lookAndFeel)
    {
        this.lookAndFeel = lookAndFeel;
        getLookAndFeel();
    }
    
    public String getLookAndFeel()
    {
        try
        {
//            UIManager.setLookAndFeel(lookAndFeel);
            SwingUtilities.updateComponentTreeUI(FMainWindow.getInstance());
            FMainWindow.getInstance().pack();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return lookAndFeel;
    }
}
