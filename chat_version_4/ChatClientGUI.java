package chat_version_4;

import otp.OTP_Driver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;


public class ChatClientGUI extends JFrame implements ActionListener,KeyListener, Runnable{
	
	private String encMarker = "THISMESSAGEISENCRYPTED";

	private Socket socket = null;
	private final String serverName = "localhost"; //"localhost"//or your friend's ip address
	private final int serverPortNumber = 8080; //needs to ma
	
	private DataInputStream strIn = null;
	private DataOutputStream strOut = null;
	
	private ChatClientThread client = null;
	private boolean done = true;//until connected you are "done"
	private String line = "";
	
	private JTextArea displayText = new JTextArea(16,50);
	private JScrollPane scroll = new JScrollPane(displayText);
	private DefaultCaret caret  = (DefaultCaret) displayText.getCaret();
	
	
	private JTextField input = new JTextField(1);
	private JTextField targetUserID = new JTextField();
	
	private JButton btnConnect = new JButton("Connect");
	private JButton btnSend = new JButton("Send");
	private JButton btnQuit = new JButton("Disconnect");
	private JButton btnPrivate = new JButton("Private");
	private JCheckBox chkboxEncrypt = new JCheckBox("Encrypt Message", false);
	
	private JPanel mainJP = new JPanel();//everything goes in here
	private JPanel displayJP = new JPanel();//textarea.. plus whatever 
	private JPanel btnsJP = new JPanel();//put this on the bottom
	
	//private JPanel east = new JPanel();
	private JPanel west = new JPanel();
	private JPanel north = new JPanel();
	private JPanel east = new JPanel();
	
	//nice looking ;P//
	ImageIcon orangeIcon = new ImageIcon("src/icon-orange.png");
	
	//Encryption//
	OTP_Driver otp = new OTP_Driver(); //remember to import it from the external package
	
	/**
	 * 
	 */
	public ChatClientGUI(){
		
		mainJP.setLayout(new BorderLayout());
		
		this.input.addKeyListener(this);
		
		displayText.setAutoscrolls(true);
		scroll.setAutoscrolls(true);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		displayText.setLineWrap(true);
		
		displayJP.setLayout( new BorderLayout());//new GridLayout(2,1));
		displayJP.add(this.scroll,BorderLayout.CENTER);//displayText); //added textarea to jpanel
		displayJP.add(input, BorderLayout.SOUTH);//added input below textarea to jpanel
		
		
		
		btnsJP.setLayout(new GridLayout(1,4));

		btnPrivate.addActionListener(this);
		this.targetUserID.addActionListener(this);
		
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
		btnQuit.addActionListener(this);
		chkboxEncrypt.addActionListener(this);
		

		
		Box box = Box.createHorizontalBox();
		box.add(btnQuit);
		box.add(btnConnect);
		box.add(btnSend);
		box.add(btnPrivate);
		box.add(targetUserID);
		box.add(chkboxEncrypt);
		
		btnsJP.add(box);
		/*btnsJP.add(btnPrivate);
		btnsJP.add(btnConnect);
		btnsJP.add(btnSend);
		btnsJP.add(btnQuit); */
		east.setBackground(Color.ORANGE);
		west.setBackground(Color.ORANGE);
		north.setBackground(Color.ORANGE);
		east.setBackground(Color.ORANGE);
		east.setBackground(Color.ORANGE);
		btnsJP.setBackground(Color.ORANGE);
		
		mainJP.add(displayJP, BorderLayout.CENTER);//add to center
		mainJP.add(btnsJP, BorderLayout.SOUTH);//add to bottom
		mainJP.add(east, BorderLayout.EAST);
		mainJP.add(west, BorderLayout.WEST);
		mainJP.add(north, BorderLayout.NORTH);
		
		input.setEditable(false);
		targetUserID.setEditable(false);
		displayText.setEditable(false);
		//input.setPreferredSize(new Dimension(500,500));
		
		add(mainJP);
		btnQuit.setEnabled(false);
		btnPrivate.setEnabled(false);
		btnSend.setEnabled(false);
		btnPrivate.setEnabled(false);
		
		this.chkboxEncrypt.setEnabled(false);
		
		this.addKeyListener(this);
		
		//fancy icon//
		this.setIconImage(this.orangeIcon.getImage());
		
		
		//Set the close on exit
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	}
	
