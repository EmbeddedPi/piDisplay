package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.io.File;
import java.io.FileWriter;

public final class Main extends JavaPlugin implements Listener {
	
	private short local = 0;
	private short notLocal = 0;
	private boolean areYouLocal = false;
	private boolean recentJoin = false;
	private String recentPlayer = "";
	private String recentPlayerIP = "";
	private static final String gpioPath="/sys/class/gpio";
	private static final String exportPath= gpioPath + "/export";
	private static final String unexportPath= gpioPath + "/unexport";
	private static final String devicePath= gpioPath + "/gpio%d";
	private static final String directionPath= devicePath + "/direction";
	private static final String valuePath= devicePath + "/value";
	private static final String gpioOut = "out";
	private static final String gpioOn = "1";
	private static final String gpioOff = "0";
	private static final int[] gpioChannel = {4,17,14,15,21,22,18,23,25,8,7,24,11};
		
	@Override
    public void onEnable() {
		// register listener
		getServer().getPluginManager().registerEvents(this, this);
				
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
		
		// Switch on server LEDs
		// for (int i=0; i<5; i++) {
		// writeLED (gpioChannel[i], gpioOn);
		// }
		// TODO Make a list of players on server with an ArrayList
		getLogger().info("piDisplay is ready to display stuff"); 
	}
 
    @Override
    public void onDisable() {
        //Switch off all LEDs
        //for (int i=0; i<14; i++) {
        //	writeLED (gpioChannel[i], gpioOff);
        //}
        getLogger().info("piDisplay has switched off");
    }
    
    // Someone joins server
    @EventHandler
    public void onLogin(PlayerJoinEvent event) throws Exception {
    	// Check whether internal or external IP address
    	recentPlayer = event.getPlayer().getName();
    	recentPlayerIP = event.getPlayer().getAddress().getHostString();
    	recentJoin = true;
    	isLocal();
    	// Update local/notLocal LED status according
    	updateLED();
        // The following lines are for test purposes only
    	debugMessage();	
    }
    
    // Someone leaves server
    @EventHandler
    public void onLogout(PlayerQuitEvent event) throws Exception{
    	// Check whether internal or external IP address
    	recentPlayer = event.getPlayer().getName();
    	recentPlayerIP = event.getPlayer().getAddress().getHostString();
    	recentJoin = false;
    	isLocal();
    	// Update local/notLocal LED status according
    	updateLED();
    	// The following lines are for test purposes only
    	debugMessage();
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
    
    // Determine player location
    private void isLocal() {
    	// Set local variables and count
    	if (recentPlayerIP.startsWith("192.168")) {
    		areYouLocal = true;
    		if (recentJoin) {
    			local++;
    			}
    		else {
    		local--;
    			}
    		}
    	else {
    		areYouLocal = false;
    		if (recentJoin) {
    			notLocal++;
    			}
    			else {
    			notLocal--;
    			}
    		}
    	}

    // Update player LED status
    private void updateLED() throws InterruptedException{
    	if (local > 0) {
    		for (int i=0; i<14; i++)
    		{
    		writeLED (gpioChannel[i], gpioOn);
    		sleep(50);
    		}
    	} 
    	else {
    		for (int i=0; i<14; i++)
    		{
    		writeLED (gpioChannel[i], gpioOff);
    		sleep(50);
    		}
    		}
    	if (notLocal > 0) {
    		for (int i=9; i<14; i++)
    		{
    		writeLED (gpioChannel[i], gpioOn);
    		}
    		}
    	else {
    		for (int i=9; i<14; i++)
    		{
    		writeLED (gpioChannel[i], gpioOff);
    		}	
    		}	
    }
    
    // LED IO 
    private void writeLED (int channel, String status) {
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
    
    
    private void sleep(int i) throws InterruptedException{
    	Thread.sleep(i);
    }
       
    // Test messages to server log
    private void debugMessage() {
    	String joinString ="";
    	if (recentJoin) {
    		joinString =" has joined Olly's server.";
    		}
    	else {
    	joinString =" has left Olly's server.";
    		}
    	getLogger().info(recentPlayer + joinString); 
    	getLogger().info("They live at " + recentPlayerIP);  	
    	getLogger().info("Are they local is " + areYouLocal);
    	getLogger().info("Locals online = " + local);
    	getLogger().info("Not locals online = " + notLocal);
    	LCDDriver.driverTest();
    	getLogger().info("just done a call to LCDDRiver.driverTest");
    }
}

