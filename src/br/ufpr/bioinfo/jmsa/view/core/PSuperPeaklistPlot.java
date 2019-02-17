package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.ui.RectangleEdge;
import br.ufpr.bioinfo.jmsa.model.OPeak;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PSuperPeaklistPlot extends JPanel
{
    public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
    
    public PSuperPeaklistPlot()
    {
        setLayout(new BorderLayout(2, 2));
        setBorder(titledBorder);
        //
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        //
        //
        titledBorder.setTitle("");
        titledBorder.setTitleColor(Color.BLACK);
        //        titledBorder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        //
        //
        setMinimumSize(new Dimension(30, 30));
    }
    
    public void buildPlot(List<OPeaklist> peaklists, boolean intensity)
    {
        removeAll();
        //
        XYIntervalSeriesCollection dataset = new XYIntervalSeriesCollection();
        for (int i = 0; i < peaklists.size(); i++)
        {
            OPeaklist peaklist = peaklists.get(i);
            XYIntervalSeries intervalSeries = new XYIntervalSeries(peaklist.toString());
            for (OPeak peak : peaklist.getPeaks())
            {
                double shownABSI = peak.absi;
                if (peaklist.reflex)
                {
                    shownABSI = -peak.absi;
                }

                if(!intensity){
	                if(shownABSI > 0){
	                	shownABSI = 1;
	                }
	                if(shownABSI < 0){
	                	shownABSI = -1;
	                }
                }
                //                intervalSeries.add(peak.mass, peak.mass, peak.mass, shownABSI, shownABSI, shownABSI);
                double base = (peak.area * 2) / peak.absi;
                intervalSeries.add(peak.mass, (peak.mass - (base / 2)), (peak.mass + (base / 2)), shownABSI, shownABSI, shownABSI);
            }
            dataset.addSeries(intervalSeries);
        }
        //
        //
        JFreeChart chart = ChartFactory.createXYBarChart("SuperSpectro", "MASS", false, "INTENSITY", dataset, PlotOrientation.VERTICAL, true, true, true);
        chart.getLegend().setPosition(RectangleEdge.LEFT);
        chart.getLegend().setVisible(false);
        //
        //
        XYPlot xyPlot = chart.getXYPlot();
        for (int i = 0; i < peaklists.size(); i++)
        {
            OPeaklist peaklist = peaklists.get(i);
            xyPlot.getRenderer().setSeriesPaint(i, peaklist.spectrumForegroundColor);
        }
        //
        //
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
        //
        //
        chartPanel.setMinimumSize(new Dimension(300, 100));
        chartPanel.setPreferredSize(getMinimumSize());
        setPreferredSize(null);
        validate();
    }
}
