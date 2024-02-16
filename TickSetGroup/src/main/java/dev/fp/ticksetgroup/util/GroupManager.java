package dev.fp.ticksetgroup.util;

import dev.fp.ticksetgroup.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;

public class GroupManager {
    private File groupsFile;
    private FileConfiguration groupConfig;
    private JavaPlugin plugin;

    public GroupManager(JavaPlugin plugin) {
        this.plugin = plugin;
        groupsFile = new File(plugin.getDataFolder(), "groups.yml");
        if (!groupsFile.exists()) {
            try {
                groupsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        groupConfig = YamlConfiguration.loadConfiguration(groupsFile);
    }

    private void updatePlayerNameTag(Player player, String prefix) {
        ScoreboardManager manager = player.getServer().getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();
        String teamName = "team" + prefix.replaceAll(ChatColor.COLOR_CHAR + "[0-9a-fk-or]", "");
        if (teamName.length() > 16) {
            teamName = teamName.substring(0, 16);
        }
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(prefix + " ");
        }
        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }
    }

    public void setGroup(String playerName, String newGroup, String time) {
        groupConfig.set(playerName, newGroup);
        Player player = Bukkit.getPlayer(playerName);

        long delay = parseTimeToTicks(time);

        if (delay > 0) {
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.runTaskLater(plugin, () -> {
                groupConfig.set(playerName, "membro");
                System.out.println("Setted to member");
                String formattedName = Main.getInstance().getConfig("tags").getString("membro");
                player.setPlayerListName(formattedName + " " + player.getDisplayName());
                updatePlayerNameTag(player, formattedName);
                try {
                    groupConfig.save(groupsFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, delay);
        }

        try {
            groupConfig.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getGroup(String playerName) {
        return groupConfig.getString(playerName);
    }

    private long parseTimeToTicks(String time) {
        try {
            char timeType = time.charAt(time.length() - 1);
            long timeValue = Long.parseLong(time.substring(0, time.length() - 1));
            switch (timeType) {
                case 'h':
                    System.out.println("Hours");
                    return timeValue * 20 * 60 * 60;
                case 'd':
                    System.out.println("Days");
                    return timeValue * 20 * 60 * 60 * 24;
                case 'm':
                    System.out.println("Minutes");
                    return timeValue * 20 * 60;
                default:
                    System.out.println("Permanent");
                    return 10000L * 20 * 60 * 60 * 24;
            }
        } catch (NumberFormatException e) {
            return 10000L * 20 * 60 * 60 * 24;
        }
    }
}
