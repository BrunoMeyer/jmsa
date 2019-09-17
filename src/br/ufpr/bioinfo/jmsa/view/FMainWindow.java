package br.ufpr.bioinfo.jmsa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufpr.bioinfo.jmsa.control.CConfig;
import br.ufpr.bioinfo.jmsa.control.CControl;
import br.ufpr.bioinfo.jmsa.model.OPeak;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.SuperPeaklist;
import br.ufpr.bioinfo.jmsa.model.event.useraction.OUserActionLoadPeakFiles;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistSimilarity;
import br.ufpr.bioinfo.jmsa.view.core.PSuperPeaklistPlot;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistClassifier;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistDBManager;

import br.ufpr.bioinfo.jmsa.view.core.PPeaklistDendrogram;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistFiles;
import br.ufpr.bioinfo.jmsa.view.core.PPeaklistAction;
import br.ufpr.bioinfo.jmsa.view.core.SIconUtil;



import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FMainWindow extends JFrame
{
    private static FMainWindow myself;
    //
    public JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    public JScrollPane scrollPeaklistTablesPlots = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane scrollPeaklistTables = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane scrollPeaklistPlots = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane scrollPeaklistClassifier = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane scrollPeaklistDBManager = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane scrollPeaklistInformations = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JTabbedPane tabbedPaneFiles = new JTabbedPane(JTabbedPane.TOP);
    public JTabbedPane tabbedPaneMain = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    public JTabbedPane tabbedPanePeaklist = new JTabbedPane(JTabbedPane.TOP);
    public JTabbedPane tabbedPaneDatabase = new JTabbedPane(JTabbedPane.TOP);
    public PPeaklistFiles panelSpectraPeaklistFiles = new PPeaklistFiles("Spectra", this);
    public PPeaklistFiles panelSpectraPeaklistFilesDB = new PPeaklistFiles("Data Base", this);
    public PPeaklistFiles panelSpectraPeaklistFilesSE = new PPeaklistFiles("Super Spectra", this);
    public JPanel panelPeaklist = new JPanel();
    public JPanel panelDatabase = new JPanel();
    public JPanel panelPeaklistTablesPlots = new JPanel();
    public JPanel panelPeaklistTables = new JPanel();
    public JPanel panelPeaklistPlots = new JPanel();
    public JPanel panelPeaklistProteins = new JPanel();
    public JPanel panelPeaklistGroups = new JPanel();
    public JPanel panelPeaklistSimilarity = new JPanel();
    public JPanel panelPeaklistAction = new JPanel();
    public JScrollPane panelPeaklistCluster = new JScrollPane();
    public JPanel panelPeaklistClassifier = new JPanel();
    public JPanel panelPeaklistDBManager = new JPanel();
    public JPanel panelPeaklistInformations = new JPanel();
    public PPeaklistDendrogram panelCluster = new PPeaklistDendrogram();
    public PPeaklistClassifier panelClassifier = new PPeaklistClassifier(this);
    public PPeaklistDBManager panelDBManager = new PPeaklistDBManager(this);
    public JPanel panelInformation = new JPanel();
    public JPanel panelStatusBar = new JPanel();
    public PSuperPeaklistPlot superPeaklistPlot = new PSuperPeaklistPlot();
    public JMenuBar menubar = new JMenuBar();
    public JMenu menuFile = new JMenu("File");
    public JMenu menuView = new JMenu("View");
    public JMenu menuShowColumns = new JMenu("Show Columns");
    public JMenu menuPlotTitle = new JMenu("Plot Name");
    public JMenu menuLoadOptions = new JMenu("Load options");
    public JMenu menuHelp = new JMenu("Help");
    public JMenuItem menuItemLoadFiles = new JMenuItem("Load Files");
    public JMenuItem menuItemConfig = new JMenuItem("Configuration");
    public JMenuItem menuItemExit = new JMenuItem("Exit");
    public JMenuItem menuItemAbout = new JMenuItem("About");
    public JCheckBoxMenuItem checkBoxMenuItemShowMSName = new JCheckBoxMenuItem("Show Name", CConfig.getInstance().showMSName);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSSpectrumID = new JCheckBoxMenuItem("Show SpectrumID", CConfig.getInstance().showMSSpectrumID);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSSpecies = new JCheckBoxMenuItem("Show Species", CConfig.getInstance().showMSSpecies);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSStrain = new JCheckBoxMenuItem("Show Strain", CConfig.getInstance().showMSStrain);
    public JCheckBoxMenuItem checkBoxMenuItemShowFilePath = new JCheckBoxMenuItem("Show File Path", CConfig.getInstance().showFilePath);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleName = new JCheckBoxMenuItem("Name", CConfig.getInstance().plotTitleName);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleSpectrumID = new JCheckBoxMenuItem("SpectrumID", CConfig.getInstance().plotTitleSpectrumID);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleSpecies = new JCheckBoxMenuItem("Species", CConfig.getInstance().plotTitleSpecies);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleStrain = new JCheckBoxMenuItem("Strain", CConfig.getInstance().plotTitleStrain);
    public JCheckBoxMenuItem checkBoxMenuItemPlotEnableIntensity = new JCheckBoxMenuItem("Enable Intensity");
    public JCheckBoxMenuItem checkBoxMenuItemIncrementalLoad = new JCheckBoxMenuItem("Incremental Loading");
    
    public JToolBar toolBar = new JToolBar("Tools");
    public JLabel labelStatusBar = new JLabel("JMSA");
    
    public JButton buttonLoadFiles = new JButton("Load");
    public JButton buttonSaveDBZIP = new JButton("Save to ZIP");
    public JButton buttonLoadDB = new JButton("Load from ZIP");
    
    public JButton buttonPeaklist = new JButton("Peaklist");
    public JButton buttonAnalyser = new JButton("Analyser");
    public JButton buttonCluster = new JButton("Cluster");
    public JButton buttonClassifier = new JButton("DB search");
    public JButton buttonDBManager = new JButton("DB Management");
    public JButton buttonInformation = new JButton("Information");
    public JButton buttonSelectAll = new JButton("Select All");
    public JButton buttonDeselectAll = new JButton("Deselect All");
    public JButton buttonExportCsv = new JButton("Export to CSV");
    
    public PPeaklistSimilarity similarityMatrix;
    
    public PPeaklistAction infoAction;
    
    public boolean lockUpdatePanels = false;
    
    public static FMainWindow getInstance()
    {
        if (myself == null)
        {
            myself = new FMainWindow();
        }
        return myself;
    }
    
    private FMainWindow()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(CConfig.getInstance().rb.getString("ABOUT.FULLNAME") + " - " + CConfig.getInstance().rb.getString("ABOUT.VERSION"));
        setIconImage(SIconUtil.imageIconMainWindow32.getImage());
        //
        //
        setLayout(new BorderLayout(2, 2));
        panelPeaklist.setLayout(new BorderLayout(2, 2));
        panelDatabase.setLayout(new BorderLayout(2, 2));
        panelPeaklistTablesPlots.setLayout(new BoxLayout(panelPeaklistTablesPlots, BoxLayout.Y_AXIS));
        panelPeaklistTables.setLayout(new BoxLayout(panelPeaklistTables, BoxLayout.Y_AXIS));
        panelPeaklistPlots.setLayout(new BoxLayout(panelPeaklistPlots, BoxLayout.Y_AXIS));
        panelPeaklistSimilarity.setLayout(new BoxLayout(panelPeaklistSimilarity, BoxLayout.Y_AXIS));
        panelPeaklistAction.setLayout(new BoxLayout(panelPeaklistAction, BoxLayout.Y_AXIS));
        panelPeaklistInformations.setLayout(new BoxLayout(panelPeaklistInformations, BoxLayout.Y_AXIS));
        panelCluster.setLayout(new BorderLayout(2, 2));
        panelClassifier.setLayout(new BorderLayout(2, 2));
        panelDBManager.setLayout(new BorderLayout(2, 2));
        
        
        panelStatusBar.setLayout(new BorderLayout(2, 2));
        //
        //
        setJMenuBar(menubar);
        menubar.add(menuFile);
        menubar.add(menuView);
        menubar.add(menuHelp);
        menuFile.add(menuItemLoadFiles);
        menuFile.addSeparator();
        menuFile.add(menuItemConfig);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);
        menuView.add(menuShowColumns);
        menuView.add(menuPlotTitle);
        menuView.add(menuLoadOptions);
        menuHelp.add(menuItemAbout);
        menuShowColumns.add(checkBoxMenuItemShowMSName);
        menuShowColumns.add(checkBoxMenuItemShowMSSpectrumID);
        menuShowColumns.add(checkBoxMenuItemShowMSSpecies);
        menuShowColumns.add(checkBoxMenuItemShowMSStrain);
        menuShowColumns.add(checkBoxMenuItemShowFilePath);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleName);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleSpectrumID);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleSpecies);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleStrain);
        menuPlotTitle.add(checkBoxMenuItemPlotEnableIntensity);
        menuLoadOptions.add(checkBoxMenuItemIncrementalLoad);
        
        //
        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(panelStatusBar, BorderLayout.SOUTH);
        toolBar.add(buttonLoadFiles);
        toolBar.add(buttonSaveDBZIP);
        toolBar.add(buttonLoadDB);
        toolBar.add(buttonPeaklist);
