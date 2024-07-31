package me.anemys.anecustomtoast.AdvancementManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AdvancementMessage extends DisplayMessage {
    private NamespacedKey id;

    private String icon;

    private String title;

    private String description;

    private String frame;// = "challenge";

    private boolean announce = false;

    private boolean toast = true;

    private JavaPlugin pl;

    public AdvancementMessage(String id, String title, String icon, String frame, JavaPlugin pl) {
        this(new NamespacedKey((Plugin)pl, id), title, icon, frame, pl);
    }

    public AdvancementMessage(NamespacedKey id, String title, String icon, String frame, JavaPlugin pl) {
        this.id = id;
        this.title = title;
        this.frame = frame;
        this.description = "Any text";
        this.icon = icon;
        this.pl = pl;
    }

    public void showTo(Player player) {
        showTo(Arrays.asList(new Player[] { player }));
    }

    public void showTo(final Collection<? extends Player> players) {
        add();
        grant(players);
        (new BukkitRunnable() {
            public void run() {
                AdvancementMessage.this.revoke(players);
                AdvancementMessage.this.remove();
            }
        }).runTaskLater((Plugin)this.pl, 20L);
    }

    private void add() {
        try {
            Bukkit.getUnsafe().loadAdvancement(this.id, getJson());
            //Bukkit.getLogger().info("Advancement " + this.id + " saved.");
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().info("ERROR[2]: " + this.id + " already exist!");
        }
    }

    private void remove() {
        Bukkit.getUnsafe().removeAdvancement(this.id);
    }

    private void grant(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(this.id);
        for (Player player : players) {
            AdvancementProgress progress = player.getAdvancementProgress(advancement);
            if (!progress.isDone())
                for (String criteria : progress.getRemainingCriteria())
                    progress.awardCriteria(criteria);
        }
    }

    private void revoke(Collection<? extends Player> players) {
        Advancement advancement = Bukkit.getAdvancement(this.id);
        for (Player player : players) {
            AdvancementProgress progress = player.getAdvancementProgress(advancement);
            if (progress.isDone())
                for (String criteria : progress.getAwardedCriteria())
                    progress.revokeCriteria(criteria);
        }
    }

    public String getJson() {
        JsonObject json = new JsonObject();
        JsonObject icon = new JsonObject();

        icon.addProperty("item", this.icon);
        JsonObject display = new JsonObject();
        display.add("icon", (JsonElement)icon);
        display.addProperty("title", this.title);
        display.addProperty("description", this.description);
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame", this.frame);
        display.addProperty("announce_to_chat", Boolean.valueOf(this.announce));
        display.addProperty("show_toast", Boolean.valueOf(this.toast));
        display.addProperty("hidden", Boolean.valueOf(true));
        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();
        trigger.addProperty("trigger", "minecraft:impossible");
        criteria.add("impossible", (JsonElement)trigger);
        json.add("criteria", (JsonElement)criteria);
        json.add("display", (JsonElement)display);
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        return gson.toJson((JsonElement)json);
    }
}
