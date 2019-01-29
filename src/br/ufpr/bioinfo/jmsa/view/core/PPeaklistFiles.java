package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.SuperPeaklist;
import br.ufpr.bioinfo.jmsa.model.event.useraction.OUserActionLoadPeakFiles;
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
    
    private JSONObject createFileOnZIP(OPeaklist peaklist, ZipOutputStream zos, byte[] buffer) throws IOException {
    	
    	ZipEntry ze;
    	String file_name = peaklist.peaklistFile.toString();
		String file_path = 
				"files"+File.separator+peaklist.toString()+File.separator+"peaklist.xml";
		ze = new ZipEntry(file_path);
		zos.putNextEntry(ze);
		
		JSONObject peak_obj = new JSONObject();
		
		peak_obj.put("id", peaklist.toString());
		peak_obj.put("path", file_path);
		
		FileInputStream in = new FileInputStream(file_name);
		
		// This is needed to create the zip file
		// The size of buffer defined before can be a future problem
		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
		in.close();
		
		return peak_obj;
    }
    
    public boolean isPeakInJSONObject(OPeaklist peaklist, JSONArray peaklists_json_array) {
    	for(Object obj : peaklists_json_array) {
			if(peaklist.toString().equals((String)((JSONObject)obj).get("id"))) {
				return true;
			}
		}
    	return false;
    }
    
    public void saveToZIP(String path_to_save) {
    	// IMPORTANT: Any modification in this method
    	// can cause an incompatibility of versions on this program
    	// Please, do not change the file names created
    	
    	try {
    		
    		// The copy is necessary because this list may be changed
	    	List<OPeaklist> peaklists = new ArrayList<>(
    			this.defaultTableModel.getSelectedPeaklists()
	    	);
	    	
	    	String zipFile = path_to_save;
			
			// Instance a zip file
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			
			// Instance variables that will be used for create an zip file
			// that represent the database 
			byte[] buffer = new byte[1024];
			
			int len;
			
			// An json file named peaklistJMSA.json will be created
			// and that contains the listing of peaklists files and superespectres
			
			// Array on format like: [
			//	{"id":"idspectre1", "path":"files/idspectre1.xml"},
			//	...
			// ]
			
			JSONArray peaklists_json_array = new JSONArray();
			
			// Array on format like: [
			// 	{"id":"SE-superspectre1", "peaklists_ids":["superspectre1","superspectre2", ...]},
			// 	...
			// ]
			JSONArray superpeaks_json_array = new JSONArray();
			
			// For each peaklist selected, save it on "files/peaklistid.xml" inside zip file
			// If the peaklist instance was an superspectre, just add it on json file
			for (OPeaklist peaklist : peaklists){
				if(peaklist instanceof SuperPeaklist) {
					SuperPeaklist sp = (SuperPeaklist) peaklist;
					JSONObject super_peak_obj = new JSONObject();
					JSONArray sp_peaklists_json_array = new JSONArray();
					
					for (OPeaklist sp_peaklist : sp.peaklists){
						sp_peaklists_json_array.add(sp_peaklist.toString());
						
						boolean already_saved = isPeakInJSONObject(sp_peaklist, peaklists_json_array);
		    			if(!already_saved) {
		    				peaklists_json_array.add(
	    						createFileOnZIP(sp_peaklist, zos, buffer)
							);
		    			}

					}
					super_peak_obj.put("id", sp.toString());
					super_peak_obj.put("peaklists_ids", sp_peaklists_json_array);
					super_peak_obj.put("distance_merge_peak", sp.distance_merge_peak);
					superpeaks_json_array.add(super_peak_obj);
					
				}
				else {
	    			
	    			boolean already_saved = isPeakInJSONObject(peaklist, peaklists_json_array);
	    			
	    			if(!already_saved) {
	    				peaklists_json_array.add(
    						createFileOnZIP(peaklist, zos, buffer)
						);
	    			} else {
	    				System.out.println("Erro no salvamento: Peaklists ID duplicados");
	    			}
				}
			}
			
	
			// Create an temporary file for json
			// This can be an problem on windows or different OS systems
			String tempFileName = "peaklistJMSA.json";
			File tempFile = File.createTempFile(tempFileName+".", ".temp");
			                    		
			JSONObject obj = new JSONObject();
			obj.put("peaklists", peaklists_json_array);
			obj.put("super_peaklists", superpeaks_json_array);
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
	    	    bw.write(obj.toJSONString());
	    	    bw.close();
			} catch(IOException err){
				err.printStackTrace();
			}
			
			// Finally, save the json inside zip file
			ZipEntry ze = new ZipEntry(tempFileName);
			zos.putNextEntry(ze);
			FileInputStream in = new FileInputStream(tempFile.getAbsoluteFile());
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			in.close();
			
			obj.put("peaklists", peaklists_json_array);
			tempFile.deleteOnExit();
			
			zos.closeEntry();
			zos.close();
    	} catch (Exception err){
    		err.printStackTrace();
        }
    }
    
    public static File createTempDirectory(String name) throws IOException {
	    final File temp;

	    temp = File.createTempFile(name, Long.toString(System.nanoTime()));

	    if(!(temp.delete()))
	    {
	        throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
	    }

	    if(!(temp.mkdir()))
	    {
	        throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
	    }

	    return (temp);
	}

    public void loadFromZIP(String zipFilePath) throws IOException, ParseException {
    	
    	String tempDirName = "jmsa_temp_load"+zipFilePath;
    	
		File dir = createTempDirectory(tempDirName);
		String destDir = dir.getAbsolutePath();
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        
        JSONParser parser = new JSONParser();
        
        JSONObject peaklistJSON;

        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            JSONArray super_peaks_file = null;
            
            while(ze != null){
                String fileName = ze.getName();
                
                File newFile = new File(destDir + File.separator + fileName);
                
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
                if(fileName.equals("peaklistJMSA.json")) {
                	Object obj = parser.parse(new FileReader(newFile.getAbsolutePath()));

                    JSONObject json_object = (JSONObject) obj;
                    super_peaks_file = (JSONArray) json_object.get("super_peaklists");
                }

                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
            final JSONArray super_peaks = super_peaks_file;
            OUserActionLoadPeakFiles peaksLoader = new OUserActionLoadPeakFiles(
        		new File[] {dir}, true
            );
            peaksLoader.executarEvento();
            FMainWindow.getInstance().updateVisibleColums();
            
            
            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                	if(super_peaks != null) {
        	            Iterator<JSONObject> iterator_super_spectres = super_peaks.iterator();
        	            while (iterator_super_spectres.hasNext()) {
        	            	JSONObject sp = iterator_super_spectres.next();
        	            	JSONArray peaklists_id = (JSONArray) sp.get("peaklists_ids");
        	            	String sp_id = (String) sp.get("id");
        	            	
        	            	int distance_merge_peak = (int) (long) sp.get("distance_merge_peak");
        	            	
        		            Iterator<String> iterator_peaklists_id_ = peaklists_id.iterator();
        		            List<OPeaklist> peaklists = new ArrayList();
        		            
        		            while (iterator_peaklists_id_.hasNext()) {
        		            	String p_id = iterator_peaklists_id_.next();
        		            	OPeaklist p = FMainWindow.getInstance().getSelectedPanelPeakById(
        		            		p_id
        		            	);
        		            	peaklists.add(p);
        		            }
        		            
        		            try {
        						SuperPeaklist merged = new SuperPeaklist((ArrayList)peaklists);
        						merged.spectrumid = sp_id;
        						merged.setDistanceMergePeak(distance_merge_peak);
        						FMainWindow.getInstance().addPeaklistToTable(merged);
        					} catch (ParserConfigurationException | SAXException | IOException e1) {
        						// TODO Auto-generated catch block
        						e1.printStackTrace();
        					}
        	            }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
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