//        toolBar.add(buttonAnalyser);
//        toolBar.add(buttonCluster);
//        toolBar.add(buttonClassifier);
//        toolBar.add(buttonDBManager);
        toolBar.add(buttonInformation);
        toolBar.add(buttonSelectAll);
        toolBar.add(buttonDeselectAll);
        splitPane.add(tabbedPaneFiles);
        splitPane.add(tabbedPaneMain);
        panelStatusBar.add(labelStatusBar, BorderLayout.CENTER);
        tabbedPaneFiles.addTab("Spectra", panelSpectraPeaklistFiles);
        tabbedPaneFiles.addTab("Super Spectra", panelSpectraPeaklistFilesSE);
        tabbedPaneFiles.addTab("Data Base", panelSpectraPeaklistFilesDB);

        tabbedPaneMain.addTab("Peaklist", panelPeaklist);
        tabbedPaneMain.addTab("Database", panelDatabase);
        tabbedPaneMain.addTab("Analyser", superPeaklistPlot);
        tabbedPaneMain.addTab("Cluster", panelCluster);
        
        tabbedPaneMain.addTab("Information", panelInformation);
        
        panelPeaklist.add(tabbedPanePeaklist, BorderLayout.CENTER);
        panelDatabase.add(tabbedPaneDatabase, BorderLayout.CENTER);
        tabbedPanePeaklist.addTab("Tables / Plots", scrollPeaklistTablesPlots);
        tabbedPanePeaklist.addTab("Tables", scrollPeaklistTables);
        tabbedPanePeaklist.addTab("Plots", scrollPeaklistPlots);
        tabbedPanePeaklist.addTab("Proteins", panelPeaklistProteins);
        tabbedPanePeaklist.addTab("Groups", panelPeaklistGroups);
        tabbedPanePeaklist.addTab("Similarity", panelPeaklistSimilarity);
