package br.ufpr.bioinfo.jmsa.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;
import br.ufpr.bioinfo.jmsa.control.CConfig;
import br.ufpr.bioinfo.jmsa.view.core.SIconUtil;

public class FConfig extends JDialog implements ActionListener
{
    private JPanel panelMain = new JPanel();
    private JPanel panelRowLookAndFeel = new JPanel();
    private JPanel panelRowLoadingPath = new JPanel();
    private JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private JLabel labelLookAndFeel = new JLabel("LookAndFeel");
    private JLabel labelLoadingPath = new JLabel("Loading Path");
    private JComboBox<String> comboboxLookAndFeel = new JComboBox<String>(new DefaultComboBoxModel<String>());
    private JTextField textLoadingPath = new JTextField(CConfig.getInstance().loadingPath);
    public JButton buttonOk = new JButton("OK");
    public JButton buttonCancel = new JButton("Cancel");
    public JButton buttonLoadingPath = new JButton(SIconUtil.imageIconLoadingPath16);
    
    private JPanel panelRawDataMergeSize = new JPanel();
    private JLabel labelRawDataMergeSize = new JLabel("Raw Data Merge Size");
    private JSpinner spinnerRawDataMergeSize = new JSpinner();
    
    private int label_width = 150;
    public FConfig()
    {
        super(FMainWindow.getInstance(), "Configuration", true);
        //
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setIconImage(SIconUtil.imageIconConfig32.getImage());
        getRootPane().setDefaultButton(buttonOk);
        //
        //
        setLayout(new BorderLayout(6, 6));
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));
        panelRowLookAndFeel.setLayout(new BoxLayout(panelRowLookAndFeel, BoxLayout.LINE_AXIS));
        panelRowLoadingPath.setLayout(new BoxLayout(panelRowLoadingPath, BoxLayout.LINE_AXIS));
        panelRawDataMergeSize.setLayout(new BoxLayout(panelRawDataMergeSize, BoxLayout.LINE_AXIS));
        //
        //
        add(panelMain, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        panelMain.add(panelRowLookAndFeel);
        panelMain.add(panelRowLoadingPath);
        panelMain.add(panelRawDataMergeSize);
        
        panelMain.add(Box.createHorizontalGlue());
        
        panelRowLookAndFeel.add(labelLookAndFeel);
        panelRowLookAndFeel.add(comboboxLookAndFeel);
        
        panelRowLoadingPath.add(labelLoadingPath);
        panelRowLoadingPath.add(textLoadingPath);
        panelRowLoadingPath.add(buttonLoadingPath);
        
        panelRawDataMergeSize.add(labelRawDataMergeSize);
        panelRawDataMergeSize.add(spinnerRawDataMergeSize);
        spinnerRawDataMergeSize.setValue(10);
        
        panelBottom.add(buttonOk);
        panelBottom.add(buttonCancel);
        //
        //
        labelLookAndFeel.setHorizontalAlignment(JLabel.CENTER);
        labelLoadingPath.setHorizontalAlignment(JLabel.CENTER);
        labelRawDataMergeSize.setHorizontalAlignment(JLabel.CENTER);
        
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //
        buttonOk.setIcon(SIconUtil.imageIconOk16);
        buttonCancel.setIcon(SIconUtil.imageIconCancel16);
        buttonLoadingPath.setIcon(SIconUtil.imageIconLoadingPath16);
        //
        //
        buttonOk.setActionCommand("OK");
        buttonCancel.setActionCommand("Cancel");
        buttonLoadingPath.setActionCommand("LoadingPath");
        //
        buttonOk.addActionListener(this);
        buttonCancel.addActionListener(this);
        buttonLoadingPath.addActionListener(this);
        //
        //
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo look : looks)
        {
            comboboxLookAndFeel.addItem(look.getClassName());
        }
        //
        //
        readConfig();
        //
        //
        comboboxLookAndFeel.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                CConfig.getInstance().setLookAndFeel((String) comboboxLookAndFeel.getSelectedItem());
            }
        });
        //
        //
        labelLookAndFeel.setPreferredSize(new Dimension(label_width, labelLookAndFeel.getPreferredSize().height));
        labelLoadingPath.setPreferredSize(new Dimension(label_width, labelLoadingPath.getPreferredSize().height));
        labelRawDataMergeSize.setPreferredSize(new Dimension(label_width, labelRawDataMergeSize.getPreferredSize().height));
        
        labelLookAndFeel.setMinimumSize(new Dimension(label_width, labelLookAndFeel.getPreferredSize().height));
        labelLoadingPath.setMinimumSize(new Dimension(label_width, labelLoadingPath.getPreferredSize().height));
        labelRawDataMergeSize.setMinimumSize(new Dimension(label_width, labelRawDataMergeSize.getPreferredSize().height));
        //
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }
    
    public void readConfig()
    {
        textLoadingPath.setText(CConfig.getInstance().loadingPath);
        spinnerRawDataMergeSize.setValue(CConfig.getInstance().rawDataMergeSize);
        comboboxLookAndFeel.setSelectedItem(CConfig.getInstance().getLookAndFeel());
    }
    
    public void saveConfig()
    {
        CConfig.getInstance().loadingPath = textLoadingPath.getText();
        CConfig.getInstance().setLookAndFeel((String) comboboxLookAndFeel.getSelectedItem());
        CConfig.getInstance().rawDataMergeSize = (int)spinnerRawDataMergeSize.getValue();
        CConfig.getInstance().saveConfig();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case "OK":
                saveConfig();
                dispose();
                break;
            case "Cancel":
                dispose();
                break;
            case "LoadingPath":
                JFileChooser chooserLoading = new JFileChooser();
                chooserLoading.setCurrentDirectory(new File(CConfig.getInstance().loadingPath));
                chooserLoading.setDialogTitle("Loading folder");
                chooserLoading.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooserLoading.setMultiSelectionEnabled(true);
                chooserLoading.setAcceptAllFileFilterUsed(false);
                if (chooserLoading.showOpenDialog(FConfig.this) == JFileChooser.APPROVE_OPTION)
                {
                    textLoadingPath.setText(chooserLoading.getSelectedFile().getAbsolutePath());
                }
                break;
            default:
                break;
        }
    }
}
