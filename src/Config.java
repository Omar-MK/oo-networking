import java.io.File;

/**
 * This class contains the Server configurations to allow for easy manipulation
 * of constants such as length of timeout, thread cound, and the serving
 * directory
 *
 * @author 170018405
 * @version Nov 9, 2018
 */
public class Config
{
	/*
	 * Inactivity timeout until server terminates a connection with a client.
	 */
	static final long TIMEOUT = 300000;

	/*
	 * Maximum number of clients to handle concurrently (threads).
	 */
	static final int THREADS = 8;

	/*
	 * Relative path to resources directory.
	 */
	static final String ROOT = "../resources/www";

	/*
	 * Additional path from ROOT to default index page.
	 */
	static final String DEFAULT_FILE = "/index.html";

	/*
	 * File Object containing 404 page.
	 */
	static final File NOT_FOUND_FILE = new File(ROOT, "/404.html");

	/*
	 * File Object containing 501 page.
	 */
	static final File NOT_IMPLEMENTED_FILE = new File(ROOT, "/501.html");
}
