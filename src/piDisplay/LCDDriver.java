package piDisplay;

public class LCDDriver {
	
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
	/*
	TODO Temporarily pulled out readWrite code, to be replaced after testing, readWrite pulled to GND
	private static final int readWrite = 1;
	*/
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
	dataWrite(fontTable.convertChar("P"));
	dataWrite(fontTable.convertChar("r"));
	dataWrite(fontTable.convertChar("o"));
	dataWrite(fontTable.convertChar("s"));
	dataWrite(fontTable.convertChar("p"));
	dataWrite(fontTable.convertChar("e"));
	dataWrite(fontTable.convertChar("r"));
	// Switch cursor to start of line 2
	commandWrite(0xC0);
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
	/*
	TODO Temporarily pulled out readWrite code, to be replaced after testing, readWrite pulled to GND
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	*/
	sleep(10);
	writeByte(command);
	sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

public static void dataWrite(Integer data) {
	// TODO Code to confirm busyCheck() == false before continuing
	// while (busyFlagCheck()==true) {
	//sleep(0);
	// }
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	/*
	TODO Temporarily pulled out readWrite code, to be replaced after testing, readWrite pulled to GND
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	*/
	sleep(10);
	writeByte(data);
	sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	sleep(10);
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

private static void writeByte (int Byte) {
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
	for (int i = 0; i<8; i++){
		if (binary.charAt(i) == '1') {
			gpioControl.writePin(dataChannel[(7-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannel[(7-i)], gpioLow);
		}
	}
}

private static void sleep(int i) {
	try {
	Thread.sleep(i);
	}
	catch (InterruptedException exception) {
		exception.printStackTrace();
	}
}

//Test code to be removed later
public static void testByteWrite(int testLCDStatus) {
	if (testLCDStatus == 0) {
		System.out.println( "LCD test char is: " + Integer.toHexString(0xCA));
		dataWrite(0xCA);
		System.out.println( "LCD test char is: " + Integer.toHexString(0xDE));
		dataWrite(0xDE);
		System.out.println( "LCD test char is: " + Integer.toHexString(0xC5));
		dataWrite(0xC5);
		System.out.println( "LCD test char is: " + Integer.toHexString(0xC5));
		dataWrite(0xC5);	
	} 
	else if (testLCDStatus == 1) {
		// clear display
		commandWrite(0x01);
		// Set cursor to position at start of line 1
		commandWrite(0x80);
		dataWrite(0xCA);
		dataWrite(0xDE);
		dataWrite(0xC5);
		dataWrite(0xC5);
		// Set cursor to position at start of line 2
		commandWrite(0xC0);
		dataWrite(fontTable.convertChar("D"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("v"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("i"));
		dataWrite(fontTable.convertChar("s"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("g"));
		dataWrite(fontTable.convertChar("r"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("t"));
		dataWrite(fontTable.convertChar("!"));
	}
	else if (testLCDStatus == 2) {
		// clear display
		commandWrite(0x01);
		// Set cursor to position at start of line 1
		commandWrite(0x80);
		dataWrite(fontTable.convertChar("D"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("v"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("i"));
		dataWrite(fontTable.convertChar("s"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("g"));
		dataWrite(fontTable.convertChar("r"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("t"));
		dataWrite(fontTable.convertChar("!"));
		// Set cursor to position at start of line 2
		commandWrite(0xC0);
		dataWrite(0xCA);
		dataWrite(0xDE);
		dataWrite(0xC5);
		dataWrite(0xC5);
	}
	else if (testLCDStatus == 3) {
		// clear display
		commandWrite(0x01);
		// Set cursor to position at start of line 1
		commandWrite(0x80);
		dataWrite(0xCA);
		dataWrite(0xDE);
		dataWrite(0xC5);
		dataWrite(0xC5);
		// Set cursor to position at start of line 2
		commandWrite(0xC0);
		dataWrite(fontTable.convertChar("J"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("n"));
		dataWrite(fontTable.convertChar("n"));
		dataWrite(fontTable.convertChar("i"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("s"));
		dataWrite(fontTable.convertChar("m"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("l"));
		dataWrite(fontTable.convertChar("l"));
		dataWrite(fontTable.convertChar("s"));
	}
	else if (testLCDStatus == 4) {
		// clear display
		commandWrite(0x01);
		// Set cursor to position at start of line 1
		commandWrite(0x80);
		dataWrite(fontTable.convertChar("J"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("a"));
		dataWrite(fontTable.convertChar("n"));
		dataWrite(fontTable.convertChar("n"));
		dataWrite(fontTable.convertChar("i"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar(" "));
		dataWrite(fontTable.convertChar("s"));
		dataWrite(fontTable.convertChar("m"));
		dataWrite(fontTable.convertChar("e"));
		dataWrite(fontTable.convertChar("l"));
		dataWrite(fontTable.convertChar("l"));
		dataWrite(fontTable.convertChar("s"));
		// Set cursor to position at start of line 2
		commandWrite(0xC0);
		dataWrite(0xCA);
		dataWrite(0xDE);
		dataWrite(0xC5);
		dataWrite(0xC5);
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
