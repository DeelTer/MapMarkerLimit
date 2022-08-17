package ru.deelter.maplimit;

import org.bukkit.plugin.java.JavaPlugin;

public final class MapMarkerLimit extends JavaPlugin {

	@Override
	public void onLoad() {
		saveDefaultConfig();
	}

	@Override
	public void onEnable() {
		MapMarkerLimitConfig.load(this);
		MarkerPlaceListener.setup(this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
