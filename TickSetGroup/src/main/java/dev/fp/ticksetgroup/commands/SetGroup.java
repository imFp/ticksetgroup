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
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
public class SetGroup implements CommandExecutor, Listener {
    private GroupManager groupManager;

    public SetGroup(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >= 2) {
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

                        sender.sendMessage("§b§lTICK §aVocê setou o grupo de " + target.getDisplayName() + " como: " + formattedName);
                        target.sendMessage("§b§lTICK §aVocê recebeu um novo grupo: " + formattedName);
                        target.setPlayerListName(formattedName + " " + target.getDisplayName());
                        updatePlayerNameTag(target, formattedName);
                        return true;
                    } else {
                        sender.sendMessage(Main.getInstance().getConfig("messages").getString("setgroupwrong"));
                        return true;
                    }
                }
            } else {
                sender.sendMessage(Main.getInstance().getConfig("messages").getString("setgroupusage"));
                return true;
            }
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
}
