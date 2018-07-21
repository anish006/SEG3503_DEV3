package addressbook.db;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

public class DbAccess implements IDbAccess {
    
    /** Creates a new instance of DbAccess */
    public DbAccess() {
        this("DefaultAddressBook");
    }
    
    public DbAccess(String addressBookName) {
        this.dbName = addressBookName;
        
        setDBSystemDir();
        dbProperties = loadDBProperties();
        String driverName = dbProperties.getProperty("derby.driver"); 
        loadDatabaseDriver(driverName);
        if(!dbExists()) {
            createDatabase();
        }
        
    }
    
    private boolean dbExists() {
        boolean bExists = false;
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if (dbFileDir.exists()) {
            bExists = true;
        }
        return bExists;
    }
    
    private void setDBSystemDir() {
        // decide on the db system directory
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "/.addressbook";
        System.setProperty("derby.system.home", systemDir);
        
        // create the db system directory
        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
    }
    
    private void loadDatabaseDriver(String driverName) {
        // load Derby driver
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private Properties loadDBProperties() {
        InputStream dbPropInputStream = null;
        dbPropInputStream = DbAccess.class.getResourceAsStream("Configuration.properties");
        dbProperties = new Properties();
        try {
            dbProperties.load(dbPropInputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dbProperties;
    }
    
    
    private boolean createTables(Connection dbConnection) {
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateAddressTable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return bCreatedTables;
    }
    private boolean createDatabase() {
        boolean bCreated = false;
        Connection dbConnection = null;
        
        String dbUrl = getDatabaseUrl();
        dbProperties.put("create", "true");
        
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            bCreated = createTables(dbConnection);
        } catch (SQLException ex) {
        }
        dbProperties.remove("create");
        return bCreated;
    }
    
    
    /* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#connect()
	 */
    /* (non-Javadoc)
	 * @see addressbook.db.IDbAccess#connect()
	 */
    public boolean connect() {
        String dbUrl = getDatabaseUrl();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);            
            isConnected = dbConnection != null;
        } catch (SQLException ex) {
        	ex.printStackTrace();
            isConnected = false;
        }
        return isConnected;
    }
    
    private String getHomeDir() {
        return System.getProperty("user.home");
    }
    
    /* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#disconnect()
	 */
    /* (non-Javadoc)
	 * @see addressbook.db.IDbAccess#disconnect()
	 */
    public void disconnect() {
        if(isConnected) {
            String dbUrl = getDatabaseUrl();
            dbProperties.put("shutdown", "true");
            try {
                DriverManager.getConnection(dbUrl, dbProperties);
            } catch (SQLException ex) {
            }
            isConnected = false;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#getDatabaseLocation()
	 */
    /* (non-Javadoc)
	 * @see addressbook.db.IDbAccess#getDatabaseLocation()
	 */
    public String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") + "/" + dbName;
        return dbLocation;
    }
    
    /* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#getDatabaseUrl()
	 */
    /* (non-Javadoc)
	 * @see addressbook.db.IDbAccess#getDatabaseUrl()
	 */
    public String getDatabaseUrl() {
        String dbUrl = dbProperties.getProperty("derby.url") + dbName;
        return dbUrl;
    }
    
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    private static final String strCreateAddressTable =
            "create table APP.ADDRESS (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    LASTNAME    VARCHAR(30), " +
            "    FIRSTNAME   VARCHAR(30), " +
            "    MIDDLENAME  VARCHAR(30), " +
            "    PHONE       VARCHAR(20), " +
            "    EMAIL       VARCHAR(30), " +
            "    ADDRESS1    VARCHAR(30), " +
            "    ADDRESS2    VARCHAR(30), " +
            "    CITY        VARCHAR(30), " +
            "    STATE       VARCHAR(30), " +
            "    POSTALCODE  VARCHAR(20), " +
            "    COUNTRY     VARCHAR(30) " +
            ")";

	/* (non-Javadoc)
	 * @see addressbook.db.IDbAccess#getConnection()
	 */
	public Connection getConnection() {
		return dbConnection;
	}
    
}
