package addressbook.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.IAddressDao;


public class UiTest {
	UIDriver _uidriver;
	
	@Before
	public void setUp() throws Exception {
		IAddressDao db = new AddressDao(); // using the real database
		_uidriver = new UIDriver(db);
	}

	@Test
	public void testNewAddress() {
		/* sample test for new address creation */
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize+1;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("John");
        addr.setFirstName("Wayne");
        addr.setPhone("5556666");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave(); 
        int aftsize = _uidriver.numberOfAddresses();
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("Wayne", addrval[0]);
	}

	@Test
	public void testDelete() {
		/* sample test for address deletion */
		// add an address 
		Address addr = new Address();
		addr.setId(-1);
	    addr.setLastName("Castanza");
	    addr.setFirstName("George");
	    addr.setPhone("22222222");	
	    _uidriver.clickNew();
		_uidriver.setAddress(addr);
        _uidriver.clickSave(); 
        // check that address has been added
        int num = _uidriver.numberOfAddresses();
        String [] addrval = _uidriver.addressEntryAt(num-1);
        Assert.assertEquals("George", addrval[0]);
        Assert.assertEquals("Castanza", addrval[1]);
        // remove last address
        num = _uidriver.numberOfAddresses();
        int expsize = num-1;		
		_uidriver.selectAddress(num-1);
		_uidriver.clickDelete();  
		int aftsize = _uidriver.numberOfAddresses();
		Assert.assertEquals(expsize, aftsize); 
		// last address if any should not be George anymore
		if (aftsize > 0) { 
			String [] addrval2 = _uidriver.addressEntryAt(aftsize-1);
			Assert.assertNotEquals("George", addrval2[0]);
			Assert.assertNotEquals("Castanza", addrval2[1]);      
		}
	}
	
	

}
