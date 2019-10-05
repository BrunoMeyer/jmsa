package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import br.ufpr.bioinfo.jmsa.analyser.CPeaklistAnalyser;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

public class PPeaklistAction extends JPanel
{
    private JPanel panelMain = new JPanel();

    private JPanel panelParameters = new JPanel();

    public JScrollPane scrollPanePeaklistFiles = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                                 JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    private JPanel panelActionType = new JPanel();

    JComboBox selectField;
    //
    private List<OPeaklist> peaklists;
    
    public PPeaklistAction(List<OPeaklist> peaklists)
    {
        setLayout(new GridLayout(0, 1));
        
        add(panelMain);
        add(scrollPanePeaklistFiles);
        scrollPanePeaklistFiles.setViewportView(panelMain);

        String[] options = new String[] {
            "Update Name",
            "Update Specie",
            "Update Strain",
            "Update DNA",
            "Update Notes"
        };
        selectField = new JComboBox(options);
        
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        selectField.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        panelParameters.setLayout(new BoxLayout(panelParameters, BoxLayout.Y_AXIS));
        panelParameters.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        panelParameters.setMaximumSize( new Dimension(
            Integer.MAX_VALUE,
            100
        ) );
        
        JPanel jp = new JPanel();
        jp.add(selectField);
        panelMain.add(jp, BorderLayout.NORTH);

        panelMain.add(panelParameters, BorderLayout.NORTH);
        
        
        this.peaklists = peaklists;
        selectField.setPrototypeDisplayValue("Update Name");
        selectField.setMaximumSize( selectField.getPreferredSize() );
        selectField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                updateAction(cb.getSelectedItem().toString());
            }
        });

        updateAction("Update Name");
    }
    
	public void updateAction(String action) {
		panelParameters.removeAll();

		JButton button = new JButton(action);
 

        switch(action){
            case "Update Name":
                JLabel labelName = new JLabel("Name");
                JTextField textName = new JTextField();
                panelParameters.add(labelName);
                panelParameters.add(textName);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        peaklists = FMainWindow.getInstance().getLoadingSelectedPeaklists();
                        for (OPeaklist peaklist : peaklists){
                            peaklist.jmsainfoName = textName.getText();
                            peaklist.saveJMSAINFO();
                            peaklist.reloadPeaklistInfo();
                        }
                        if(peaklists.size() > 0) {                        	
                        	FMainWindow.getInstance().firePeaklistPanelChange();
                        }
                    }
                });
                break;

            case "Update Specie":
                JLabel labelSpecie = new JLabel("Specie");
                JTextField textSpecie = new JTextField();
                panelParameters.add(labelSpecie);
                panelParameters.add(textSpecie);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        peaklists = FMainWindow.getInstance().getLoadingSelectedPeaklists();
                        for (OPeaklist peaklist : peaklists){
                            peaklist.jmsainfoSpecie = textSpecie.getText();
                            peaklist.saveJMSAINFO();
                            peaklist.reloadPeaklistInfo();
                        }
                        if(peaklists.size() > 0) {                        	
                        	FMainWindow.getInstance().firePeaklistPanelChange();
                        }
                    }
                });
                break;
            
            case "Update Strain":
                JLabel labelStrain = new JLabel("Strain");
                JTextField textStrain = new JTextField();
                panelParameters.add(labelStrain);
                panelParameters.add(textStrain);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        peaklists = FMainWindow.getInstance().getLoadingSelectedPeaklists();
                        for (OPeaklist peaklist : peaklists){
                            peaklist.jmsainfoStrain = textStrain.getText();
                            peaklist.saveJMSAINFO();
                            peaklist.reloadPeaklistInfo();
                        }
                        if(peaklists.size() > 0) {                        	
                        	FMainWindow.getInstance().firePeaklistPanelChange();
                        }
                    }
                });
                break;
            
            case "Update DNA":
                JLabel labelDNA = new JLabel("DNA");
                JTextArea textDNA = new JTextArea();
                panelParameters.add(labelDNA);
                panelParameters.add(textDNA);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        peaklists = FMainWindow.getInstance().getLoadingSelectedPeaklists();
                        for (OPeaklist peaklist : peaklists){
                            peaklist.jmsainfoDNA = textDNA.getText();
                            peaklist.saveJMSAINFO();
                            peaklist.reloadPeaklistInfo();
                        }
                        if(peaklists.size() > 0) {                        	
                        	FMainWindow.getInstance().firePeaklistPanelChange();
                        }
                    }
                });
                break;
            
            case "Update Notes":
                JLabel labelNotes = new JLabel("Notes");
                JTextArea textNotes = new JTextArea();
                panelParameters.add(labelNotes, BorderLayout.NORTH);
                panelParameters.add(textNotes, BorderLayout.NORTH);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        peaklists = FMainWindow.getInstance().getLoadingSelectedPeaklists();
                        for (OPeaklist peaklist : peaklists){
                            peaklist.jmsainfoNotes = textNotes.getText();
                            peaklist.saveJMSAINFO();
                            peaklist.reloadPeaklistInfo();
                        }
                        if(peaklists.size() > 0) {                        	
                        	FMainWindow.getInstance().firePeaklistPanelChange();
                        }
                    }
                });
                break;
        }

        panelParameters.add(button);
        panelMain.revalidate();
	}
    
}

