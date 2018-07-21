package addressbook.ui;

import java.lang.reflect.Field;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

import addressbook.core.Address;
import addressbook.db.IAddressDao;

/**
 * Utility class to drive the GUI. Reflection is used to access "private" members. 
 * @author ssome
 *
 */
public class UIDriver {
	AddressFrame app;
	AddressActionPanel addressActionPanel;
	
	public UIDriver(IAddressDao db) {
		app = new AddressFrame(db);
		app.setVisible(true);	
		addressActionPanel = getAddressActionPanel();
	}
	
	private AddressActionPanel getAddressActionPanel() {
		Field addressActionPanelF;
		try {
			addressActionPanelF = app.getClass().getDeclaredField("addressActionPanel");
	        addressActionPanelF.setAccessible(true);
	        AddressActionPanel addressActionPanel = (AddressActionPanel) addressActionPanelF.get(app);
			return addressActionPanel;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	private AddressPanel getAddressPanel() {
		Field addressPanelF;
		try {
			addressPanelF = app.getClass().getDeclaredField("addressPanel");
		    addressPanelF.setAccessible(true);
		    AddressPanel addressPanel = (AddressPanel) addressPanelF.get(app);
		    return addressPanel;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private AddressListPanel getAddressListPanel() {
		Field addressListPanelF;
		try {
			addressListPanelF = app.getClass().getDeclaredField("addressListPanel");
	        addressListPanelF.setAccessible(true);
	        AddressListPanel addressListPanel = (AddressListPanel) addressListPanelF.get(app);
			return addressListPanel;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	DefaultListModel getListModel() {
		AddressListPanel panel = getAddressListPanel();
		Field modelF;
		try {
			modelF = panel.getClass().getDeclaredField("model");
	        modelF.setAccessible(true);
	        DefaultListModel model = (DefaultListModel) modelF.get(panel);
			return model;	
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	int numberOfAddresses() {
		return getListModel().size();
	}
	
	String[] addressEntryAt(int index) {
		DefaultListModel model = getListModel();
		ListEntry entry = (ListEntry) model.getElementAt(index);
		String[] res = new String[3];
		res[0] = entry.getFirstName();
		res[1] = entry.getLastName();
		res[2] = entry.getMiddleName();
		return res;
	}
	
	private JButton getActionPanelButton(AddressActionPanel addressActionPanel, String button) {
		Field field;
		try {
			field = addressActionPanel.getClass().getDeclaredField(button);
	        field.setAccessible(true);
	        JButton btn = (JButton) field.get(addressActionPanel);
			return btn;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private JButton getBtnSaveAddress(AddressActionPanel addressActionPanel) {
		return getActionPanelButton(addressActionPanel, "btnSaveAddress");
	}

	private JButton getBtnNewAddress(AddressActionPanel addressActionPanel) {
		return getActionPanelButton(addressActionPanel, "btnNewAddress");
	}

	private JButton getBtnDeleteAddress(AddressActionPanel addressActionPanel) {
		return getActionPanelButton(addressActionPanel, "btnDeleteAddress");
	}
	
	private JButton getBtnEditAddress(AddressActionPanel addressActionPanel) {
		return getActionPanelButton(addressActionPanel, "btnEditAddress");
	}
	
	private JButton getBtnCancelAddress(AddressActionPanel addressActionPanel) {
		return getActionPanelButton(addressActionPanel, "btnCancelAddress");
	}

	void clickNew() {
		JButton btnNewAddress = getBtnNewAddress(addressActionPanel);
        btnNewAddress.doClick();
	}
	
	void clickDelete() {
		JButton btnDeleteAddress = getBtnDeleteAddress(addressActionPanel);
        btnDeleteAddress.doClick();
	}
	
	void clickEdit() {
		JButton btnEditAddress = getBtnEditAddress(addressActionPanel);
        btnEditAddress.doClick();
	}
	
	void clickSave() {
		JButton btnSaveAddress = getBtnSaveAddress(addressActionPanel);
        btnSaveAddress.doClick();
	}
	
	void clickCancel() {
		JButton btnCancelAddress = getBtnCancelAddress(addressActionPanel);
        btnCancelAddress.doClick();
	}
	
	void setAddress(Address addr) {
		AddressPanel addressPanel = getAddressPanel();
        addressPanel.setAddress(addr);
	}
	
	Address getAddress() {
		AddressPanel addressPanel = getAddressPanel();
        return addressPanel.getAddress();
	}
	
	void selectAddress(int sel) {
		AddressListPanel addressListPanel = getAddressListPanel();
        addressListPanel.setSelectedIndex(sel);
	}
}
