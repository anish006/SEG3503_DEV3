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
import addressbook.core.AddressException;
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
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
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
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test2() {
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
		System.out.println("size after delete : " +aftsize);
		Assert.assertEquals(expsize, aftsize);
		
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test3() {
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
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
		
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
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test4() {
		System.out.println("test Cancel");
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
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
		
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
        _uidriver.clickCancel();
        System.out.println("name after cancel : "+_uidriver.getAddress().getLastName());

        Assert.assertEquals("John", _uidriver.getAddress().getLastName());
        Assert.assertEquals("Wayne", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());   
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test5() {
		System.out.println("test save");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
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
        addr.setPostalCode("A1Z9A1");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        System.out.println(aftsize);
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        /* verify if saved as worked and address is added to the list */
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("a_-zAZ", addrval[0]);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test6() {
		System.out.println("test save");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
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
        addr.setPostalCode("A1Z 9A1");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        System.out.println(aftsize);
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        /* verify if saved as worked and address is added to the list */
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("a_-zAZ", addrval[0]);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test7() {
		System.out.println("test save");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        System.out.println(befsize);
        int expsize = befsize+1;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("a_-zAZ");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        System.out.println(aftsize);
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        /* verify if saved as worked and address is added to the list */
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("a_-zAZ", addrval[1]);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test8() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test9() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("_A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test10() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test11() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setFirstName("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test12() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setFirstName("_A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test13() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setMiddleName("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test14() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setMiddleName("_a");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test15() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPhone("012-345");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test16() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPhone("1A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test17() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setEmail("aa");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test18() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setEmail("*@a");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test19() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setEmail("a@1");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test20() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setEmail("a@a.");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test21() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setCity("_A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test22() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setCity("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test23() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setState("_a");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test24() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setState("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test25() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPostalCode("A1A1A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test26() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPostalCode("A1A1A1A");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test27() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPostalCode("a1a1a1");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test28() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setPostalCode("AAAAAA");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
	@Test
	public void test29() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setCountry("_a");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}

	@Test
	public void test30() {
		System.out.println("test save with invalid data");
        List<ListEntry> list = new ArrayList<ListEntry>();
        when(mockedDb.getListEntries()).thenReturn(list);
		when(mockedDb.connect()).thenReturn(true);
		_uidriver = new UIDriver(mockedDb);
        int befsize = _uidriver.numberOfAddresses();
        int expsize = befsize;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("AZ");
        addr.setCountry("a*");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        
        /* sized should not have changed since save should fail */
        Assert.assertEquals(expsize, aftsize);
        
        verify(mockedDb).connect();
        verify(mockedDb).getListEntries();
	}
	
}
