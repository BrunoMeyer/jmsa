package br.ufpr.bioinfo.jmsa.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


// This class contains multiples spectres that was merged
// All peaks are still saved and this class can be used just like another OPeaklist
public class SuperPeaklist extends OPeaklist{

	public ArrayList<OPeaklist> peaklists = new ArrayList<OPeaklist>();
	
	public SuperPeaklist(ArrayList<OPeaklist> peaklists) throws ParserConfigurationException, SAXException, IOException{
		super(peaklists.get(0).peaklistFile);
		ArrayList<OPeaklist> clone = (ArrayList<OPeaklist>) peaklists.clone();
		this.peaklists = clone;
	}
	// Constructors
	public SuperPeaklist(File peaklistFile) throws ParserConfigurationException, SAXException, IOException {
		super(peaklistFile);
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
		
		return newPeaklist;
    }
	
	
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
