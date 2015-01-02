package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
// Test code to be removed once LCD driver is working
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
// End of test code to be removed once LCD driver is working


public final class Main extends JavaPlugin implements Listener {
	
	private short local = 0;
	private short notLocal = 0;
	private boolean areYouLocal = false;
	private boolean recentJoin = false;
	private String recentPlayer = "";
	private String recentPlayerIP = "";
	private static final int powerLED = 11;
	// private static String LCDStatus = "Off";
	private static final String LEDOn = "1";
	private static final String LEDOff = "0";
	private static final String LEDOut = "out";
	// Test code to be removed once LCD driver is working
	String testLEDStatus = "1";
	// End of test code to be removed once LCD driver is working
	
	@Override
    public void onEnable(){
		// register listener
		getServer().getPluginManager().registerEvents(this, this);
		// LCD and LED pin initialisations
		gpioControl.initialiseGpio(powerLED, LEDOut);
		LCDDriver.initialiseLCD ();
		// Switch on power LED
		gpioControl.writePin (powerLED, LEDOn);
		// TODO Make a list of players on server with an ArrayList
		getLogger().info("piDisplay is ready to display stuff"); 
	}
 
    @Override
    public void onDisable() {
    	// TODO Clear LCD
    	// LCDStatus = "Off";
    	// Switch off backlight
    	LCDDriver.backlightControl (LEDOff);
    	// Switch off power LED
    	gpioControl.writePin (powerLED, LEDOff);
    	getLogger().info("piDisplay has switched off");
    }
    
    // Someone joins server
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
    	// Check whether internal or external IP address
    	recentPlayer = event.getPlayer().getName();
    	recentPlayerIP = event.getPlayer().getAddress().getHostString();
    	recentJoin = true;
    	isLocal();
    	// TODO check playerArray as currently just handles one player
    	// LCDStatus = "On";
    	// LCDDriver.updateLCD(LCDStatus);
    	LCDDriver.backlightControl (LEDOn);
        // The following lines are for test purposes only"
    	debugMessage();
    }
    
    // Someone leaves server
    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
    	// Check whether internal or external IP address
    	recentPlayer = event.getPlayer().getName();
    	recentPlayerIP = event.getPlayer().getAddress().getHostString();
    	recentJoin = false;
    	isLocal();
    	// Update local/notLocal LED status according
    	// TODO check playerArray as currently just handles one player
    	// Turn the lights off when you leave!
    	// LCDStatus = "Off";
    	// LCDDriver.updateLCD(LCDStatus);
    	LCDDriver.backlightControl (LEDOff);
    	// The following lines are for test purposes only
    	debugMessage();
    }
     
   // Detect when a block has been placed, 
   // Test code to be removed once LCD driver is written and tested
    @EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType(); 
		// Display a message to the player telling them what type of block they placed.
		player.sendMessage("You placed a block with ID : " + mat);
		// TODO add test code utilising block material
		// SAND, DIRT
		if (testLEDStatus.matches("0")) {
			testLEDStatus= "1";
		}
		else {
			testLEDStatus= "0";
		}
		LCDDriver.testByteWrite (testLEDStatus);
		// TODO Test code to read and display data LED status
    	int testByte = LCDDriver.testByteRead();
    	// Test line 
    	getLogger().info("Busy flag check is " + LCDDriver.busyFlagCheck());    	
    	getLogger().info("Byte just read is " + testByte);
    	getLogger().info("Busy flag check is " + LCDDriver.busyFlagCheck());
    }
    // End of test code to be removed once LCD driver is working
    
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
    }
}

