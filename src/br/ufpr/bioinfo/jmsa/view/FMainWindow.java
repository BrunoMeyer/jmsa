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
    public JTabbedPane tabbedPaneFiles = new JTabbedPane(JTabbedPane.TOP);
    public JTabbedPane tabbedPaneMain = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    public JTabbedPane tabbedPanePeaklist = new JTabbedPane(JTabbedPane.TOP);
    public PPeaklistFiles panelLoadingPeaklistFiles = new PPeaklistFiles("Loading", this);
    public PPeaklistFiles panelLoadingPeaklistFilesDB = new PPeaklistFiles("Data Base", this);
    public JPanel panelPeaklist = new JPanel();
    public JPanel panelPeaklistTablesPlots = new JPanel();
    public JPanel panelPeaklistTables = new JPanel();
    public JPanel panelPeaklistPlots = new JPanel();
    public JPanel panelPeaklistProteins = new JPanel();
    public JPanel panelPeaklistGroups = new JPanel();
    public JPanel panelPeaklistSimilarity = new JPanel();
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
    public JMenu menuHelp = new JMenu("Help");
    public JMenuItem menuItemLoadFiles = new JMenuItem("Load Files");
    public JMenuItem menuItemConfig = new JMenuItem("Configuration");
    public JMenuItem menuItemExit = new JMenuItem("Exit");
    public JMenuItem menuItemAbout = new JMenuItem("About");
    public JCheckBoxMenuItem checkBoxMenuItemShowMSName = new JCheckBoxMenuItem("Show Name", CConfig.getInstance().showMSName);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSSpectrumID = new JCheckBoxMenuItem("Show SpectrumID", CConfig.getInstance().showMSSpectrumID);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSSpecies = new JCheckBoxMenuItem("Show Species", CConfig.getInstance().showMSSpecies);
    public JCheckBoxMenuItem checkBoxMenuItemShowMSStrain = new JCheckBoxMenuItem("Show Strain", CConfig.getInstance().showMSStrain);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleName = new JCheckBoxMenuItem("Name", CConfig.getInstance().plotTitleName);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleSpectrumID = new JCheckBoxMenuItem("SpectrumID", CConfig.getInstance().plotTitleSpectrumID);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleSpecies = new JCheckBoxMenuItem("Species", CConfig.getInstance().plotTitleSpecies);
    public JCheckBoxMenuItem checkBoxMenuItemPlotTitleStrain = new JCheckBoxMenuItem("Strain", CConfig.getInstance().plotTitleStrain);
    public JCheckBoxMenuItem checkBoxMenuItemPlotEnableIntensity = new JCheckBoxMenuItem("Enable Intensity");
    
    public JToolBar toolBar = new JToolBar("Tools");
    public JLabel labelStatusBar = new JLabel("JMSA");
    
    public JButton buttonLoadFiles = new JButton("Load");
    public JButton buttonSaveDB = new JButton("Save to ZIP");
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
        panelPeaklistTablesPlots.setLayout(new BoxLayout(panelPeaklistTablesPlots, BoxLayout.Y_AXIS));
        panelPeaklistTables.setLayout(new BoxLayout(panelPeaklistTables, BoxLayout.Y_AXIS));
        panelPeaklistPlots.setLayout(new BoxLayout(panelPeaklistPlots, BoxLayout.Y_AXIS));
        panelPeaklistSimilarity.setLayout(new BoxLayout(panelPeaklistSimilarity, BoxLayout.Y_AXIS));
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
        menuHelp.add(menuItemAbout);
        menuShowColumns.add(checkBoxMenuItemShowMSName);
        menuShowColumns.add(checkBoxMenuItemShowMSSpectrumID);
        menuShowColumns.add(checkBoxMenuItemShowMSSpecies);
        menuShowColumns.add(checkBoxMenuItemShowMSStrain);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleName);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleSpectrumID);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleSpecies);
        menuPlotTitle.add(checkBoxMenuItemPlotTitleStrain);
        menuPlotTitle.add(checkBoxMenuItemPlotEnableIntensity);
        //
        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(panelStatusBar, BorderLayout.SOUTH);
        toolBar.add(buttonLoadFiles);
        toolBar.add(buttonSaveDB);
        toolBar.add(buttonLoadDB);
        toolBar.add(buttonPeaklist);
        toolBar.add(buttonAnalyser);
        toolBar.add(buttonCluster);
        toolBar.add(buttonClassifier);
        toolBar.add(buttonDBManager);
        toolBar.add(buttonInformation);
        toolBar.add(buttonSelectAll);
        toolBar.add(buttonDeselectAll);
        splitPane.add(tabbedPaneFiles);
        splitPane.add(tabbedPaneMain);
        panelStatusBar.add(labelStatusBar, BorderLayout.CENTER);
        tabbedPaneFiles.addTab("Loading", panelLoadingPeaklistFiles);
        tabbedPaneFiles.addTab("Data Base", panelLoadingPeaklistFilesDB);
        
        tabbedPaneMain.addTab("Peaklist", panelPeaklist);
        tabbedPaneMain.addTab("Analyser", superPeaklistPlot);
        tabbedPaneMain.addTab("Cluster", panelCluster);
        tabbedPaneMain.addTab("DB search", panelClassifier);
        tabbedPaneMain.addTab("DB Manager", panelDBManager);
        tabbedPaneMain.addTab("Information", panelInformation);
        panelPeaklist.add(tabbedPanePeaklist, BorderLayout.CENTER);
        
        tabbedPanePeaklist.addTab("Tables / Plots", scrollPeaklistTablesPlots);
        tabbedPanePeaklist.addTab("Tables", scrollPeaklistTables);
        tabbedPanePeaklist.addTab("Plots", scrollPeaklistPlots);
        
        tabbedPanePeaklist.addTab("Proteins", panelPeaklistProteins);
        tabbedPanePeaklist.addTab("Groups", panelPeaklistGroups);
        tabbedPanePeaklist.addTab("Similarity", panelPeaklistSimilarity);
        //tabbedPanePeaklist.addTab("Cluster", panelPeaklistCluster);
     
        tabbedPanePeaklist.addTab("Informations", panelPeaklistInformations);
        scrollPeaklistTablesPlots.setViewportView(panelPeaklistTablesPlots);
        
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
        tabbedPaneMain.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
            	try
                {
            		if(lockUpdatePanels) return;
            		List<OPeaklist> dbpeaklists = panelLoadingPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
	            	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
	               
	            	if(tabbedPaneMain.getSelectedComponent() == superPeaklistPlot) {
            			superPeaklistPlot.buildPlot(
    	                	peaklists,
    	                	checkBoxMenuItemPlotEnableIntensity.isSelected()
    	                );
	            	}
	            	if(tabbedPaneMain.getSelectedComponent() == panelCluster) {
	            		panelCluster.reloadDendrogram(peaklists);
	            	}
	            	if(tabbedPaneMain.getSelectedComponent() == panelDBManager) {
	            		panelDBManager.reloadClassifier(peaklists);
	            	}
	            	if(tabbedPaneMain.getSelectedComponent() == panelClassifier) {
	            		dbpeaklists.addAll(peaklists);
	            		panelClassifier.reloadClassifier(peaklists,dbpeaklists);
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
                List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
                
                
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
                else if (tabbedPanePeaklist.getSelectedComponent() == panelPeaklistInformations)
                {
                    panelPeaklistInformations.removeAll();
                    for (OPeaklist peaklist : peaklists)
                    {
                        panelPeaklistInformations.add(peaklist.getPeaklistInfo());
                        //
                        //Break to show only the first selected
                        break;
                    }
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
            	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
            	List<OPeaklist> dbpeaklists = panelLoadingPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
            	List<OPeaklist> allpeaklists = panelLoadingPeaklistFiles.defaultTableModel.getAllPeaklists();
            	JFileChooser chooserSave;
            	int retrival;
            	FileNameExtensionFilter filter;

            	switch (e.getActionCommand())
                {
                    case "ShowMSName":
                    case "ShowMSSpectrumID":
                    case "ShowMSSpecies":
                    case "ShowMSStrain":
                    case "PlotTitleName":
                    case "PlotTitleSpectrumID":
                    case "PlotTitleSpecies":
                    case "PlotTitleStrain":
                        CConfig.getInstance().showMSName = checkBoxMenuItemShowMSName.isSelected();
                        CConfig.getInstance().showMSSpectrumID = checkBoxMenuItemShowMSSpectrumID.isSelected();
                        CConfig.getInstance().showMSSpecies = checkBoxMenuItemShowMSSpecies.isSelected();
                        CConfig.getInstance().showMSStrain = checkBoxMenuItemShowMSStrain.isSelected();
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
                    case "savedb":
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

                            	saveDB(file_name_to_save);
                            } catch (Exception err){
                        		err.printStackTrace();
                            }
                        }  
                        break;
                    case "loaddb":

                    	// Open the file selector interface
                    	chooserSave = new JFileChooser();
                    	chooserSave.setCurrentDirectory(
                    		new File(CConfig.getInstance().loadingPath)
                    	);
                    	filter = new FileNameExtensionFilter(null, "zip");
                    	chooserSave.setFileFilter(filter);
                    	
                    	if(lockUpdatePanels) break;
                        
                        lockUpdatePanels = true;
                        if (chooserSave.showOpenDialog(FMainWindow.this) == JFileChooser.APPROVE_OPTION) {
                        	// When the user choose a file name and directory to save
                            try {
                            	// Get the name of file choosed by user
                            	String file_name_to_save = chooserSave.getSelectedFile().toString();

                            	loadDB(file_name_to_save);
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
                    	dbpeaklists.addAll(peaklists);
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
                    	
                    	lockUpdatePanels = true;
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
                    		panelLoadingPeaklistFiles.defaultTableModel.setAllValuesAt(true, 0);
                    	}
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
                    		panelLoadingPeaklistFilesDB.defaultTableModel.setAllValuesAt(true, 0);
                    	}
                    	lockUpdatePanels = false;
                    	firePeaklistPanelChange();
                    	break;
                    case "deselect-all":
                    	lockUpdatePanels = true;
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
                    		panelLoadingPeaklistFiles.defaultTableModel.setAllValuesAt(false, 0);
                    	}
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
                    		panelLoadingPeaklistFilesDB.defaultTableModel.setAllValuesAt(false, 0);
                    	}
                    	lockUpdatePanels = false;
                    	firePeaklistPanelChange();
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
        //
        checkBoxMenuItemShowMSName.addActionListener(actionListener);
        checkBoxMenuItemShowMSSpectrumID.addActionListener(actionListener);
        checkBoxMenuItemShowMSSpecies.addActionListener(actionListener);
        checkBoxMenuItemShowMSStrain.addActionListener(actionListener);
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
        //
        menuItemLoadFiles.setActionCommand("loadpeakfiles");
        menuItemConfig.setActionCommand("config");
        menuItemExit.setActionCommand("exit");
        menuItemAbout.setActionCommand("about");
        buttonLoadFiles.setActionCommand("loadpeakfiles");
        buttonSaveDB.setActionCommand("savedb");
        buttonLoadDB.setActionCommand("loaddb");
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
        buttonSaveDB.addActionListener(actionListener);
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
        tabbedPaneMain.setEnabledAt(tabbedPaneMain.indexOfComponent(panelDBManager), true);
        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistProteins), false);
        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistGroups), false);
        
        //        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistSimilarity), false);
        //        tabbedPanePeaklist.setEnabledAt(tabbedPanePeaklist.indexOfComponent(panelPeaklistInformations), false);
    }
    public void firePeaklistPanelChange() {
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
    		panelLoadingPeaklistFiles.defaultTableModel.fireTableDataChanged();
    	}
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
    		panelLoadingPeaklistFilesDB.defaultTableModel.fireTableDataChanged();
    	}
    }
    
    public void clearTable()
    {
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
    		panelLoadingPeaklistFiles.clearTable();
    	}
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
    		panelLoadingPeaklistFilesDB.clearTable();
    	}
        
    }
    
    public void addPeaklistToTable(final OPeaklist peaklist)
    {
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
    		panelLoadingPeaklistFiles.addPeaklistToTable(peaklist);
    	}
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
    		panelLoadingPeaklistFilesDB.addPeaklistToTable(peaklist);
    		peaklist.selected = true;
    	}
