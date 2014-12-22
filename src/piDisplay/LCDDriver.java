package piDisplay;

public class LCDDriver {
	
	private static final String gpioOn = "1";
	private static final String gpioOff = "0";
	private static final int backlightLED = 24;
	private static final int [] controlChannel = {25,8,7};
	private static final int RS = 2;
	private static final int readWrite = 1;
	private static final int enable = 0;
	private static final int[] dataChannel = {23,18,22,21,15,14,17,4};

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
		
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioOn);
		sleep(50);
		}
		
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioOff);
		sleep(100);
		gpioControl.writePin(dataChannel, gpioOn);
		sleep(100);
		}
	} 
	else {
		
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (dataChannel[i], gpioOff);
		sleep(50);
		}
		
		for (int i = 0; i<8; i++)
		{
		gpioControl.writePin(dataChannel, gpioOn);
		sleep(100);
		gpioControl.writePin(dataChannel, gpioOff);
		sleep(100);
		}
	}
}
	 
    private static void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
}
