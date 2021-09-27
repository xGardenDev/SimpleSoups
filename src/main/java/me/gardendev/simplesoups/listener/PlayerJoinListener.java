package me.gardendev.simplesoups.listener;

import me.gardendev.simplesoups.PluginCore;
import me.gardendev.simplesoups.manager.FileManager;
import me.gardendev.simplesoups.utils.ItemFactory;
import me.gardendev.simplesoups.enums.GameStatus;
import me.gardendev.simplesoups.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerJoinListener implements Listener {

    private final PluginCore pluginCore;
    private final FileManager config;

    public PlayerJoinListener(PluginCore pluginCore) {
        this.pluginCore = pluginCore;
        this.config = pluginCore.getFilesLoader().getConfig();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void loadPlayerData(PlayerJoinEvent event) {
        pluginCore.getPlugin().getLogger().info("Load data of " + event.getPlayer().getName());
        pluginCore.getPlayerData().loadPlayerData(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void loadScoreboard(PlayerJoinEvent event) {
        if (!config.getBoolean("scoreboard.enable")) return;
        pluginCore.gameScoreboard().create(event.getPlayer());
    }

    @EventHandler
    public void joinPlayerItems(PlayerJoinEvent event) {
        event.getPlayer().setMetadata("status", new FixedMetadataValue(pluginCore.getPlugin(), GameStatus.SPAWN));
        if (!config.getBoolean("items-on-join")) return;
        if (config.getBoolean("clear-on-join")){
            PlayerUtils.clearInventory(event.getPlayer());
        }

        ItemFactory itemFactory = new ItemFactory(
                Material.valueOf(config.getString("items-join.kits.material")),
                1,
                config.getString("items-join.kits.display"),
                config.getStringList("items-join.kits.lore")
        );

        event.getPlayer().getInventory().setItem(
                config.getInt("items-join.kits.slot"),
                itemFactory.getItem()
        );
    }

    @EventHandler
    public void joinPlayerTeleport(PlayerJoinEvent event) {

        if(!config.getBoolean("join-teleport")) return;
        if (config.contains("spawn.world")) {
            event.getPlayer().teleport(new Location(
                    Bukkit.getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"),
                    (float) config.getDouble("spawn.yaw"),
                    (float) config.getDouble("spawn.pirch")
            ));
        }
    }




}
