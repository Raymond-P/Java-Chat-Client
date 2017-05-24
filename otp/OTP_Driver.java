package otp;

public class OTP_Driver extends OneTimePad {
	String myKey;//need my own key since the key for the one time pad class is private and with no getter
	
	/**
	 * Determines how an encrypted message with a key will be made<br>
	 * example: Key = key, Message = msg<br>
	 * 1.<b>Key_Then_Msg:</b> keymsg<br>
	 * 2.<b>Msg_Then_Key:</b> msgKey<br>
	 * 3.<b>Combined_Key_Then_Msg:</b> kmesyg<br>
	 * 4.<b>Combined_Msg_Then_Key:</b> mksegy<br>
	 * @author Raymond Perez
	 *
	 */
	public enum EncryptionType{Key_Then_Msg,Msg_Then_Key,Combined_Key_Then_Msg,Combined_Msg_Then_Key}
	
	//copied from the one time pad class
	private int getNumFromChar(char c){
		return Character.valueOf(c);
	}
	
	//copied from the one time pad class
	private char getCharFromNum(int num){
		return Character.toChars(num)[0];
	}
	
	//copied from the one time pad class
	//minor changes: added the first line in the method and replaced references to the class's key to this classes key
	@Override
	public String encrypt(String plainMsg){
		this.myKey = generateKey(plainMsg);
		String encMsg = "";
		for(int i=0; i<plainMsg.length(); i++){
		int numForPlainChar = getNumFromChar(plainMsg.charAt(i));//get num from plain char
		int numForKeyChar = getNumFromChar(myKey.charAt(i));//get num from the key's char
		int numForEncChar = numForPlainChar + numForKeyChar;
		char encryptedChar = getCharFromNum(numForEncChar); 
		encMsg += encryptedChar;// append the char to out encrypted message
		}
		return encMsg;
	}
	
	//copied from the one time pad class
	//minor changes: replaced references to the class's key to the key being provided
	/**
	 * Decrypts a message using the given key
	 * @param encMsg -Message to be decrypted
	 * @param key -The key to decrypt the message
	 * @return the message in plain text
	 */
	public String decryptWithKey(String encMsg,String key){
		this.myKey=key;
		String decMsg = "";
		for(int i=0; i<encMsg.length(); i++){
			int numForEncChar = getNumFromChar(encMsg.charAt(i));
			int numForKeyChar = getNumFromChar(key.charAt(i));
			int numForPlainChar = numForEncChar - numForKeyChar;
			char plainChar = getCharFromNum(numForPlainChar);
			decMsg += plainChar;
		}
		return decMsg;
	}
	
	/**
	 * Calls <code>decryptWithKey</code> and passes this classes key as the 
	 * <code>key</code> parameter.
	 * @param encMsg -Message to be decrypted
	 */
	@Override
	public String decrypt(String encMsg){
		return this.decryptWithKey(encMsg, myKey);
	}
	
	
	/**
	 * Prints out the original message along with it's encryption 
	 * @param message -message to be encrypted
	 */
	public void testEncryption(String message){
		System.out.println("Testing Encryption...");
		System.out.println("Message: "+message);
		System.out.println("Encrypting message...");
		
		String encryptedMessage = this.encrypt(message);
		System.out.println("Encrypted Message: "+encryptedMessage);
		
		System.out.println("Key: "+this.myKey);
		
		String decryptedMessage = this.decrypt(encryptedMessage);
		System.out.println("Decrypted Message: "+decryptedMessage);
		
		System.out.println();
	}
	
	/**
	 * Generates a string that contains the encrypted message along with it's key
	 * The key is located before the message
	 * @param message
	 */
	public String genKeyAndMsg(String message){
//		String encryptedMsg = this.encrypt(message);
//		String key = this.myKey;
//		return key+encryptedMsg;
		return genKeyAndMsg(message, EncryptionType.Key_Then_Msg);
	}
	
