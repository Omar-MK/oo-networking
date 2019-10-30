import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.io.File;

/**
 * TThis is a simple web server class containing a web server which listens to
 * incoming connections over a port, and allocates a connection handler to
 * handle the request.
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class WebServer
{
	// file on which client requests and server responses will be logged
	private String outFile;
	// the root directory of the resources to serve data from
	private String root;
	// the port which this webserver listens on
	private int port;
	private boolean verbose = false;
	private boolean records = false;

	public WebServer(String root, int port)
	{
		this.root = root;
		this.port = port;
		outFile = ("../logs/WebServer_" + new Date() + ".txt").replace(" ", "_");
	}

	public WebServer(String root, int port, boolean verbose)
	{
		this(root, port);
		this.verbose = verbose;
	}

	public WebServer(String root, int port, boolean verbose, boolean records)
	{
		this(root, port, verbose);
		this.records = records;
	}

	// method used to initate WebServer to start listening for connections over
	// the chosen port. 
	public void launch()
	{
		try (ServerSocket ss = new ServerSocket(port);)
		{
			// thread pool is created to limit the number of threads used
			ExecutorService pool = Executors.newFixedThreadPool(Config.THREADS);

			System.out.println("Server initiated on " + new Date());
			System.out.println("Listening for connections on " + port + "...");

			/* a ServerActive Object is created to terminate the while loop
			 when a client requests the server to stop accepting new
			  connections */
			ServerActive serverStatus = new ServerActive(true);

			while (serverStatus.getStatus())
			{
				Socket conn = ss.accept();

				if (verbose)
				{
					System.out.println("New connecton recieved from " + conn.getInetAddress() + " on " + new Date());
				}

				ConnectionHandler myConnection;

				if (records)
				{
					myConnection = new ConnectionHandler(root, conn, verbose, outFile, serverStatus);
				}
				else
				{
					myConnection = new ConnectionHandler(root, conn, verbose, serverStatus);
				}

				// new connection is submitted to be handled by the thread pool
				pool.submit(myConnection);
			}
			// close the threadpool
			pool.shutdown();
		}
		catch (IOException ioe)
		{
			System.err.println("Server Connection error: " + ioe.getMessage());
		}
	}

	public String getOutFile()
	{
		return outFile;
	}

	public boolean setOutFile(String outFile) throws IOException
	{
		File file = new File(outFile);
		if (file.createNewFile())
		{
			this.outFile = outFile;
			return true;
		}
		return false;
	}

	public void setVerboseMode(boolean verbose)
	{
		this.verbose = verbose;
	}

	public void setLogMode(boolean records)
	{
		this.records = records;
	}
}
