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
import com.ninja_squad.dbsetup.operation.Operation;

import addressbook.core.Address;
import addressbook.db.IAddressDao;



/**
 * @author alonz
 *
 */
public class UiTestWithDbSetup {
	
	private DataSource dataSource;
	private UIDriver _uidriver;
	private static DbSetupTracker dbSetupTracker = new DbSetupTracker();
	
	public static final Operation DELETE_ALL =
	        deleteAllFrom("APP.ADDRESS");
	
	public static final Operation INSERT_REFERENCE_DATA =
	        sequenceOf(
	            insertInto("APP.ADDRESS")
	                .columns("LASTNAME", "FIRSTNAME", "MIDDLENAME", "PHONE", "EMAIL", "ADDRESS1", "ADDRESS2", "CITY",
	                		"STATE", "POSTALCODE", "COUNTRY")
	                .values("?", "?", "?", "?", "?", "?", "?", "?", "?", "?", "?")
	                .build());
	
	@Before
    public void prepare() throws Exception {
        Operation operation =
            sequenceOf(
                DELETE_ALL,
                INSERT_REFERENCE_DATA,
                insertInto("VENDOR")
                    .columns("ID", "CODE", "NAME", "COUNTRY_ID")
                    .values(1L, "AMA", "Amazon", 2)
                    .values(2L, "PMI", "PriceMinister", 1)
                    .build());
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), operation);
        
        dbSetup.launch();
    }
	
	@Test
	public void test01() {
		dbSetupTracker.skipNextLaunch();
		System.out.println("test new");
		int beforeSize = _uidriver.numberOfAddresses();
        int expectedSize = beforeSize+1;
        _uidriver.clickNew(); 
        Address addr = new Address();
        addr.setId(0);
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
        Assert.assertEquals("Wayne", addrval[0]);
		
	}
	

}


