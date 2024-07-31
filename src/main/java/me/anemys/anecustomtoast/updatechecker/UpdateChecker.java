package me.anemys.anecustomtoast.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~")
                    .openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().warning("§8[§6aneCustomToast§8] "+"§eUnable to check for updates: §c" + e.getMessage());
            }
        });
    }

}
//https://www.spigotmc.org/resources/<resource's name>.<resource's id>
//https://api.spigotmc.org/legacy/update.php?resource=<resource's id>
//https://api.spigotmc.org/legacy/update.php?resource=9089
