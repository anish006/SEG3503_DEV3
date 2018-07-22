/**
 * 
 */
package addressbook.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mockrunner.jdbc.CallableStatementResultSetHandler;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.jdbc.StatementResultSetHandler;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;
import java.util.List;


import addressbook.core.Address;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import addressbook.core.Address;
import addressbook.db.AddressDao;
import addressbook.ui.ListEntry;

import com.mockrunner.jdbc.BasicJDBCTestCaseAdapter;



/**
 * @author alonz
 *
 */
public class DbtestWithMocks {
	
	 private AddressDao aDao;
	 private Address address;
	 private MockResultSet resultSet; 
	 PreparedStatementResultSetHandler resultHandler;
	 MockStatement mStatement;
	 MockConnection connection;
	
	 private int id = 0;
	 private String lastName = "a_-zAZ";
	 private String firstName = "a_-zAZ";
	 private String middleName = "a_-zAZ";
	 private String phone = "666-6969";
	 private String email = "azAZ09_@azAZ.com";
	 private String address1 = "a_-zAZ19";
	 private String address2 = "a_-zAZ19";
	 private String city = "a_-zAZ";
	 private String state = "a_-zAZ";
	 private String postalCode = "a_-zAZ";
	 private String country = "Canada";
	 
	 @Before
	 public void setUp() throws Exception {
		 super.setUp();
		 JDBCMockObjectFactory mockFactory = getJDBCMockObjectFactory();
		  connection = getJDBCMockObjectFactory().getMockConnection(); 
		 resultHandler = connection.getPreparedStatementResultSetHandler();	     
		 resultSet = resultHandler.createResultSet();
		 mStatement=new MockStatement(connection);
		 aDao = new AddressDao();
		 aDao.connect();
		 address = new Address(lastName, 
				 firstName, 
				 middleName, 
				 phone, 
				 email, 
				 address1, 
				 address2, 
				 city, 
				 state, 
				 postalCode, 
				 country, 
				 id);
	 }
	 
	 @After
	 public void tearDown() throws Exception { 
		 super.tearDown();
	 }
	 
	 private static final String strGetAddress =
			 "SELECT * FROM APP.ADDRESS " +
		     "WHERE ID = ?";
	 
	 private static final String strSaveAddress =
			 "INSERT INTO APP.ADDRESS " +
		     "   (LASTNAME, FIRSTNAME, MIDDLENAME, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
		     "    CITY, STATE, POSTALCODE, COUNTRY) " +
		     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	 private static final String strGetListEntries =
			 "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.ADDRESS "  +
		     "ORDER BY LASTNAME ASC";
	 
	 private static final String strUpdateAddress =
			 "UPDATE APP.ADDRESS " +
		     "SET LASTNAME = ?, " +
		     "    FIRSTNAME = ?, " +
		     "    MIDDLENAME = ?, " +
		     "    PHONE = ?, " +
		     "    EMAIL = ?, " +
		     "    ADDRESS1 = ?, " +
		     "    ADDRESS2 = ?, " +
		     "    CITY = ?, " +
		     "    STATE = ?, " +
		     "    POSTALCODE = ?, " +
		     "    COUNTRY = ? " +
		     "WHERE ID = ?";
		    
	 private static final String strDeleteAddress =
			 "DELETE FROM APP.ADDRESS " +
		     "WHERE ID = ?";
	 
	
	 @Test
	 public void testSaveRecord() throws Exception {
		 int id = 0;
		 resultSet.addRow(new Object[] { id, 
				 lastName, 
				 firstName, 
				 middleName, 
				 phone, 
				 email, 
				 address1, 
				 address2, 
				 city, 
				 state, 
				 postalCode, 
				 country });
		 resultHandler.prepareGlobalGeneratedKeys(resultSet);
		 int expected = id;
		 int actual = aDao.saveRecord(address);
		 assertEquals(expected, actual);
		 verifyPreparedStatementPresent(strSaveAddress);
		 verifySQLStatementExecuted(strSaveAddress);
		 verifySQLStatementParameter(strSaveAddress, 0, 1, lastName);
		 verifySQLStatementParameter(strSaveAddress, 0, 2, firstName);
		 verifySQLStatementParameter(strSaveAddress, 0, 3, middleName);
		 verifySQLStatementParameter(strSaveAddress, 0, 4, phone);
		 verifySQLStatementParameter(strSaveAddress, 0, 5, email);
		 verifySQLStatementParameter(strSaveAddress, 0, 6, address1);
		 verifySQLStatementParameter(strSaveAddress, 0, 7, address2);
		 verifySQLStatementParameter(strSaveAddress, 0, 8, city);
		 verifySQLStatementParameter(strSaveAddress, 0, 9, state);
		 verifySQLStatementParameter(strSaveAddress, 0, 10, postalCode);
		 verifySQLStatementParameter(strSaveAddress, 0, 11, country);
	 }
	 
	 private void verifySQLStatementParameter(String strsaveaddress2, int i, int j, String firstName2) {
		// TODO Auto-generated method stub
		
	}

	private void verifySQLStatementExecuted(String strsaveaddress2) {
		// TODO Auto-generated method stub
		
	}

	private void verifyPreparedStatementPresent(String strsaveaddress2) {
		// TODO Auto-generated method stub
		
	}

