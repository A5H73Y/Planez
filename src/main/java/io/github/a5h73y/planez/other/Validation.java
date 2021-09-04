package io.github.a5h73y.planez.other;

import io.github.a5h73y.planez.Planez;
import io.github.a5h73y.planez.enums.PurchaseType;
import io.github.a5h73y.planez.enums.Permissions;
import org.bukkit.Material;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

public class Validation {

    /**
     * Validate that the vehicle is a valid Planez vehicle
     * Checks to see if the vehicle is a Minecart that isn't on rails
     * @param cart
     * @return boolean
     */
    public static boolean isAPlanezVehicle(Vehicle cart) {
        if (!(cart instanceof Minecart))
            return false;

        Material material = cart.getLocation().getBlock().getType();
        return (material != XMaterial.RAIL.parseMaterial()) && (material != Material.POWERED_RAIL) && (material != Material.DETECTOR_RAIL);
    }

    /**
     * Check to see if the player is currently able to purchase a car
     * This includes checking the permission status
     * @param player
     * @return boolean
     */
    public static boolean canPurchasePlane(Player player) {
        if (player.isInsideVehicle()) {
            player.sendMessage(Utils.getTranslation("Error.InPlane"));
            return false;
        }

        if (player.getInventory().contains(Material.MINECART)) {
            player.sendMessage(Utils.getTranslation("Error.HavePlane"));
            return false;
        }

        if (!Utils.hasPermission(player, Permissions.PURCHASE))
            return false;

        return Planez.getInstance().getEconomyController().processPurchase(player, PurchaseType.PLANE);
    }

    /**
     * Check to see if the player is currently able to purchase an upgrade
     * This includes checking the permission status
     * @param player
     * @return boolean
     */
    public static boolean canPurchaseUpgrade(Player player) {
        if (!player.isInsideVehicle()) {
            player.sendMessage(Utils.getTranslation("Error.NotInPlane"));
            return false;
        }

        if (!isAPlanezVehicle((Vehicle) player.getVehicle())) {
            return false;
        }

        if (!Utils.hasPermission(player, Permissions.UPGRADE))
            return false;

        double currentSpeed = Planez.getInstance().getPlaneController().getUpgradeController().getPlaneSpeed(player.getVehicle().getEntityId());

        if (currentSpeed >= Planez.getInstance().getSettings().getUpgradeMaxSpeed()) {
            player.sendMessage(Utils.getTranslation("Error.FullyUpgraded"));
            return false;
        }

        return Planez.getInstance().getEconomyController().processPurchase(player, PurchaseType.UPGRADE);
    }

    /**
     * Check to see if the player is currently able to purchase fuel
     * There will be no permission node to purchase fuel, as this would be silly
     * @param player
     * @return boolean
     */
    public static boolean canPurchaseFuel(Player player) {
        if (!player.isInsideVehicle()) {
            player.sendMessage(Utils.getTranslation("Error.NotInPlane"));
            return false;
        }

        if (!isAPlanezVehicle((Vehicle) player.getVehicle())) {
            return false;
        }

        return Planez.getInstance().getEconomyController().processPurchase(player, PurchaseType.FUEL);
    }
}
