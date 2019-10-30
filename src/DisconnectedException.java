/**
 * This is a simple class which extends the Exception class to allow for
 * Disconnected Exceptions to be thrown
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class DisconnectedException extends Exception
{
	/**
	 * Constructor for DisconnectedException Object.
	 *
	 * @param msg - the disconnection message (reason)
	 */
	public DisconnectedException(String msg)
	{
		super(msg);
	}
}
