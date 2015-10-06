package piDisplay;

import java.io.File;
import java.io.FileWriter;
/*
TODO Temporarily pulled out read code, to be replaced after testing
import java.io.RandomAccessFile;
*/

public class gpioControl {
	
	private static final String gpioPath="/sys/class/gpio";
	private static final String exportPath= gpioPath + "/export";
	private static final String unexportPath= gpioPath + "/unexport";
	private static final String devicePath= gpioPath + "/gpio%d";
	private static final String directionPath= devicePath + "/direction";
	private static final String valuePath= devicePath + "/value";
	/*
	TODO Temporarily pulled out read code, to be replaced after testing
	private static final int maxBuffer = 128;
	*/
	
public static void initialiseGpio (int [] gpioChannel, String direction) {
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
					directionFile.write(direction);
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

// Overloaded single integer version of initialiseGpio
 public static void initialiseGpio (int singleChannel, String direction) {
	  int[] gpioChannel = {0};
	  gpioChannel[0] = singleChannel;
	  initialiseGpio (gpioChannel, direction);
}
 
// Write to a single gpio pin
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

// TODO Possibly delete this later.
// Overloaded array version of WritePin 
public static void writePin (int [] gpioChannel, String status) {
	for (Integer pin : gpioChannel) {
 	writePin(pin, status);
 	}
}


//Variable setting for device path
private static String getDevicePath(int pinNumber) {
	   return String.format(devicePath, pinNumber);
}

//Variable setting for direction path
private static String getDirectionPath(int pinNumber) {
	   return String.format(directionPath, pinNumber);
}

//Variable setting for value path
private static String getValuePath(int pinNumber) {
	   return String.format(valuePath, pinNumber);
}

/*
TODO Temporarily pulled out read code, to be replaced after testing
public static int readPin(int channel[]) {
	RandomAccessFile[] raf = new RandomAccessFile[channel.length];
	byte[] readData = new byte[maxBuffer];
	// String tempString = "";
	String status = "";
	try {
		for (int i=0; i < raf.length; i++) {
		raf[i] = new RandomAccessFile(getValuePath(channel[i]), "r");
		raf[i].seek(0);
		raf[i].read(readData);
		status = status + new String(readData);
		}
	}
	catch (Exception exception) {
  	exception.printStackTrace();
	}
	// System.out.println("Before char strip " + status);
	// Tidy up by removing unwanted characters and newlines
	String numberString = status.replaceAll("[^0-1]","");
	// System.out.println("After char strip " + numberString);
	// Reverse so bit order is MSB -> LSB
	numberString = new StringBuilder(numberString).reverse().toString();
	// System.out.println("After reverse " + numberString);
	try {
	int pinData = Integer.parseInt(numberString,2);
	System.out.println(pinData);
	return pinData;
	}
	catch (NumberFormatException nfe) {
		nfe.printStackTrace();
	}
	return -1;
}
*/

/*
TODO Temporarily pulled out read code, to be replaced after testing
//Overloaded single integer version of readPin
public static int readPin(int singleChannel) {
	  int[] gpioChannel = {0};
	  gpioChannel[0] = singleChannel;
	  int singlePinData = readPin(gpioChannel);
	  return singlePinData;	  
}
*/

}