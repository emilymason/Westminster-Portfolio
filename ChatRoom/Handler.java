
/**
 * Handler class containing the logic for sending 
 * results back to the client.
 *
 * @author Greg Gagne 
 * @author Emily Mason
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.google.gson.*;
import dealios.*;

public class Handler
{
	public static final int BUFFER_SIZE = 256;

	/**
	 * this method is invoked by a separate thread
	 */
	public void process(Socket client) throws java.io.IOException
	{

		BufferedReader in = null;
		DataOutputStream out = null;
		String incomingMsg = null;
		String username = null;
		try
		{
			// http://tutorials.jenkov.com/java-json/gson.html
			Gson gson = new GsonBuilder().create();
			out = new DataOutputStream(client.getOutputStream());

			// read what the client sent
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			while ((incomingMsg = in.readLine()) != null)
			{

				try (Writer writer = new OutputStreamWriter(new FileOutputStream("log.txt", true), "UTF-8"))
				{
					writer.append("\n");
					writer.append(incomingMsg);
					writer.close();
				}

				if (incomingMsg.contains("chatroom-begin"))
				{
					ChatroomBegin cb = gson.fromJson(incomingMsg, ChatroomBegin.class);
					username = cb.username + Server.counter++;
					Server.users.put(username, client);

					ChatroomResponse cr = new ChatroomResponse(username, Server.users.size(), this.getListOfUsers());
					String chatroomResponse = gson.toJson(cr);
					out.writeBytes(chatroomResponse);
					
					ChatroomUpdate cu = new ChatroomUpdate("enter", username);
					String chatroomUpdate = gson.toJson(cu);
					DataOutputStream outBroadcast = null;
					for (String u : this.getListOfUsers())
					{
						Socket broadcast = Server.users.get(u);
						outBroadcast = new DataOutputStream(broadcast.getOutputStream());
						outBroadcast.writeBytes(chatroomUpdate);
					}
					
				} else if (incomingMsg.contains("chatroom-send"))
				{
					ChatroomSend cs = gson.fromJson(incomingMsg, ChatroomSend.class);
					String from = cs.from;
					String message = cs.message;
					int message_length = cs.message.length();

					ArrayList<String> to = new ArrayList<String>();
					if (cs.to.isEmpty())
						to = this.getListOfUsers();
					else
						to = cs.to;

					ChatroomBroadcast cb = new ChatroomBroadcast(from, to, message, message_length);
					String chatroomBroadcast = gson.toJson(cb);

					DataOutputStream outBroadcast = null;
					for (String u : to)
					{
						Socket broadcast = Server.users.get(u);
						outBroadcast = new DataOutputStream(broadcast.getOutputStream());
						outBroadcast.writeBytes(chatroomBroadcast);
					}

				} else if (incomingMsg.contains("chatroom-end"))
				{
					ChatroomEnd ce = gson.fromJson(incomingMsg, ChatroomEnd.class);
					String user = ce.id;

					ChatroomUpdate cu = new ChatroomUpdate("leave", user);
					String chatroomUpdate = gson.toJson(cu);

					DataOutputStream outBroadcast = null;
					for (String u : this.getListOfUsers())
					{
						Socket broadcast = Server.users.get(u);
						outBroadcast = new DataOutputStream(broadcast.getOutputStream());
						outBroadcast.writeBytes(chatroomUpdate);
					}
					
					break;

				} else
				{
					ChatroomError ce = new ChatroomError("unexpected_dealio_type", username);
					String chatroomError = gson.toJson(ce);
					out.writeBytes(chatroomError);
				}
			}

		} catch (IOException ioe)
		{
			System.err.println(ioe);
		}
		 finally {
		 // close streams and socket
		 System.out.println("closing socket");
		 Server.users.get(username).close();
		 Server.users.remove(username);
		 in.close();
		 }

	}

	public ArrayList<String> getListOfUsers()
	{
		ArrayList<String> users = new ArrayList<String>();

		for (String user : Server.users.keySet())
			users.add(user);

		return users;
	}
}
