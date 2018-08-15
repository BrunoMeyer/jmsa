package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import br.ufpr.bioinfo.jmsa.analyser.CPeaklistAnalyser;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.SuperPeaklist;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class PPeaklistDBManager extends JPanel
{
    public JScrollPane scrollPanePeaklistFiles = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public DefaultTableModel defaultTableModel = new DefaultTableModel();
    public JTable table = new JTable(defaultTableModel);
    public JPanel myContent = new JPanel();
    public JPanel spectreVisualizer = new JPanel();
    
    public JLabel selectedPeaksNumber = null;
    private JButton buttonMerge = new JButton("Merge spectres selected");
    //
    private List<OPeaklist> peaklists;
    private List<OPeaklist> loadingPeakLists;
    private JComboBox selectField;
    
    public List<OPeaklist> DBPeaks;
    public FMainWindow fmain;
    private NameNumber[] distances;
    
    public PSuperPeaklistPlot superSpectro = new PSuperPeaklistPlot();
    public PPeaklistDBManager(FMainWindow fmain)
    {
    	this.fmain = fmain;
    	myContent.setLayout((LayoutManager) new BoxLayout(myContent, BoxLayout.Y_AXIS));
        //
    	
    	setLayout(new BorderLayout(1, 1));
    	
    	//
    	table.setVisible(true);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        //
        scrollPanePeaklistFiles.setMinimumSize(new Dimension(200, 0));
        scrollPanePeaklistFiles.setPreferredSize(new Dimension(200, 0));
        
        //
        //myContent.add(spectreVisualizer);
        
        buttonMerge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					SuperPeaklist merged = new SuperPeaklist((ArrayList)peaklists);
					fmain.addPeaklistToTable(merged);
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        
    }
    
    public void reloadSpectresVisualizer() {
    	int selected = selectField.getSelectedIndex();
    	OPeaklist peaklistSelected = loadingPeakLists.get(selected);
    	selectedPeaksNumber.setText(peaklistSelected.getPeaks().size()+" peaks");
    	
        spectreVisualizer.removeAll();
        
        spectreVisualizer.add(peaklistSelected.getPeaklistPlot(false));//checkBoxMenuItemPlotEnableIntensity.isSelected())
        spectreVisualizer.add(peaklistSelected.getPeaklistTable());
        // revalidate();
        
        
    }
    
    private static class JTableButtonRenderer implements TableCellRenderer {        
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;  
        }
    }
    
    
    private class NameNumber 
    {

        public NameNumber(String name, double similarity, OPeaklist pk, int npeaks, int matchpeaks) {
            this.name = name;
            this.similarity = similarity;
            this.pk = pk;
            this.npeaks = npeaks;
            this.matchpeaks = matchpeaks;
        }
        public String getName() {
        	return this.name;
        }
        public double getSimilarity() {
        	return this.similarity;
        }
        public double getNpeaks() {
        	return this.npeaks;
        }
        public int matchpeaks() {
        	return this.matchpeaks;
        }
        
        public String name;
        public double similarity;
        public int npeaks;
        public int matchpeaks;
        public OPeaklist pk;
    }
    
    public void selectPeak() {
    	
    	table.setDefaultRenderer(Object.class, new PeaklistSimilarityTableStringCellRenderer(peaklists));
    	
    	distances = new NameNumber[peaklists.size()];
    	
    	
    	
        int selected = selectField.getSelectedIndex();
        
       
        OPeaklist peaklistSelected = loadingPeakLists.get(selected);
        
        
        defaultTableModel.setRowCount(peaklists.size());
        defaultTableModel.setColumnCount(0);
        defaultTableModel.addColumn("Name", new Object[] {});
        defaultTableModel.addColumn("Similarity", new Object[] {});
        defaultTableModel.addColumn("Number of peaks", new Object[] {});
        defaultTableModel.addColumn("Total Match peaks", new Object[] {});
        defaultTableModel.addColumn("Move to Loading", new Object[] {});
        
        AbstractAction moveToLoad = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
            	int modelRow = Integer.valueOf( e.getActionCommand() );
            	if(fmain.isInDB(distances[modelRow].pk)) {
	                JTable table = (JTable)e.getSource();
	                if( !fmain.isInLoad(distances[modelRow].pk) ) {
		                fmain.addPeaklistToLoadingTable(distances[modelRow].pk);
//		                fmain.removePeaklistFromDBTable(distances[modelRow].pk);
		                ((DefaultTableModel)table.getModel()).setValueAt("", modelRow, 4);
	                }
	                
	              
            	}
            }
        };
        
        
        ButtonColumn buttonColumn = new ButtonColumn(table, moveToLoad, 4);

        
        for (int i = 0; i < peaklists.size(); i++)
        {
        	OPeaklist peaklistRow = peaklists.get(i);
            double similarity = CPeaklistAnalyser.getPeakistSimilarity(peaklistSelected, peaklistRow);
        	int matchpeaks = CPeaklistAnalyser.getMatchPeaksSimilarity(peaklistSelected, peaklistRow);
            distances[i] = new NameNumber(peaklistRow.toString(),similarity, peaklistRow, peaklistRow.getPeaks().size(), matchpeaks);
        }
    	
        Arrays.sort(distances, new Comparator<NameNumber>() {

            @Override
            public int compare(NameNumber o1, NameNumber o2) {
                if (o1.similarity > o2.similarity)
                	return -1;
                return 1;
            }
        });
        
        
        for (int i = 0; i < peaklists.size(); i++)
        {
        	defaultTableModel.setValueAt(distances[i].name, i, 0);
        	defaultTableModel.setValueAt(distances[i].similarity, i, 1);
        	defaultTableModel.setValueAt(distances[i].npeaks+" peaks", i, 2);
        	defaultTableModel.setValueAt(distances[i].matchpeaks+" matched peaks", i, 3);
        	if( !fmain.isInLoad(distances[i].pk) ){
        		defaultTableModel.setValueAt("Move to Loading", i, 4);
        	}
        	else {
        		defaultTableModel.setValueAt("", i, 4);
        	}
        }
        
        
    	table.getTableHeader().setReorderingAllowed(false);
    	
        table.getTableHeader().setDefaultRenderer(new PeaklistSimilarityTableStringCellRenderer(peaklists, table.getTableHeader().getDefaultRenderer()));
        //
        defaultTableModel.fireTableDataChanged();
        
    }
    
   
    public void reloadClassifier(List<OPeaklist> peaklists){
    	this.peaklists = peaklists;
    	PPeaklistFiles newList = fmain.panelLoadingPeaklistFilesDB.clone();
    	newList.setMarkersVisibility(false);
    	newList.setGlobalTrigger(false);
    	newList.defaultTableModel.addTableModelListener(new TableModelListener()
        {
            @Override
            public void tableChanged(TableModelEvent e)
            {
//
//                int rowIndex = newList.table.getSelectedRow();
//                int colIndex = newList.table.getSelectedColumn();
//                
//                if(colIndex == 1) {
//                	OPeaklist peaklist = peaklists.get(rowIndex);
//                	peaklist.reflex = !peaklist.reflex;
//                	superSpectro.buildPlot(
//                			peaklists,
//                			fmain.checkBoxMenuItemPlotEnableIntensity.isSelected()
//                	);
//                	revalidate();
//                }
//        
            }
        });
    	myContent.removeAll();
    	JPanel topPanel = new JPanel();
    	topPanel.setLayout(new GridLayout(1, 2));
    	
//    	JLabel teste = new JLabel("Teste");
    	JPanel topRightPanel = new JPanel();
//    	topRightPanel.add(teste);
    	topRightPanel.add(buttonMerge);
    	
    	topPanel.add(topRightPanel);
    	topPanel.add(newList);
    	this.superSpectro.setLayout(new GridLayout(1, 0));
    	this.superSpectro.buildPlot(
    		peaklists,
    		fmain.checkBoxMenuItemPlotEnableIntensity.isSelected()
    	);
    	
    	
    	myContent.add(topPanel);
    	myContent.add(this.superSpectro);
    	myContent.setLayout(new GridLayout(2, 1));
    	
    	scrollPanePeaklistFiles.setViewportView(myContent);
    	add(scrollPanePeaklistFiles, BorderLayout.CENTER);
    	
    	
        revalidate();
    	
    }
    
    public void fillTable(List<OPeaklist> peaklists)
    {
    	
        defaultTableModel.setRowCount(peaklists.size());
        defaultTableModel.setColumnCount(0);
        //
        defaultTableModel.addColumn("", new Object[] {});
        defaultTableModel.addColumn("Number of peaks", new Object[] {});
        defaultTableModel.addColumn("Total Match peaks", new Object[] {});
        
        for (int col = 0; col < peaklists.size(); col++)
        {
            OPeaklist peaklistCol = peaklists.get(col);
            defaultTableModel.addColumn(peaklistCol, new Object[] {});
            
            for (int row = 0; row < peaklists.size(); row++)
            {
                OPeaklist peaklistRow = peaklists.get(row);
                double similarity = CPeaklistAnalyser.getPeakistSimilarity(peaklistCol, peaklistRow);
                
                defaultTableModel.setValueAt(peaklistRow, row, 0);
                defaultTableModel.setValueAt(similarity, row, col + 1);

                defaultTableModel.setValueAt(peaklistRow.getPeaks().size()+" peaks", row, col + 2);
//                
            }
        }
        //
        table.getTableHeader().setDefaultRenderer(new PeaklistSimilarityTableStringCellRenderer(peaklists, table.getTableHeader().getDefaultRenderer()));
        //
        defaultTableModel.fireTableDataChanged();
        //
        TableColumn column = null;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
        }
        
       
    }
    
}
