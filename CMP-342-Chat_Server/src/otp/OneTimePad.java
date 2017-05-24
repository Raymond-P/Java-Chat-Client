package otp;

public class OneTimePad {
	
	private String plainMessage ="";
	private String encryptedMessage = "";
	private String currentKey="";
	
	public OneTimePad(){
		plainMessage="no message";
		currentKey = generateKey(plainMessage);
		encryptedMessage = encrypt(plainMessage);
		
	}
	public OneTimePad(String msg){
		plainMessage= msg;
		currentKey = generateKey(msg);
		encryptedMessage = encrypt(msg);
		
	}
	private int getNumFromChar(char c){
		return Character.valueOf(c);
	}
	
	private char getCharFromNum(int num){
		return Character.toChars(num)[0];
	}
	
	public String encrypt(String plainMsg){
		String encMsg = "";
		for(int i=0; i<plainMsg.length(); i++){
		int numForPlainChar = getNumFromChar(plainMsg.charAt(i));//get num from plain char
		int numForKeyChar = getNumFromChar(currentKey.charAt(i));//get num from the key's char
		int numForEncChar = numForPlainChar + numForKeyChar;
		char encryptedChar = getCharFromNum(numForEncChar); 
		encMsg += encryptedChar;// append the char to out encrypted message
		}
		return encMsg;
	}
	
	public String decrypt(String encMsg){
		String decMsg = "";
		for(int i=0; i<encMsg.length(); i++){
			int numForEncChar = getNumFromChar(encMsg.charAt(i));
			int numForKeyChar = getNumFromChar(currentKey.charAt(i));
			int numForPlainChar = numForEncChar - numForKeyChar;
			char plainChar = getCharFromNum(numForPlainChar);
			decMsg += plainChar;
		}
		return decMsg;
	}
	
	public String generateKey(String plainMsg){
		String key = "";
		for(int i=0; i<plainMsg.length(); i++){
			int randNum = 64 + (int)(Math.random()*26);
			key += getCharFromNum(randNum);
		}
		return key;
	}
	
	public static void main(String [] args){
		OneTimePad otp = new OneTimePad("Hello World! :D HOPE TO SEE YOU SOON! ;)");//"ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		System.out.println("The Plain Message: "+ otp.plainMessage);
		System.out.println("The Key for the  Message: "+ otp.currentKey);
		String encMsg = otp.encryptedMessage;
		System.out.println("The Encrypted Message: "+ encMsg );
		
		System.out.println("The Decripted Message: "+ otp.decrypt(encMsg));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
