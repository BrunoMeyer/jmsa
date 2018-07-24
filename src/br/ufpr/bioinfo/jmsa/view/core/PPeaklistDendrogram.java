package br.ufpr.bioinfo.jmsa.view.core;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import algorithm.clustering.AverageLinkageStrategy;
import algorithm.clustering.Cluster;
import algorithm.clustering.ClusteringAlgorithm;
import algorithm.clustering.DefaultClusteringAlgorithm;
import algorithm.clustering.visualization.DendrogramPanel;
import br.ufpr.bioinfo.jmsa.analyser.CPeaklistAnalyser;
import br.ufpr.bioinfo.jmsa.control.CControl;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.event.useraction.OUserActionLoadPeakFiles;


public class PPeaklistDendrogram extends JPanel
{
	 
	    //
	 public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
	
	 public JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	 
	 public JPanel content = new JPanel();
	 private JButton exportButton = new JButton("Export to Newick File");
	 
	 private Cluster cluster;
	 
	 public PPeaklistDendrogram()
	 {
    	
		content.setMinimumSize(new Dimension(300,300));
	    content.setPreferredSize(new Dimension(800,600));
	    content.setLayout((LayoutManager) new BoxLayout(content, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout(1, 2));
       
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setViewportView(content);
        
        
        this.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String newickString = cluster.toNewickString(0)+";";
            	JFileChooser chooserSave = new JFileChooser();
            	chooserSave.setCurrentDirectory(new File("/home/me/Documents"));
                int retrival = chooserSave.showSaveDialog(null);
                if (retrival == JFileChooser.APPROVE_OPTION) {
                    try {
                    	CControl.getInstance().threadUserActionsPool.addEvento(new OUserActionLoadPeakFiles(chooserSave.getSelectedFiles()));
                        FileWriter fw = new FileWriter(chooserSave.getSelectedFile()+".newick");
                        fw.write(newickString);
                        fw.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        validate();
    }

    public void reloadDendrogram(List<OPeaklist> peaklists) {
    	content.removeAll();
    	
    	if(peaklists.size() == 0)
    		return;

    	content.add(exportButton);
    	exportButton.setAlignmentX(content.CENTER_ALIGNMENT);
    	
    	String[] names = new String[peaklists.size()];
        double[][] distances = new double[peaklists.size()][peaklists.size()];
        
    	for (int col = 0; col < peaklists.size(); col++)
        {
    		
    		OPeaklist peaklistCol = peaklists.get(col);
    		names[col] = peaklistCol.toString();
            for (int row = 0; row < peaklists.size(); row++)
            {
                OPeaklist peaklistRow = peaklists.get(row);
                double similarity = CPeaklistAnalyser.getPeakistSimilarity(peaklistCol, peaklistRow);
            	distances[col][row] = (1-similarity)*100;
            }
    		
        }
    	
    	for(int i = 0; i < peaklists.size(); ++i) {
    		int equals = 1;
    		for(int j = 0; j < peaklists.size(); ++j) {
    			if(i != j && names[i].equals(names[j])) {
    				equals+=1;
    				names[j] = names[j] + " - "+ equals;
    			}
    		}
    		if(equals != 1) {
				names[i] = names[i]+" - 1";
			}
    	}
    	
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        cluster = alg.performClustering(distances, names,
        		new AverageLinkageStrategy());
        
        DendrogramPanel dp = new DendrogramPanel();
        dp.setModel(cluster);
        content.setMinimumSize(new Dimension(100, 50*peaklists.size()));
        content.setPreferredSize(new Dimension(100, 50*peaklists.size()));
        
        content.add(dp);
        
        removeAll();
        
        add(scrollPane);
        
    }
}
