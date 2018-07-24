package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.sun.corba.se.spi.orbutil.fsm.Action;

import br.ufpr.bioinfo.jmsa.analyser.CPeaklistAnalyser;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class PPeaklistClassifier extends JPanel
{
    public JScrollPane scrollPanePeaklistFiles = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public DefaultTableModel defaultTableModel = new DefaultTableModel();
    public JTable table = new JTable(defaultTableModel);
    public JPanel myContent = new JPanel();
    public JPanel spectreVisualizer = new JPanel();
    
    public JLabel selectedPeaksNumber = null;
    private JButton buttonClassify = new JButton("Classify");
    //
    private List<OPeaklist> peaklists;
    private List<OPeaklist> loadingPeakLists;
    JComboBox selectField;
    
    public List<OPeaklist> loadingPeaks;
    public List<OPeaklist> DBPeaks;
    public FMainWindow fmain;
    private NameNumber[] distances;
    
    public PPeaklistClassifier(FMainWindow fmain)
    {
    	this.fmain = fmain;
    	myContent.setLayout((LayoutManager) new BoxLayout(myContent, BoxLayout.Y_AXIS));
        //
    	
    	setLayout(new BorderLayout(2, 2));
        //
    	table.setVisible(true);
    	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        //
        scrollPanePeaklistFiles.setMinimumSize(new Dimension(200, 0));
        scrollPanePeaklistFiles.setPreferredSize(new Dimension(200, 0));
        
        //
        myContent.add(spectreVisualizer);
        
        buttonClassify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	selectPeak();
            }
        });
    }
    
    public void reloadSpectresVisualizer() {
    	int selected = selectField.getSelectedIndex();
    	OPeaklist peaklistSelected = loadingPeakLists.get(selected);
    	selectedPeaksNumber.setText(peaklistSelected.peaks.size()+" peaks");
    	
        spectreVisualizer.removeAll();
        
        spectreVisualizer.add(peaklistSelected.getPeaklistPlot(false));//checkBoxMenuItemPlotEnableIntensity.isSelected())
        spectreVisualizer.add(peaklistSelected.getPeaklistTable());
        revalidate();
        
        
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
            distances[i] = new NameNumber(peaklistRow.toString(),similarity, peaklistRow, peaklistRow.peaks.size(), matchpeaks);
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
    
    
    public void reloadClassifier(List<OPeaklist> newLoadingPeakLists, List<OPeaklist> newPeaklists){
    	
    	if(newPeaklists.size() <= 0)
    		return;
    	
    	if(newPeaklists != null) {
    		peaklists = newPeaklists;
    		loadingPeakLists = newLoadingPeakLists;
    	}
    	
    	myContent.remove(scrollPanePeaklistFiles);
    	myContent.removeAll();
    	
    	
    	add(scrollPanePeaklistFiles, BorderLayout.CENTER);
    	
        
        
    	
        NameNumber[] distances = new NameNumber[peaklists.size()];

        String[] names  = new String[loadingPeakLists.size()];
        for (int i = 0; i < loadingPeakLists.size(); i++)
        {
        	names[i] = loadingPeakLists.get(i).toString();
        }
        
        
        selectField = new JComboBox(names);
    	
    	selectField.setVisible(true);
    	
    	selectField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	reloadSpectresVisualizer();
            }
        });

    	
    	
    	JPanel panel = new JPanel();
    	panel.add(selectField);
    	
    	
    	myContent.add(panel);
    	myContent.add(spectreVisualizer);
    	myContent.add(table);
    	
    	scrollPanePeaklistFiles.setViewportView(myContent);
    	
    	table.getTableHeader().setDefaultRenderer(new PeaklistSimilarityTableStringCellRenderer(peaklists, table.getTableHeader().getDefaultRenderer()));
        
    	
    	
    	selectedPeaksNumber = new JLabel("");
    	

    	panel.add(selectedPeaksNumber);
    	panel.add(buttonClassify);
    	
    	reloadSpectresVisualizer();
    	
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

                defaultTableModel.setValueAt(peaklistRow.peaks.size()+" peaks", row, col + 2);
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
