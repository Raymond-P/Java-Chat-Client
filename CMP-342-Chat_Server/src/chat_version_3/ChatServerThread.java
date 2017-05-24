package chat_version_3;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatServerThread extends Thread{
	
	Socket socket = null;
	private ChatServer server = null;
	private int ID = -1;
	private DataInputStream strIn = null;
	boolean done = true;
	
	public ChatServerThread(ChatServer chatServer, Socket theSocket){
		server = chatServer;
		socket = theSocket;
		ID = socket.getPort();
		System.out.println("Chat Server Info - THREAD INFO - "+
				server + "SOCKET: "+socket+" ID: "+ ID) ;
	}
	
	public void run(){
		while(ID !=-1){
			getInput();
			//close everythign up
		}
	}
	
	public void getInput(){
		done = false;
		while(!done){
			try{
			String lineIn = strIn.readUTF();
			System.out.println("ID "+ID+" said: "+ lineIn);//step5 print out what they said
			if(lineIn.equalsIgnoreCase("bye")){
					done = true;
				}
			}
			catch(IOException e){
				System.err.println("ERROR getting Input from client"+ e.getMessage());
			}
		}
		System.out.println("ID"+ ID + " has left the chat");
		ID=-1;
		
	}
	
	public void close() throws IOException{//same as version 1,version2
		if(strIn!=null){
			strIn.close();
		}
		if(socket!=null){
			socket.close();
		}
	}
	public void open() throws IOException{//same as version 1,version2
		strIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	

}
