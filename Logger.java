/* 
 * A Logger class, serving two major functions:
 * 1) Store relevant directories as static Strings,
 * 		to be used for finding files.
 * 2) Log messages in a single, consolidated log file, rather than
 * 		resorting to System.out / stdout messages.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Logger {
	/* Can replace this with your own directory structure */
	public static final String root_dir="\"artwork\"/";

	/* In the future, the directory structure will be split
	 * into red and map directories */
	/*
	public static final String res_dir="res/";
	public static final String map_dir="map/";
	*/

	private PrintWriter output;
	public Logger(PrintWriter outputStream) {
		output = outputStream;
	}

	public Logger(String filename) {
		try {
			File matchingFile = new File(filename);
			output = new PrintWriter(matchingFile);
		} catch (FileNotFoundException j) {
			j.printStackTrace();
		}
	}

	public Logger(File fileObj) {
		try {
			output = new PrintWriter(fileObj);
		} catch (FileNotFoundException j) {
			j.printStackTrace();
		}
	}

	public void log(String msg) {
		output.println(msg);
	}
}
