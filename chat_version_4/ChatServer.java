package chat_version_4;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


public class ChatServer extends JFrame implements Runnable {
	
	private int clientCount =0;
	private ChatServerThread clients[] = new ChatServerThread[50];
	private ServerSocket server = null;
	Thread thread = null;
	
	//GUI STUFF//
	private JTextArea displayText = new JTextArea(16,50);
	private JScrollPane scroll = new JScrollPane(displayText);
	private DefaultCaret caret  = (DefaultCaret) displayText.getCaret();
	
	private JPanel mainJP = new JPanel();//everything goes in here
	
	
	
	//same as version3
	public ChatServer(int port){
		try{
			//GUI STUFF//
			this.setTitle("Orange Chat Server! ~Long Live The Orange~");
			this.setSize(700, 600);
			this.setLocationRelativeTo(null);
			//Set the close on exit
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			//Inner display//
			this.displayText.setAutoscrolls(true);
			this.scroll.setAutoscrolls(true);
			this.caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			this.displayText.setLineWrap(true);
			this.displayText.setEditable(false);
			
			this.mainJP.setLayout(new BorderLayout());
			
			this.mainJP.add(scroll,BorderLayout.CENTER);
			
			this.add(this.mainJP);
			this.setVisible(true);
			
			//ServerStuff//
			server = new ServerSocket(port);//step1
			System.out.println("Started the server...waiting for a client");
			this.displayMsg("Started the server...waiting for a client");
			start(); //the chatserver's start method that goes ahead and creates a new thread
			
			
			
		}
		catch(IOException e){
			System.err.println("ERROR "+e.getMessage());
			
		}
	}
	
	private void displayMsg(String msg){
		displayText.append(msg +"\n");
	}
	
	public void start(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {//same as version 3
		while(thread !=null){
			try{
				System.out.println("Waiting for a client...");
				displayMsg("Waiting for a client...");
				//now we add a new Thread and accept a client
				addThread(server.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addThread(Socket socket){
		if(clientCount < clients.length){
		   clients[clientCount] = new ChatServerThread(this, socket);
			try {
				 clients[clientCount].open();//open the stream for the ChatServerThread client
				 clients[clientCount].start();//start to run the ChatServerThread client
				 clientCount++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	public synchronized void handle(int ID, String input){
		String privMsg ="private";			
		if(input.startsWith(privMsg)){		
			int ID_SendTo = Integer.parseInt(input.substring(privMsg.length(),privMsg.length()+5));		
							
			String msg = input.substring(privMsg.length()+6);		
			if(findClient(ID_SendTo)!=-1){		
				clients[findClient(ID_SendTo)].send(privMsg + " from " + ID + ": " +msg);
				System.out.println(privMsg + " from " + ID + "to "+ID_SendTo+": " +msg);
				this.displayMsg(privMsg + " from " + ID + "to "+ID_SendTo+": " +msg);
			}		
			else{		
				clients[findClient(ID)].send("user: " + ID_SendTo+ " was not found");		
			}		
							
		}		
		else {									
				System.out.println("Message from " + ID+ ": "+ input);
				this.displayMsg("Message from " + ID+ ": "+ input);
			for(int i=0; i<clientCount; i++){
				//add line of code to print the user's message
				//on the server side for spying
				clients[i].send("User: "+ ID + ": "+input);
//				System.out.println("User: "+ ID + ": "+input);
//				this.displayMsg("User: "+ ID + ": "+input);
			}
		
		}
		if(input.equalsIgnoreCase("bye")){
			remove(ID);//person said bye so remove them
		}
	}
	
	public synchronized void remove(int ID){
		int position = findClient(ID);
		if(position >=0){
			ChatServerThread toRemove = clients[position];
			if(position <clientCount-1){
				for(int i= position+1; i <clientCount; i++){
					clients[i-1] = clients[i];
				}
				clientCount--;
			}
			try {
				toRemove.close();//close the person's that said bye connection
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private int findClient(int ID){
		for(int i=0; i<clientCount; i++){
			if(clients[i].getID() == ID){
				return i;
			}
		}
		return -1;//not in the array
	}
	
	public static void main(String [] args){
		ChatServer myServer = null;
		if(args.length !=1){
			System.out.println("You need to specify a port number!!!");
		}
		else{
			int portNum = Integer.parseInt(args[0]);
			myServer = new ChatServer(portNum);//create an instance of my ChatServer
		}
	}
	
	

}
