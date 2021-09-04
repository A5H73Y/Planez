package io.github.a5h73y.planez.other;

import io.github.a5h73y.planez.Planez;
import io.github.a5h73y.planez.enums.Commands;
import io.github.a5h73y.planez.enums.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Help {

    public static void displayCommands(Player player) {
        FileConfiguration config = Planez.getInstance().getConfig();
        player.sendMessage(Utils.getStandardHeading("Planez Commands"));

        if (Planez.getInstance().getFuelController().isFuelEnabled()) {
            displayCommandUsage(player, "fuel", "Display the plane's fuel");
            if (config.getBoolean(Commands.REFUEL.getConfigPath()))
                displayCommandUsage(player, "refuel", "Refuel your plane");
        }
        if (config.getBoolean(Commands.SPAWN.getConfigPath()) && player.isOp())
            displayCommandUsage(player, "spawn", "Spawn a plane at your location");

        if (config.getBoolean(Commands.PURCHASE.getConfigPath()))
            displayCommandUsage(player, "purchase", "Purchase a plane");

        if (config.getBoolean(Commands.UPGRADE.getConfigPath()))
            displayCommandUsage(player, "upgrade", "Upgrade your plane");

        if (Utils.hasStrictPermission(player, Permissions.ADMIN))
            displayCommandUsage(player, "reload", "Reload the config");
    }

    /**
     * Format and display command usage
     *
     * @param player
     * @param title
     * @param description
     */
    private static void displayCommandUsage(Player player, String title, String description) {
        player.sendMessage(ChatColor.DARK_AQUA + "/Planez " + ChatColor.AQUA + title +
                ChatColor.BLACK + " : " + ChatColor.WHITE + description);
    }
}
