/**
 * 
 */
package addressbook.ui;
import static com.ninja_squad.dbsetup.Operations.*;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.db.DbAccess;
import addressbook.db.IAddressDao;
import addressbook.db.IDbAccess;



/**
 * @author alex nishimwe
 *
 */
public class UiTestWithDbSetup {
	
	private UIDriver _uidriver;
	private AddressDao addressDao;
	private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
	
	public static final Operation DELETE_ALL =
	        deleteAllFrom("APP.ADDRESS");
	/*
	public static final Operation INSERT_REFERENCE_DATA =
	        sequenceOf(
	            insertInto("APP.ADDRESS")
	                .columns("LASTNAME", "FIRSTNAME", "MIDDLENAME", "PHONE", "EMAIL", "ADDRESS1", "ADDRESS2", "CITY",
	                		"STATE", "POSTALCODE", "COUNTRY")
	                .values("John", "Wayne", "", "", "", "", "", "", "", "","")
	                .build());*/
	@Before
    public void prepare() throws Exception {
        Operation operation =
            sequenceOf(
                DELETE_ALL,/*
                INSERT_REFERENCE_DATA,*/
                insertInto("APP.ADDRESS")
                .columns("LASTNAME", "FIRSTNAME", "MIDDLENAME", "PHONE", "EMAIL", "ADDRESS1", "ADDRESS2", "CITY",
                		"STATE", "POSTALCODE", "COUNTRY")
                .values("John", "Wayne", "", "", "", "", "", "", "", "", "")
                .build());
        
        DbSetup dbSetup = new DbSetup(new DriverManagerDestination("jdbc:derby:"+ "DefaultAddressBook", "addressuser", "addressuser"), operation);
        
        addressDao = new AddressDao();
        
        dbSetup.launch();
    }
	
	@Test
	public void test01() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test new");
        _uidriver = new UIDriver(addressDao);
		int beforeSize = _uidriver.numberOfAddresses();
        int expectedSize = beforeSize+1;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(-1);
        addr.setLastName("a_-zAZ");
        addr.setMiddleName("a_-zAZ");
        addr.setFirstName("a_-zAZ");
        addr.setCity("a_-zAZ");
        addr.setState("a_-zAZ");
        addr.setCountry("a_-zAZ");
        addr.setAddress1("a_-zAZ19");
        addr.setAddress2("a_-zAZ19");
        addr.setPostalCode("A1Z9A1");
        addr.setPhone("666-6969");
        addr.setEmail("azAZ_@azAZ.com");
        
