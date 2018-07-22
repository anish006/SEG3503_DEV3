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
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.JDBCMockObjectFactory;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockPreparedStatement;
import com.mockrunner.mock.jdbc.MockResultSet;*/

import addressbook.core.Address;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;



/**
 * @author alonz
 *
 */
public class DbtestWithMocks {
	private IDbAccess table;
	/*private JDBCMockObjectFactory mockFactory;
	private JDBCTestModule jdbcModule;
	private MockConnection connection;*/
	
	private void prepareResultSet() {
		
       
	}
	
	
	@Before
	public void setup() throws ClassNotFoundException, SQLException {
		prepareResultSet();
		table = new DbAccess();
		table.connect();
	}
	
	@After
	public void teardown() {
		table.disconnect();
//		jdbcModule.verifyConnectionClosed();
	}
	
	
	
	

}