//        tabbedPanePeaklist.addTab("Cluster", panelPeaklistCluster);
        tabbedPanePeaklist.addTab("Informations", scrollPeaklistInformations);
        tabbedPanePeaklist.addTab("Peaklists Action", panelPeaklistAction);
        
        tabbedPaneDatabase.addTab("Search", panelClassifier);
        tabbedPaneDatabase.addTab("Manager", panelDBManager);
        
        scrollPeaklistTablesPlots.setViewportView(panelPeaklistTablesPlots);
        scrollPeaklistInformations.setViewportView(panelPeaklistInformations);
        scrollPeaklistTables.setViewportView(panelPeaklistTables);
        scrollPeaklistPlots.setViewportView(panelPeaklistPlots);
        
        //
        //
        splitPane.setOneTouchExpandable(true);
        tabbedPanePeaklist.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        //
        //
        //
        buttonLoadFiles.setIcon(SIconUtil.imageIconLoadFiles16);
        buttonPeaklist.setIcon(SIconUtil.imageIconPeaklist16);
        buttonAnalyser.setIcon(SIconUtil.imageIconAnalyser16);
        buttonCluster.setIcon(SIconUtil.imageIconCluster16);
        buttonInformation.setIcon(SIconUtil.imageIconInformation16);
        //
        //
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                super.windowClosed(e);
                CControl.getInstance().exit();
            }
        });
        
        tabbedPaneDatabase.addChangeListener(new ChangeListener()
        {
        	
            @Override
            public void stateChanged(ChangeEvent e)
            {
            	try
                {
            		if(lockUpdatePanels) return;
            		List<OPeaklist> dbpeaklists = panelSpectraPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
            		List<OPeaklist> peaklists = getLoadingSelectedPeaklists();
	            	
	            	if(tabbedPaneDatabase.getSelectedComponent() == panelDBManager) {
	            		panelDBManager.reloadClassifier(peaklists);
	            	}
	            	else if(tabbedPaneDatabase.getSelectedComponent() == panelClassifier) {
	            		panelClassifier.reloadClassifier(peaklists, dbpeaklists);
	            	}
                }
            	catch (Exception err)
                {
            		err.printStackTrace();
                }
                
            }
        });
        
        tabbedPaneMain.addChangeListener(new ChangeListener()
        {
        	
            @Override
            public void stateChanged(ChangeEvent e)
            {
            	try
                {
            		if(lockUpdatePanels) return;
            		List<OPeaklist> dbpeaklists = panelSpectraPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
	            	List<OPeaklist> peaklists = getLoadingSelectedPeaklists();
	               
	            	if(tabbedPaneMain.getSelectedComponent() == superPeaklistPlot) {
            			superPeaklistPlot.buildPlot(
    	                	peaklists,
    	                	checkBoxMenuItemPlotEnableIntensity.isSelected()
    	                );
	            	}
	            	else if(tabbedPaneMain.getSelectedComponent() == panelCluster) {
	            		panelCluster.reloadDendrogram(peaklists);
	            	}
	            	else if(tabbedPaneDatabase.getSelectedComponent() == panelDBManager) {
	            		panelDBManager.reloadClassifier(peaklists);
	            	}
	            	else if(tabbedPaneDatabase.getSelectedComponent() == panelClassifier) {
	            		panelClassifier.reloadClassifier(peaklists, dbpeaklists);
	            	}
                }
            	catch (Exception err)
                {
            		err.printStackTrace();
                }
                
            }
        });
        
        tabbedPanePeaklist.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
        	try
            {
                List<OPeaklist> peaklists = getLoadingSelectedPeaklists();
                
                if (tabbedPanePeaklist.getSelectedComponent() == scrollPeaklistTablesPlots)
                {
                    panelPeaklistTablesPlots.removeAll();
                    for (OPeaklist peaklist : peaklists)
                    {
                        Box box = Box.createHorizontalBox();
                        box.add(peaklist.getPeaklistPlot(checkBoxMenuItemPlotEnableIntensity.isSelected()));
                        box.add(peaklist.getPeaklistTable());
                        panelPeaklistTablesPlots.add(box);
                    }
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == scrollPeaklistTables)
                {
                    panelPeaklistTables.removeAll();
                    for (OPeaklist peaklist : peaklists)
                    {
                        panelPeaklistTables.add(peaklist.getPeaklistTable());
                    }
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == scrollPeaklistPlots)
                {
                    panelPeaklistPlots.removeAll();
                    for (OPeaklist peaklist : peaklists)
                    {
                        panelPeaklistPlots.add(peaklist.getPeaklistPlot(checkBoxMenuItemPlotEnableIntensity.isSelected()));
                    }
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == panelPeaklistProteins)
                {
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == panelPeaklistGroups)
                {
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == panelPeaklistSimilarity)
                {
                    panelPeaklistSimilarity.removeAll();
                    panelPeaklistSimilarity.add(buttonExportCsv);
                    similarityMatrix = new PPeaklistSimilarity(peaklists);
                    
                    panelPeaklistSimilarity.add(similarityMatrix);
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == scrollPeaklistInformations)
                {
                    updatePeaklistInformationPanel(peaklists);
                }
                else if (tabbedPanePeaklist.getSelectedComponent() == panelPeaklistAction)
                {
                    panelPeaklistAction.removeAll();
                    infoAction = new PPeaklistAction(peaklists);
                    panelPeaklistAction.add(infoAction);
                }
                //
                repaint();
            } //Fim do trycat
        	catch (Exception err)
            {
        		err.printStackTrace();
            }
        	
            }
        });
        
        ActionListener actionListener = new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
            	List<OPeaklist> peaklists = getLoadingSelectedPeaklists();
            	List<OPeaklist> dbpeaklists = panelSpectraPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
            	
            	PPeaklistFiles selectedPanelPeaklistFiles = (PPeaklistFiles)tabbedPaneFiles.getSelectedComponent();
            	JFileChooser chooserSave;
            	int retrival;
            	FileNameExtensionFilter filter;

            	switch (e.getActionCommand())
                {
                    case "ShowMSName":
                    case "ShowMSSpectrumID":
                    case "ShowMSSpecies":
                    case "ShowMSStrain":
                    case "ShowFilePath":
                    case "PlotTitleName":
                    case "PlotTitleSpectrumID":
                    case "PlotTitleSpecies":
                    case "PlotTitleStrain":
                        CConfig.getInstance().showMSName = checkBoxMenuItemShowMSName.isSelected();
                        CConfig.getInstance().showMSSpectrumID = checkBoxMenuItemShowMSSpectrumID.isSelected();
                        CConfig.getInstance().showMSSpecies = checkBoxMenuItemShowMSSpecies.isSelected();
                        CConfig.getInstance().showMSStrain = checkBoxMenuItemShowMSStrain.isSelected();
                        CConfig.getInstance().showFilePath = checkBoxMenuItemShowFilePath.isSelected();
                        CConfig.getInstance().plotTitleName = checkBoxMenuItemPlotTitleName.isSelected();
                        CConfig.getInstance().plotTitleSpectrumID = checkBoxMenuItemPlotTitleSpectrumID.isSelected();
                        CConfig.getInstance().plotTitleSpecies = checkBoxMenuItemPlotTitleSpecies.isSelected();
                        CConfig.getInstance().plotTitleStrain = checkBoxMenuItemPlotTitleStrain.isSelected();
                        CConfig.getInstance().saveConfig();
                        
                        //
                        updateVisibleColums();
                        
                        for (OPeaklist peaklist : peaklists)
                        {
                            peaklist.reset();
                        }
                        superPeaklistPlot.buildPlot(
                        	peaklists,
                        	checkBoxMenuItemPlotEnableIntensity.isSelected()
                        );
                        //
                        int prevTab = tabbedPanePeaklist.getSelectedIndex();
                        tabbedPanePeaklist.setSelectedIndex(-1);
                        tabbedPanePeaklist.setSelectedIndex(prevTab);
                        repaint();
                        break;
                    case "PlotIntensity":
                    	panelPeaklistTablesPlots.removeAll();
                        for (OPeaklist peaklist : peaklists)
                        {
                        	peaklist.reset();
                            Box box = Box.createHorizontalBox();
                            box.add(peaklist.getPeaklistPlot(checkBoxMenuItemPlotEnableIntensity.isSelected()));
                            box.add(peaklist.getPeaklistTable());
                            panelPeaklistTablesPlots.add(box);
                        }
                        
                        superPeaklistPlot.buildPlot(
                        		peaklists,
                            	checkBoxMenuItemPlotEnableIntensity.isSelected()
                        );
                        
                        repaint();
                        break;
                    case "loadpeakfiles":
                        JFileChooser chooser = new JFileChooser();
                        chooser.setCurrentDirectory(new File(CConfig.getInstance().loadingPath));
                        chooser.setDialogTitle("Select Mass Spectrum/Spectra folder");
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setMultiSelectionEnabled(true);
                        chooser.setAcceptAllFileFilterUsed(false);
                        
                        if(lockUpdatePanels) break;
                        
                        lockUpdatePanels = true;
                        if (chooser.showOpenDialog(FMainWindow.this) == JFileChooser.APPROVE_OPTION)
                        {
                            CControl.getInstance().threadUserActionsPool.addEvento(new OUserActionLoadPeakFiles(chooser.getSelectedFiles()));
                        }
                        else {
                        	lockUpdatePanels = false;
                        	firePeaklistPanelChange();
                        }
                        
                        updateVisibleColums();
                        
                        break;
                    case "save-db-zip":
                    	// Open the file selector interface
                    	chooserSave = new JFileChooser();
                    	chooserSave.setCurrentDirectory(
                    		new File(CConfig.getInstance().loadingPath)
                    	);
                    	filter = new FileNameExtensionFilter(null, "zip");
                    	chooserSave.setFileFilter(filter);
                        chooserSave.setSelectedFile(new File("database_name.jmsadb.zip"));
                        
                    	retrival = chooserSave.showSaveDialog(null);
                        
                        if (retrival == JFileChooser.APPROVE_OPTION) {
                        	// When the user choose a file name and directory to save
                            try {
                            	// Get the name of file choosed by user
                            	String file_name_to_save = chooserSave.getSelectedFile().toString();

                            	selectedPanelPeaklistFiles.saveToZIP(file_name_to_save, getSelectedPanelPeaks());
                            } catch (Exception err){
                        		err.printStackTrace();
                            }
                        }
                        break;
                    case "load-from-zip":
                    	// Open the file selector interface
                    	chooserSave = new JFileChooser();
                    	chooserSave.setCurrentDirectory(
                    		new File(CConfig.getInstance().loadingPath)
                    	);
                    	filter = new FileNameExtensionFilter(null, "zip");
                    	chooserSave.setFileFilter(filter);
                    	chooserSave.setMultiSelectionEnabled(true);
                    	
                    	if(lockUpdatePanels) break;
                        
                        if (chooserSave.showOpenDialog(FMainWindow.this) == JFileChooser.APPROVE_OPTION) {
                        	tabbedPaneMain.setSelectedComponent(panelPeaklist);
                        	lockUpdatePanels = true;
                        	// When the user choose a file name and directory to save
                            try {
                            	for(File f : chooserSave.getSelectedFiles()) {
                            		// Get the name of file choosed by user
                            		String file_name_to_save = f.toString();
                                	
                            		selectedPanelPeaklistFiles.loadFromZIP(file_name_to_save);
                            	}
                            } catch (Exception err){
                        		err.printStackTrace();
                            }
                        }
                        else {
                        	lockUpdatePanels = false;
                        	firePeaklistPanelChange();
                        }
                        break;
                    case "config":
                        new FConfig().setVisible(true);
                        break;
                    case "exit":
                        dispose();
                        break;
                    case "about":
                        FAbout.getInstance().setVisible(true);
                        break;
                    case "tab-peaklist":
                        tabbedPaneMain.setSelectedComponent(panelPeaklist);
                        break;
                    case "tab-analyser":
                        tabbedPaneMain.setSelectedComponent(superPeaklistPlot);
                        break;
                    case "tab-cluster":
                        tabbedPaneMain.setSelectedComponent(panelCluster);
                        panelCluster.removeAll();
                        panelCluster.reloadDendrogram(peaklists);
                        break;
                    case "tab-db-search":
                    	tabbedPaneMain.setSelectedComponent(panelClassifier);
                        panelClassifier.reloadClassifier(peaklists,dbpeaklists);
                        break;
                    case "tab-db-manager":
                    	
                    	tabbedPaneMain.setSelectedComponent(panelDBManager);
                    	panelDBManager.removeAll();
                    	
                        //panelClassifier.fillTable(peaklists);
                    	panelDBManager.reloadClassifier(peaklists);
                        break;
                    case "tab-information":
                        tabbedPaneMain.setSelectedComponent(panelInformation);
                        break;
                    case "select-all":
                    	setAllSelectedValue(true);
                    	break;
                    case "deselect-all":
                    	setAllSelectedValue(false);
                    	break;
                    
                    case "export-csv":
                    	String tableText = (similarityMatrix.csvTable());
                    	chooserSave = new JFileChooser();
                    	chooserSave.setCurrentDirectory(
                    		new File(CConfig.getInstance().loadingPath)
                    	);
                    	chooserSave.setSelectedFile(new File("table_name.csv"));
                        retrival = chooserSave.showSaveDialog(null);
                        if (retrival == JFileChooser.APPROVE_OPTION) {
                            try {
                            	FileWriter fw = new FileWriter(chooserSave.getSelectedFile().toString());
                                fw.write(tableText);
                                fw.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    
                    default:
                        break;
                }
            }
        };
        checkBoxMenuItemShowMSName.setActionCommand("ShowMSName");
        checkBoxMenuItemShowMSSpectrumID.setActionCommand("ShowMSSpectrumID");
        checkBoxMenuItemShowMSSpecies.setActionCommand("ShowMSSpecies");
        checkBoxMenuItemShowMSStrain.setActionCommand("ShowMSStrain");
        checkBoxMenuItemShowFilePath.setActionCommand("ShowFilePath");
        //
        checkBoxMenuItemShowMSName.addActionListener(actionListener);
        checkBoxMenuItemShowMSSpectrumID.addActionListener(actionListener);
        checkBoxMenuItemShowMSSpecies.addActionListener(actionListener);
        checkBoxMenuItemShowMSStrain.addActionListener(actionListener);
        checkBoxMenuItemShowFilePath.addActionListener(actionListener);
        //
        checkBoxMenuItemPlotTitleName.setActionCommand("PlotTitleName");
        checkBoxMenuItemPlotTitleSpectrumID.setActionCommand("PlotTitleSpectrumID");
        checkBoxMenuItemPlotTitleSpecies.setActionCommand("PlotTitleSpecies");
        checkBoxMenuItemPlotTitleStrain.setActionCommand("PlotTitleStrain");
        //
        checkBoxMenuItemPlotTitleName.addActionListener(actionListener);
        checkBoxMenuItemPlotTitleSpectrumID.addActionListener(actionListener);
        checkBoxMenuItemPlotTitleSpecies.addActionListener(actionListener);
        checkBoxMenuItemPlotTitleStrain.addActionListener(actionListener);
        
        checkBoxMenuItemPlotEnableIntensity.setActionCommand("PlotIntensity");
        checkBoxMenuItemPlotEnableIntensity.addActionListener(actionListener);
        
        checkBoxMenuItemPlotEnableIntensity.setActionCommand("IncrementalLoad");
        checkBoxMenuItemIncrementalLoad.addActionListener(actionListener);
        
        //
        menuItemLoadFiles.setActionCommand("loadpeakfiles");
        menuItemConfig.setActionCommand("config");
        menuItemExit.setActionCommand("exit");
        menuItemAbout.setActionCommand("about");
        buttonLoadFiles.setActionCommand("loadpeakfiles");
        buttonSaveDBZIP.setActionCommand("save-db-zip");
        buttonLoadDB.setActionCommand("load-from-zip");
        buttonPeaklist.setActionCommand("tab-peaklist");
        buttonAnalyser.setActionCommand("tab-analyser");
        buttonCluster.setActionCommand("tab-cluster");
        buttonClassifier.setActionCommand("tab-db-search");
        buttonDBManager.setActionCommand("tab-db-manager");
        buttonInformation.setActionCommand("tab-information");
        buttonSelectAll.setActionCommand("select-all");
        buttonDeselectAll.setActionCommand("deselect-all");
        buttonExportCsv.setActionCommand("export-csv");
        //
        menuItemLoadFiles.addActionListener(actionListener);
        menuItemConfig.addActionListener(actionListener);
        menuItemExit.addActionListener(actionListener);
        menuItemAbout.addActionListener(actionListener);
        buttonLoadFiles.addActionListener(actionListener);
        buttonSaveDBZIP.addActionListener(actionListener);
        buttonLoadDB.addActionListener(actionListener);
        buttonPeaklist.addActionListener(actionListener);
        buttonAnalyser.addActionListener(actionListener);
        buttonCluster.addActionListener(actionListener);
        buttonClassifier.addActionListener(actionListener);
        buttonDBManager.addActionListener(actionListener);
        buttonInformation.addActionListener(actionListener);
        buttonSelectAll.addActionListener(actionListener);
        buttonDeselectAll.addActionListener(actionListener);
        buttonExportCsv.addActionListener(actionListener);
        //
        //
        setPreferredSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);
        //
        //
        tabbedPanePeaklist.setSelectedIndex(0);
        //        buttonAnalyser.setEnabled(false);
        buttonCluster.setEnabled(true);
        buttonInformation.setEnabled(false);
        buttonDBManager.setEnabled(true);
        //        tabbedPaneMain.setEnabledAt(tabbedPaneMain.indexOfComponent(panelAnalyser), false);
        tabbedPaneMain.setEnabledAt(tabbedPaneMain.indexOfComponent(panelCluster), true);
        tabbedPaneMain.setEnabledAt(tabbedPaneMain.indexOfComponent(panelInformation), false);
