package br.ufpr.bioinfo.jmsa.model;

import java.io.File;
import java.io.IOException;
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
	
	public SuperPeaklist(ArrayList<OPeaklist> peaklists) throws ParserConfigurationException, SAXException, IOException{
		super(peaklists.get(0).peaklistFile);
		ArrayList<OPeaklist> clone = (ArrayList<OPeaklist>) peaklists.clone();
		this.peaklists = clone;
		this.spectrumid = "SE-"+this.spectrumid;
	}
	// Constructors
	public SuperPeaklist(File peaklistFile) throws ParserConfigurationException, SAXException, IOException {
		super(peaklistFile);
	}
	
	public void setDistanceMergePeak(int newDistance) {
		this.distance_merge_peak = newDistance;
	}

	
	//Override methods from OPeaklist class
	
	//TODO: Add others possibilities to merge the spectres.
	//      The user may chosen this methods on configuration
	@Override
	public ArrayList<OPeak> getPeaks() {
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
	        	Double result = new Double(p1.mass - p2.mass);
	            return result.intValue();
	        }
	    });
		
		ArrayList<OPeak> newPeaklistMerged = new ArrayList<OPeak>(); 
		int i = 0;
		for (OPeak peak : newPeaklist){
			if(i+1 < newPeaklist.size() && newPeaklist.get(i+1).mass - peak.mass <= distance_merge_peak) {
				i++;
				continue;
			}

			newPeaklistMerged.add(peak);
			i++;
		}
		
		return newPeaklistMerged;
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
	}
	
	public void removePeaklist(OPeaklist newPeaklist) {
		for (OPeaklist peaklist : this.peaklists){
			if(newPeaklist == peaklist) {
				this.peaklists.add(newPeaklist);
			}
		}
	}
	
	public void removePeaklistAt(int index) {
		this.removePeaklist( peaklists.get(index) );
	}
}
