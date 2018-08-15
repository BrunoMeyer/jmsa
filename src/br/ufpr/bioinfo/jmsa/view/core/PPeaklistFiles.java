package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class PPeaklistFiles extends JPanel
{
    public JScrollPane scrollPanePeaklistFiles = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public PeaklistFilesTableModel defaultTableModel = new PeaklistFilesTableModel();
    public ArrayList<TableColumn> removedColumns = new ArrayList<TableColumn>();
    public JTable table = new JTable(defaultTableModel);
    public JLabel labelStatusBarPeaklistFiles = new JLabel();
    public boolean showMarkers = true;
    public FMainWindow fmain;
    public boolean globalTrigger = true;
    
    
    public PPeaklistFiles(String title, FMainWindow fmain)
    {
    	this.fmain = fmain;
        setLayout(new BorderLayout(2, 2));
        //
        //
        add(scrollPanePeaklistFiles, BorderLayout.CENTER);
        add(labelStatusBarPeaklistFiles, BorderLayout.SOUTH);
        scrollPanePeaklistFiles.setViewportView(table);
        //
        //
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(String.class, new PeaklistFilesTableStringCellRenderer());
        //
        //
        defaultTableModel.addTableModelListener(new TableModelListener()
        {
            @Override
            public void tableChanged(TableModelEvent e)
            {
                int prevTab = FMainWindow.getInstance().tabbedPaneMain.getSelectedIndex();
                FMainWindow.getInstance().tabbedPaneMain.setSelectedIndex(-1);
                FMainWindow.getInstance().tabbedPaneMain.setSelectedIndex(prevTab);
                //
                prevTab = FMainWindow.getInstance().tabbedPanePeaklist.getSelectedIndex();
                FMainWindow.getInstance().tabbedPanePeaklist.setSelectedIndex(-1);
                FMainWindow.getInstance().tabbedPanePeaklist.setSelectedIndex(prevTab);
                //
                labelStatusBarPeaklistFiles.setText(defaultTableModel.getSelectedPeaklists().size() + "/" + defaultTableModel.getRowCount());
            }
        });
        //
        //
        scrollPanePeaklistFiles.setMinimumSize(new Dimension(200, 0));
        scrollPanePeaklistFiles.setPreferredSize(new Dimension(200, 0));
        
        
    }
    
    public PPeaklistFiles clone() {
    	PPeaklistFiles newCopie = new PPeaklistFiles("DB Manager",this.fmain);
    	List<OPeaklist> peaklists = fmain.panelLoadingPeaklistFiles.defaultTableModel.getAllPeaklists();
    	for (OPeaklist peaklist : peaklists){
    		try {
				OPeaklist copiePeaklist = new OPeaklist(peaklist.peaklistFile);
				newCopie.addPeaklistToTable(copiePeaklist);
    		} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
        }
    	
    	return newCopie;
    }
    
    public void setGlobalTrigger(boolean isTrigger) {
    	this.globalTrigger = isTrigger;
    	this.defaultTableModel = new PeaklistFilesTableModel(isTrigger);
    }
    
    public void clearTable()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    defaultTableModel.clear();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void addPeaklistToTable(final OPeaklist peaklist)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    defaultTableModel.addRow(peaklist);
                    defaultTableModel.fireTableDataChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void removePeaklistFromTable(final OPeaklist peaklist) {
    	SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    defaultTableModel.removeRow(peaklist);
                    if(globalTrigger) defaultTableModel.fireTableDataChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    	
    }
    
    
    
    public void setVisibleColumns(boolean showName, boolean showSpectrumID, boolean showSpecies, boolean showStrain)
    {
        Boolean[] columnsToShow = new Boolean[] { this.showMarkers, this.showMarkers, showName, showSpectrumID, showSpecies, showStrain };
        //
        
        TableColumnModel tableColumnModel = table.getColumnModel();
        while (tableColumnModel.getColumnCount() > 0)
        {
            TableColumn tableColumn = tableColumnModel.getColumn(0);
            removedColumns.add(tableColumn);
            tableColumnModel.removeColumn(tableColumn);
        }
        //
        for (int columnsIndex = 0; columnsIndex < columnsToShow.length; columnsIndex++)
        {
            if (columnsToShow[columnsIndex])
            {
                for (int removedColumnsIndex = 0; removedColumnsIndex < removedColumns.size(); removedColumnsIndex++)
                {
                    if (removedColumns.get(removedColumnsIndex).getHeaderValue().equals(defaultTableModel.columnNames[columnsIndex]))
                    {
                        TableColumn tableColumn = removedColumns.get(removedColumnsIndex);
                        table.getColumnModel().addColumn(tableColumn);
                        removedColumns.remove(removedColumnsIndex);
                    }
                }
            }
        }
    }
    
    public void setMarkersVisibility(boolean value)
    {
    	this.showMarkers = value;
    	this.setVisibleColumns( 
    			fmain.checkBoxMenuItemShowMSName.isSelected(),
    			fmain.checkBoxMenuItemShowMSSpectrumID.isSelected(),
    			fmain.checkBoxMenuItemShowMSSpecies.isSelected(),
    			fmain.checkBoxMenuItemShowMSStrain.isSelected()
    	);
    }
}

class PeaklistFilesTableStringCellRenderer extends DefaultTableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        PeaklistFilesTableModel model = (PeaklistFilesTableModel) table.getModel();
        OPeaklist peaklist = model.getPeaklistAt(row);
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setForeground(peaklist.spectrumForegroundColor);
        return c;
    }
}
