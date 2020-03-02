package piDisplay;

public class LCDDriver {
	
	private static final int lineLength = 16;
	private static final int space = 0x20;
	private static final String gpioHigh = "1";
	private static final String gpioLow = "0";
	private static final String gpioOut = "out";
	/*
	TODO Temporarily pulled out read code, to be replaced after testing
	private static final String gpioIn = "in";
	*/
	private static final int backlightLED = 14;
	// Central port 8 (array 1) for readWrite not currently used
	private static final int [] controlChannel = {23,18,15};
	private static final int RS = 0;
	private static final int readWrite = 1;
	private static final int enable = 2;
	//private static final int[] dataChannelP = {7,8,25,24,23,18,15,14};
	private static final int[] dataChannel = {7,8,25,24};
	/*
	TODO Temporarily pulled out read code, to be replaced after testing
	private static final int busyFlagPin = 7;
	*/

public static void initialiseLCD () {
	gpioControl.initialiseGpio (backlightLED, gpioOut);
	gpioControl.initialiseGpio (controlChannel, gpioOut);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
	backlightControl(gpioLow);
	sleep(0);
	// Initialisation code
	// Function set : 8 bit interface, 2 display lines, 5x8 font
	//commandWrite(0x38);
	// Function set : 4 bit interface, 2 display lines, 5x8 font
	commandWrite(0x28);
	// Entry mode set :Increment auto, display shift off
	commandWrite(0x06);
	// Display control : display on, cursor on, no blinking
	commandWrite(0x0E);;
	// Clear display, set cursor position to zero
	commandWrite(0x01);
	// Sets cursor to start of line 1
	commandWrite(0x80);
	writeString(">>Initialised.<<");	
	// Switch cursor to start of line 2
	commandWrite(0xC0);
	writeString ("piDisplay online");
}

// Backlight control method
public static void backlightControl (String backlightStatus) {
	gpioControl.writePin (backlightLED, backlightStatus);
}

// Method for writing strings to display
public static void writeString(String textString) {
	int length = textString.length();
	//Truncate display if longer than lineLength
	if (length>lineLength) {
		length=lineLength;
	}
	for (int i = 0; i<length; i++) {
		dataWrite(fontTable.convertChar(String.valueOf(textString.charAt(i))));
	}
}

//Method for clear line
public static void clearLine(int line) {
	// set cursor to start of required line
	commandWrite(0x80 + (--line) * 0x40);
	for (int i =0; i<lineLength; i++) {
		dataWrite(space);
	}
	// Reset cursor to start of line
	commandWrite(0x80 + (line) * 0x40);
}

//Method for writing commands to display
public static void commandWrite(Integer sendData) {
	// TODO Code to confirm busyCheck() == false before continuing
	//while (busyFlagCheck()==true) {
	//sleep(0);
	//}
	String command = checkByte(sendData);
	String commandMSB = command.substring(0,4);
	String commandLSB = command.substring(4);
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	writeByte(commandMSB);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	writeByte(commandLSB);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

private static void dataWrite(Integer sendData) {
	// TODO Code to confirm busyCheck() == false before continuing
	// while (busyFlagCheck()==true) {
	//sleep(0);
	// }
	String data = checkByte(sendData);
	String dataMSB = data.substring(0,4);
	String dataLSB = data.substring(4);
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	writeByte(dataMSB);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	writeByte(dataLSB);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

//4 bit interface version
private static void writeByte (String dataByte) {
	// TODO Code to confirm busyCheck() == false before continuing
	/*
	 * While (busyFlagCheck()) {
	 * sleep(0)
	 * }
	 */
	// Just checking stuff
	// System.out.println("Final binary is " + binary);
	// int testInt = Integer.parseInt(binary, 2);
	// System.out.println("Final hex is " + Integer.toHexString(testInt));
	for (int i = 0; i<4; i++) {
		if (dataByte.charAt(i) == '1') {
			gpioControl.writePin(dataChannel[(3-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannel[(3-i)], gpioLow);
		}
	}
}

/*8 bit interface version
private static void writeByteP (int Byte) {
	// Check within a single byte range
	if (Byte<0 | Byte>255) {
		System.out.println(Byte + "isn't in range 0~255");
		return;
	}
	String binary = Integer.toBinaryString (Byte);
	// Fill to 8 bits if less than 128
	if (binary.length()<8) {
		String str = "";
		for (int s=0; s<(8-binary.length()); s++) {
			str = "0" + str;
		}
		binary = str + binary;
	}
	
	// Just checking stuff
	// System.out.println("Final binary is " + binary);
	// int testInt = Integer.parseInt(binary, 2);
	// System.out.println("Final hex is " + Integer.toHexString(testInt));
	for (int i = 0; i<8; i++) {
		if (binary.charAt(i) == '1') {
			gpioControl.writePin(dataChannelP[(7-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannelP[(7-i)], gpioLow);
		}
	}
}
End of currently unused 8 bit version */

private static String checkByte(int sendData) {
	if (sendData<0 | sendData>255) {
		System.out.println(sendData+ "isn't in range 0~255");
		return "";
	}
	String binary = Integer.toBinaryString (sendData);
	// Fill to 8 bits if less than 128
	if (binary.length()<8) {
		String str = "";
		for (int s=0; s<(8-binary.length()); s++) {
			str = "0" + str;
		}
		binary = str + binary;
	}
	return binary;
}


private static void sleep(int i) {
	try {
	Thread.sleep(i);
	}
	catch (InterruptedException exception) {
		exception.printStackTrace();
	}
}

/*
TODO Temporarily pulled out read code, to be replaced after testing
// This method is just test code for now
public static void updateLCD(String LCDStatus) {
	if (LCDStatus.matches("On") ) {
		gpioControl.writePin (controlChannel[RS], gpioHigh);
// TODO Check this line, does read instead of write
		gpioControl.writePin (controlChannel[readWrite], gpioHigh);
		gpioControl.writePin (controlChannel[enable], gpioHigh);
		} 
	else {
		gpioControl.writePin (controlChannel[RS], gpioLow);
		gpioControl.writePin (controlChannel[readWrite], gpioLow);
		gpioControl.writePin (controlChannel[enable], gpioLow);
		gpioControl.writePin (dataChannel, gpioLow);
	}
}  

// TODO Possibly remove later
public static int testByteRead() {
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	// int dataByte = gpioControl.readPin(dataChannel[busyFlagPin]);
	// Array read version
	int dataByte = gpioControl.readPin(dataChannel);
	// Single pin read version
	gpioControl.initialiseGpio(dataChannel, gpioOut);
	return dataByte;
}

// TODO change back to private once tested
public static Boolean busyFlagCheck() {
	// Assume busy by default
	Boolean busyFlag = true;
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel[busyFlagPin], gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	int status = gpioControl.readPin(dataChannel[busyFlagPin]);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel[busyFlagPin], gpioOut);
	if (status==1) {
		busyFlag = true;
	} else {
		busyFlag = false;
	}
	return busyFlag;
}

// TODO Possibly remove this after testing
private integer commandRead(Integer command) {
	// TODO Code to confirm busyCheck() == false before continuing
	While (busyFlagCheck()) {
	sleep(0)
	}
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO return(readByte);
	sleep(500);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}

// TODO Possibly remove this after testing
private void dataRead(Integer data) {
	// TODO Code to confirm busyCheck() == false before continuing
	While (busyFlagCheck()) {
	sleep(0)
	}
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO return(readByte);
	sleep(500);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}
*/

}
