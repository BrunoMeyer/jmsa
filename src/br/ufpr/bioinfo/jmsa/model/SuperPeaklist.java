package br.ufpr.bioinfo.jmsa.model;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;


import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

// This class contains multiples spectres that was merged
// All peaks are still saved and this class can be used just like another OPeaklist
public class SuperPeaklist extends OPeaklist{

	public ArrayList<OPeaklist> peaklists = new ArrayList<OPeaklist>();

	public int distance_merge_peak = 200;
	
	public ArrayList<OPeak> peaklistMerged;
	public SuperPeaklist(ArrayList<OPeaklist> peaklists) throws ParserConfigurationException, SAXException, IOException{
		super(peaklists.get(0).peaklistFile);
		ArrayList<OPeaklist> clone = (ArrayList<OPeaklist>) peaklists.clone();
		this.peaklists = clone;
		
		DecimalFormat decimalFormat = new DecimalFormat("#");
		String threadId = decimalFormat.format(Thread.currentThread().getId());
		String time = decimalFormat.format(System.currentTimeMillis());
		this.spectrumid = "SE-"+time+threadId;
		this.buildPeaks();
	}
	// Constructors
	public SuperPeaklist(File peaklistFile) throws ParserConfigurationException, SAXException, IOException {
		super(peaklistFile);
	}
	
	public void setDistanceMergePeak(int newDistance) {
		this.distance_merge_peak = newDistance;
		this.buildPeaks();
	}
	
	//Override methods from OPeaklist class

	//TODO: Add others possibilities to merge the spectres.
	//      The user may chosen this methods on configuration
	@Override
	public ArrayList<OPeak> getPeaks() {
		return this.peaklistMerged;
	}
	
	
	public void buildPeaks() {
		ArrayList<OPeak> newPeaklist = new ArrayList<OPeak>();
		
		for (OPeaklist peaklist : this.peaklists){
			for (OPeak peak : peaklist.getPeaks()){
//				OPeak newPeak = new OPeak();
				newPeaklist.add(peak);
			}
		}
		
		
		Collections.sort(newPeaklist, new Comparator<OPeak>() {
	        @Override
			public int compare(OPeak p1, OPeak p2)
	        {
	        	return p1.mass > p2.mass ? 1 : p1.mass < p2.mass ? -1 : 0;
	        }
	    });
		
		// The new instance of OPeak have all attributes set into 0
		OPeak sum = new OPeak();
		int count = 0;
		
		ArrayList<OPeak> newPeaklistMerged = new ArrayList<OPeak>(); 
		int i = 0;
		for (OPeak peak : newPeaklist){
			// Sum the peak to the aux variable sum
			sum.add(peak);
			count ++;
			if(i+1 < newPeaklist.size() && newPeaklist.get(i+1).mass - peak.mass <= distance_merge_peak) {
				i++;
				continue;
			}
			
			// Get average from closest peaks
			sum.divide(count);
			
			newPeaklistMerged.add(sum);
			
			// Restart variables used to compute the average peak
			count = 0;
			sum = new OPeak();
			
			i++;
		}
		
		this.peaklistMerged = newPeaklistMerged;
    }
	
	
//	@Override
//	public ArrayList<OPeak> getPeaks() {
//		ArrayList<OPeak> newPeaklist = new ArrayList<OPeak>();
//		for (OPeaklist peaklist : this.peaklists){
//			
//			for (OPeak peak : peaklist.getPeaks()){
////				OPeak newPeak = new OPeak();
//				newPeaklist.add(peak);
//			}
//		}
//		
//		return newPeaklist;
//    }
	
	

	public void addPeaklist(OPeaklist newPeaklist) {
		this.peaklists.add(newPeaklist);
		this.buildPeaks();
	}
	
	public void removePeaklist(OPeaklist newPeaklist) {
		for (OPeaklist peaklist : this.peaklists){
			if(newPeaklist == peaklist) {
				this.peaklists.add(newPeaklist);
			}
		}
		this.buildPeaks();
	}
	
	public void removePeaklistAt(int index) {
		this.removePeaklist( peaklists.get(index) );
		this.buildPeaks();
	}
}
