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
		/* sample test for new address creation */
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
        addr.setEmail("azAZ09_@mail.com");
        
        _uidriver.setAddress(addr);
        // click to save
        _uidriver.clickSave();
        int aftsize = _uidriver.numberOfAddresses();
        System.out.println(aftsize);
        String [] addrval = _uidriver.addressEntryAt(aftsize-1);
        
        Assert.assertEquals(expsize, aftsize);
        Assert.assertEquals("a_-zAZ", addrval[0]);
	}

}
