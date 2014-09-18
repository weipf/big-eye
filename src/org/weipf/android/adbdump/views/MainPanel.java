package org.weipf.android.adbdump.views;

import org.weipf.android.adbdump.Contants;
import org.weipf.android.adbdump.model.AdbDump;
import org.weipf.android.adbdump.model.DumpReader;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class MainPanel extends JPanel implements ActionListener{
    
    private JComboBox<String> jComboBox = null;
    
    private JButton jbRefreshDevice = null;

	public MainPanel() {
	    this.setLayout(new BorderLayout());
	    
        this.add(new TabDumpPane(Contants.DEFAULT_TAB_PLACEMENT, JTabbedPane.WRAP_TAB_LAYOUT),
                BorderLayout.CENTER);
        
        jComboBox = new JComboBox<String>();
//        jComboBox.setBorder(BorderFactory.createTitledBorder("Select Device:"));
        jComboBox.addActionListener(this);

        jbRefreshDevice = new JButton("Re-Connect Devices");
        jbRefreshDevice.addActionListener(this);
        
        JPanel topPannel = new JPanel();
        topPannel.setLayout(new FlowLayout(FlowLayout.LEADING));
        topPannel.setBorder(BorderFactory.createTitledBorder("Devices:"));
        topPannel.add(jComboBox);
        topPannel.add(jbRefreshDevice);
        this.add(topPannel, BorderLayout.NORTH);
        AdbDump.getInstance().getConnectedDevices(mConnectedDeviceReader);
	}

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == jComboBox) {
            System.out.println("actionPerformed - " + jComboBox.getSelectedItem());
            if (jComboBox.getSelectedItem() != null) {
                AdbDump.getInstance().setDeviceId(jComboBox.getSelectedItem().toString());
            }
        } else if (arg0.getSource() == jbRefreshDevice) {
            AdbDump.getInstance().getConnectedDevices(mConnectedDeviceReader);
        }
    }
    
    
    DumpReader mConnectedDeviceReader = new DumpReader() {
        
        private static final String HEADER = "List of devices attached";
        
        private static final String SUFIX_TO_REMOVE = "device";
            
            @Override
            public void readDump(InputStream inpuStream) {
                jComboBox.removeAllItems();
                BufferedInputStream in = new BufferedInputStream(inpuStream);  
                BufferedReader inBr = new BufferedReader(new InputStreamReader(in));  
                String tmp = null;
                try {
                    while ((tmp = inBr.readLine()) != null) {
                        if (tmp == null || tmp.length() <= 0) {
                            break;
                        }
                        if (HEADER.contains(tmp.trim())) {
                            continue;
                        }
                        if (tmp.contains(SUFIX_TO_REMOVE)) {
                            jComboBox.addItem(tmp.substring(0, tmp.indexOf(SUFIX_TO_REMOVE)).trim());
                        }
                    }
                    AdbDump.getInstance().setDeviceId(jComboBox.getItemAt(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void readErrorDump(InputStream inputStream) {
                System.out.println("ReadErrorDump/in");
                StringBuilder errorMessage = new StringBuilder();
                if (inputStream == null) {
                    errorMessage.append("Unknown error!");
                }
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String tmp = null;
                try {
                    while((tmp = bufferedReader.readLine()) != null) {
                        errorMessage.append(tmp);
                    }
                } catch (IOException e) {
                    errorMessage.append("Cannot read error message! - " + e.getMessage());
                }
                System.out.println("ReadErrorDump/error message:" + errorMessage);
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(errorMessage);
            }
        };
	
}
