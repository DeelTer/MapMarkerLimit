package ru.deelter.maplimit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapMarkerLimit extends JavaPlugin {

	private static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);

	public static String getNmsVersion() {
		return NMS_VERSION;
	}

	@Override
	public void onLoad() {
		saveDefaultConfig();
	}

	@Override
	public void onEnable() {
		MapMarkerLimitConfig.load(this);
		new MarkerPlaceListener(this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
