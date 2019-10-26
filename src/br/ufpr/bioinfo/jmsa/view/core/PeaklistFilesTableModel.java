package br.ufpr.bioinfo.jmsa.view.core;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.SuperPeaklist;

public class PeaklistFilesTableModel extends AbstractTableModel
{
    public ArrayList<OPeaklist> peaklists = new ArrayList<OPeaklist>();
    public String[] columnNames = new String[] { "TemporaryID", "Selected", "Reflex", "Name", "SpectrumID", "Species", "Strain", "File Path", "Total Spectres"};
    public boolean globalTrigger = true; // boolean used to cause or prevent triggers when use interact with table
    public boolean isSuperSpectre = false;
    
    public PeaklistFilesTableModel()
    {
    }
    
    public PeaklistFilesTableModel(boolean globalTrigger)
    {
    	this.globalTrigger = globalTrigger;
    }
    
    
    public void addPeaklist(OPeaklist peaklist)
    {
        peaklists.add(peaklist);
        if(this.globalTrigger) fireTableDataChanged();
    }
    
    public OPeaklist getPeaklistAt(int row)
    {
    	if(peaklists.size()-1 < row) return null;
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
        if(this.globalTrigger) fireTableDataChanged();
    }
    
    public void addRow(OPeaklist peaklist)
    {
        addPeaklist(peaklist);
        if(this.globalTrigger) fireTableDataChanged();
    }
    
    public void removeRow(OPeaklist peaklist)
    {
        peaklists.remove(peaklist);
        if(this.globalTrigger) fireTableDataChanged();
    }
    
    @Override
    public int getRowCount()
    {
        return peaklists.size();
    }
    
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
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
                    value = peaklist.panel_id;
                    break;
                case 1:
                    value = peaklist.selected;
                    break;
                case 2:
            		value = peaklist.reflex;
            		break;
                case 3:
                    value = peaklist.jmsainfoName;
                    break;
                case 4:
                    value = peaklist.spectrumid;
                    break;
                case 5:
                    value = peaklist.jmsainfoSpecie;
                    break;
                case 6:
                    value = peaklist.jmsainfoStrain;
                    break;
                case 7:
                    value = peaklist.localPath.toString();
                    break;
                case 8:
                	if(peaklist instanceof SuperPeaklist) {                		
                		value = ((SuperPeaklist)(peaklist)).peaklists.size();
                	}
                	else {
                		value = "Individual Spectre";
                	}
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
    	// Only reflex and select columns must be editable
        return (col < 3 && col > 0);
    }
    
    public void setValueAt(Object value, int row, int col, boolean trigger)
    {
        OPeaklist peaklist = peaklists.get(row);
        switch (col)
        {
            case 1:
                if (value.getClass() == Boolean.class)
                {
                    peaklist.selected = (Boolean) value;
                }
                break;
            case 2:
                if (value.getClass() == Boolean.class)
                {
                    peaklist.reflex = (Boolean) value;
                }
                break;
        }
        if(this.globalTrigger && trigger) fireTableCellUpdated(row, col);
    }
    
    public void setValueAt(Object value, int row, int col) {
    	setValueAt(value, row, col, true);
    }
    
    public void setAllValuesAt(boolean value, int col){
    	int row;
    	for(row = 0; row < peaklists.size()-1; row++){
    		setValueAt(value, row, col, false);
    	}
    	setValueAt(value, row, col, true);
    }
}
