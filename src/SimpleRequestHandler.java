import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This is a HTTP request handler class that inherits the RequestHandler.
 * The purpose of this class is to send the appropriate responses to HTTP
 * client requests. This class allows for more HTTP methods to be supported and
 * more responses to be coded without the need to change the ConnectionHandler
 * implementation.
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class SimpleRequestHandler extends RequestHandler
{

	/**
	 * Primary SimpleRequestHandler Constructor.
	 * @param connHandler - the connection handler Object through which the
	 * responses are sent
	 *
	 */
	public SimpleRequestHandler(ConnectionHandler connHandler)
	{
		// call super constructor
		super(connHandler);
	}

	/**
	 * Secondary SimpleRequestHandler Constructor.
	 * @param connHandler - the connection handler Object through which the
	 * responses are sent
	 * @param request - the request made by the client to be handled
	 *
	 */
	public SimpleRequestHandler(ConnectionHandler connHandler, String request) throws IOException, DisconnectedException
	{
		// call super constructor
		super(connHandler, request);
	}

	/**
	 * This method contains the logic required to respond to all the supported
	 * requests.
	 *
	 */
	@Override
	public void respondToRequest() throws IOException, DisconnectedException
	{
		String reqFilePath = "";

		// first the request is checked to contain 2 parts or more where:
		// index 0 is the request type (HTTP method)
		// index 1 is the resource path
		// index 2 is the protocol version
		if (requestArr.length > 1)
		{
			// if the resource path ends in /, then concatenate the index (home) page path to the resource path provided.
			reqFilePath = requestArr[1];
			if (reqFilePath.endsWith("/")) reqFilePath += Config.DEFAULT_FILE;
		}
		else
		{
			/* less than 2 arguments in requestArr means that either a
			 termination command has been sent, white space, or an unsupported
			  request */
			/* concatenate the index page path to reqFilePath so errors are
			 avoided in the next step */
			reqFilePath += Config.DEFAULT_FILE;
		}
		// a File Object is made using the requested file path & resources root
		File file = new File(connHandler.getRoot(), reqFilePath);

		// based on request, an appropriate response is chosen
		switch(requestArr[0])
		{
			case "": break;
			case "head": head(file);
			break;
			case "get": get(file);
			break;
			case "!exit": exit();
			break;
			case "!serverexit": serverExit();
			break;
			// more HTTP methods can be supported as needed...
			default: returnNotImplemented(Config.NOT_IMPLEMENTED_FILE);
		}
	}

	/**
	 * This method outputs file header information over the socket.
	 *
	 * @param file - the file who's size, MIME type will be sent
	 */
	public void head(File file)
	{
		// check if file requested exists
		if (file.isFile())
			// send OK Header
			headHelper(file, "HTTP/1.1 200 OK");
		else
			// send Not Found Header
			headHelper(Config.NOT_FOUND_FILE, "HTTP/1.1 404 Not Found");
	}

	/**
	 * This method outputs file body/content over the socket (binary output).
	 *
	 * @param file - the file who's contents will be sent
	 */
	public void get(File file) throws IOException
	{
		// send header
		head(file);
		// check if the file exits
		if(!file.isFile())
		{
			// if the file doesn't exist, change file to return 404 body
			file = Config.NOT_FOUND_FILE;
		}
		byte[] fileData = getFileData(file, (int) file.length());
		connHandler.getOutputStream().write(fileData, 0, (int) file.length());
		connHandler.getOutputStream().flush();
	}

	/**
	 * This method outputs not implemented response.
	 *
	 * @param file - the 501 file who's header will be sent
	 */
	public void returnNotImplemented(File file)
	{
		// send Not Implemented Header
		headHelper(Config.NOT_IMPLEMENTED_FILE, "HTTP/1.1 501 Not Implemented");
	}

	/**
	 * This method throws a DisconnectedException forcing the server to
	 * terminate the client connection.
	 *
	 */
	public void exit() throws DisconnectedException
	{
		// close client connection
		throw new DisconnectedException("Client disconnected");
	}

	/**
	 * This method throws a ServerDisconnectedException forcing the server to
	 * terminate the client connection and stop accepting new connections.
	 *
	 */
	public void serverExit() throws ServerDisconnectException
	{
		System.out.println("here");
		/* tell WebServer to stop accepting new connections and terminate
		 client connection */
		throw new ServerDisconnectException("Server disconnection initiated");
	}

	/**
	 * This is the head method helper.
	 *
	 * @param file - the file who's size, MIME type will be sent
	 * @param msg - the response code to be sent to the client
	 */
	private void headHelper(File file, String msg)
	{
		// update response field
		response = msg;
		// output appropriate header response code
		connHandler.getWriter().println(msg);
		// output the rest of the header
		connHandler.getWriter().println("Server: Simple Java HTTP Server");
		connHandler.getWriter().println("Content-Type: " + getMIME(file.getPath()));
		connHandler.getWriter().println("Content-Length: " + file.length());
		connHandler.getWriter().println();
		connHandler.getWriter().flush();
	}

	/**
	 * This method returns the MIME type of a given file.
	 *
	 * @param fileRequested - the file who's MIME type will returned
	 * @return the MIME type of fileRequested
	 */
	private String getMIME(String fileRequested)
	{
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
		else if (fileRequested.endsWith(".jpg"))
			return "image/jpeg";
		else
			return "text/plain";
	}

	/**
	 * This method returns a byte array containing the file contents.
	 *
	 * @param file - the file who's data will be returned
	 * @param fileLen - the length of file
	 */
	private byte[] getFileData(File file, int fileLen) throws IOException
	{
		byte[] fileData = new byte[fileLen];
		try (BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));)
		{
			fileIn.read(fileData);
		}
		return fileData;
	}
}
