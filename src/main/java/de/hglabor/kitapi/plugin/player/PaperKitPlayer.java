package de.hglabor.kitapi.plugin.player;

import de.hglabor.kitapi.kit.AbstractKit;
import de.hglabor.kitapi.kit.player.AbstractKitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PaperKitPlayer extends AbstractKitPlayer {

    public PaperKitPlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    public boolean hasKit(AbstractKit kit) {
        return true;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sendCooldownInfo(AbstractKit kit, String key) {
        getPlayer().ifPresent(player -> {
            long endTime = ((Map<String, Long>) kitAttributes.getOrDefault(kit.getName() + "kitCooldown", new HashMap<>())).getOrDefault(key, 0L);
            if (endTime > 0) {
                long remainingTime = endTime - System.currentTimeMillis();
                player.sendMessage("Cooldown for Key " + key + " " + remainingTime);
            }
        });
    }
}
