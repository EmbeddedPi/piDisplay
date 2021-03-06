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

//TODO Consider removing if not used
//import java.net.InetAddress;

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
	//Test line to debug backlight issues
	private static final int backlightLED = 14;
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
		//LCDDriver.backlightControl (backlightOn);
		gpioControl.writePin (backlightLED, backlightOn);
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
    	LCDDriver.display ("off");	
    	//LCDDriver.backlightControl (backlightOff);
    	gpioControl.writePin (backlightLED, backlightOff);
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
    	int intArgs;
      if (cmd.getName().equalsIgnoreCase("backlight")) { 
        getLogger().info("I've recognised a backlight command");
        // Check a correct number of arguments
        if (!checkArgs(sender, args.length, 1)) {
        	return false; 
        } else {
        	// Command is valid, check argument is valid 
        	if (args[0].equalsIgnoreCase("on")) {
        		// Switch on backlight
        		//LCDDriver.backlightControl (backlightOn);
        		gpioControl.writePin (backlightLED, backlightOn);
        		sender.sendMessage("Backlight switched " + args[0]);
        		return true;
        	} else if (args[0].equalsIgnoreCase("off")) {
        		// Switch off backlight
        		//LCDDriver.backlightControl (backlightOff);
        		gpioControl.writePin (backlightLED, backlightOff);
        		sender.sendMessage("Backlight switched " + args[0]);
        		return true;
        	} else {
        		sender.sendMessage("Status needs to be on or off, " + args[0] + " is not a valid argument.");
        		return false;
        	}
        }
      } else if (cmd.getName().equalsIgnoreCase("display")) { 	  
    	  getLogger().info("I've recognised a display command");
          // Check a correct number of arguments
    	  if (!checkArgs(sender, args.length, 1)) {
          	return false; 
          } else {
        	  // Command is valid, check argument is valid 
              if (args[0].equalsIgnoreCase("on")) {
            	  // Switch on power LED
            	  LCDDriver.display ("On");
            	  sender.sendMessage("Display switched " + args[0]);
            	  return true;
              } else if (args[0].equalsIgnoreCase("off")) {
            	  // Switch off power LED
            	  LCDDriver.display ("off");
                  sender.sendMessage("Display switched " + args[0]);
                  return true;
              } else {
                  sender.sendMessage("Status needs to be on or off, " + args[0] + " is not a valid argument.");
                  return false;
              }
          }    
      } else if (cmd.getName().equalsIgnoreCase("initialise")) { 	  
    	  getLogger().info("I've recognised an initialise command");
          // Check a correct number of arguments
    	  if (!checkArgs(sender, args.length, 0)) {
    		  return false; 
          } else {
        	  LCDDriver.initialiseLCD ();
        	  return true;
          }
    	  
    	  
      } else if (cmd.getName().equalsIgnoreCase("initLCD")) { 	  
    	  getLogger().info("I've recognised an initLCD command");
          // Check a correct number of arguments
    	  if (!checkArgs(sender, args.length, 1)) {
    		  return false; 
          } else {
        	  if (isInteger(args[0])) {
        		  intArgs = Integer.parseInt(args[0]);
        		  LCDDriver.initLCD (intArgs);
        	  return true;
        	  } else {
        		  sender.sendMessage("Argument needs to be an integer, " + args[0] + " is not.");
        		  return false;
        	  }
          }
      } else if (cmd.getName().equalsIgnoreCase("powerLED")) { 	  
    	  getLogger().info("I've recognised a powerLED command");
          // Check a correct number of arguments
    	  if (!checkArgs(sender, args.length, 1)) {
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
            	  gpioControl.writePin (powerLED, LEDOff);
                  sender.sendMessage("Power LED switched " + args[0]);
                  return true;
              } else {
                  sender.sendMessage("Status needs to be on or off, " + args[0] + " is not a valid argument.");
                  return false;
              }
          }
      } else if (cmd.getName().equalsIgnoreCase("displaySend")) { 
    	  if (!checkArgs(sender, args.length, 42)) {
          	return false; 
         } else {
          	  	int i=0;
          	  	String displayString ="";
            	for (i=0; i<(args.length-1); i++) {
            		// Display message
            		displayString += args[i] + " ";
            	} 
            	displayString += args[args.length-1];
            	LCDDriver.clearLine(1);
        		LCDDriver.writeString(displayString);
        		sender.sendMessage(displayString + " displayed ");
            	return true;
            }
      }
      
      else {
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
    
    private boolean checkArgs(CommandSender sender, int argsLength, int argsRequired) {
    	//If 42 args required, don't panic as long as 1 is there, you have your towel with you
    	if (argsRequired==42) {
    		if (argsLength==0) {
    			sender.sendMessage("This needs an argument!");
    			return false;	
    		} else {
    			return true;
    		}
    	//Otherwise just check correct number of arguments
    	} else if (argsRequired==0){ 
    		if (argsLength>argsRequired) {
    			sender.sendMessage("Calm down, this requires no arguments");
    			return false;
    		} else if (argsLength<argsRequired) {
    			sender.sendMessage("How on earth did you manage that?");
    			return false;
    		} else {
    			return true;
    		}	
    	}
    	
    	{ 
    		if (argsLength>argsRequired) {
    			sender.sendMessage("Calm down, too many arguments!");
    			return false;
    		} else if (argsLength<argsRequired) {
    			sender.sendMessage("Liven things up, an argument is needed!");
    			return false;
    		} else {
    			return true;
    		}	
    	}
    }
    
    private static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}

