package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
// Test code to be removed once LCD driver is working
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.net.InetAddress;

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
	private static final int powerLED = 17;
	// private static String LCDStatus = "Off";
	private static final String LEDOn = "1";
	private static final String LEDOff = "0";
	//Backlight LED is active low
	private static final String backlightOn = "0";
	private static final String backlightOff = "1";
	private static final String LEDOut = "out";
	// Test code to be removed once LCD driver is working
	int testLCDStatus = 0;
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
		//Switch on backlight
		LCDDriver.backlightControl (backlightOn);
		// TODO Make a list of players on server with an ArrayList
		getLogger().info("piDisplay is ready to display stuff"); 
		// LCDDriver.clearLine(1);
    	// LCDDriver.writeString("piDisplayHi");
    	// LCDDriver.clearLine(2);
    	// LCDDriver.writeString("has started.");
	}
 
    @Override
    public void onDisable() {
    	// TODO Clear LCD
    	// LCDStatus = "Off";
    	// Switch off backlight
    	// Clear display
    	LCDDriver.commandWrite(0x01);
    	// LCDDriver.clearLine(1);
    	// LCDDriver.writeString("piDisplayBye");
    	// LCDDriver.clearLine(2);
    	// LCDDriver.writeString("left the arena");
    	// Clear display
    	LCDDriver.commandWrite(0x01);
    	// Switch off display
    	LCDDriver.commandWrite(0x0A);    	
    	LCDDriver.backlightControl (backlightOff);
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
    	LCDDriver.backlightControl (backlightOn);
    	LCDDriver.clearLine(1);
    	LCDDriver.writeString (recentPlayer);
    	LCDDriver.clearLine(2);
    	LCDDriver.writeString ("has arrived!");
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
    	LCDDriver.clearLine(1);
    	LCDDriver.writeString(recentPlayer);
    	LCDDriver.clearLine(2);
    	LCDDriver.writeString("has gone home!");
    	// Switch this line back on after testing
    	// LCDDriver.backlightControl (backlightOff);
    	// The following lines are for test purposes only
    	debugMessage();
    }
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {    
      if (cmd.getName().equalsIgnoreCase("backlight")) { 
        getLogger().info("I've recognised a backlight command");
        // Check a correct number of arguments
        if (args.length <1) {
          sender.sendMessage("This needs an argument!");
          return false;
        } else if (args.length >1) {
          sender.sendMessage("Calm down, too many arguments!");
          return false;
        } else {
          // Command is valid, check argument is valid 
          if (args[0].equalsIgnoreCase("on")) {
        	  // Switch on backlight
        	  LCDDriver.backlightControl (backlightOn);
        	  sender.sendMessage("Backlight switched " + args[0]);
        	  return true;
          } else if (args[0].equalsIgnoreCase("off")) {
        	  // Switch off backlight
        	  LCDDriver.backlightControl (backlightOn);
              sender.sendMessage("Backlight switched " + args[0]);
              return true;
          } else {
              sender.sendMessage("Status needs to be on or off, " + args[0] + " is not a valid argument.");
              return false;
          }
        }
      } else if (cmd.getName().equalsIgnoreCase("powerLED")) { 	  
    	  getLogger().info("I've recognised a powerLED command");
          // Check a correct number of arguments
          if (args.length < 1) {
            sender.sendMessage("This needs an argument!");
            return false;
          } else if (args.length >1) {
            sender.sendMessage("Calm down, too many arguments!");
            return false;
          } else {
        	// Command is valid, check argument is valid 
              if (args[0].equalsIgnoreCase("on")) {
            	  // Switch on power LED
            	  gpioControl.writePin (powerLED, LEDOn);
            	  sender.sendMessage("Power LED switched " + args[0]);
            	  return true;
              } else if (args[0].equalsIgnoreCase("off")) {
            	  // Switch off power LED
            	  gpioControl.writePin (powerLED, LEDOn);
                  sender.sendMessage("Power LED switched " + args[0]);
                  return true;
              } else {
                  sender.sendMessage("Status needs to be on or off, " + args[0] + " is not a valid argument.");
                  return false;
              }
          }
      } else {
        getLogger().info("Gibberish or a typo, either way it ain't happening");
        return false; 
      }
    }    
     
   // Detect when a block has been placed, 
   // Test code to be removed once LCD driver is written and tested
    @EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType(); 
		// Display a message on screen regarding who placed what.
		LCDDriver.clearLine(1);
    	LCDDriver.writeString(player.getName());
    	LCDDriver.clearLine(2);
		LCDDriver.writeString("Placed " + mat);
		/* TODO Test code to read and display data LED status
    	int testByte = LCDDriver.testByteRead();
    	Test line 
    	getLogger().info("Busy flag check is " + LCDDriver.busyFlagCheck());    	
    	getLogger().info("Byte just read is " + testByte);
    	getLogger().info("Busy flag check is " + LCDDriver.busyFlagCheck());
		End of test code to be removed once LCD driver is working
		*/
    } 
    // Determine player location
    private void isLocal() {
    	// Set local variables and count
    	if (recentPlayerIP.equals("192.168.1.1")) {
    		areYouLocal = false;
			if (recentJoin) {
				notLocal++;
			}
			else {
			notLocal--;
			}
		}    	
    	else if (recentPlayerIP.startsWith("192.168.1")) {
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

