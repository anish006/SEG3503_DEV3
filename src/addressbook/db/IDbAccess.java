package addressbook.db;

import java.sql.Connection;

public interface IDbAccess {

	/* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#connect()
	 */
	public abstract boolean connect();

	/* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#disconnect()
	 */
	public abstract void disconnect();

	/* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#getDatabaseLocation()
	 */
	public abstract String getDatabaseLocation();

	/* (non-Javadoc)
	 * @see com.sun.demo.addressbook.db.IAddressDao#getDatabaseUrl()
	 */
	public abstract String getDatabaseUrl();

	public abstract Connection getConnection();

}