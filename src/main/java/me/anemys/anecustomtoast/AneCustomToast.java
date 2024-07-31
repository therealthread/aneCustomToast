package me.anemys.anecustomtoast;

import me.anemys.anecustomtoast.AdvancementManager.AdvancementMessage;
import me.anemys.anecustomtoast.bstats.Metrics;
import me.anemys.anecustomtoast.updatechecker.UpdateChecker;
import me.anemys.anecustomtoast.utils.ColorUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AneCustomToast extends JavaPlugin implements TabCompleter {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage( "§8[§6aneCustomToast§8] "
                +"§ev_" + this.getDescription().getVersion() + " §eenabling ...");
        Metrics metrics = new Metrics(this,22853);
        Bukkit.getConsoleSender().sendMessage( "§8[§6aneCustomToast§8] "
                +"§eEnabled! "+"v_"+this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage( "§8[§6aneCustomToast§8] "
                +"§eAuthor:§a Anemys!");
        new UpdateChecker(this, 118531).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                Bukkit.getConsoleSender().sendMessage("§8[§6aneCustomToast§8] "
                        +"§aThere is not a new update available.");
            } else {
                Bukkit.getConsoleSender().sendMessage("§8[§6aneCustomToast§8] "
                        +"§cThere is a new update available.");
            }
        });
        this.getCommand("anetoast").setTabCompleter(this);
    }

    @Override // 2. ARG MAX 43 LENGTH
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("anetoast")) {
            if (!sender.hasPermission("anetoast.use")) {
                sender.sendMessage("§8[§6aneCT§8] §cYou do not have permission to use this command!");
                return true;
            }

            if (args.length < 4) {
                if(sender instanceof Player){
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 2, 1);
                }
                sender.sendMessage("§8------------§8[§6§l aneCustomToast §8]------------");
                sender.sendMessage("§8");
                sender.sendMessage("§e/anetoast reload (Not needed...)");
                sender.sendMessage("§e/anetoast <type> <frame> <player> <title> <icon>");
                sender.sendMessage("§8");
                sender.sendMessage("§8------------§8[§6§l aneCustomToast §8]------------");
                return true;
            }

            Player targetPlayer = Bukkit.getPlayer(args[2]);
            if (targetPlayer == null) {
                if (args[0].equalsIgnoreCase("player")){
                    sender.sendMessage("§8[§6aneCustomToast§8] §cPlayer not found!");
                    return true;
                }
            }

            String iconName = args[args.length - 1].toUpperCase().replace('İ', 'I');
            Material iconMaterial = Material.getMaterial(iconName);
            //sender.sendMessage(iconName + " " + iconMaterial);
            if (iconMaterial == null) {  //CHECK ICON NAME
                sender.sendMessage("§8[§6aneCustomToast§8] §cInvalid item type: §4" + iconMaterial);
                return true;//LAST ARG TO ICON NAME 
            }

            String title;
            if (args[0].equalsIgnoreCase("all")) {
                title = ColorUtils.translateHexColorCodes(String.join(" ", Arrays.copyOfRange(args, 2, args.length - 1)));
            } else if (args[0].equalsIgnoreCase("player")) {
                title = ColorUtils.translateHexColorCodes(String.join(" ", Arrays.copyOfRange(args, 3, args.length - 1)));
            } else {
                title = "ERROR[1]: No selection (all or player)";
            }

            String frame;
            if (args[1].equalsIgnoreCase("challenge")) {
                frame = "challenge";
            } else if (args[1].equalsIgnoreCase("goal")) {
                frame = "goal";
            } else if (args[1].equalsIgnoreCase("task")) {
                frame = "task";
            } else {
                sender.sendMessage("§8[§6aneCustomToast§8] §cPlease enter a frame: challenge/goal/task");
                return true;
            }

            String icon = iconName.toLowerCase().replace('ı', 'i');
            NamespacedKey key = new NamespacedKey(this, "anecustomtoast_" + System.currentTimeMillis());
            AdvancementMessage advancementMessage = new AdvancementMessage(key, title, icon, frame, this);

            if (args[0].equalsIgnoreCase("player")){
                advancementMessage.showTo(targetPlayer);
                sender.sendMessage("§8[§6aneCustomToast§8] §e Toast message was shown to player: " + ChatColor.GREEN + targetPlayer.getName());

            }else if (args[0].equalsIgnoreCase("all")) {
                advancementMessage.showToAll();
                sender.sendMessage("§8[§6aneCustomToast§8] §eToast message shown to all players.");

            }else{
                sender.sendMessage("§8[§6aneCustomToast§8] §cPlease enter a type: player/all");
            }
            return true;
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("anetoast")) {
            if (args[0].equalsIgnoreCase("player")) {
                if (args.length == 3) {
                    return null; // Aktif oyuncuları döndür
                }
            }
            if (args[0].equalsIgnoreCase("player")) {
                if (args.length > 4) { // 5. argümandan sonra icon istiyecek
                    String input = args[args.length - 1].toLowerCase();
                    StringUtil.copyPartialMatches(input, Arrays.stream(Material.values())
                            .filter(Material::isItem)
                            .map(material -> material.name().toLowerCase()
                                    .replace('ı', 'i'))
                            .collect(Collectors.toList()), completions);
                }
            } else if (args[0].equalsIgnoreCase("all")) {
                if (args.length > 3) { // 5. argümandan sonra icon istiyecek
                    String input = args[args.length - 1].toLowerCase();
                    StringUtil.copyPartialMatches(input, Arrays.stream(Material.values())
                            .filter(Material::isItem)
                            .map(material -> material.name().toLowerCase()
                                    .replace('ı', 'i'))
                            .collect(Collectors.toList()), completions);
                }
            }

            if (args[0].equalsIgnoreCase("player")) {
                if (args.length >= 4) {
                    completions.add(0,"<String> <String>");
                }

            }else if (args[0].equalsIgnoreCase("all")) {
                if (args.length >= 3) {
                    completions.add(0, "<String> <String>");
                }
            }

            if (args.length == 1) {
                completions.add(0,"player");
                completions.add(1,"all");
            }

            if (args.length == 2) {
                completions.add(0,"challenge");
                completions.add(1,"goal");
                completions.add(2,"task");
            }
        }
        return completions;
    }
}