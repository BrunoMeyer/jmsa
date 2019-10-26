package br.ufpr.bioinfo.jmsa.view.core;
import br.ufpr.bioinfo.jmsa.view.FMainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;
import br.ufpr.bioinfo.jmsa.model.SuperPeaklist;

public class PPeaklistInfo extends JPanel implements ActionListener
{
    public OPeaklist peaklist;
    
    private int labelWidth = 125;
    //
    public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
    private JPanel panelMain = new JPanel();
    private JPanel panelRowSpectrumID = new JPanel();
    private JPanel panelRowTempID = new JPanel();
    private JPanel panelRowDate = new JPanel();
    private JPanel panelRowShots = new JPanel();
    private JPanel panelRowCreator = new JPanel();
    private JPanel panelRowVersion = new JPanel();
    private JPanel panelRowName = new JPanel();
    private JPanel panelRowSpecie = new JPanel();
    private JPanel panelRowStrain = new JPanel();
    private JPanel panelRowNotes = new JPanel();;
    private JPanel panelRowDNA = new JPanel();
    private JPanel panelRowSSList = new JPanel();
    private JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    private JLabel labelSpectrumID = new JLabel("SpectrumID");
    private JLabel labelTempID = new JLabel("TemporaryID");
    private JLabel labelDate = new JLabel("Date");
    private JLabel labelShots = new JLabel("Shots");
    private JLabel labelCreator = new JLabel("Creator");
    private JLabel labelVersion = new JLabel("Version");
    private JLabel labelName = new JLabel("Name");
    private JLabel labelSpecie = new JLabel("Specie");
    private JLabel labelStrain = new JLabel("Strain");
    private JLabel labelNotes = new JLabel("Notes");
    private JLabel labelSSList = new JLabel("Spectres from Super Spectre");
    private JLabel labelDNA = new JLabel("DNA");
    private JTextField textSpectrumID = new JTextField();
    private JTextField textTempID = new JTextField();
    private JTextField textDate = new JTextField();
    private JTextField textShots = new JTextField();
    private JTextField textCreator = new JTextField();
    private JTextField textVersion = new JTextField();
    private JTextField textName = new JTextField();
    private JTextField textSpecie = new JTextField();
    private JTextField textStrain = new JTextField();
    private JTextArea textNotes = new JTextArea();
    private JTextArea textDNA = new JTextArea();
    public JButton buttonOk = new JButton("OK");
    public JButton buttonReset = new JButton("Reset");
    
