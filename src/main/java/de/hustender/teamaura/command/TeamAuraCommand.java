package de.hustender.teamaura.command;

import de.hustender.teamaura.impl.TeamAuraImpl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamAuraCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(!player.hasPermission("group.team")) {
            player.sendMessage(ChatColor.RED + "Du bist nicht berechtigt diesen Command auszuführen!");
            return true;
        }
        if(TeamAuraImpl.activate(player)) {
            player.sendMessage(ChatColor.GREEN + "Erfolgreich Team-Aura aktiviert");
            return true;
        }
        player.sendMessage(ChatColor.RED + "Erfolgreich Team-Aura deaktiviert!");
        return true;
    }

}
