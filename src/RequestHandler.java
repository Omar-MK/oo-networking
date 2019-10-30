import java.io.File;
import java.io.IOException;

/**
 * This is a HTTP request handler abstract class which contains fields,
 * constructors, and an abstract method that define how a sub-class of this
 * class should be written so that it is compatible with the ConnectionHandler
 * class.
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
abstract class RequestHandler
{
	/**
	 * The ConnectionHandler Object which the HTML responces are sent through.
	 */
	protected ConnectionHandler connHandler;

	/**
	 * The client request split into an array of Strings.
	 */
	protected String[] requestArr;

	/**
	 * The response sent back to the client.
	 */
	protected String response;

	/**
	 * Primary abstract constructor - no need to pass a request, but to use the
	 * public methods of a RequestHandler Object constructed using this
	 * constructor, a File objects must be passed.
	 *
	 * @param connHandler - the ConnectionHandler Object through which the
	 * responses are sent
	 */
	public RequestHandler(ConnectionHandler connHandler)
	{
		this.connHandler = connHandler;
	}

	/**
	 * Secondary abstract constructor - accepts a request String in addition to
	 * a ConnectionHandler Object. Will automatically call the setNewRequest
	 * method which calls the respondToRequest method
	 *
	 * @param ConnHandler - the ConnectionHandler Object through which the
	 * responses are sent
	 * @param request - the HTTP request String to decode and handle
	 */
	public RequestHandler(ConnectionHandler connHandler, String request) throws IOException, DisconnectedException
	{
		this(connHandler);
		setNewRequest(request);
	}

	/**
	 * This method is used to give a new client request to the RequestHandler
	 * Object.
	 *
	 * @param request - the request made by the client
	 */
	public void setNewRequest(String request) throws IOException, DisconnectedException
	{
		this.requestArr = request.toLowerCase().split(" ");
		// the respond to request method is invoked
		respondToRequest();
	}

	/**
	 * Returns the respose given back to the client.
	 *
	 * @return the response given back to the client
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * This method should contain the logic to deal with all the requests
	 * supported by the RequestHandler.
	 */
	abstract public void respondToRequest() throws IOException, DisconnectedException;
}
