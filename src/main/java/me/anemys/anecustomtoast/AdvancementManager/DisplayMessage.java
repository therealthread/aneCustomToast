package me.anemys.anecustomtoast.AdvancementManager;

import java.util.Arrays;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class DisplayMessage {
    public abstract void showTo(Player paramPlayer);

    public void showToAll() {
        showTo(Bukkit.getServer().getOnlinePlayers());
    }

    public void showTo(Collection<? extends Player> collection) {
        for (Player player : collection)
            showTo(player);
    }

    public void showTo(Player... players) {
        showTo(Arrays.asList(players));
    }
}

