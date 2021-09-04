package io.github.a5h73y.planez.commands;

import io.github.a5h73y.planez.Planez;
import io.github.a5h73y.planez.enums.Commands;
import io.github.a5h73y.planez.enums.Permissions;
import io.github.a5h73y.planez.other.DelayTasks;
import io.github.a5h73y.planez.other.Help;
import io.github.a5h73y.planez.other.Utils;
import io.github.a5h73y.planez.other.Validation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlanezCommands implements CommandExecutor {

    private final Planez planez;

    public PlanezCommands(Planez planez) {
        this.planez = planez;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("planez")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(Planez.getPrefix() + "Commands are only available in game.");
                return false;
            }

            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage(Planez.getPrefix() + "proudly created by " + ChatColor.AQUA + "A5H73Y");
                player.sendMessage(Utils.getTranslation("Commands"));
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "spawn":
                    if (!Utils.commandEnabled(player, Commands.SPAWN))
                        return false;

                    if (!Utils.hasStrictPermission(player, Permissions.ADMIN))
                        return false;

                    if (!DelayTasks.getInstance().delayPlayer(player, 4))
                        return false;

                    Utils.spawnPlane(player.getLocation());
                    player.sendMessage(Utils.getTranslation("Spawned"));
                    break;

                case "purchase":
                    if (!Utils.commandEnabled(player, Commands.PURCHASE))
                        return false;

                    if (!Validation.canPurchasePlane(player))
                        return false;

                    Utils.givePlayerOwnedPlane(player);
                    player.sendMessage(Utils.getTranslation("Purchased"));
                    break;

                case "fuel":
                    planez.getFuelController().displayFuelLevel(player);
                    break;

                case "refuel":
                    if (!Utils.commandEnabled(player, Commands.REFUEL))
                        return false;

                    if (!Validation.canPurchaseFuel(player))
                        return false;

                    planez.getFuelController().refuel(player.getVehicle().getEntityId(), player);
                    break;

                case "upgrade":
                    if (!Utils.commandEnabled(player, Commands.UPGRADE))
                        return false;

                    if (!Validation.canPurchaseUpgrade(player))
                        return false;

                    planez.getPlaneController().getUpgradeController().upgradePlaneSpeed(player);
                    break;

                case "reload":
                    if (!Utils.hasStrictPermission(player, Permissions.ADMIN))
                        return false;

                    planez.reloadConfig();
                    player.sendMessage(Utils.getTranslation("ConfigReloaded"));
                    break;

                case "cmds":
                    Help.displayCommands(player);
                    break;

                default:
                    player.sendMessage(Utils.getTranslation("Error.UnknownCommand"));
                    player.sendMessage(Utils.getTranslation("Commands"));
            }
        }
        return true;
    }
}
