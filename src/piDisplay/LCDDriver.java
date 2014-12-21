package piDisplay;

public class LCDDriver {
	
	private static final String gpioOn = "1";
	private static final String gpioOff = "0";
	private static final int backlightLED = 24;
	private static final int [] controlChannel = {25, 8, 7};
	private static final int RS = 2;
	private static final int readWrite = 1;
	private static final int enable = 0;
	private static final int[] dataChannel = {4,17,14,15,21,22,18,23};

public static void initialiseLCD () {
	gpioControl.initialiseGpio (backlightLED);
	gpioControl.initialiseGpio (controlChannel);
	gpioControl.initialiseGpio (dataChannel);
}
	
public static void backlightControl (String backlightStatus) {
gpioControl.writePin (backlightLED, backlightStatus);
}

public static void updateLCD(String LCDStatus) throws InterruptedException {
	if (LCDStatus.matches("On") ) {
		gpioControl.writePin (controlChannel[RS], gpioOn);
		sleep(50);
		gpioControl.writePin (controlChannel[readWrite], gpioOn);
		sleep(50);
		gpioControl.writePin (controlChannel[enable], gpioOn);		
	} 
	else {
		gpioControl.writePin (controlChannel[RS], gpioOff);
		sleep(50);
		gpioControl.writePin (controlChannel[readWrite], gpioOff);
		sleep(50);
		gpioControl.writePin (controlChannel[enable], gpioOff);
	}
}  

public static void testByteWrite(String testLEDStatus) throws InterruptedException {
	if (testLEDStatus.matches("1") ) {
/*		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioOn);
		sleep(50);
		}
*/
		gpioControl.writePin(dataChannel, gpioOn);
	} 
	else {
		/*
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioOff);
		sleep(25);
		}
		*/
		gpioControl.writePin(dataChannel, gpioOff);
	}
}
	 
    private static void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
}
