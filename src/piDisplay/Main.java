package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class Main extends JavaPlugin implements Listener {
	
	private short local = 0;
	private short notLocal = 0;
	private boolean areYouLocal = false;
	private boolean recentJoin = false;
	private String recentPlayer = "";
	private String recentPlayerIP = "";
	private static final int powerLED = 12;
	public static String LCDStatus = "Off";
	public static final String gpioOn = "1";
	public static final String gpioOff = "0";
	// TODO remove last elements related to LCD
	private static final int[] gpioChannel = {4,17,14,15,21,22,18,23,25,8,7,24,11};
		
	@Override
    public void onEnable() {
		// register listener
		getServer().getPluginManager().registerEvents(this, this);
				
		// Open file handles for GPIO unexport and export
		// TODO Split LCD and LED pin initialisations
		gpioControl.initialiseGpio (gpioChannel);
		// TODO Change to non array call
		gpioControl.writePin (gpioChannel[powerLED], gpioOn);
		
		// TODO Make a list of players on server with an ArrayList
		getLogger().info("piDisplay is ready to display stuff"); 
	}
 
    @Override
    public void onDisable() {
    	// TODO Change to non array call
    	gpioControl.writePin (gpioChannel[powerLED], gpioOff);
    	// TODO Switch off backlight
    	// TODO Clear LCD
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
    	// TODO check playerArray as currently just handles one player
    	LCDStatus = "On";
    	LCDDriver.updateLCD(LCDStatus);
    	LCDDriver.backlightControl (gpioOn);
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
    	// TODO check playerArray as currently just handles one player
    	LCDStatus = "Off";
    	LCDDriver.updateLCD(LCDStatus);
    	LCDDriver.backlightControl (gpioOff);
    	// The following lines are for test purposes only
    	debugMessage();
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
    	getLogger().info("just done a call to LCDDRiver.driverTest");
    }
}

