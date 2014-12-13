package piDisplay;

import java.io.File;
import java.io.FileWriter;

import org.bukkit.plugin.java.JavaPlugin;

public class gpioControl extends JavaPlugin {
	
	private static final String gpioPath="/sys/class/gpio";
	private static final String exportPath= gpioPath + "/export";
	private static final String unexportPath= gpioPath + "/unexport";
	private static final String devicePath= gpioPath + "/gpio%d";
	private static final String directionPath= devicePath + "/direction";
	private static final String valuePath= devicePath + "/value";
	private static final String gpioOut = "out";
//	private static final String gpioOn = "1";
//	private static final String gpioOff = "0";
//	private static final int[] gpioChannel = {4,17,14,15,21,22,18,23,25,8,7,24,11};
	

public static void gpioTest() {
	// getLogger().info("gpioControl testline being called from LCDDriver");
	System.out.println("gpioControl testline being called from LCDDriver");
}

public static void initialiseGpio (int [] gpioChannel) {
	// Open file handles for GPIO unexport and export
			try {
				FileWriter unexportFile = new FileWriter(unexportPath);
				FileWriter exportFile = new FileWriter(exportPath);
			
				// Initialise GPIO settings
				for (Integer channel : gpioChannel) {
					File exportFileCheck = new File(getDevicePath(channel));
					if (exportFileCheck.exists()) {
						unexportFile.write(channel.toString());
						unexportFile.flush();	
						}
					// Set port for use
					exportFile.write(channel.toString());
					exportFile.flush();
					//Set direction file
					FileWriter directionFile = new FileWriter(getDirectionPath(channel));
					directionFile.write(gpioOut);
					directionFile.flush();
					directionFile.close();
					}
						
				unexportFile.close();
				exportFile.close();
				}
			catch (Exception exception) {
				exception.printStackTrace();
				}
			
}


// Variable setting for device path
private static String getDevicePath(int pinNumber) {
	   return String.format(devicePath, pinNumber);
}

// Variable setting for direction path
private static String getDirectionPath(int pinNumber) {
	   return String.format(directionPath, pinNumber);
}

// Variable setting for value path
private static String getValuePath(int pinNumber) {
	   return String.format(valuePath, pinNumber);
}

// LED IO 

public static void writePin (int channel, String status) {
	try {
 		FileWriter commandFile = new FileWriter(getValuePath(channel));
 		commandFile.write(status);
 		commandFile.flush();
 		commandFile.close();
    }
    catch (Exception exception) {
    	exception.printStackTrace();
    }
}
		
public String readPin() {
	final String status = "";
//	TODO Make some code to read pin status
	return status;
	}


}