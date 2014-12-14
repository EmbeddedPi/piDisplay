package piDisplay;

public class LCDDriver {
	
	private static final String gpioOn = "1";
	private static final String gpioOff = "0";
	private static final int backlightLED = 11;
	private static final int RS = 10;
	private static final int readWrite = 9;
	private static final int enable = 8;
	private static final int[] gpioChannel = {4,17,14,15,21,22,18,23,25,8,7,24};

public static void initialiseLCD () {
	gpioControl.initialiseGpio (gpioChannel);
}
	
public static void backlightControl (String backlightStatus) {
gpioControl.writePin (gpioChannel[backlightLED], backlightStatus);
}

public static void updateLCD(String LCDStatus) throws InterruptedException{
	if (LCDStatus.matches("On") ) {
		gpioControl.writePin (gpioChannel[RS], gpioOn);
		gpioControl.writePin (gpioChannel[readWrite], gpioOn);
		gpioControl.writePin (gpioChannel[enable], gpioOn);
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (gpioChannel[i], gpioOn);
		sleep(150);
		}
		for (int i=7; i>-1; i--)
		{
		gpioControl.writePin (gpioChannel[i], gpioOff);
		sleep(150);
		}
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (gpioChannel[i], gpioOn);
		sleep(50);
		}
	} 
	else {
		for (int i=0; i<8; i++)
		{
		gpioControl.writePin (gpioChannel[i], gpioOff);
		sleep(50);
		gpioControl.writePin (gpioChannel[RS], gpioOff);
		gpioControl.writePin (gpioChannel[readWrite], gpioOff);
		gpioControl.writePin (gpioChannel[enable], gpioOff);
		}
	}
}       	
	
    private static void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
}
