import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;


/**
 * TThis is the connection handler class used by the WebServer to manage client
 * requests.
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class ConnectionHandler implements Runnable
{
	// client TCP connection socket
	private Socket conn;
	// root of directory to fetch data from
	private String root;
	// Buffered reader used to read client input coming over the socket
	private BufferedReader br;
	// Buffered output stream used to send back data such as pictures
	private BufferedOutputStream bos;
	// print writer used to send back headers
	private PrintWriter out;
	// buffered writer used to write events to a text file
	private PrintWriter fOut;
	// verbose output
	private boolean verbose;
	// output text file to log requests
	private String outFile;
	// Server status Object used to terminate server given a special request from the client
	private ServerActive serverStatus;

	public ConnectionHandler(String root, Socket conn, boolean verbose, ServerActive serverStatus)
	{
		this.serverStatus = serverStatus;
		this.verbose = verbose;
		ConnectionHandlerHelper(root, conn);
	}

	public ConnectionHandler(String root, Socket conn, boolean verbose, String outFile, ServerActive serverStatus)
	{
		this.serverStatus = serverStatus;
		this.outFile = outFile;
		this.verbose = verbose;
		ConnectionHandlerHelper(root, conn);
	}

	private void ConnectionHandlerHelper(String root, Socket conn)
	{
		this.conn = conn;
		this.root = root;
		try
		{
			// prepare buffered reader to read client request
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// preprare PrintWriter to send back headers
			out = new PrintWriter(conn.getOutputStream());
			// prepare BufferedOutputStream to send back binary output
			bos = new BufferedOutputStream(conn.getOutputStream());
			// prepare print writer to write to file
			fOut = new PrintWriter(new BufferedWriter(new FileWriter(outFile, true)));
		}
		catch (IOException ioe)
		{
			System.err.println("ConnectionHandler: " + ioe.getMessage());
		}
	}

	/**
	 * This method manages the client connection
	 *
	 */
	@Override
	public void run()
	{
		try
		{
			// record start time
			long prevTime = System.currentTimeMillis();
			// create a RequestHandler object to respond to requests
			SimpleRequestHandler reqHandler = new SimpleRequestHandler(this);
			// check the client made a request within the timeout limit
			while ((System.currentTimeMillis() - prevTime) < Config.TIMEOUT)
			{
				// check if there is a request input in the buffered reader
				if (br.ready())
				{
					// reset previous time
					prevTime = System.currentTimeMillis();
					// get the request from the client
					String clientReq = br.readLine();
					if(clientReq.equals("")) continue;
					// manage the request using the SimpleRequestHandler Object
					reqHandler.setNewRequest(clientReq);
					// log event
					printEvent(clientReq, reqHandler.getResponse());
					logEvent(clientReq, reqHandler.getResponse());
				}
			}
		}
		// catch IO and DiconnectedExceptions
		catch (ServerDisconnectException sde)
		{
			/* ServerDisconnectExceptions mean the client requested the server
			 to stop accepting new connections and terminate */
			System.out.println("ConnectionHandler: run: " +
			sde.getMessage());
			serverStatus.setStatus(false);
		}
		catch (Exception e)
		{
			System.out.println("ConnectionHandler: run: " +
			e.getMessage());
		}
		finally
		{
			// exit cleanly
			cleanExit();
			if (verbose) System.out.println("Connection closed.\n");
		}
	}

	public String getRoot()
	{
		return root;
	}

	// for headers
	public PrintWriter getWriter()
	{
		return out;
	}

	// for content
	public BufferedOutputStream getOutputStream()
	{
		return bos;
	}

	// clean exit code
	private void cleanExit()
	{
		try
		{
			br.close();
			out.close();
			fOut.close();
			bos.close();
			conn.close();
		}
		catch (IOException ioe)
		{
			System.err.println("ConnectionHandler: cleanExit: " + ioe.getMessage());
		}
	}

	// contains code to print events to terminal
	private void printEvent(String request, String response)
	{
		if (verbose)
		{
			System.out.println(new Date());
			System.out.println("New request from " + conn.getInetAddress());
			System.out.println("Request data: " + request);
			System.out.println("Response returned: " + response);
		}
	}

	// contains code to log events to text file
	private void logEvent(String request, String response)
	{
		if (outFile != null)
		{
			fOut.println(new Date());
			fOut.println("New request from " + conn.getInetAddress());
			fOut.println("Request data: " + request);
			fOut.println("Response returned: " + response);
			fOut.flush();
		}
	}
}
