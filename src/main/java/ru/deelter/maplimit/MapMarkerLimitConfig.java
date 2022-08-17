package ru.deelter.maplimit;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class MapMarkerLimitConfig {

	private static int maxBannersOnMap;
	private static String warnLimitMessage;

	public static void load(@NotNull MapMarkerLimit instance) {
		FileConfiguration config = instance.getConfig();
		maxBannersOnMap = config.getInt("max-banners-on-map");
		warnLimitMessage = config.getString("warn-limit-message");
	}

	public static int getMaxBannersOnMap() {
		return maxBannersOnMap;
	}

	public static String getWarnLimitMessage() {
		return warnLimitMessage;
	}
}
