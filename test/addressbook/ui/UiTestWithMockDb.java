package addressbook.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.IAddressDao;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class UiTestWithMockDb {
	UIDriver _uidriver;
	
	@Mock
    private IAddressDao mockedDb;

	@Before
	public void setUp() throws Exception {
	}
	@Test
	public void test1() {
		System.out.println("test new");
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        System.out.println(befsize);
        int expsize = befsize+1;
        _uidriver.clickNew(); 
        Address addr = new Address();
 
        addr.setId(-1);
        addr.setLastName("a_-zAZ");
        addr.setFirstName("a_-zAZ");
        addr.setMiddleName("a_-zAZ");
        addr.setCity("a_-zAZ");
        addr.setState("a_-zAZ");
        addr.setCountry("a_-zAZ");
        addr.setAddress1("a_-zAZ19");
        addr.setAddress2("a_-zAZ19");
        addr.setPhone("666-6969");
        addr.setEmail("azAZ09_@azAZ.com");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        System.out.println(aftsize);
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("a_-zAZ", addrval[0]);
	}
	
	@Test
	public void test2() {
		// add an address
		System.out.println("test delete");
		/* Setting up a mock with one address */
		when(mockedDb.connect()).thenReturn(true);
        ListEntry listEntry = new ListEntry("John","Wayne","",0);
        List<ListEntry> list = new ArrayList<ListEntry>();
        list.add(listEntry);
        when(mockedDb.getListEntries()).thenReturn(list);
        /* initiating a UiDriver with the above mock */
        _uidriver = new UIDriver(mockedDb);
        int num = _uidriver.numberOfAddresses();
        System.out.println("Initial size : " + num);
        int expsize = num-1;
        /* delete the address */
		_uidriver.selectAddress(0);
		_uidriver.clickDelete();
		int aftsize = _uidriver.numberOfAddresses();
		System.out.println("size after delete : " + aftsize);
		Assert.assertEquals(expsize, aftsize);
	}
	
	@Test
	public void test3() {
		// add an address
		System.out.println("test Edit");
		/* Setting up a mock with one address */
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("John");
        addr.setFirstName("Wayne");
		when(mockedDb.connect()).thenReturn(true);
        ListEntry listEntry = new ListEntry("John","Wayne","",0);
        List<ListEntry> list = new ArrayList<ListEntry>();
        list.add(listEntry);
        when(mockedDb.getListEntries()).thenReturn(list);
        when(mockedDb.getAddress(0)).thenReturn(addr);
        /* initiating a UiDriver with the above mock */
        _uidriver = new UIDriver(mockedDb);
        /* select the address */
		_uidriver.selectAddress(0);
		System.out.println("initial name : "+_uidriver.getAddress().getLastName());
		Assert.assertEquals("John",_uidriver.getAddress().getLastName());
		Assert.assertEquals("Wayne",_uidriver.getAddress().getFirstName());
		
		/* add modifications to the address */
		_uidriver.clickEdit();
		addr = new Address();
        addr.setId(0);
        addr.setLastName("a_-zAZ");
        addr.setFirstName("a_-zAZ");
        addr.setMiddleName("a_-zAZ");
        addr.setCity("a_-zAZ");
        addr.setState("a_-zAZ");
        addr.setCountry("a_-zAZ");
        addr.setAddress1("a_-zAZ19");
        addr.setAddress2("a_-zAZ19");
        addr.setPhone("666-6969");
        addr.setEmail("azAZ09_@azAZ.com");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        System.out.println("name after modification : "+_uidriver.getAddress().getLastName());

        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getCity());
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getState());
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getCountry());
        Assert.assertEquals("a_-zAZ19", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("a_-zAZ19", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("666-6969", _uidriver.getAddress().getPhone());
        Assert.assertEquals("azAZ09_@azAZ.com", _uidriver.getAddress().getEmail());
        		
	}

}
