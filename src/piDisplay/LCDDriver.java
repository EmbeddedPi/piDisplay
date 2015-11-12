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
	private static final int backlightLED = 24;
	// Central port 8 (array 1) for readWrite not currently used
	private static final int [] controlChannel = {25,8,7};
	private static final int RS = 2;
	private static final int readWrite = 1;
	private static final int enable = 0;
	private static final int[] dataChannel = {23,18,22,21,15,14,17,4};
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
	commandWrite(0x38);
	// Entry mode set :Increment auto, display shift off
	commandWrite(0x06);
	// Display control : display on, cursor on, no blinking
	commandWrite(0x0E);;
	// Clear display, set cursor position to zero
	commandWrite(0x01);
	// Test line
	dataWrite(fontTable.convertChar("I"));
	dataWrite(fontTable.convertChar("n"));
	dataWrite(fontTable.convertChar("i"));
	dataWrite(fontTable.convertChar("t"));
	dataWrite(fontTable.convertChar("i"));
	dataWrite(fontTable.convertChar("a"));
	dataWrite(fontTable.convertChar("l"));
	dataWrite(fontTable.convertChar("i"));
	dataWrite(fontTable.convertChar("s"));
	dataWrite(fontTable.convertChar("e"));
	dataWrite(fontTable.convertChar("d"));
	// Switch cursor to start of line 2
	commandWrite(0xC0);
	writeString ("Prosper is furry");
}
	
public static void backlightControl (String backlightStatus) {
	gpioControl.writePin (backlightLED, backlightStatus);
}

public static void commandWrite(Integer command) {
	// TODO Code to confirm busyCheck() == false before continuing
	//while (busyFlagCheck()==true) {
	//sleep(0);
	//}
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	// sleep(10);
	writeByte(command);
	// sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	// sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

public static void dataWrite(Integer data) {
	// TODO Code to confirm busyCheck() == false before continuing
	// while (busyFlagCheck()==true) {
	//sleep(0);
	// }
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	// sleep(10);
	writeByte(data);
	// sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	// sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

public static void writeByte (int Byte) {
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
	for (int i = 0; i<8; i++) {
		if (binary.charAt(i) == '1') {
			gpioControl.writePin(dataChannel[(7-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannel[(7-i)], gpioLow);
		}
	}
}
public static void writeString(String textString) {
	for (int i = 0; i<lineLength; i++) {
		dataWrite(fontTable.convertChar(String.valueOf(textString.charAt(i))));
	}
}

public static void clearLine(int line) {
	// set cursor to start of required line
	commandWrite(0x80 + (--line) * 0x40);
	for (int i =0; i<lineLength; i++) {
		dataWrite(space);
	}
	// Reset cursor to start of line
	commandWrite(0x80 + (line) * 0x40);
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
