package de.hustender.teamaura;

import de.hustender.teamaura.command.TeamAuraCommand;
import de.hustender.teamaura.impl.TeamAuraImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamAura extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("teamaura").setExecutor(new TeamAuraCommand());
        Bukkit.getPluginManager().registerEvents(new TeamAuraImpl(), this);
        TeamAuraImpl.init(this);
    }

    @Override
    public void onDisable() {
        TeamAuraImpl.auras.values().forEach(pairs -> pairs.forEach(pair -> pair.left.remove()));
        TeamAuraImpl.auras.clear();
    }

}