	/**
	 * 
	 * @param message
	 * @param encType
	 * @return
	 */
	public String genKeyAndMsg(String message, EncryptionType encType){
		String encryptedMsg = this.encrypt(message);
		String key = this.myKey;
		String code="";
		switch (encType) {
		case Key_Then_Msg:
			code=key+encryptedMsg;
			break;
		case Msg_Then_Key:
			code = encryptedMsg+key;
			break;
		case Combined_Key_Then_Msg:
			for (int i = 0; i < key.length(); i++) {
				String temp = ""+key.charAt(i)+encryptedMsg.charAt(i);
				code+=temp;
			}
			break;
			
		case Combined_Msg_Then_Key:
			for (int i = 0; i < key.length(); i++) {
				String temp = ""+encryptedMsg.charAt(i)+key.charAt(i);
				code+=temp;
			}
			break;
		}
		return code;
	}
	
	/**
	 * Decrypts an encrypted message where the key and the message are passed in together
	 * and the key is located before the message
	 * @param message
	 * @return
	 */
	public String decKeyAndMsg(String message){
//		String key = message.substring(0, message.length()/2);
//		String msg = message.substring(message.length()/2);
//		this.myKey = key;
//		return this.decrypt(msg);
		return this.decKeyAndMsg(message, EncryptionType.Key_Then_Msg);
	}
	
	/**
	 * 
	 * @param message
	 * @param encType
	 * @return
	 */
	public String decKeyAndMsg(String message, EncryptionType encType){
		String key = "";
		String msg = "";
		switch (encType) {
		case Key_Then_Msg:
			key = message.substring(0, message.length()/2);
			msg = message.substring(message.length()/2);
			break;
		case Msg_Then_Key:
			key = message.substring(message.length()/2);
			msg = message.substring(0, message.length()/2);
			break;
		case Combined_Key_Then_Msg:
			for(int i=0;i< message.length();i++){
				if(i%2==0){
					key+=""+message.charAt(i);
				}
				else{
					msg+=""+message.charAt(i);
				}
			}
			break;
		case Combined_Msg_Then_Key:
			for(int i=0;i< message.length();i++){
				if(i%2==0){
					msg+=""+message.charAt(i);
				}
				else{
					key+=""+message.charAt(i);
				}
			}
			break;
		}
		return decryptWithKey(msg, key);
	}
	
	
	public void test_KeyAndMsg(String message){
//		System.out.println("Testing Encryption...");
//		System.out.println("Message: "+message);
//		System.out.println("Encrypting message...");
//		
//		String encryptedMessage = this.genKeyAndMsg(message);
//		System.out.println("Encrypted Message: "+encryptedMessage);
//		
//		System.out.println("Decrypting Message....");
//		System.out.println("Key: "+this.myKey);
//		
//		String decryptedMessage = this.decKeyAndMsg(encryptedMessage);
//		System.out.println("Decrypted Message: "+decryptedMessage);
//		//System.out.println("Reprinting Key: "+this.myKey);
//		
//		System.out.println();
		this.test_KeyAndMsg(message, EncryptionType.Key_Then_Msg);
	}
	
	public void test_KeyAndMsg(String message, EncryptionType encType){
		System.out.println("Testing Encryption...");
		System.out.println("Message: "+message);
		System.out.println("Encrypting message...");
		
		String encryptedMessage = this.genKeyAndMsg(message,encType);
		System.out.println("Encrypted Message: "+encryptedMessage);
		
		System.out.println("Decrypting Message....");
		System.out.println("Key: "+this.myKey);
		
		String decryptedMessage = this.decKeyAndMsg(encryptedMessage,encType);
		System.out.println("Decrypted Message: "+decryptedMessage);
		//System.out.println("Reprinting Key: "+this.myKey);
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		OTP_Driver d = new OTP_Driver();
		String test = "Hello there this is a test ZZZZ ZZZZ ";
//		String[] testWords = test.split(" ");
//		d.testEncryption(test);
//		for(String s: testWords){
//			d.testEncryption(s);
//		}
		for(EncryptionType type: EncryptionType.values()){
			System.out.println("Encryption Type: "+type);
			System.out.println("=======================================");
			d.test_KeyAndMsg(test,type);
//			for(String s:testWords){
//				d.test_KeyAndMsg(s, type);
//			}
		}
	}
}