//        tabbedPaneDatabase.setEnabledAt(tabbedPaneMain.indexOfComponent(panelDBManager), true);
        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistProteins), false);
        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistGroups), false);
        
        //        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistSimilarity), false);
        //        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistInformations), false);
    }
    
    public boolean isPeaklistsSelected() {
    	return tabbedPaneFiles.getSelectedComponent() == panelSpectraPeaklistFiles;
    }
    public boolean isSESelected() {
    	return tabbedPaneFiles.getSelectedComponent() == panelSpectraPeaklistFilesSE;
    }    
    public boolean isDBSelected() {
    	return tabbedPaneFiles.getSelectedComponent() == panelSpectraPeaklistFilesDB;
    }
    public boolean isLoadingSelected() {
    	return  isPeaklistsSelected() || isSESelected();
    }
    
    public void setAllSelectedValue(boolean value){
    	lockUpdatePanels = true;
    	if(isPeaklistsSelected() && panelSpectraPeaklistFiles.defaultTableModel.peaklists.size() > 0) {
			panelSpectraPeaklistFiles.defaultTableModel.setAllValuesAt(value, 1);
    	}
    	if(isSESelected() && panelSpectraPeaklistFilesSE.defaultTableModel.peaklists.size() > 0) {
    		panelSpectraPeaklistFilesSE.defaultTableModel.setAllValuesAt(value, 1);
    	}
    	lockUpdatePanels = false;
    	firePeaklistPanelChange();
    }
    
    public List<OPeaklist> getLoadingSelectedPeaklists() {
    	List<OPeaklist> peaklists = panelSpectraPeaklistFiles.defaultTableModel.getSelectedPeaklists();
		List<OPeaklist> superpeaklists = panelSpectraPeaklistFilesSE.defaultTableModel.getSelectedPeaklists();
    	peaklists.addAll(superpeaklists);
    	return peaklists;
    }
    public List<OPeaklist> getLoadingPeaklists() {
    	List<OPeaklist> peaklists = panelSpectraPeaklistFiles.defaultTableModel.getAllPeaklists();
		List<OPeaklist> superpeaklists = panelSpectraPeaklistFilesSE.defaultTableModel.getAllPeaklists();
    	peaklists.addAll(superpeaklists);
    	return peaklists;
    }
    
    public void firePeaklistPanelChange() {
    	if(isPeaklistsSelected()) {
    		panelSpectraPeaklistFiles.defaultTableModel.fireTableDataChanged();
    	}
    	if(isSESelected()) {
    		panelSpectraPeaklistFilesSE.defaultTableModel.fireTableDataChanged();
    	}
    	if(isDBSelected()) {
    		panelSpectraPeaklistFilesDB.defaultTableModel.fireTableDataChanged();
    	}
    }
    
    public void clearTable()
    {
    	if(isLoadingSelected()) {
    		panelSpectraPeaklistFiles.clearTable();
    		panelSpectraPeaklistFilesSE.clearTable();
    	}
    	if(isDBSelected()) {
    		panelSpectraPeaklistFilesDB.clearTable();
    	}
    }
    
    public void setTableTriggerChange(boolean active)
    {
    	if(isLoadingSelected()) {
    		panelSpectraPeaklistFiles.defaultTableModel.globalTrigger = active;
    		panelSpectraPeaklistFilesSE.defaultTableModel.globalTrigger = active;
    	}
    	if(isDBSelected()) {
    		panelSpectraPeaklistFilesDB.defaultTableModel.globalTrigger = active;
    	}
    }
    
    public void updateTable()
    {
    	if(isLoadingSelected()){ 
    		panelSpectraPeaklistFiles.defaultTableModel.fireTableDataChanged();
    		panelSpectraPeaklistFilesSE.defaultTableModel.fireTableDataChanged();
    	}
    	if(isDBSelected()) {
    		panelSpectraPeaklistFilesDB.defaultTableModel.fireTableDataChanged();
    	}
    }
    
    public void addPeaklistToTable(final OPeaklist peaklist)
    {
    	if(isLoadingSelected()) {
    		addPeaklistToLoadingTable(peaklist);
    	}
    	if(isDBSelected()) {
    		panelSpectraPeaklistFilesDB.addPeaklistToTable(peaklist);
    		peaklist.selected = true;
    	}
//    	updateVisibleColums();
    }
    
    public void addPeaklistToLoadingTable(final OPeaklist peaklist)
    {
    	if(peaklist instanceof SuperPeaklist) {
			panelSpectraPeaklistFilesSE.addPeaklistToTable(peaklist);
		}
		else {
			panelSpectraPeaklistFiles.addPeaklistToTable(peaklist);
		}
    }
    
    public void removePeaklistFromDBTable(final OPeaklist peaklist)
    {
    	panelSpectraPeaklistFilesDB.removePeaklistFromTable(peaklist);
    }
    
    public boolean isInDB(final OPeaklist peaklist) {
    	List<OPeaklist> peaklists = panelSpectraPeaklistFilesDB.defaultTableModel.getAllPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
    }
    
    public boolean isInLoad(final OPeaklist peaklist) {
    	List<OPeaklist> peaklists = getLoadingPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
    }
    
    public List<OPeaklist> getSelectedPanelPeaks(){
    	if(isLoadingSelected()) {
    		return getLoadingPeaklists();
    	}
    	if(isDBSelected()) {
    		return panelSpectraPeaklistFilesDB.defaultTableModel.getAllPeaklists();
    	}
    	
    	return null;
    }
    
    public OPeaklist getSelectedPanelPeakById(String id) {
    	List<OPeaklist> peaklists = getSelectedPanelPeaks();
    	for(OPeaklist p : peaklists ) {
    		if(p.spectrumid.equals(id)) return p;
    	}
    	return null;
    }
    public void updateVisibleColums() {
    	panelSpectraPeaklistFiles.setVisibleColumns(
        		checkBoxMenuItemShowMSName.isSelected(),
        		checkBoxMenuItemShowMSSpectrumID.isSelected(),
        		checkBoxMenuItemShowMSSpecies.isSelected(),
        		checkBoxMenuItemShowMSStrain.isSelected(),
        		checkBoxMenuItemShowFilePath.isSelected()
        );
    	panelSpectraPeaklistFilesSE.setVisibleColumns(
        		checkBoxMenuItemShowMSName.isSelected(),
        		checkBoxMenuItemShowMSSpectrumID.isSelected(),
        		checkBoxMenuItemShowMSSpecies.isSelected(),
        		checkBoxMenuItemShowMSStrain.isSelected(),
        		checkBoxMenuItemShowFilePath.isSelected()
        );
        panelSpectraPeaklistFilesDB.setVisibleColumns(
        		checkBoxMenuItemShowMSName.isSelected(),
        		checkBoxMenuItemShowMSSpectrumID.isSelected(),
        		checkBoxMenuItemShowMSSpecies.isSelected(),
        		checkBoxMenuItemShowMSStrain.isSelected(),
        		checkBoxMenuItemShowFilePath.isSelected()
        );
        
        if(isDBSelected()) {
        	panelSpectraPeaklistFilesDB.setMarkersVisibility(false);
    	}
    }

    public void updatePeaklistInformationPanel(List<OPeaklist> peaklists){
        panelPeaklistInformations.removeAll();
        for (OPeaklist peaklist : peaklists)
        {
            panelPeaklistInformations.add(peaklist.getPeaklistInfo());
            //
            //Break to show only the first selected
            //break;
        }
    }
    
}
