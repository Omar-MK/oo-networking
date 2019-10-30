import java.io.*;
import java.net.Socket;

public class WebServerMain
{
	public static void main(String[] args)
	{
		// check a valid directory path and port number are provided
		checkCommandArgs(args);

		// create a new web server instance and launch it
		WebServer s = new WebServer(args[0], Integer.parseInt(args[1]), true, true);
		s.launch();
	}


	/** This method checks the correct number of command line arguments are
	 * given.
	 * The method will print an error message to the Standard output if an
	 * incorrect number of arguments are give or unexpected values are entered
	 * that do not satisfy the format expected. The method will then force the
	 * program to exit.
	 *
	 * @param args the args string array that is main's parameter
	 * @return a File object with the root directory specified
	 */
	static void checkCommandArgs(String[] args)
	{
		// check number of command-line agruments
		if (args.length != 2)
		{
			System.out.println("Usage: java WebServerMain <document_root> <port>");
			System.exit(1);
		}
		try
		{
			// check first command-line argument is a valid directory
			File dir = new File(args[0]);
			if (!dir.isDirectory())
			{
				System.out.println("Please provide a valid directory path");
				System.exit(1);
			}
			// check second command-line argument is a valid port number
			int port = Integer.parseInt(args[1]);
			if (port < 0 || port > 65535)
			{
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException e)
		{
			System.out.println("Please provide a valid port number between 0 and 65535");
			System.exit(1);
		}
	}
}
