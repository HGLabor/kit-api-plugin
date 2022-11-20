package de.hglabor.kitapi.plugin;

import de.hglabor.kitapi.KitApi;
import de.hglabor.kitapi.kit.AbstractKit;
import de.hglabor.kitapi.kit.item.KitItemBuilder;
import de.hglabor.kitapi.kit.player.IKitPlayer;
import de.hglabor.kitapi.plugin.command.KitSettingsCommand;
import de.hglabor.kitapi.plugin.player.PaperKitPlayer;
import net.minecraft.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class KitApiPlugin extends JavaPlugin implements Listener {
    private static final Map<UUID, IKitPlayer> PLAYER_REGISTRY = new HashMap<>();

    @Override
    public void onEnable() {
        KitApi.init(uuid -> PLAYER_REGISTRY.computeIfAbsent(uuid, PaperKitPlayer::new), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();
    }

    private void registerCommands() {
        Commands dispatcher = ((CraftServer) Bukkit.getServer()).getServer().vanillaCommandDispatcher;
        KitSettingsCommand.register(dispatcher.getDispatcher());
        //Update loaded Kits
        Bukkit.getScheduler().runTaskLater(this, () -> KitSettingsCommand.register(dispatcher.getDispatcher()), 20L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(event.getItemDrop().getItemStack().hasItemMeta() && event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(KitItemBuilder.MARKER));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager) {
            IKitPlayer kitDamager = KitApi.getKitPlayer(damager.getUniqueId());
            kitDamager.setLatestTarget(event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (AbstractKit kit : KitApi.getKits()) {
            event.getPlayer().getInventory().addItem(kit.getKitItems().toArray(new ItemStack[0]));
        }
    }
}
