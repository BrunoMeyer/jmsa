package br.ufpr.bioinfo.jmsa.view.core;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PeaklistFilesTableModel extends AbstractTableModel
{
    private ArrayList<OPeaklist> peaklists = new ArrayList<OPeaklist>();
    public String[] columnNames = new String[] { "Selected", "Reflex", "Name", "SpectrumID", "Species", "Strain" };
    
    public PeaklistFilesTableModel()
    {
    }
    
    public void addPeaklist(OPeaklist peaklist)
    {
        peaklists.add(peaklist);
        fireTableDataChanged();
    }
    
    public OPeaklist getPeaklistAt(int row)
    {
        return peaklists.get(row);
    }
    
    public ArrayList<OPeaklist> getSelectedPeaklists()
    {
        ArrayList<OPeaklist> selectedPeaklists = new ArrayList<OPeaklist>();
        for (OPeaklist peaklist : peaklists)
        {
            if (peaklist.selected)
            {
                selectedPeaklists.add(peaklist);
            }
        }
        return selectedPeaklists;
    }
    
    public ArrayList<OPeaklist> getAllPeaklists()
    {
        return peaklists;
    }
    
    public void clear()
    {
        peaklists.clear();
        fireTableDataChanged();
    }
    
    public void addRow(OPeaklist peaklist)
    {
        addPeaklist(peaklist);
    }
    
    public void removeRow(OPeaklist peaklist)
    {
        peaklists.remove(peaklist);
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount()
    {
        return peaklists.size();
    }
    
    @Override
    public int getColumnCount()
    {
        return 6;
    }
    
    public String getColumnName(int col)
    {
        return columnNames[col];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return getValueAt(0, columnIndex).getClass();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object value = "";
        if (!peaklists.isEmpty())
        {
            OPeaklist peaklist = peaklists.get(rowIndex);
            switch (columnIndex)
            {
                case 0:
                    value = peaklist.selected;
                    break;
                case 1:
                    value = peaklist.reflex;
                    break;
                case 2:
                    value = peaklist.jmsainfoName;
                    break;
                case 3:
                    value = peaklist.spectrumid;
                    break;
                case 4:
                    value = peaklist.jmsainfoSpecie;
                    break;
                case 5:
                    value = peaklist.jmsainfoStrain;
                    break;
                default:
                    value = peaklist.spectrumid;
                    break;
            }
        }
        return value;
    }
    
    public boolean isCellEditable(int row, int col)
    {
        return (col < 2);
    }
    
    public void setValueAt(Object value, int row, int col)
    {
        OPeaklist peaklist = peaklists.get(row);
        switch (col)
        {
            case 0:
                if (value.getClass() == Boolean.class)
                {
                    peaklist.selected = (Boolean) value;
                }
                break;
            case 1:
                if (value.getClass() == Boolean.class)
                {
                    peaklist.reflex = (Boolean) value;
                }
                break;
        }
        fireTableCellUpdated(row, col);
    }
    
    public void setAllValuesAt(boolean value, int col){
    	int row;
    	for(row = 0; row < peaklists.size(); row++){
    		OPeaklist peaklist = peaklists.get(row);
            switch (col)
            {
                case 0:
                    peaklist.selected = (Boolean) value;
                    break;
                case 1:
                    peaklist.reflex = (Boolean) value;  
                    break;
            }
    	}
    	fireTableCellUpdated(0, 0);
    }
}