    public PPeaklistInfo(OPeaklist peaklist)
    {
        this.peaklist = peaklist;
        //
        setBorder(titledBorder);
        setAlignmentX(JPanel.CENTER_ALIGNMENT);
        //
        //
        setLayout(new BorderLayout(6, 6));
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));
        panelRowSpectrumID.setLayout(new BoxLayout(panelRowSpectrumID, BoxLayout.LINE_AXIS));
        panelRowTempID.setLayout(new BoxLayout(panelRowTempID, BoxLayout.LINE_AXIS));
        panelRowDate.setLayout(new BoxLayout(panelRowDate, BoxLayout.LINE_AXIS));
        panelRowShots.setLayout(new BoxLayout(panelRowShots, BoxLayout.LINE_AXIS));
        panelRowCreator.setLayout(new BoxLayout(panelRowCreator, BoxLayout.LINE_AXIS));
        panelRowVersion.setLayout(new BoxLayout(panelRowVersion, BoxLayout.LINE_AXIS));
        panelRowName.setLayout(new BoxLayout(panelRowName, BoxLayout.LINE_AXIS));
        panelRowSpecie.setLayout(new BoxLayout(panelRowSpecie, BoxLayout.LINE_AXIS));
        panelRowStrain.setLayout(new BoxLayout(panelRowStrain, BoxLayout.LINE_AXIS));
        panelRowNotes.setLayout(new BoxLayout(panelRowNotes, BoxLayout.LINE_AXIS));
        panelRowDNA.setLayout(new BoxLayout(panelRowDNA, BoxLayout.LINE_AXIS));
        panelRowSSList.setLayout(new BoxLayout(panelRowSSList, BoxLayout.LINE_AXIS));
        //
        //
        add(panelMain, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        //
        panelMain.add(panelRowSpectrumID);
        panelMain.add(panelRowTempID);
        panelMain.add(panelRowDate);
        panelMain.add(panelRowShots);
        panelMain.add(panelRowCreator);
        panelMain.add(panelRowVersion);
        panelMain.add(panelRowName);
        panelMain.add(panelRowSpecie);
        panelMain.add(panelRowStrain);
        panelMain.add(panelRowNotes);
        panelMain.add(panelRowDNA);
        panelMain.add(panelRowSSList);
        panelMain.add(Box.createHorizontalGlue());
        panelRowSpectrumID.add(labelSpectrumID);
        panelRowSpectrumID.add(textSpectrumID);
        panelRowTempID.add(labelTempID);
        panelRowTempID.add(textTempID);
        panelRowDate.add(labelDate);
        panelRowDate.add(textDate);
        panelRowShots.add(labelShots);
        panelRowShots.add(textShots);
        panelRowCreator.add(labelCreator);
        panelRowCreator.add(textCreator);
        panelRowVersion.add(labelVersion);
        panelRowVersion.add(textVersion);
        panelRowName.add(labelName);
        panelRowName.add(textName);
        panelRowSpecie.add(labelSpecie);
        panelRowSpecie.add(textSpecie);
        panelRowStrain.add(labelStrain);
        panelRowStrain.add(textStrain);
        panelRowNotes.add(labelNotes);
        panelRowNotes.add(textNotes);
        panelRowDNA.add(labelDNA);
        panelRowDNA.add(textDNA);
        panelBottom.add(buttonOk);
        panelBottom.add(buttonReset);
        

        if(peaklist instanceof SuperPeaklist) {
        	SuperPeaklist sspeaklist = (SuperPeaklist) peaklist;
        	PPeaklistFiles superPeaklistTable = new PPeaklistFiles("Subspectres of "+peaklist.toString(), FMainWindow.getInstance(), false);
        	superPeaklistTable.setMarkersVisibility(false);
        	
        	for (OPeaklist p : sspeaklist.peaklists){
        		superPeaklistTable.defaultTableModel.addRow(p);
        	}
        	
        	superPeaklistTable.setVisibleColumns( 
        			FMainWindow.getInstance().checkBoxMenuItemShowMSName.isSelected(),
        			FMainWindow.getInstance().checkBoxMenuItemShowMSSpectrumID.isSelected(),
        			FMainWindow.getInstance().checkBoxMenuItemShowMSSpecies.isSelected(),
        			FMainWindow.getInstance().checkBoxMenuItemShowMSStrain.isSelected(),
        			FMainWindow.getInstance().checkBoxMenuItemShowFilePath.isSelected(),
        			FMainWindow.getInstance().checkBoxMenuItemShowSuperSpectreSize.isSelected()
        	);
        	labelSSList.setText("<html><p style=\"width:100px\">"+labelSSList.getText()+"</p></html>");
        	panelRowSSList.add(labelSSList);
        	
        	superPeaklistTable.setPreferredSize(new Dimension(30, 100));
        	superPeaklistTable.setMinimumSize(new Dimension(30, 100));
//        	superPeaklistTable.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        	panelRowSSList.add(superPeaklistTable);
        	
        }
        
        //
        //
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //
        labelSpectrumID.setHorizontalAlignment(JLabel.CENTER);
        labelTempID.setHorizontalAlignment(JLabel.CENTER);
        labelDate.setHorizontalAlignment(JLabel.CENTER);
        labelShots.setHorizontalAlignment(JLabel.CENTER);
        labelCreator.setHorizontalAlignment(JLabel.CENTER);
        labelVersion.setHorizontalAlignment(JLabel.CENTER);
        labelName.setHorizontalAlignment(JLabel.CENTER);
        labelSpecie.setHorizontalAlignment(JLabel.CENTER);
        labelStrain.setHorizontalAlignment(JLabel.CENTER);
        labelNotes.setHorizontalAlignment(JLabel.CENTER);
        labelSSList.setHorizontalAlignment(JLabel.CENTER);
        labelDNA.setHorizontalAlignment(JLabel.CENTER);
        //
        //
        buttonOk.setIcon(SIconUtil.imageIconOk16);
        buttonReset.setIcon(SIconUtil.imageIconCancel16);
        //
        //
        buttonOk.setActionCommand("OK");
        buttonReset.setActionCommand("Reset");
        //
        buttonOk.addActionListener(this);
        buttonReset.addActionListener(this);
        //
        //
        panelRowSpectrumID.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowSpectrumID.getPreferredSize().height));
        panelRowTempID.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowTempID.getPreferredSize().height));
        panelRowDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowDate.getPreferredSize().height));
        panelRowShots.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowShots.getPreferredSize().height));
        panelRowCreator.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowCreator.getPreferredSize().height));
        panelRowVersion.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowVersion.getPreferredSize().height));
        panelRowName.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowName.getPreferredSize().height));
        panelRowSpecie.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowSpecie.getPreferredSize().height));
        panelRowStrain.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowStrain.getPreferredSize().height));
