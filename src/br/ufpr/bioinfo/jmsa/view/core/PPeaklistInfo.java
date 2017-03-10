package br.ufpr.bioinfo.jmsa.view.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import br.ufpr.bioinfo.jmsa.model.OPeaklist;

public class PPeaklistInfo extends JPanel implements ActionListener
{
    public OPeaklist peaklist;
    //
    public TitledBorder titledBorder = BorderFactory.createTitledBorder("");
    private JPanel panelMain = new JPanel();
    private JPanel panelRowSpectrumID = new JPanel();
    private JPanel panelRowDate = new JPanel();
    private JPanel panelRowShots = new JPanel();
    private JPanel panelRowCreator = new JPanel();
    private JPanel panelRowVersion = new JPanel();
    private JPanel panelRowName = new JPanel();
    private JPanel panelRowSpecie = new JPanel();
    private JPanel panelRowStrain = new JPanel();
    private JPanel panelRowNotes = new JPanel();
    private JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JLabel labelSpectrumID = new JLabel("SpectrumID");
    private JLabel labelDate = new JLabel("Date");
    private JLabel labelShots = new JLabel("Shots");
    private JLabel labelCreator = new JLabel("Creator");
    private JLabel labelVersion = new JLabel("Version");
    private JLabel labelName = new JLabel("Name");
    private JLabel labelSpecie = new JLabel("Specie");
    private JLabel labelStrain = new JLabel("Strain");
    private JLabel labelNotes = new JLabel("Notes");
    private JTextField textSpectrumID = new JTextField();
    private JTextField textDate = new JTextField();
    private JTextField textShots = new JTextField();
    private JTextField textCreator = new JTextField();
    private JTextField textVersion = new JTextField();
    private JTextField textName = new JTextField();
    private JTextField textSpecie = new JTextField();
    private JTextField textStrain = new JTextField();
    private JTextArea textNotes = new JTextArea();
    public JButton buttonOk = new JButton("OK");
    public JButton buttonCancel = new JButton("Cancel");
    
    public PPeaklistInfo(OPeaklist peaklist)
    {
        this.peaklist = peaklist;
        //
        setBorder(titledBorder);
        setAlignmentX(JPanel.CENTER_ALIGNMENT);
        //
        //
        setLayout(new BorderLayout(5, 5));
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));
        panelRowSpectrumID.setLayout(new BoxLayout(panelRowSpectrumID, BoxLayout.LINE_AXIS));
        panelRowDate.setLayout(new BoxLayout(panelRowDate, BoxLayout.LINE_AXIS));
        panelRowShots.setLayout(new BoxLayout(panelRowShots, BoxLayout.LINE_AXIS));
        panelRowCreator.setLayout(new BoxLayout(panelRowCreator, BoxLayout.LINE_AXIS));
        panelRowVersion.setLayout(new BoxLayout(panelRowVersion, BoxLayout.LINE_AXIS));
        panelRowName.setLayout(new BoxLayout(panelRowName, BoxLayout.LINE_AXIS));
        panelRowSpecie.setLayout(new BoxLayout(panelRowSpecie, BoxLayout.LINE_AXIS));
        panelRowStrain.setLayout(new BoxLayout(panelRowStrain, BoxLayout.LINE_AXIS));
        panelRowNotes.setLayout(new BoxLayout(panelRowNotes, BoxLayout.LINE_AXIS));
        //
        //
        add(panelMain, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        //
        panelMain.add(panelRowSpectrumID);
        panelMain.add(panelRowDate);
        panelMain.add(panelRowShots);
        panelMain.add(panelRowCreator);
        panelMain.add(panelRowVersion);
        panelMain.add(panelRowName);
        panelMain.add(panelRowSpecie);
        panelMain.add(panelRowStrain);
        panelMain.add(panelRowNotes);
        panelMain.add(Box.createHorizontalGlue());
        panelRowSpectrumID.add(labelSpectrumID);
        panelRowSpectrumID.add(textSpectrumID);
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
        panelBottom.add(buttonOk);
        panelBottom.add(buttonCancel);
        //
        //
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //
        labelSpectrumID.setHorizontalAlignment(JLabel.CENTER);
        labelDate.setHorizontalAlignment(JLabel.CENTER);
        labelShots.setHorizontalAlignment(JLabel.CENTER);
        labelCreator.setHorizontalAlignment(JLabel.CENTER);
        labelVersion.setHorizontalAlignment(JLabel.CENTER);
        labelName.setHorizontalAlignment(JLabel.CENTER);
        labelSpecie.setHorizontalAlignment(JLabel.CENTER);
        labelStrain.setHorizontalAlignment(JLabel.CENTER);
        labelNotes.setHorizontalAlignment(JLabel.CENTER);
        //
        //
        buttonOk.setIcon(SIconUtil.imageIconOk16);
        buttonCancel.setIcon(SIconUtil.imageIconCancel16);
        //
        //
        buttonOk.setActionCommand("OK");
        buttonCancel.setActionCommand("Cancel");
        //
        buttonOk.addActionListener(this);
        buttonCancel.addActionListener(this);
        //
        //
        panelRowSpectrumID.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowSpectrumID.getPreferredSize().height));
        panelRowDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowDate.getPreferredSize().height));
        panelRowShots.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowShots.getPreferredSize().height));
        panelRowCreator.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowCreator.getPreferredSize().height));
        panelRowVersion.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowVersion.getPreferredSize().height));
        panelRowName.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowName.getPreferredSize().height));
        panelRowSpecie.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowSpecie.getPreferredSize().height));
        panelRowStrain.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowStrain.getPreferredSize().height));
