package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;

public class LCDDriver extends JavaPlugin {
	
	private static final int backlightLED = 11;
	private static final int[] gpioChannel = {4,17,14,15,21,22,18,23,25,8,7,24,11};

public static void driverTest() {
	// getLogger().info("LCDDriver testline being called from piDisplay");
	System.out.println("LCDDriver testline being called from piDisplay");
	gpioControl.gpioTest ();
	}

public static void backlightControl (String backlightStatus) {
gpioControl.writePin (gpioChannel[backlightLED], backlightStatus);
}

public static void updateLCD(String LCDStatus) throws InterruptedException{
	if (LCDStatus.matches("On") ) {
		for (int i=0; i<11; i++)
		{
		gpioControl.writePin (gpioChannel[i], Main.gpioOn);
		sleep(150);
		}
		for (int i=9; i>-1; i--)
		{
		gpioControl.writePin (gpioChannel[i], Main.gpioOff);
		sleep(150);
		}
		for (int i=0; i<11; i++)
		{
		gpioControl.writePin (gpioChannel[i], Main.gpioOn);
		sleep(50);
		}
	} 
	else {
		for (int i=0; i<11; i++)
		{
		gpioControl.writePin (gpioChannel[i], Main.gpioOff);
		sleep(50);
		}
	}
}       	

	
    private static void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
}
