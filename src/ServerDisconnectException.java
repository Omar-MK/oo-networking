/**
 * This is a simple class which extends the DisconnectedException class to
 * allow for ServerDisconnectExceptions to be thrown
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class ServerDisconnectException extends DisconnectedException
{
	/**
	 * Constructor for ServerDisconnectException Object.
	 *
	 * @param msg - the disconnection message (reason)
	 */
	public ServerDisconnectException(String msg)
	{
		super(msg);
	}
}
