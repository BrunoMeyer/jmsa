package br.ufpr.bioinfo.jmsa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufpr.bioinfo.jmsa.control.CConfig;
import br.ufpr.bioinfo.jmsa.control.CControl;
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
            		List<OPeaklist> dbpeaklists = panelLoadingPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
	            	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
	               
	            	superPeaklistPlot.buildPlot(
	                	peaklists,
	                	checkBoxMenuItemPlotEnableIntensity.isSelected()
	                );
	                panelCluster.reloadDendrogram(peaklists);
	                
	                panelDBManager.reloadClassifier(peaklists);
//	                panelDBManager.superSpectro.buildPlot(
//		                peaklists,
//		                checkBoxMenuItemPlotEnableIntensity.isSelected()
//		            );
	                
	                
	                List<OPeaklist> mergeDbLoad = new ArrayList<>(dbpeaklists);
	                for(OPeaklist pk : peaklists) {
	                	if( !isInDB(pk) ) {
	                		mergeDbLoad.add(pk);
	                	}
	                }
	                
                    panelClassifier.reloadClassifier(peaklists,mergeDbLoad);
                    
                    
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
                        if (chooser.showOpenDialog(FMainWindow.this) == JFileChooser.APPROVE_OPTION)
                        {
                            CControl.getInstance().threadUserActionsPool.addEvento(new OUserActionLoadPeakFiles(chooser.getSelectedFiles()));
                        }
                        
                        updateVisibleColums();
                        
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
                    	dbpeaklists.addAll(peaklists);
                    	tabbedPaneMain.setSelectedComponent(panelClassifier);
                        panelClassifier.removeAll();
                        //panelClassifier.fillTable(peaklists);
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
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
                    		panelLoadingPeaklistFiles.defaultTableModel.setAllValuesAt(true, 0);
                    	}
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
                    		panelLoadingPeaklistFilesDB.defaultTableModel.setAllValuesAt(true, 0);
                    	}
                    	
                    	break;
                    case "deselect-all":
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFiles) {
                    		panelLoadingPeaklistFiles.defaultTableModel.setAllValuesAt(false, 0);
                    	}
                    	if(tabbedPaneFiles.getSelectedComponent() == panelLoadingPeaklistFilesDB) {
                    		panelLoadingPeaklistFilesDB.defaultTableModel.setAllValuesAt(false, 0);
                    	}
                    	break;
                    
                    case "export-csv":
                    	String tableText = (similarityMatrix.csvTable());
                    	JFileChooser chooserSave = new JFileChooser();
                    	chooserSave.setCurrentDirectory(new File("/home/me/Documents"));
                        int retrival = chooserSave.showSaveDialog(null);
                        if (retrival == JFileChooser.APPROVE_OPTION) {
                            try {
                            	CControl.getInstance().threadUserActionsPool.addEvento(new OUserActionLoadPeakFiles(chooserSave.getSelectedFiles()));
                                FileWriter fw = new FileWriter(chooserSave.getSelectedFile()+".csv");
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
    	
    	updateVisibleColums();
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
    	boolean isIn = false;
    	List<OPeaklist> peaklists = panelLoadingPeaklistFilesDB.defaultTableModel.getSelectedPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
    }
    
    public boolean isInLoad(final OPeaklist peaklist) {
    	boolean isIn = false;
    	List<OPeaklist> peaklists = panelLoadingPeaklistFiles.defaultTableModel.getSelectedPeaklists();
    	for(OPeaklist p : peaklists ) {
    		if(p == peaklist)
    			return true;
    	}
    	
    	return false;
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
    
    
}
