package com.mockrunner.mock.jdbc;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mockrunner.base.NestedApplicationException;

/**
 * Used to create all types of JDBC mock objects. 
 * Maintains the necessary dependencies between the mock objects.
 * If you use the mock objects returned by this
 * factory in your tests you can be sure that they are all
 * up to date.
 * Please note, that this class removes all drivers
 * from the JDBC <code>DriverManager</code> and registers
 * the {@link MockDriver}. All drivers are preserved and
 * can be restored with {@link #restoreDrivers}.
 */
public class JDBCMockObjectFactory
{
    private final MockDataSource dataSource;
    private final MockDriver driver;
    private final MockConnection connection;
    private final Set<Driver> preservedDrivers;
    
    /**
     * Creates a new set of mock objects.
     */
    public JDBCMockObjectFactory()
    {
        dataSource = createMockDataSource();
        driver = createMockDriver();
        connection = createMockConnection();
        preservedDrivers = new HashSet<Driver>();
        setUpDependencies();
    }

    private void setUpDependencies()
    {
        dataSource.setupConnection(connection);
        driver.setupConnection(connection);
        registerMockDriver();
    }

    private void deregisterDrivers()
    {
        try
        {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
            {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    private void deregisterMockDrivers()
    {
        try
        {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while(drivers.hasMoreElements())
            {
                Driver currentDriver = drivers.nextElement();
                if(currentDriver instanceof MockDriver)
                {
                    DriverManager.deregisterDriver(currentDriver);
                }
            }
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    private void preserveDrivers()
    {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements())
        {
            Driver currentDriver = drivers.nextElement();
            if(!(currentDriver instanceof MockDriver))
            {
                preservedDrivers.add(currentDriver);
            }
        }
    }
    
    /**
     * Removes all JDBC drivers from the <code>DriveManager</code> and
     * registers the mock driver. The removed drivers are preserved and
     * can be restored with {@link #restoreDrivers}.
     */
    public void registerMockDriver()
    {
        try
        {
            preserveDrivers();
            deregisterDrivers();
            DriverManager.registerDriver(driver);
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        }
    }
    
    /**
     * Since <code>JDBCMockObjectFactory</code> removes all the
     * drivers from the <code>DriveManager</code> (so the
     * {@link MockDriver} is guaranteed to be the only one)
     * you can use this method to restore the original drivers.
     * Automatically called by com.mockrunner.base.BaseTestCase#tearDown.
     */
    public void restoreDrivers()
    {
        deregisterMockDrivers();
        try
        {
            for (Driver preservedDriver : preservedDrivers) {
                DriverManager.registerDriver(preservedDriver);
            }
        }
        catch(SQLException exc)
        {
            throw new NestedApplicationException(exc);
        }
        preservedDrivers.clear();
    }
    
    /**
     * Creates the {@link com.mockrunner.mock.jdbc.MockConnection} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection createMockConnection()
    {
        return new MockConnection();
    }

    /**
     * Creates the {@link com.mockrunner.mock.jdbc.MockDriver} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.jdbc.MockDriver}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDriver}
     */
    public MockDriver createMockDriver()
    {
        return new MockDriver();
    }

    /**
     * Creates the {@link com.mockrunner.mock.jdbc.MockDataSource} using <code>new</code>.
     * This method can be overridden to return a subclass of {@link com.mockrunner.mock.jdbc.MockDataSource}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDataSource}
     */
    public MockDataSource createMockDataSource()
    {
        return new MockDataSource();
    }

    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockDataSource}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDataSource}
     */
    public MockDataSource getMockDataSource()
    {
        return dataSource;
    }
    
    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockDriver}.
     * @return the {@link com.mockrunner.mock.jdbc.MockDriver}
     */
    public MockDriver getMockDriver()
    {
        return driver;
    }

    /**
     * Returns the {@link com.mockrunner.mock.jdbc.MockConnection}.
     * @return the {@link com.mockrunner.mock.jdbc.MockConnection}
     */
    public MockConnection getMockConnection()
    {
        return connection;
    }
}
