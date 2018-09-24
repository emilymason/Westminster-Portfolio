/**
 * Handler class containing the logic for sending 
 * results back to the client.
 *
 * @author Greg Gagne 
 */

import java.io.*;
import java.net.*;


public class Handler 
{
	public static final int BUFFER_SIZE = 256;

	/**
	 * this method is invoked by a separate thread
	 */
	public void process(Socket client) throws java.io.IOException {

        try{
        BufferedReader in = null;		


		try {
            // read what the client sent
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));


            // we just want the first line
            String requestLine = in.readLine();

            /* If we don't read a GET, just ignore it and close the socket */
            if ( requestLine == null || !requestLine.substring(0,3).equals("GET") ) {
                client.close();

                return;
            }


   		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			// close streams and socket
            System.out.println("closing socket");
            in.close();
		}
    }
    catch (IOException ioe) {
            System.err.println(ioe);
        }

	}
}
