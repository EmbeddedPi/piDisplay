package piDisplay;

import org.bukkit.plugin.java.JavaPlugin;

public class LCDDriver extends JavaPlugin {

public static void driverTest() {
	// getLogger().info("LCDDriver testline being called from piDisplay");
	System.out.println("LCDDriver testline being called from piDisplay");
	gpioControl.gpioTest ();
}

}