	@Test
	 public void testEditRecord() throws SQLException {
		 int id = 0;
		 resultSet.addRow(new Object[] { id, 
				 "John", 
				 "Wayne", 
				 middleName, 
				 phone, 
				 email, 
				 address1, 
				 address2, 
				 city, 
				 state, 
				 postalCode, 
				 country });
		 String changedLastName = "John";
		 String changedFirstName = "Wayne";
		 resultSet.getRow(0).set(1, changedLastName);
		 resultSet.getRow(0).set(2, changedFirstName);
		 resultHandler.prepareGlobalGeneratedKeys(resultSet);
		 aDao.saveRecord(address);
		 address.setLastName("John");
		 address.setFirstName("Wayne");
		 boolean expected = true;
		 boolean actual = aDao.editRecord(address);
		 assertEquals(expected, actual);
		 verifyPreparedStatementPresent(strUpdateAddress);
		 verifySQLStatementExecuted(strUpdateAddress);
		 verifySQLStatementParameter(strUpdateAddress, 0, 1, changedLastName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 2, changedFirstName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 3, middleName);
		 verifySQLStatementParameter(strUpdateAddress, 0, 4, phone);
		 verifySQLStatementParameter(strUpdateAddress, 0, 5, email);
		 verifySQLStatementParameter(strUpdateAddress, 0, 6, address1);
		 verifySQLStatementParameter(strUpdateAddress, 0, 7, address2);
		 verifySQLStatementParameter(strUpdateAddress, 0, 8, city);
		 verifySQLStatementParameter(strUpdateAddress, 0, 9, state);
		 verifySQLStatementParameter(strUpdateAddress, 0, 10, postalCode);
		 verifySQLStatementParameter(strUpdateAddress, 0, 11, country);
	 }
	 
	 @Test
	 public void testDeleteRecord() throws SQLException {
		 int id = 1;
		 resultSet.addRow(new Object[] { id, 
				 lastName, 
				 firstName, 
				 middleName, 
				 phone, 
				 email, 
				 address1, 
				 address2, 
				 city, 
				 state, 
				 postalCode, 
				 country });
		 aDao.saveRecord(address);
		 boolean expected = true;
		 boolean actual = aDao.deleteRecord(id);
		 assertEquals(expected, actual);
		 verifyPreparedStatementPresent(strDeleteAddress);
		 verifySQLStatementExecuted(strDeleteAddress);
		 verifySQLStatementParameter(strDeleteAddress, 0, 1, id);
		 verifySQLStatementParameter(strDeleteAddress, 0, 2, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 3, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 4, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 5, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 6, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 7, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 8, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 9, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 10, null);
		 verifySQLStatementParameter(strDeleteAddress, 0, 11, null);
	 }
	 
	 @Test
	 public void testGetAddress() {
		 
		 resultSet.addColumn("LASTNAME", new Object[]{ lastName });
		 resultSet.addColumn("FIRSTNAME", new Object[]{ firstName });
		 resultSet.addColumn("MIDDLENAME", new Object[]{ middleName });
		 resultSet.addColumn("PHONE", new Object[]{ phone });
		 resultSet.addColumn("EMAIL", new Object[]{ email });
		 resultSet.addColumn("ADDRESS1", new Object[]{ address1 } );
		 resultSet.addColumn("ADDRESS2", new Object[]{ address2 });
		 resultSet.addColumn("CITY", new Object[]{ city });
		 resultSet.addColumn("STATE", new Object[]{ state });
		 resultSet.addColumn("POSTALCODE", new Object[]{ postalCode });
		 resultSet.addColumn("COUNTRY", new Object[]{ country });
		 resultSet.addColumn("ID", new Object[]{ id });
		 resultHandler.prepareResultSet(strGetAddress, resultSet, new HashMap());
		 resultHandler.prepareGlobalGeneratedKeys(resultSet);
		 Address expected = address;
		 Address actual = aDao.getAddress(id);		
		 assertTrue(expected.equals(actual));
		 verifyPreparedStatementPresent(strGetAddress);
		 verifySQLStatementExecuted(strGetAddress);
		 verifySQLStatementParameter(strGetAddress, 0, 1, id);
	}
	 
	 @Test
	 public void testGetListEntries() {
		 
		 StatementResultSetHandler statementHandler =
				 connection.getStatementResultSetHandler();
				 MockResultSet result = statementHandler.createResultSet();
				
				 
				 result.addColumn("ID", new Object[]{ id });
				 result.addColumn("LASTNAME", new Object[]{ lastName });
				 result.addColumn("FIRSTNAME", new Object[]{ firstName });
				 result.addColumn("MIDDLENAME", new Object[]{ middleName });
		
		 statementHandler.prepareResultSet(strGetListEntries,result);
				
		
		 List<ListEntry> listEntries = aDao.getListEntries();
		 ListEntry listEntry = listEntries.get(0);
			
		 int entryCount = listEntries.size();
		 assertEquals(1, entryCount);
			
		 assertEquals(id, listEntry.getId());	
		 assertEquals(lastName, listEntry.getLastName());
		 assertEquals(firstName, listEntry.getFirstName());
		 assertEquals(middleName, listEntry.getMiddleName());
		 
		 verifySQLStatementExecuted(strGetListEntries);
	 }
	 
	
	
	

}
