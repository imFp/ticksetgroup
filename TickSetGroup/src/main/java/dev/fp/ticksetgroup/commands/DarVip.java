package dev.fp.ticksetgroup.commands;

import dev.fp.ticksetgroup.Main;
import dev.fp.ticksetgroup.util.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class DarVip implements CommandExecutor {

    private GroupManager groupManager;

    public DarVip(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 3) {
            if(Bukkit.getPlayerExact(args[0]) != null) {
                Player target = Bukkit.getPlayer(args[0]);
                String playerName = target.getName();
                String group = args[1].toLowerCase();

                if(Main.getInstance().getConfig("tags").getString(group) != null) {
                    if (args.length >= 3) {
                        String time = args[2];
                        groupManager.setGroup(playerName, group, time);
                    } else {
                        groupManager.setGroup(playerName, group, "perm");
                    }

                    String formattedName = Main.getInstance().getConfig("tags").getString(group);
                    sender.sendMessage("§b§lTICK §fVocê deu o vip " + formattedName + "§f para o jogador " + target.getDisplayName());
                    target.sendMessage("§b§lTICK §fVocê recebeu o vip: " + formattedName);
                    target.setPlayerListName(formattedName + " " + target.getDisplayName());
                    updatePlayerNameTag(target, formattedName);
                    return true;
                } else {
                    sender.sendMessage(Main.getInstance().getConfig("messages").getString("darvipwrong"));
                    return true;
                }
            }
        } else {
            sender.sendMessage(Main.getInstance().getConfig("messages").getString("darvipusage"));
            return true;
        }

        return false;

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
        } else {
            String formattedName = Main.getInstance().getConfig("tags").getString(groupName);
            player.getServer().broadcastMessage(formattedName + " " + playerName + ": §f" + message);
        }

        e.setCancelled(true);
    }
}
