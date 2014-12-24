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
		
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioHigh);
		sleep(50);
		}
		
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioLow);
		sleep(100);
		gpioControl.writePin(dataChannel, gpioHigh);
		sleep(100);
		}
		writeByte("AA");
	} 
	else {
		
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioLow);
		sleep(50);
		}
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioHigh);
		sleep(100);
		gpioControl.writePin(dataChannel, gpioLow);
		sleep(100);
		}
		writeByte("CC");
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
private static void writeByte (String hexByte){
	/*
	// Some code to write a whole byte here
	int decimal = Integer.parseInt(hexByte);
	int[] data = {0,0,0,0,0,0,0,0};
	System.out.println( "Decimal version is: " + decimal );
	//String bitOutput = "";
	data[0] = decimal&1;
	data[1] = decimal&2;
	data[2] = decimal&4;
	data[3] = decimal&8;
	data[4] = decimal&16;
	data[5] = decimal&32;
	data[6] = decimal&64;
	data[7] = decimal&128;
	//for (int i=0; i<8; i++) {
	//	bitOutput = (((data[i])>0)? gpioHigh : gpioLow);
	//	// gpioControl.writePin(dataChannel[i], bitOutput);
	//}
	 */
	gpioControl.writePin(dataChannel[0], gpioHigh);
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
