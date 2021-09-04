package io.github.a5h73y.planez.controllers;

import io.github.a5h73y.planez.Planez;
import io.github.a5h73y.planez.other.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FuelController {

    private final double MAX_FUEL;
    private final boolean USE_FUEL;
    private final int GAUGE_SCALE;

    // map of plane ID with fuel level (0 - 100)
    private final Map<Integer, Integer> fuelLevel = new HashMap<>();

    public FuelController() {
        USE_FUEL = Planez.getInstance().getConfig().getBoolean("Fuel.Enable");
        MAX_FUEL = Planez.getInstance().getConfig().getDouble("Fuel.StartAmount");
        GAUGE_SCALE = Planez.getInstance().getConfig().getInt("Fuel.GaugeScale");
    }

    public boolean isFuelEnabled() {
        return USE_FUEL;
    }

    public Integer getFuelLevel(Integer planeID) {
        Integer amount = fuelLevel.get(planeID);

        // if the plane has not been started, the amount will not exist, so we can fill it here
        if (amount == null) {
            refuel(planeID);
            amount = fuelLevel.get(planeID);
        }

        return amount;
    }

    /**
     * Display the formatted fuel level of the plane to the player
     * If fuel is disabled, or the player is not inside a vehicle an error will appear instead
     * @param player
     */
    public void displayFuelLevel(Player player) {
        if (!USE_FUEL) {
            player.sendMessage(Utils.getTranslation("Error.FuelDisabled"));
            return;
        }

        if (!player.isInsideVehicle()) {
            player.sendMessage(Utils.getTranslation("Error.NotInPlane"));
            return;
        }

        player.sendMessage(formattedFuelLevel(player.getVehicle().getEntityId()));
    }

    public boolean isFuelConsumed(Integer planeID) {
        return getFuelLevel(planeID) <= 0;
    }

    public void refuel(Integer planeID) {
        fuelLevel.put(planeID, (int) MAX_FUEL);
    }

    public void refuel(Integer planeID, Player player) {
        refuel(planeID);
        player.sendMessage(Utils.getTranslation("Refuel"));
    }

    /**
     * If fuel is enabled then reduce the amount of fuel available
     * This will be run from the VehicleUpdateEvent
     * @param planeID
     */
    public void decreaseFuel(Integer planeID) {
        if (USE_FUEL && !isFuelConsumed(planeID))
            fuelLevel.put(planeID, fuelLevel.get(planeID) - 1);
    }

    public void registerPlane(Integer planeID) {
        refuel(planeID);
    }

    public void deregisterPlane(Integer planeID) {
        fuelLevel.remove(planeID);
    }

    /**
     * Build a formatted string of the fuel gauge, to resemble how much fuel is remaining
     * @param planeID
     * @return
     */
    private String formattedFuelLevel(Integer planeID) {
        StringBuilder sb = new StringBuilder();
        double fuelRemaining = Math.floor((getFuelLevel(planeID) / MAX_FUEL) * GAUGE_SCALE);
        double fuelMissing = GAUGE_SCALE - fuelRemaining;

        sb.append(ChatColor.RED + "E ");
        sb.append(ChatColor.WHITE);

        for (int i=0; i < fuelRemaining; i++)
            sb.append("|");

        sb.append(ChatColor.GRAY);

        for (int i=0; i < fuelMissing; i++)
            sb.append("|");

        sb.append(ChatColor.GREEN + " F");
        return sb.toString();
    }
}
