package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import br.ufpr.bioinfo.jmsa.analyser.CPeaklistAnalyser;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PPeaklistSimilarity extends JPanel
{
    public JScrollPane scrollPanePeaklistFiles = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public DefaultTableModel defaultTableModel = new DefaultTableModel();
    public JTable table = new JTable(defaultTableModel);
    //
    private List<OPeaklist> peaklists;
    
    public PPeaklistSimilarity(List<OPeaklist> peaklists)
    {
        this.peaklists = peaklists;
        //
        setLayout(new BorderLayout(2, 2));
        //
        add(scrollPanePeaklistFiles, BorderLayout.CENTER);
        scrollPanePeaklistFiles.setViewportView(table);
        //
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new PeaklistSimilarityTableStringCellRenderer(peaklists));
        table.getTableHeader().setReorderingAllowed(false);
        //
        scrollPanePeaklistFiles.setMinimumSize(new Dimension(200, 0));
        scrollPanePeaklistFiles.setPreferredSize(new Dimension(200, 0));
        //
        fillTable();
    }
    
    public void fillTable()
    {
        defaultTableModel.setRowCount(peaklists.size());
        defaultTableModel.setColumnCount(0);
        //
        defaultTableModel.addColumn("", new Object[] {});
        
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
    
    public String csvTable(){
    	String tableCsv = ",";
    	
    	for (int col = 0; col < peaklists.size(); col++){
    		tableCsv += "\"";
    		tableCsv+=(peaklists.get(col).toString());
    		//tableCsv+=(peaklists.get(col).jmsainfoName);
    		tableCsv+="\"";
    		if(col != peaklists.size()-1) {
    			tableCsv+=",";
    		}
    	}
    	tableCsv+="\n";
    	
    	for (int col = 0; col < peaklists.size(); col++)
        {
    		tableCsv += "\"";
    		tableCsv+=(peaklists.get(col).toString());
    		//tableCsv+=(peaklists.get(col).jmsainfoName);
    		tableCsv+="\"";
    		OPeaklist peaklistCol = peaklists.get(col);
            for (int row = 0; row < peaklists.size(); row++)
            {
                OPeaklist peaklistRow = peaklists.get(row);
                double similarity = CPeaklistAnalyser.getPeakistSimilarity(peaklistCol, peaklistRow);
                tableCsv+=","+(similarity*100);
            }
    		tableCsv+="\n";
        }
    	
    	return tableCsv;
    }
}

class PeaklistSimilarityTableStringCellRenderer implements TableCellRenderer
{
    private TableCellRenderer defaultRenderer;
    private List<OPeaklist> peaklists;
    public DecimalFormat decimalFormat = new DecimalFormat("#.########%");
    
    public PeaklistSimilarityTableStringCellRenderer(List<OPeaklist> peaklists)
    {
        this.peaklists = peaklists;
        this.defaultRenderer = new DefaultTableCellRenderer();
    }
    
    public PeaklistSimilarityTableStringCellRenderer(List<OPeaklist> peaklists, TableCellRenderer defaultRenderer)
    {
        this.peaklists = peaklists;
        this.defaultRenderer = defaultRenderer;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value == null) return c;
        
        OPeaklist peaklist = null;
        if (value.getClass() == OPeaklist.class)
        {
            peaklist = (OPeaklist) value;
        }
        else if ((column == 0) && (row >= 0 && row < peaklists.size()))
        {
            peaklist = peaklists.get(row);
        }
        else if ((column > 0) && (row < 0 && row < peaklists.size()))
        {
            peaklist = peaklists.get(column - 1);
        }
        //
        if (peaklist != null)
        {
            c.setForeground(peaklist.spectrumForegroundColor);
        }
        else
        {
            if (value.getClass() == Double.class)
            {
                ((JLabel) c).setText(decimalFormat.format(value));
            }
            c.setForeground(table.getForeground());
        }
        return c;
    }
    
}
