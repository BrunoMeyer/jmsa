package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import br.ufpr.bioinfo.jmsa.model.OPeak;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PPeaklistTable extends JPanel
{
    public OPeaklist peaklist;
    //
    public JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
    public DecimalFormat decimalFormat = new DecimalFormat("#.#");
    public DefaultTableModel defaultTableModel = new DefaultTableModel();
    public JTable table = new JTable(defaultTableModel);
    
    public PPeaklistTable(OPeaklist peaklist)
    {
        this.peaklist = peaklist;
        //
        //
        //        setLayout(new BorderLayout());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(table);
        //
        setBorder(titledBorder);
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        //
        //
        fillTable();
    }
    
    private void fillTable()
    {
        defaultTableModel.addColumn("", new Object[] {
        	"ABSI",
        	"MASS",
        	"IND",
        	"LIND",
        	"METH",
        	"RESO",
        	"RIND",
        	"S2N",
        	"TYPE"
        });
        //        defaultTableModel.addColumn("TESTE", new Object[] { new Boolean(true), new Boolean(false) });
        //
        //
        for (OPeak peak : peaklist.getPeaks())
        {
            defaultTableModel.addColumn("", new Object[] {
            	decimalFormat.format(peak.absi),
            	decimalFormat.format(peak.mass),
            	decimalFormat.format(peak.ind),
            	decimalFormat.format(peak.lind),
            	decimalFormat.format(peak.meth),
            	decimalFormat.format(peak.reso),
            	decimalFormat.format(peak.rind),
            	decimalFormat.format(peak.s2n),
            	decimalFormat.format(peak.type)
            });
        }
        //        
        //        
        add(table.getTableHeader(), BorderLayout.NORTH);
        //
        //
        table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(new Dimension(500, table.getPreferredSize().height));
        table.setFillsViewportHeight(true);
        //
        titledBorder.setTitle(peaklist.toString());
        titledBorder.setTitleColor(peaklist.spectrumForegroundColor);
        titledBorder.setBorder(BorderFactory.createLineBorder(peaklist.spectrumForegroundColor, 3));
        //
        setPreferredSize(null);
        setMaximumSize(getPreferredSize());
    }
}
