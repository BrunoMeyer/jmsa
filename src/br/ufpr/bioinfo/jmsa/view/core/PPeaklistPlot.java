package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
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

import br.ufpr.bioinfo.jmsa.control.CConfig;
import br.ufpr.bioinfo.jmsa.model.OPeak;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PPeaklistPlot extends JPanel
{
    public OPeaklist peaklist;
    //
    public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
    
    public PPeaklistPlot(OPeaklist peaklist, boolean intensity)
    {
        this.peaklist = peaklist;
        reload(intensity);
    }
    
    private void reload(boolean intensity) {
    	//
        //
        setLayout(new BorderLayout());
        //        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //
        setBorder(titledBorder);
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        //
        //
        XYIntervalSeriesCollection dataset = new XYIntervalSeriesCollection();
        XYIntervalSeries intervalSeries = new XYIntervalSeries(peaklist.toString());
        JFreeChart jFreeChart = ChartFactory.createXYBarChart(peaklist.toString(), "MASS", false, "INTENSITY", dataset, PlotOrientation.VERTICAL, false, true, true);
        XYPlot xyPlot = jFreeChart.getXYPlot();
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        
        // Decide if the plot only peaklist data or full spectra (raw data)
        // TODO: Optimize plots when use full spectra
        if(CConfig.getInstance().plotEnableRawSpectre) {
        	int i = 1;
            for (Iterator<Long> iterator = peaklist.rawSpectre.iterator(); iterator.hasNext();)
            {
            	long val = iterator.next();
                if(val > 0 && !intensity){
                	val = 1;
                }
                intervalSeries.add(i/2,i/2,i/2,val,val,val);
                i++;
            }
        }
        else{        	
        	for (Iterator<OPeak> iterator = peaklist.getPeaks().iterator(); iterator.hasNext();)
        	{
        		OPeak peak = iterator.next();
        		double val = peak.absi;
        		if(val > 0 && !intensity){
                	val = 1;
                }
        		intervalSeries.add(peak.mass, peak.mass, peak.mass, val, val, val);
        	}
        }
        
        dataset.addSeries(intervalSeries);
        //
        xyPlot.getRenderer().setSeriesPaint(0,peaklist.spectrumForegroundColor);
        add(chartPanel, BorderLayout.CENTER);
        //
        //
        titledBorder.setTitle(peaklist.toString());
        titledBorder.setTitleColor(peaklist.spectrumForegroundColor);
        titledBorder.setBorder(BorderFactory.createLineBorder(peaklist.spectrumForegroundColor, 3));
        //
        //
        chartPanel.setMinimumSize(new Dimension(300, 100));
        chartPanel.setPreferredSize(getMinimumSize());
        setMinimumSize(new Dimension(30, 30));
        setPreferredSize(null);
        validate();
    }
}
