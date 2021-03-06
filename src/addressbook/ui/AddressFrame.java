package addressbook.ui;
import addressbook.core.Address;
import addressbook.core.AddressException;
import addressbook.db.IAddressDao;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AddressFrame 
        extends javax.swing.JFrame 
        implements ActionListener, ListSelectionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates new form AddressFrame */
    public AddressFrame(IAddressDao db) {
    	this.db = db;
        initComponents();
        loadFrameIcon();
        windowAdapter = new WindowCloser();
        this.addWindowListener(windowAdapter);
        db.connect();
        addressActionPanel.addActionListener(this);
        addressPanel.setEditable(false);
        List<ListEntry> entries = db.getListEntries();
        addressListPanel.addListEntries(entries);
        addressListPanel.addListSelectionListener(this);
    }
    
   
    /**
     * Load our own "address book" icon into our frame window.
     */
    private void loadFrameIcon() {
        URL imgUrl = null;
        ImageIcon imgIcon = null;
        
        imgUrl = AddressFrame.class.getResource("../resource/addressbook32.gif");
        imgIcon = new ImageIcon(imgUrl);
        Image img = imgIcon.getImage();
        this.setIconImage(img);
        
    }    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        addressActionPanel = new addressbook.ui.AddressActionPanel();
        addressPanel = new addressbook.ui.AddressPanel();
        addressListPanel = new addressbook.ui.AddressListPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Address Book Demo");
        getContentPane().add(addressActionPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(addressPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(addressListPanel, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    
    private void cancelAddress() {
        addressPanel.clear();
        addressPanel.setEditable(false);
        ListEntry entry = addressListPanel.getSelectedListEntry();
        if (entry != null) {
            int id = entry.getId();
            Address address = db.getAddress(id);
            addressPanel.setAddress(address);
        }
    }
    
    private void newAddress(){
        addressPanel.clear();
        addressPanel.setEditable(true);
        
    }
    
    private void deleteAddress() {
        int id = addressPanel.getId();
        if (id != -1) {
            db.deleteRecord(id);
            int selectedIndex = addressListPanel.deleteSelectedEntry();
            if (selectedIndex >= 0) {
                selectedIndex = addressListPanel.setSelectedIndex(selectedIndex);
                ListEntry entry = addressListPanel.getSelectedListEntry();
                if (entry != null) {
                    id = entry.getId();
                    Address address = db.getAddress(id);
                    addressPanel.setAddress(address);
                } else {
                    addressPanel.clear();
                }
            }
        } else {
            addressPanel.clear();
        }
        addressPanel.setEditable(false);
        
        
        
    }
    
    private void editAddress() {
        int selected = addressListPanel.getSelectedIndex();
        if (selected >=0) {
            addressPanel.setEditable(true);
        }
        
    }
    
    private void saveAddress() {
    	try {
    		if (addressPanel.isEditable()) {
    			Address address = addressPanel.getAddress();
				address.validate();
				int id = address.getId();
                String lname = address.getLastName();
                String fname = address.getFirstName();
                String mname = address.getMiddleName();
            if (id == -1) {
                id = db.saveRecord(address);
                address.setId(id);

                ListEntry entry = new ListEntry(lname, fname, mname, id);
                addressListPanel.addListEntry(entry);

            } else {
                db.editRecord(address);
                ListEntry entry = addressListPanel.getSelectedListEntry();
                entry.setFirstName(fname);
                entry.setMiddleName(mname);
                entry.setLastName(lname);
                addressListPanel.repaint();
            }
            addressPanel.setEditable(false);
    		}
		} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("ActionEvent: " + actionCommand);
        if (actionCommand.equalsIgnoreCase("CANCEL_ADDRESS")) {
            cancelAddress();
        } else if (actionCommand.equalsIgnoreCase("NEW_ADDRESS")) {
            newAddress();
        } else if (actionCommand.equalsIgnoreCase("DELETE_ADDRESS")) {
            deleteAddress();
        } else if (actionCommand.equalsIgnoreCase("EDIT_ADDRESS")) {
            editAddress();
        } else if (actionCommand.equalsIgnoreCase("SAVE_ADDRESS")) {
            saveAddress();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        JList entryList = (JList) e.getSource();
        selectedEntry = entryList.getSelectedIndex();
        ListEntry entry = (ListEntry)entryList.getSelectedValue();
        if (entry != null) {
            int id = entry.getId();
            Address address = db.getAddress(id);
            addressPanel.setAddress(address);
        } else {
            addressPanel.clear();
        }
    }
    
    public void setDb(IAddressDao db) {
		this.db = db;
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private addressbook.ui.AddressActionPanel addressActionPanel;
    private addressbook.ui.AddressListPanel addressListPanel;
    private addressbook.ui.AddressPanel addressPanel;
    // End of variables declaration//GEN-END:variables
    
    private int selectedEntry = -1;
    private IAddressDao db;


	private WindowAdapter windowAdapter;
    
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            db.disconnect();
        }
    
    }
}
