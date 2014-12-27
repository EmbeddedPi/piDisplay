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
	/* Initialisation code
	 * Function set : 8 bit interface, 2 display lines, 5x7 font
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

public static void updateLCD(String LCDStatus) throws InterruptedException {
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

public static void testByteWrite(String testLEDStatus) throws InterruptedException {
	// System.out.println( "testLEDStatus is: " + testLEDStatus );
	if (testLEDStatus.matches("1") ) {
		/*
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioHigh);
		sleep(25);
		}
		
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioLow);
		sleep(50);
		gpioControl.writePin(dataChannel, gpioHigh);
		sleep(50);
		}
		*/
		for (int i =255; i>-1; i--)
		{
		writeByte(i);	
		}
		// writeByte(0xAA);
	} 
	else {
		/*
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioLow);
		sleep(25);
		}
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioHigh);
		sleep(50);
		gpioControl.writePin(dataChannel, gpioLow);
		sleep(50);
		}
		*/
		for (int i=0; i<256; i++)
		{
		writeByte(i);
		}
		// writeByte(0xCC);
	}
}

// TODO complete this method
public static String testByteRead() {
	String dataByte = "";
	try {gpioControl.initialiseGpio (dataChannel, gpioIn);
	// TODO Add code to read dataChannel here.
	System.out.println( "Testing read code: ");
	sleep(500);
	}
	catch (Exception exception) {
		exception.printStackTrace();
		}
	gpioControl.initialiseGpio(dataChannel, gpioOut);
	return dataByte;
}


// TODO Fix this bit
private static void writeByte (int Byte) throws InterruptedException {
	// Test lined for debugging
	// System.out.println( "hexByte is: " + Byte );
	// Check within a single byte range
	if (Byte<0 | Byte>255)
	{
		System.out.println(Byte + "isn't in range 0~255");
		return;
	}
	String binary = Integer.toBinaryString (Byte);
	// Test line to be deleted later
	// System.out.println("Binary string is " + binary);
	// Fill to 8 bits if less than 128
	if (binary.length()<8) {
		String str = "";
		{
		for (int s=0; s<(8-binary.length()); s++) 
			{
			str = str + "0";
			}
		}
		binary = str + binary; 
		// Test line to be deleted later
		// System.out.println("Adjusted binary string is " + binary);
	}
	for (int i = 0; i<8; i++){
		// Test delay
		sleep(1);
		if (binary.charAt(i) == '1') {
			gpioControl.writePin(dataChannel[(7-i)], gpioHigh);
		}
		else {
			gpioControl.writePin(dataChannel[(7-i)], gpioLow);
		}
	}
}


/*
private void commandWrite(Integer command) {
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	TODO Write "command" to dataChannel;
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO Check data is written, busy flag or both
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

private void dataWrite(Integer data) {
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioLow);
	TODO Write "data" to dataChannel;
	gpioControl.writePin (controlChannel[enable], gpioHigh);
	TODO Check data is written, busy flag or both
	gpioControl.writePin (controlChannel[enable], gpioLow);
}

private integer commandRead(Integer command) {
	gpioControl.writePin (controlChannel[RS], gpioLow);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioOn);
	TODO return(readByte);
	gpioControl.writePin (controlChannel[enable], gpioOff);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}

private void dataRead(Integer data) {
	gpioControl.writePin (controlChannel[RS], gpioHigh);
	gpioControl.writePin (controlChannel[readWrite], gpioHigh);
	gpioControl.initialiseGpio (dataChannel, gpioIn);
	gpioControl.writePin (controlChannel[enable], gpioOn);
	TODO return(readByte);
	gpioControl.writePin (controlChannel[enable], gpioOff);
	gpioControl.initialiseGpio (dataChannel, gpioOut);
}
*/

    private static void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
}