	@Override
	public void run() {
		while(!done){
			try {
				line = strIn.readUTF();
				//displayMessage(line);
				
				//"private"+" from 12345"+": " is of len 20 must account for this
				
				if(this.isEncrypted(line)){
					System.out.println("Encrypted private detedtec");
					line = this.decryptWithMarker(line);
				}
				System.out.println(line);
				
				displayMsg(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	private String decryptWithMarker(String message) {
		//"private"+" from 12345"+": " is of len 20 must account for this
		int prefixLen = 20;//this is for private messages
		System.out.println("substring: "+message.substring(0,4));
		if(message.substring(0,4).equals("User")){//this is for public messages
			prefixLen = 13;
		}
		String encryptedMessage = message.substring(this.encMarker.length()+prefixLen, message.length());
		String from = message.substring(0, prefixLen);
		from = "ENCRYPTED "+from;
		String plainMessage = this.otp.decKeyAndMsg(encryptedMessage);
		return from+plainMessage;
	}

	/**
	 * checks whether the message has been flagged as encrypted or not
	 * @param message -the string to be checked
	 * @return true if the flag is detected 
	 */
	private boolean isEncrypted(String message) {
		//"private"+" from 12345"+": " is of len 20 must account for this
		//System.out.println("Inside isEncrypted...");
		boolean answer = false;
		int prefixLen = 20;//this is for private messages
		//System.out.println("substring: "+message.substring(0,4));
		if(message.substring(0,4).equals("User")){//this is for public messages
			prefixLen = 13;
			//System.out.println("Prefix = 13");
		}
		int mark =(this.encMarker.length()+prefixLen);
		if(message.length() >= mark){
			//System.out.println("Message is larger than mark..");
			//System.out.println(message);
			String subString = message.substring(prefixLen, encMarker.length()+prefixLen);
			//System.out.println("sub string : "+subString);
			if(subString.equals(this.encMarker)){
				//System.out.println("Checking if "+subString+" is our marker.");
				answer= true;
				//System.out.println("ans is true. it's encrypted");
			}
			
		}
		
		//System.out.println("Exiting isEncrypted");
		return answer;
	}

	private void displayMsg(String msg){
		displayText.append(msg +"\n");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//do something when connect
		//connect(serverName, serverPortNumber);
		if(e.getSource() == btnConnect)
			connect(serverName, serverPortNumber);
		
		//do something when send
		if(e.getSource()== btnSend)
			send();
		
		if(e.getSource() == btnQuit)
			disconnect();
		if(e.getSource() == btnPrivate)
			privatemsg();
		
		//send()
		//do something when private
		//send... private...
		//do something when quit
		//disconnect();
		
	}
	public void connect(String serverName, int portNum){
		try {
			done=false;
			socket = new Socket(serverName, portNum);
			System.out.println("Client just got connected");
			//display("hooray that we got connected");... 
			open();
			input.setText("Connected");
			send();
//			btnSend.enable();
//			btnConnect.enable();
//			btnQuit.enable();
//			btnPrivate.enable();
//			
			
			btnSend.setEnabled(true);
			btnConnect.setEnabled(false);
			btnPrivate.setEnabled(true);
			btnQuit.setEnabled(true);
			
			input.setEditable(true);
			targetUserID.setEditable(true);
			
			this.chkboxEncrypt.setEnabled(true);
			
			//enable our buttons.... 
			//either.... use .setEnabled... and have separate buttons and use
			//getSource from within actionPerformed
			//or....... use .setText ... and have 1 button whose face changes and use
			//getActionCommand from within actionPerformed
			//or... still use getSource... and compare against the boolean done
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			done=true;
			//displayMessage("OOPSY"+ e.getMessage()+"... or something nicer");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			done=true;
			//displayMessage("OOPSY"+ e.getMessage()+"... or something nicer");
		}
		finally{
			//displayMessage("the connection attempt completed... success/failure..");
			//onto the next task.. blah blha
		}
		
	}
	

	
	public void send(){
		try {
		//get the message from the input textfield getText(); 
		//...store it in a String
		// make sure to 
			input.requestFocus();
			
			String msgs = input.getText();
			if(msgs.length()<1){
				return;
			}
			if(this.chkboxEncrypt.isSelected()){
				msgs = this.otp.genKeyAndMsg(msgs);
				msgs = this.encMarker+msgs;
				
			}
			strOut.writeUTF(msgs);
			strOut.flush();
			input.setText("");
			btnSend.setEnabled(true);
			btnConnect.setEnabled(false);
			btnPrivate.setEnabled(true);
			btnQuit.setEnabled(true);
			input.setEditable(true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//displayMessage("BLAH");
		}
		
	}
	
	public void privatemsg(){
		try {
		//get the message from the input textfield getText(); 
		//...store it in a String
		// make sure to 
			input.requestFocus();
			String msgs = input.getText();
			String userID = this.targetUserID.getText();
			if(userID.length()!=5){
				return;
			}
			String myView = "Private to "+userID+": "+msgs;
			if(this.chkboxEncrypt.isSelected()){
				myView = "ENCRYPTED "+myView;
			}
			this.displayMsg(myView);
			if(this.chkboxEncrypt.isSelected()){	
				msgs = this.otp.genKeyAndMsg(msgs);
				msgs = this.encMarker+msgs;
			}
			msgs ="private"+userID+" "+msgs;
			strOut.writeUTF(msgs);
			strOut.flush();
			input.setText("");
//			btnConnect.setEnabled(false);
//			btnPrivate.setEnabled(true);
//			btnSend.setEnabled(true);
//			btnQuit.setEnabled(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}	
	public void disconnect(){
		done=true;
		input.setText("Left the chat");
		this.chkboxEncrypt.setSelected(false);
		this.chkboxEncrypt.setEnabled(false);
		send();
		btnSend.setEnabled(false);
		btnConnect.setEnabled(true);
		btnPrivate.setEnabled(false);
		btnQuit.setEnabled(false);
		
		input.setEditable(false);
		targetUserID.setText("");
		targetUserID.setEditable(false);
		
		///either setEnabled(false/true)... for your buttons
		//or...setText("Connect")... for your buttons...
		//if ... stream... is !=null
		//stream.close();
		//do that for in... and for out... do that for the socket...
	}
	public void open(){
		try {
			strOut = new DataOutputStream(socket.getOutputStream());
			strIn = new DataInputStream(socket.getInputStream());
			new Thread(this).start();//to be able to listen in
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//strIn =...
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		 if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isShiftDown()){ 
		    	System.out.println("private text");
		    	this.privatemsg();
		 }
		 else if (e.getKeyCode() == KeyEvent.VK_ENTER){
			 System.out.println("public text");
			 this.send();
		 }
		 else if(e.isControlDown()){
			 this.chkboxEncrypt.doClick();
		 }
	 
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater( new Runnable(){
			public void run() {
				ChatClientGUI chatclient = new ChatClientGUI();
				chatclient.pack();
				chatclient.setVisible(true);
				//chatclient.setSize(500,500);
				chatclient.setTitle("Orange Chat");
				
			}
			
			
		}
				
				);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()== KeyEvent.VK_ENTER){
			this.send();
		}
		
	}

		
	}

