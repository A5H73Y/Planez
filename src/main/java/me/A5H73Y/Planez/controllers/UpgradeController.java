package me.A5H73Y.Planez.controllers;

import me.A5H73Y.Planez.Planez;
import me.A5H73Y.Planez.other.Utils;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class UpgradeController {

    private final Map<Integer, Double> planeSpeed = new HashMap<>();

    /**
     * Check to see if the entity ID has been registered as a plane
     * A plane will have a default upgrade of start speed
     * @param planeID
     * @return
     */
    public boolean isPlaneRegistered(Integer planeID) {
        return planeSpeed.containsKey(planeID);
    }

    public Double getPlaneSpeed(Integer planeID) {
        Double speed = planeSpeed.get(planeID);

        // if the plane has not been started, the speed will not exist, so we can default it here
        if (speed == null) {
            setDefaultPlaneSpeed(planeID);
            speed = planeSpeed.get(planeID);
        }

        return speed;
    }

    /**
     * Set the vehicles speed to the start speed
     * @param planeID
     */
    public void setDefaultPlaneSpeed(Integer planeID) {
        planeSpeed.put(planeID, Planez.getInstance().getSettings().getStartSpeed());
    }

    /**
     * Upgrade the vehicle the player is in
     * Validation is done before this stage, in case this needs to be called regardless
     * @param player
     */
    public void upgradePlaneSpeed(Player player) {
        int planeID = player.getVehicle().getEntityId();
        upgradePlaneSpeed(planeID);
        player.playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, null);
        player.sendMessage(Utils.getTranslation("UpgradeSpeed")
                .replace("%SPEED%", getPlaneSpeed(planeID).toString()));

        Planez.getInstance().getPlaneController().createUpgradeEffect((Vehicle) player.getVehicle());
    }

    /**
     * Apply a speed upgrade to the vehicle
     * The current speed of the plane will be increased by the upgrade speed
     * until the current speed reaches the maximum upgrade limit.
     * @param planeID
     */
    private void upgradePlaneSpeed(int planeID) {
        Double currentSpeed = getPlaneSpeed(planeID);
        Double upgradeBy = Planez.getInstance().getSettings().getUpgradeSpeed();
        Double maxSpeed = Planez.getInstance().getSettings().getUpgradeMaxSpeed();

        if ((currentSpeed + upgradeBy) > maxSpeed) //&& !event.getPlayer().hasPermission("Planez.Admin"))
            return;

        planeSpeed.put(planeID, currentSpeed + upgradeBy);
    }
}