//        panelRowNotes.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowNotes.getPreferredSize().height));
        panelRowSSList.setMinimumSize(new Dimension(panelRowSSList.getPreferredSize().width, 100));
        labelSpectrumID.setPreferredSize(new Dimension(labelWidth, labelSpectrumID.getPreferredSize().height));
        labelTempID.setPreferredSize(new Dimension(labelWidth, labelSpectrumID.getPreferredSize().height));
        labelDate.setPreferredSize(new Dimension(labelWidth, labelDate.getPreferredSize().height));
        labelShots.setPreferredSize(new Dimension(labelWidth, labelShots.getPreferredSize().height));
        labelCreator.setPreferredSize(new Dimension(labelWidth, labelCreator.getPreferredSize().height));
        labelVersion.setPreferredSize(new Dimension(labelWidth, labelVersion.getPreferredSize().height));
        labelName.setPreferredSize(new Dimension(labelWidth, labelName.getPreferredSize().height));
        labelSpecie.setPreferredSize(new Dimension(labelWidth, labelSpecie.getPreferredSize().height));
        labelStrain.setPreferredSize(new Dimension(labelWidth, labelStrain.getPreferredSize().height));
        labelNotes.setPreferredSize(new Dimension(labelWidth, labelNotes.getPreferredSize().height));
        labelSSList.setPreferredSize(new Dimension(labelWidth, labelSSList.getPreferredSize().height));
        labelDNA.setPreferredSize(new Dimension(labelWidth, labelDNA.getPreferredSize().height));
        //
        //
        titledBorder.setTitle(peaklist.toString());
        titledBorder.setTitleColor(peaklist.spectrumForegroundColor);
        titledBorder.setBorder(BorderFactory.createLineBorder(peaklist.spectrumForegroundColor, 3));
        //
        //
        readConfig();
        //
        //
        setMinimumSize(new Dimension(30, 30));
        setPreferredSize(null);
        validate();
        //
        //
        textSpectrumID.setEditable(false);
        textTempID.setEditable(false);
        textDate.setEditable(false);
        textShots.setEditable(false);
        textCreator.setEditable(false);
        textVersion.setEditable(false);
        
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        textNotes.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelRowNotes.setBorder(new EmptyBorder(0, 0, 10, 10));
        textDNA.setBorder(BorderFactory.createCompoundBorder(border, 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panelRowDNA.setBorder(new EmptyBorder(0, 0, 10, 10));
        
        
        validate();
    }
    
    public void readConfig()
    {
        textSpectrumID.setText(peaklist.spectrumid);
        textTempID.setText(Integer.toString(peaklist.panel_id));
        textDate.setText(peaklist.date);
        textShots.setText(peaklist.shots);
        textCreator.setText(peaklist.creator);
        textVersion.setText(peaklist.version);
        textName.setText(peaklist.jmsainfoName);
        textSpecie.setText(peaklist.jmsainfoSpecie);
        textStrain.setText(peaklist.jmsainfoStrain);
        textNotes.setText(peaklist.jmsainfoNotes);
        textDNA.setText(peaklist.jmsainfoDNA);
    }
    
    public void saveConfig()
    {
        peaklist.jmsainfoName = textName.getText();
        peaklist.jmsainfoSpecie = textSpecie.getText();
        peaklist.jmsainfoStrain = textStrain.getText();
        peaklist.jmsainfoNotes = textNotes.getText();
        peaklist.jmsainfoDNA = textDNA.getText();
        peaklist.saveJMSAINFO();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "OK":
                saveConfig();
                FMainWindow.getInstance().updateTable();
                break;
            case "Reset":
                readConfig();
                break;
            default:
                break;
        }
    }
}