//    	updateVisibleColums();
//    	panelLoadingPeaklistFilesDB.defaultTableModel.fireTableDataChanged();
    }
    
    public void addPeaklistToLoadingTable(final OPeaklist peaklist)
    {
    	panelLoadingPeaklistFiles.addPeaklistToTable(peaklist);
    }
    
    public void removePeaklistFromDBTable(final OPeaklist peaklist)
    {
    	panelLoadingPeaklistFilesDB.removePeaklistFromTable(peaklist);
    }
    
    public boolean isInDB(final OPeaklist peaklist) {
    	List<OPeaklist> peaklists = panelLoadingPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
    }
    
    public boolean isInLoad(final OPeaklist peaklist) {
    	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
    }
    
    public List<OPeaklist> getSelectedPanelPeaks(){
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
    		return panelLoadingPeaklistFiles.defaultTableModel.getAllPeaklists();
    	}
    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
    		return panelLoadingPeaklistFilesDB.defaultTableModel.getAllPeaklists();
    	}
    	
    	return null;
    }
    
    public OPeaklist getSelectedPanelPeakById(String id) {
    	List<OPeaklist> peaklists = this.getSelectedPanelPeaks();
    	for(OPeaklist p : peaklists ) {
    		if(p.toString().equals(id)) return p;
    	}
    	return null;
    }
    public void updateVisibleColums() {
    	panelLoadingPeaklistFiles.setVisibleColumns(
        		checkBoxMenuItemShowMSName.isSelected(),
        		checkBoxMenuItemShowMSSpectrumID.isSelected(),
        		checkBoxMenuItemShowMSSpecies.isSelected(),
        		checkBoxMenuItemShowMSStrain.isSelected()
        );
        panelLoadingPeaklistFilesDB.setVisibleColumns(
        		checkBoxMenuItemShowMSName.isSelected(),
        		checkBoxMenuItemShowMSSpectrumID.isSelected(),
        		checkBoxMenuItemShowMSSpecies.isSelected(),
        		checkBoxMenuItemShowMSStrain.isSelected()
        );
        
        if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
        	panelLoadingPeaklistFilesDB.setMarkersVisibility(false);
    	}
    }
    
    
    public void saveDB(String path_to_save) {
    	// IMPORTANT: Any modification in this method
    	// can cause an incompatibility of versions on this program
    	// Please, do not change the file names created
    	
    	try {
	    	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
	    	
	    	String zipFile = path_to_save;
			
			// Instance a zip file
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			
			// Instance variables that will be used for create an zip file
			// that represent the database 
			byte[] buffer = new byte[1024];
			FileInputStream in;
			ZipEntry ze;
			int len;
			
			// An json file named peaklistJMSA.json will be created
			// and that contains the listing of peaklists files and superespectres
			
			// Array on format like: [
			//	{"id":"idspectre1", "path":"files/idspectre1.xml"},
			//	...
			// ]
			
			JSONArray peaklists_json_array = new JSONArray();
			
			// Array on format like: [
			// 	{"id":"SE-superspectre1", "peaklists_ids":["superspectre1","superspectre2", ...]},
			// 	...
			// ]
			JSONArray superpeaks_json_array = new JSONArray();
			
			// For each peaklist selected, save it on "files/peaklistid.xml" inside zip file
			// If the peaklist instance was an superspectre, just add it on json file
			for (OPeaklist peaklist : peaklists){
				if(peaklist instanceof SuperPeaklist) {
					SuperPeaklist sp = (SuperPeaklist) peaklist;
					JSONObject super_peak_obj = new JSONObject();
					JSONArray sp_peaklists_json_array = new JSONArray();
					
					for (OPeaklist sp_peaklist : sp.peaklists){
						sp_peaklists_json_array.add(sp_peaklist.toString());
					}
					super_peak_obj.put("id", sp.toString());
					super_peak_obj.put("peaklists_ids", sp_peaklists_json_array);
					super_peak_obj.put("distance_merge_peak", sp.distance_merge_peak);
					superpeaks_json_array.add(super_peak_obj);
				}
				else {
	    			String file_name = peaklist.peaklistFile.toString();
	    			String file_path = 
	    					"files"+File.separator+peaklist.toString()+File.separator+"peaklist.xml";
	    			ze = new ZipEntry(file_path);
	    			zos.putNextEntry(ze);
	    			
	    			JSONObject peak_obj = new JSONObject();
	        		peak_obj.put("id", peaklist.toString());
	        		peak_obj.put("path", file_path);
	        		peaklists_json_array.add(peak_obj);
	    			in = new FileInputStream(file_name);
	    			
	    			// This is needed to create the zip file
	    			// The size of buffer defined before can be a future problem
	    			while ((len = in.read(buffer)) > 0) {
	    				zos.write(buffer, 0, len);
	    			}
	    			in.close();
				}
			}
			
	
			// Create an temporary file for json
			// This can be an problem on windows or different OS systems
			String tempFileName = "peaklistJMSA.json";
			File tempFile = File.createTempFile(tempFileName+".", ".temp");
			                    		
			JSONObject obj = new JSONObject();
			obj.put("peaklists", peaklists_json_array);
			obj.put("super_peaklists", superpeaks_json_array);
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
	    	    bw.write(obj.toJSONString());
	    	    bw.close();
			} catch(IOException err){
				err.printStackTrace();
			}
			
			// Finally, save the json inside zip file
			ze = new ZipEntry(tempFileName);
			zos.putNextEntry(ze);
			in = new FileInputStream(tempFile.getAbsoluteFile());
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			in.close();
			
			obj.put("peaklists", peaklists_json_array);
			tempFile.deleteOnExit();
			
			zos.closeEntry();
			zos.close();
    	} catch (Exception err){
    		err.printStackTrace();
        }
    }
    public static File createTempDirectory() throws IOException {
	    final File temp;

	    temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

	    if(!(temp.delete()))
	    {
	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
	    }

	    if(!(temp.mkdir()))
	    {
	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
	    }

	    return (temp);
	}

    public void loadDB(String zipFilePath) throws IOException, ParseException {
    	
    	String tempDirName = "jmsa_temp_load";
		File dir = createTempDirectory();
		String destDir = dir.getAbsolutePath();
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        
        JSONParser parser = new JSONParser();
        
        JSONObject peaklistJSON;

        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            JSONArray super_peaks_file = null;
            
            while(ze != null){
                String fileName = ze.getName();
                
                File newFile = new File(destDir + File.separator + fileName);
                
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
                if(fileName.equals("peaklistJMSA.json")) {
                	Object obj = parser.parse(new FileReader(newFile.getAbsolutePath()));

                    JSONObject json_object = (JSONObject) obj;
                    super_peaks_file = (JSONArray) json_object.get("super_peaklists");
                }

                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            final JSONArray super_peaks = super_peaks_file;
            OUserActionLoadPeakFiles peaksLoader = new OUserActionLoadPeakFiles(
        		new File[] {dir}
            );
            peaksLoader.executarEvento();
            updateVisibleColums();
            
            
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                	if(super_peaks != null) {
        	            Iterator<JSONObject> iterator_super_spectres = super_peaks.iterator();
        	            while (iterator_super_spectres.hasNext()) {
        	            	JSONObject sp = iterator_super_spectres.next();
        	            	JSONArray peaklists_id = (JSONArray) sp.get("peaklists_ids");
        	            	String sp_id = (String) sp.get("id");
        	            	
        	            	int distance_merge_peak = (int) (long) sp.get("distance_merge_peak");
        	            	
        		            Iterator<String> iterator_peaklists_id_ = peaklists_id.iterator();
        		            List<OPeaklist> peaklists = new ArrayList();
        		            while (iterator_peaklists_id_.hasNext()) {
        		            	String p_id = iterator_peaklists_id_.next();
        		            	OPeaklist p = FMainWindow.getInstance().getSelectedPanelPeakById(
        		            		p_id
        		            	);
        		            	
        		            	peaklists.add(p);
        		            }
        		            
        		            try {
        						SuperPeaklist merged = new SuperPeaklist((ArrayList)peaklists);
        						merged.spectrumid = sp_id;
        						merged.setDistanceMergePeak(distance_merge_peak);
        						FMainWindow.getInstance().addPeaklistToTable(merged);
        					} catch (ParserConfigurationException | SAXException | IOException e1) {
        						// TODO Auto-generated catch block
        						e1.printStackTrace();
        					}
        	            }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
