package dev.fp.ticksetgroup.event;
import dev.fp.ticksetgroup.Main;
import dev.fp.ticksetgroup.commands.SetGroup;
import dev.fp.ticksetgroup.util.GroupManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public class PlayerEvents implements Listener {

    private GroupManager groupManager;
    public PlayerEvents(GroupManager groupManager) {
        this.groupManager = groupManager;
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getDisplayName();
        String message = e.getMessage();
        String groupName = groupManager.getGroup(player.getName());

        if (Main.getInstance().getConfig("tags").getString(groupName) == null) {
            String formattedName = Main.getInstance().getConfig("tags").getString("membro");
            player.getServer().broadcastMessage(formattedName + " " + playerName + ": §f" + message);
            updatePlayerNameTag(player, formattedName);
            player.setPlayerListName(formattedName + " " + player.getDisplayName());
        } else {
            String formattedName = Main.getInstance().getConfig("tags").getString(groupName);
            player.getServer().broadcastMessage(formattedName + " " + playerName + ": §f" + message);
            updatePlayerNameTag(player, formattedName);
            player.setPlayerListName(formattedName + " " + player.getDisplayName());
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getDisplayName();

        e.setJoinMessage("§7[§a+§7] §a" + playerName + " entrou no servidor.");

        if(groupManager.getGroup(playerName) == null) {
            groupManager.setGroup(playerName, "membro", "perm");
            String formattedName = Main.getInstance().getConfig("tags").getString("membro");
            updatePlayerNameTag(player, formattedName);
            player.setPlayerListName(formattedName + " " + player.getDisplayName());
            System.out.println("User without a group joined");
        } else {
            String group = groupManager.getGroup(playerName);
            if (group != null) {
                String formattedName = Main.getInstance().getConfig("tags").getString(group);
                updatePlayerNameTag(player, formattedName);
                player.setPlayerListName(formattedName + " " + player.getDisplayName());
            } else {
                groupManager.setGroup(playerName, "membro", "perm");
                String formattedName = Main.getInstance().getConfig("tags").getString("membro");
                updatePlayerNameTag(player, formattedName);
                player.setPlayerListName(formattedName + " " + player.getDisplayName());
            }

            System.out.println("User with a group joined");
        }
    }
}
