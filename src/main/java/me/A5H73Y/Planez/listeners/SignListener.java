package me.A5H73Y.Planez.listeners;

import me.A5H73Y.Planez.Planez;
import me.A5H73Y.Planez.enums.Permissions;
import me.A5H73Y.Planez.enums.PurchaseType;
import me.A5H73Y.Planez.other.Utils;
import me.A5H73Y.Planez.other.Validation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    private final Planez planez;

    public SignListener(Planez planez) {
        this.planez = planez;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        if (!event.getLine(0).equalsIgnoreCase("[planez]"))
            return;

        Player player = event.getPlayer();

        if (!Utils.hasPermission(player, Permissions.CREATE_SIGN)) {
            event.getBlock().breakNaturally();
            event.setCancelled(true);
            return;
        }

        // if it's a valid command, break, otherwise it's unknown, so cancel
        switch (event.getLine(1).toLowerCase()) {
            case "refuel":
            case "purchase":
            case "upgrade":
                break;
            default:
                player.sendMessage(Utils.getTranslation("Error.UnknownSignCommand"));
                player.sendMessage(Planez.getPrefix() + "Valid signs: refuel, purchase, upgrade");
                event.getBlock().breakNaturally();
                event.setCancelled(true);
                return;
        }

        String title = Utils.standardizeText(event.getLine(1));
        player.sendMessage(Planez.getPrefix() + title + " sign created");

        event.setLine(0, Planez.getInstance().getSettings().getSignHeader());
        event.setLine(3, ChatColor.RED + String.valueOf(PurchaseType.fromString(title).getCost()));
    }

    @EventHandler
    public void onSignBreak(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        if ((event.getClickedBlock().getType() != Material.SIGN)
                && (event.getClickedBlock().getType() != Material.WALL_SIGN))
            return;

        if (!Planez.getInstance().getConfig().getBoolean("Planez.SignProtection"))
            return;

        String[] lines = ((Sign) event.getClickedBlock().getState()).getLines();

        if (!ChatColor.stripColor(lines[0]).equalsIgnoreCase("[planez]"))
            return;

        if (!Utils.hasStrictPermission(event.getPlayer(), Permissions.ADMIN)) {
            event.getPlayer().sendMessage(Utils.getTranslation("SignProtected"));
            event.setCancelled(true);
        } else {
            event.getClickedBlock().breakNaturally();
            event.getPlayer().sendMessage("Sign Removed!"); //TODO translation
        }
    }

    @EventHandler
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if ((event.getClickedBlock().getType() != Material.SIGN)
                && (event.getClickedBlock().getType() != Material.WALL_SIGN))
            return;

        Sign sign = (Sign) event.getClickedBlock().getState();
        String[] lines = sign.getLines();

        if (!ChatColor.stripColor(lines[0]).equalsIgnoreCase("[planez]"))
            return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        switch (lines[1].toLowerCase()) {
            case "refuel":
                if (!Validation.canPurchaseFuel(player))
                    return;

                Vehicle car = (Vehicle) player.getVehicle();
                planez.getFuelController().refuel(car.getEntityId(), player);
                break;

            case "purchase":
                if (!Validation.canPurchasePlane(player))
                    return;

                Utils.givePlayerOwnedPlane(player);
                player.sendMessage(Utils.getTranslation("Purchased"));
                break;

            case "upgrade":
                if (!Validation.canPurchaseUpgrade(player))
                    return;

                planez.getPlaneController().getUpgradeController().upgradePlaneSpeed(player);
                break;

            default:
                player.sendMessage(Utils.getTranslation("Error.UnknownSignCommand"));
        }
    }
}