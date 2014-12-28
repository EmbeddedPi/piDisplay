package piDisplay;

public class LCDDriver {
	
	private static final String gpioHigh = "1";
	private static final String gpioLow = "0";
	private static final String gpioOut = "out";
	private static final String gpioIn = "in";
	private static final int backlightLED = 24;
	private static final int [] controlChannel = {25,8,7};
	private static final int RS = 2;
	private static final int readWrite = 1;
	private static final int enable = 0;
	private static final int[] dataChannel = {23,18,22,21,15,14,17,4};

public static void initialiseLCD () {
	gpioControl.initialiseGpio (backlightLED, gpioOut);
	gpioControl.initialiseGpio (controlChannel, gpioOut);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
	sleep(0);
	/* Initialisation code
	 * Function set : 8 bit interface, 2 display lines, 5x8 font
	 * commandWrite("0x38");
	 * Entry mode set :Increment auto, display shift off
	 * commandWrite("0x06");
	 * Display control : display on, cursor on, no blinking
	 * commandWrite("0x0E")
	 * Clear display, set cursor position to zero
	 * commandWrite("0x01");
	 * Test line
	 * dataWrite("P");
	 * dataWrite("r");
	 * dataWrite("o");
	 * dataWrite("s");
	 * dataWrite("P");
	 * dataWrite("e");
	 * dataWrite("r");
	 * dataWrite("!");
	 */
}
	
public static void backlightControl (String backlightStatus) {
	gpioControl.writePin (backlightLED, backlightStatus);
}

// This method is just test code for now
public static void updateLCD(String LCDStatus) {
	if (LCDStatus.matches("On") ) {
		gpioControl.writePin (controlChannel[RS], gpioHigh);
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

// Test code to be removed later
public static void testByteWrite(String testLEDStatus) {
	// System.out.println( "testLEDStatus is: " + testLEDStatus );
	if (testLEDStatus.matches("1") ) {
		//for (int i =255; i>-1; i--)
		//{
		//writeByte(i);	
		//}
		writeByte(0xAA);
	} 
	else {
		//for (int i=0; i<256; i++)
		//{
		//writeByte(i);
		//}
		writeByte(0xCC);
	}
}

// TODO complete this method
public static String testByteRead() {
	String dataByte = "";
	try {gpioControl.initialiseGpio (dataChannel, gpioIn);
	// TODO Add code to read dataChannel here.
	dataByte = gpioControl.readPin(dataChannel);
	sleep(500);
	}
	catch (Exception exception) {
		exception.printStackTrace();
		}
	gpioControl.initialiseGpio(dataChannel, gpioOut);
	return dataByte;
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
			str = str + "0";
		}
		binary = str + binary; 
	}
	// TODO Code to confirm busyCheck() == false before continuing
	/*
	 * While (!busyCheck) {
	 * sleep(0)
	 * end loop
	 */
	for (int i = 0; i<8; i++){
		if (binary.charAt(i) == '1') {
			gpioControl.writePin(dataChannel[(7-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannel[(7-i)], gpioLow);
		}
	}
}

/*
private Boolean busyCheck() {
	Boolean checkBit = true;
	String status ="";
 	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	status = gpioControl.readPin(dataChannel[7]);
	if (status.matches("1")) {
			checkBit = true;
	} else {
		checkBit = false;
	}
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
	return checkBit;
}
*/

/*
private void commandWrite(Integer command) {
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO Write "command" to dataChannel;
	gpioControl.writePin (controlChannel[enable], gpioLow);
	TODO Check data is written, busy flag or both
}

private void dataWrite(Integer data) {
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO Write "data" to dataChannel;
	gpioControl.writePin (controlChannel[enable], gpioLow);
	TODO Check data is written, busy flag or both
}

// TODO Possibly remove this after testing
private integer commandRead(Integer command) {
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO return(readByte);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}

// TODO Possibly remove this after testing
private void dataRead(Integer data) {
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO return(readByte);
	gpioControl.writePin (controlChannel[enable], gpioLow);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}
*/

    private static void sleep(int i) {
    	try {
    	Thread.sleep(i);
    	}
    	catch (InterruptedException exception) {
    		exception.printStackTrace();
    	}
    }
}