//        panelRowNotes.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelRowNotes.getPreferredSize().height));
        labelSpectrumID.setPreferredSize(new Dimension(75, labelSpectrumID.getPreferredSize().height));
        labelDate.setPreferredSize(new Dimension(75, labelDate.getPreferredSize().height));
        labelShots.setPreferredSize(new Dimension(75, labelShots.getPreferredSize().height));
        labelCreator.setPreferredSize(new Dimension(75, labelCreator.getPreferredSize().height));
        labelVersion.setPreferredSize(new Dimension(75, labelVersion.getPreferredSize().height));
        labelName.setPreferredSize(new Dimension(75, labelName.getPreferredSize().height));
        labelSpecie.setPreferredSize(new Dimension(75, labelSpecie.getPreferredSize().height));
        labelStrain.setPreferredSize(new Dimension(75, labelStrain.getPreferredSize().height));
        labelNotes.setPreferredSize(new Dimension(75, labelNotes.getPreferredSize().height));
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
        textDate.setEditable(false);
        textShots.setEditable(false);
        textCreator.setEditable(false);
        textVersion.setEditable(false);
    }
    
    public void readConfig()
    {
        textSpectrumID.setText(peaklist.spectrumid);
        textDate.setText(peaklist.date);
        textShots.setText(peaklist.shots);
        textCreator.setText(peaklist.creator);
        textVersion.setText(peaklist.version);
        textName.setText(peaklist.jmsainfoName);
        textSpecie.setText(peaklist.jmsainfoSpecie);
        textStrain.setText(peaklist.jmsainfoStrain);
        textNotes.setText(peaklist.jmsainfoNotes);
    }
    
    public void saveConfig()
    {
        peaklist.jmsainfoName = textName.getText();
        peaklist.jmsainfoSpecie = textSpecie.getText();
        peaklist.jmsainfoStrain = textStrain.getText();
        peaklist.jmsainfoNotes = textNotes.getText();
        peaklist.saveJMSAINFO();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "OK":
                saveConfig();
                break;
            case "Cancel":
                readConfig();
                break;
            default:
                break;
        }
    }
}
