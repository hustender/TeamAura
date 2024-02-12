package de.hustender.teamaura.impl;

import de.hustender.teamaura.TeamAura;
import de.hustender.teamaura.util.Pair;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TeamAuraImpl implements Listener {

    public static Map<Player, Set<Pair<ArmorStand, Integer>>> auras = new HashMap<>();

    public static void init(TeamAura instance) {
        new Impl().runTaskTimer(instance, 0, 5);
    }

    public static boolean activate(Player player) {
        if(auras.containsKey(player)) {
            disable(player);
            return false;
        }
        auras.put(player, new HashSet<>());
        return true;
    }

    public static void disable(Player player) {
        auras.get(player).forEach(pair -> pair.left.remove());
        auras.remove(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!auras.containsKey(player)) return;
        auras.get(player).forEach(pair -> pair.left.remove());
        auras.remove(player);
    }

    public static String getRank(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%").split(" »")[0];
    }

    private static class Impl extends BukkitRunnable {
        @Override
        public void run() {
            auras.values().forEach(list -> list.forEach(pair -> pair.right -= 8));
            auras.values().forEach(list -> list.stream().filter(pair -> pair.right <= 0).forEach(pair -> pair.left.remove()));
            auras.values().forEach(list -> list.removeIf(pair -> pair.right <= 0));
            auras.forEach(((player, pairs) -> {
                if(!player.isOnline()) return;
                if(player.isInvisible() || player.getGameMode().equals(GameMode.SPECTATOR) || PlaceholderAPI.setPlaceholders(player, "%premiumvanish_isvanished%").equals("Yes")) return;
                double xOffset = Math.random() * 2.0D - 1.0D;
                double yOffset = Math.random() * 2.0D;
                double zOffset = Math.random() * 2.0D - 1.0D;
                Location loc = player.getLocation().clone().add(xOffset, yOffset, zOffset);
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                armorStand.setMarker(true);
                armorStand.setVisible(false);
                armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', getRank(player)));
                armorStand.setCustomNameVisible(true);
                armorStand.setInvulnerable(true);
                armorStand.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
                armorStand.setCollidable(false);

                pairs.add(new Pair<>(armorStand, 40));
            }));
        }
    }

}
