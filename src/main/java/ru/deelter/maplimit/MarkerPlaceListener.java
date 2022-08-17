package ru.deelter.maplimit;

import net.kyori.adventure.text.Component;
import net.minecraft.world.level.saveddata.maps.MapIconBanner;
import net.minecraft.world.level.saveddata.maps.WorldMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MarkerPlaceListener implements Listener {

	private final Class<?> mapRenderClass;

	public MarkerPlaceListener(MapMarkerLimit instance) {
		try {
			this.mapRenderClass = Class.forName("org.bukkit.craftbukkit." + MapMarkerLimit.getNmsVersion() + ".map.CraftMapRenderer");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(this, instance);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlaceMarker(@NotNull PlayerInteractEvent event) {
		if (!event.getAction().isRightClick()) return;

		Block block = event.getClickedBlock();
		if (block == null || !Tag.BANNERS.isTagged(block.getType())) return;

		EquipmentSlot hand = event.getHand();
		if (hand == null) return;

		Player player = event.getPlayer();
		ItemStack itemHand = player.getInventory().getItem(hand);
		if (itemHand.getType() != Material.FILLED_MAP) return;

		MapMeta mapMeta = (MapMeta) itemHand.getItemMeta();
		MapView mapView = mapMeta.getMapView();
		if (mapView == null) return;

		List<MapRenderer> renders = mapView.getRenderers();
		if (renders.isEmpty()) return;

		MapRenderer renderer = renders.get(0);
		WorldMap worldMap = getWorldMap(renderer);

		Map<String, MapIconBanner> banners = getMapIcons(worldMap);
		if (banners.size() < MapMarkerLimitConfig.getMaxBannersOnMap()) return;

		Component warn = Component.text(MapMarkerLimitConfig.getWarnLimitMessage());
		player.sendActionBar(warn);
		event.setCancelled(true);

	}

	private Map<String, MapIconBanner> getMapIcons(@NotNull WorldMap worldMap) {

		try {
			Field field = worldMap.getClass().getDeclaredField("p");
			field.setAccessible(true);
			return (Map<String, MapIconBanner>) field.get(worldMap);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private WorldMap getWorldMap(@NotNull MapRenderer renderer) {
		try {
			Field field = mapRenderClass.getDeclaredField("worldMap");
			field.setAccessible(true);
			return (WorldMap) field.get(renderer);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}