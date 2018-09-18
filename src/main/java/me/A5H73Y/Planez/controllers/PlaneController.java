package me.A5H73Y.Planez.controllers;

import me.A5H73Y.Planez.Planez;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlaneController {

    private final Planez planez;

    // Players currently inside a Planez vehicle, not necessarily the owner
    private final Set<String> playersFlying = new HashSet<>();

    // Vehicle ID with it's corresponding Owner's name
    private Map<Integer, String> ownership = new HashMap<>();

    // Control to handle the plane's speed upgrades
    private UpgradeController upgradeController = new UpgradeController();

    public PlaneController(Planez planez) {
        this.planez = planez;
    }

    public UpgradeController getUpgradeController() {
        return upgradeController;
    }

    public void addDriver(String playerName, Integer planeId) {
        playersFlying.add(playerName);
        registerNewPlane(planeId);
    }

    public void removeDriver(String playerName) {
        playersFlying.remove(playerName);
    }

    public boolean isDriving(String playerName) {
        return playersFlying.contains(playerName);
    }

    /**
     * Register a new plane
     * Set the default plane speed, and start the fuel management
     * @param planeID
     */
    public void registerNewPlane(Integer planeID) {
        if (upgradeController.isPlaneRegistered(planeID))
            return;

        upgradeController.setDefaultPlaneSpeed(planeID);
        planez.getFuelController().registerPlane(planeID);
    }

    /**
     * Completely remove a plane
     * Eject a player, remove the ownership and fuel management
     * @param plane
     */
    public void destroyPlane(Vehicle plane) {
        removeOwnership(plane.getEntityId());
        //TODO improve this (1.7)
        try {removeDriver(plane.getPassenger().getName());} catch (NoSuchMethodError ex) {}
        planez.getFuelController().deregisterPlane(plane.getEntityId());
        createDamageEffect(plane);
        plane.eject();
        plane.remove();
    }

    // --- Ownership methods ---
    public String getOwner(Integer planeID) {
        return ownership.get(planeID);
    }

    public boolean isPlaneOwned(Integer planeID) {
        return ownership.containsKey(planeID);
    }

    public boolean isPlaneOwnedByPlayer(Integer planeID, String playerName) {
        return isPlaneOwned(planeID) && ownership.get(planeID).equals(playerName);
    }

    public void declareOwnership(Integer planeID, String playerName) {
        ownership.put(planeID, playerName);
    }

    public void removeOwnership(Integer planeID) {
        ownership.remove(planeID);
    }

    public void createEffect(Location location, Effect effect, final int repeat) {
        if (!planez.getConfig().getBoolean("Other.UseEffects"))
            return;

        new BukkitRunnable() {
            int amount = repeat;
            @Override
            public void run() {
                if (amount == 0) cancel();
                location.getWorld().playEffect(location, effect, 4);
                amount--;
            }
        }.runTaskTimer(Planez.getInstance(), 0, 10);
    }

    public void createDamageEffect(Vehicle plane) {
        createEffect(plane.getLocation().add(0, 1, 0), Effect.SMOKE, 2);
    }

    public void createUpgradeEffect(Vehicle plane) {
        createEffect(plane.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
    }
}
