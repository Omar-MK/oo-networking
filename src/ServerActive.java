/**
 * This is a simple class that defines a ServerActive Object used to keep track
 * and stop the server from accepting new client connections.
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class ServerActive
{
	/**
	 * A field to track the server status whether it is still accepting new
	 * connections or not.
	 */
	private boolean serverStatus;

	/**
	 * Constructor
	 *
	 * @param serverStatus - boolean indicating if the server accept new client
	 * connections. True indicated the server can accept more connections.
	 */
	public ServerActive(boolean serverStatus)
	{
		this.serverStatus = serverStatus;
	}

	/**
	 * Sets the serverStatus.
	 * True allows the server to accept connections, and false stops the server
	 * from accepting new connections.
	 *
	 * @param serverStatus - the server status. True - accepts new connections;
	 * false otherwsie.
	 */
	public void setStatus(boolean serverStatus)
	{
		this.serverStatus = serverStatus;
	}

	/**
	 * Returns the serverStatus.
	 *
	 * @return the serverStatus
	 */
	public boolean getStatus()
	{
		return serverStatus;
	}
}
