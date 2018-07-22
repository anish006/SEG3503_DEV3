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
import com.mockrunner.jdbc.JDBCTestModule;
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

public class DbtestWithMocks {
	
	 private AddressDao aDao;
	 private Address address;
	 private MockResultSet result; 
	 PreparedStatementResultSetHandler resultHandler;
	 MockStatement mStatement;
	 MockConnection connection;
	private JDBCMockObjectFactory mockFactory;
	private JDBCTestModule jdbcModule;
	
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
		 prepareResultSet();
		 address = new Address(lastName, firstName, middleName, phone, email, address1, address2, city, state, 
				 postalCode, country, id);
		 mStatement=new MockStatement(connection);
		 aDao = new AddressDao( );
		 aDao.connect();
		 //aDao.setDbaccess(connection);
	 }
	 
	 private void prepareResultSet() {
		 mockFactory = new JDBCMockObjectFactory();
		 jdbcModule = new JDBCTestModule(mockFactory);
		 connection = mockFactory.getMockConnection();
		 PreparedStatementResultSetHandler statementHandler =
		 connection.getPreparedStatementResultSetHandler();
		 MockPreparedStatement mockstatement =
		 new MockPreparedStatement(connection, "SELECT * from CUSTOMER");
		 statementHandler.addPreparedStatement(mockstatement);
		 /*MockResultSet*/ result = statementHandler.createResultSet();
		 result.addColumn("ID");
		 result.addColumn("LASTNAME");
		 result.addColumn("FIRSTNAME");
		 result.addColumn("MIDDLENAME");
		 result.addColumn("PHONE");
		 result.addColumn("EMAIL");
		 result.addColumn("ADDRESS1");
		 result.addColumn("ADDRESS2");
		 result.addColumn("CITY");
		 result.addColumn("STATE");
		 result.addColumn("POSTALCODE");
		 result.addColumn("COUNTRY");
		 List<Object> rowItems = new ArrayList<Object>();
		 rowItems.add(1);
		 rowItems.add("Bruce");
		 rowItems.add("Wayne");
		 rowItems.add("the godfather");
		 rowItems.add("666");
		 rowItems.add("a@a.com");
		 rowItems.add("a");
		 rowItems.add("a");
		 rowItems.add("a");
		 rowItems.add("a");
		 rowItems.add("A1B2C3");
		 rowItems.add("Gondor");
		 result.addRow(rowItems);
		 rowItems.clear();
		 rowItems.add(2);
		 rowItems.add("John");
		 rowItems.add("Doe");
		 result.addRow(rowItems);
		 statementHandler.prepareGlobalResultSet(result);
		 }
	 
	 @After
	 public void tearDown() throws Exception { 
		 //super.tearDown();
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
		 List<Object> rowItems = new ArrayList<Object>();
		 rowItems.add(-1);
		 rowItems.add(lastName);
		 rowItems.add(firstName);
		 rowItems.add(middleName);
		 rowItems.add(phone);
		 rowItems.add(email);
		 rowItems.add(address1);
		 rowItems.add(address2);
		 rowItems.add(city);
		 rowItems.add(state);
		 rowItems.add(postalCode);
		 rowItems.add(country);
		 result.addRow(rowItems);
		 //resultHandler.prepareGlobalGeneratedKeys(result);
		 int expected = -1;
		 int actual = aDao.saveRecord(address);
		 System.out.println(actual);
		 assertEquals(expected, actual);
		 jdbcModule.verifyPreparedStatementPresent(strSaveAddress);
		 jdbcModule.verifySQLStatementExecuted(strSaveAddress);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 1, lastName);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 2, firstName);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 3, middleName);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 4, phone);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 5, email);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 6, address1);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 7, address2);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 8, city);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 9, state);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 10, postalCode);
		 jdbcModule.verifySQLStatementParameter(strSaveAddress, 0, 11, country);
	 }
	 
	 @Test
	 public void testEditRecord() throws SQLException {
		 int id = 0;
		 List<Object> rowItems = new ArrayList<Object>();
		 rowItems.add(0);
		 rowItems.add(lastName);
		 rowItems.add(firstName);
		 rowItems.add(middleName);
		 rowItems.add(phone);
		 rowItems.add(email);
		 rowItems.add(address1);
		 rowItems.add(address2);
		 rowItems.add(city);
		 rowItems.add(state);
		 rowItems.add(postalCode);
		 rowItems.add(country);
		 result.addRow(rowItems);
		 String changedLastName = "John";
		 String changedFirstName = "Wayne";
		 //result.getRow(0).set(1, changedLastName);
		 //result.getRow(0).set(2, changedFirstName);
		 //resultHandler.prepareGlobalGeneratedKeys(result);
		 aDao.saveRecord(address);
		 address.setLastName("John");
		 address.setFirstName("Wayne");
		 boolean expected = true;
		 boolean actual = aDao.editRecord(address);
		 assertEquals(expected, actual);
		 jdbcModule.verifyPreparedStatementPresent(strUpdateAddress);
		 jdbcModule.verifySQLStatementExecuted(strUpdateAddress);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 1, changedLastName);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 2, changedFirstName);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 3, middleName);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 4, phone);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 5, email);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 6, address1);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 7, address2);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 8, city);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 9, state);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 10, postalCode);
		 jdbcModule.verifySQLStatementParameter(strUpdateAddress, 0, 11, country);
	 }
	 
	 @Test
	 public void testDeleteRecord() throws SQLException {
		 int id = 1;
		 result.addRow(new Object[] { id, 
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
		 jdbcModule.verifyPreparedStatementPresent(strDeleteAddress);
		 jdbcModule.verifySQLStatementExecuted(strDeleteAddress);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 1, id);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 2, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 3, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 4, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 5, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 6, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 7, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 8, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 9, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 10, null);
		 jdbcModule.verifySQLStatementParameter(strDeleteAddress, 0, 11, null);
	 }
	 
	 @Test
	 public void testGetAddress() {
		 
		 result.addColumn("LASTNAME", new Object[]{ lastName });
		 result.addColumn("FIRSTNAME", new Object[]{ firstName });
		 result.addColumn("MIDDLENAME", new Object[]{ middleName });
		 result.addColumn("PHONE", new Object[]{ phone });
		 result.addColumn("EMAIL", new Object[]{ email });
		 result.addColumn("ADDRESS1", new Object[]{ address1 } );
		 result.addColumn("ADDRESS2", new Object[]{ address2 });
		 result.addColumn("CITY", new Object[]{ city });
		 result.addColumn("STATE", new Object[]{ state });
		 result.addColumn("POSTALCODE", new Object[]{ postalCode });
		 result.addColumn("COUNTRY", new Object[]{ country });
		 result.addColumn("ID", new Object[]{ id });
		 //resultHandler.prepareResultSet(strGetAddress, result);
		 //resultHandler.prepareGlobalGeneratedKeys(result);
		 Address expected = address;
		 Address actual = aDao.getAddress(id);		
		 assertTrue(expected.equals(actual));
		 jdbcModule.verifyPreparedStatementPresent(strGetAddress);
		 jdbcModule.verifySQLStatementExecuted(strGetAddress);
		 jdbcModule.verifySQLStatementParameter(strGetAddress, 0, 1, id);
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
		 
		 jdbcModule.verifySQLStatementExecuted(strGetListEntries);
	 }
	 
	
	
	

}