        _uidriver.setAddress(addr);
        _uidriver.clickSave(); 
        int afterSize = _uidriver.numberOfAddresses();
        String [] addrval = _uidriver.addressEntryAt(afterSize-1);        
        Assert.assertEquals(expectedSize, afterSize);
        Assert.assertEquals("a_-zAZ", addrval[1]);	
	}
	
	@Test
	public void test02() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test delete");
		_uidriver = new UIDriver(addressDao);
		int beforeSize = _uidriver.numberOfAddresses();
        int expectedSize = beforeSize-1;
        _uidriver.selectAddress(beforeSize-1);
        //last address value should be a_-zAZ
		String [] addrvalue = _uidriver.addressEntryAt(beforeSize-1);
		Assert.assertEquals("John", addrvalue[1]);
        
        _uidriver.clickDelete(); 
        int afterSize = _uidriver.numberOfAddresses();        
        Assert.assertEquals(expectedSize, afterSize);
      //if there is still addresses, last address value should not be a_-zAZ once delete is done
        if(afterSize>0) {
        	String [] addrval = _uidriver.addressEntryAt(afterSize-1);
        	Assert.assertNotEquals("a_-zAZ", addrval[1]);
        }
	}
	
	@Test
	public void test03() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Edit");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
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
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		//_uidriver.selectAddress(afterSize);
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

	@Test
	public void test4() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Cancel");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
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
        
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be the same after cancel
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
	}
	
	@Test
	public void test05() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 1");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("a_-zAZ");
        addr.setFirstName("a_-zAZ");
        addr.setMiddleName("a_-zAZ");
        addr.setCity("a_-zAZ");
        addr.setState("a_-zAZ");
        addr.setCountry("a_-zAZ");
        addr.setAddress1("a_-zAZ19");
        addr.setAddress2("a_-zAZ19");
        addr.setPostalCode("A1Z9A1");
        addr.setPhone("666-6969");
        addr.setEmail("azAZ09_@azAZ.com");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
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
	
	@Test
	public void test06() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 2");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("a_-zAZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("A1Z 9A1");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("A1Z 9A1", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test07() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 3");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("a_-zAZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("a_-zAZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test08() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 4 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test09() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 5 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("_A");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("_A", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test10() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 6 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("a*");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("a*", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test11() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 7 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("a*");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("a*", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test12() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 8 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("_A");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("_A", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test13() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 9 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("a*");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("a*", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test14() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 10 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("_a");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("_a", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test15() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 11 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("012-345");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("012-345", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test16() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 12 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("1A");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("1A", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test17() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 13 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("aa");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("aa", _uidriver.getAddress().getEmail());
	}
	
	
	@Test
	public void test18() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 13 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("*@a");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("*@a", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test19() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 14 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("a@*");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("a@*", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test20() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 15 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("a@a.");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
      //last address value should be a_-zAZ once Edit is done
		_uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("a@a.", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test21() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 16 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("_A");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("_A", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test22() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 17 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("a*");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("a*", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test23() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 18 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("_a");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("_a", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test24() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 19 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("a*");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("a*", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test25() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 20 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("A1A1A");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("A1A1A", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test26() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 21 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("A1A1A1A");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("A1A1A1A", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test27() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 22 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("a1a1a1");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("a1a1a1", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	
	@Test
	public void test28() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 23 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("AAAAAA");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("AAAAAA", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test29() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 24 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("_a");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("_a", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}
	
	@Test
	public void test30() {
		//dbSetupTracker.skipNextLaunch();
		System.out.println("test Save 25 with invalid data");
		_uidriver = new UIDriver(addressDao);
		int size = _uidriver.numberOfAddresses();
        _uidriver.selectAddress(size-1);
        //last address value should be John
		String [] addrvalue = _uidriver.addressEntryAt(size-1);
		Assert.assertEquals("John", addrvalue[1]);
        
		_uidriver.clickEdit();
		Address addr = new Address();
        addr.setId(0);
        addr.setLastName("AZ");
        addr.setFirstName("");
        addr.setMiddleName("");
        addr.setCity("");
        addr.setState("");
        addr.setCountry("a*");
        addr.setAddress1("");
        addr.setAddress2("");
        addr.setPostalCode("");
        addr.setPhone("");
        addr.setEmail("");
        _uidriver.setAddress(addr);
        _uidriver.clickSave();
        _uidriver.clickCancel();
        int afterSize = _uidriver.numberOfAddresses();      
        Assert.assertEquals(size, afterSize);
       _uidriver.selectAddress(afterSize);
        Assert.assertEquals("AZ", _uidriver.getAddress().getLastName());
        Assert.assertEquals("", _uidriver.getAddress().getFirstName());
        Assert.assertEquals("", _uidriver.getAddress().getMiddleName());
        Assert.assertEquals("", _uidriver.getAddress().getCity());
        Assert.assertEquals("", _uidriver.getAddress().getState());
        Assert.assertEquals("a*", _uidriver.getAddress().getCountry());
        Assert.assertEquals("", _uidriver.getAddress().getAddress1());
        Assert.assertEquals("", _uidriver.getAddress().getAddress2());
        Assert.assertEquals("", _uidriver.getAddress().getPostalCode());
        Assert.assertEquals("", _uidriver.getAddress().getPhone());
        Assert.assertEquals("", _uidriver.getAddress().getEmail());
	}


}


