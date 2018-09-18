package me.A5H73Y.Planez.listeners;

import me.A5H73Y.Planez.Planez;
import me.A5H73Y.Planez.other.DelayTasks;
import me.A5H73Y.Planez.other.Utils;
import me.A5H73Y.Planez.other.Validation;
import org.bukkit.Effect;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VehicleListener implements Listener {

    private final Planez planez;

    public VehicleListener(Planez planez) {
        this.planez = planez;
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        if (!(event.getVehicle().getPassenger() instanceof Player))
            return;

        if (!Validation.isAPlanezVehicle(event.getVehicle()))
            return;

        Player player = (Player) event.getVehicle().getPassenger();
        Integer planeId = event.getVehicle().getEntityId();

        if (!planez.getPlaneController().isDriving(player.getName()))
            return;

        if (planez.getFuelController().isFuelConsumed(planeId)) {
            player.sendMessage(Utils.getTranslation("FuelEmpty"));
            planez.getPlaneController().removeDriver(player.getName());
            return;
        }

        //TODO stop looking straight up.

        if (event.getVehicle().getLocation().getBlock().isLiquid() &&
                planez.getSettings().isDestroyInLiquid()) {
            planez.getPlaneController().destroyPlane(event.getVehicle());
            player.playEffect(player.getLocation(), Effect.EXTINGUISH, null);
            player.sendMessage(Utils.getTranslation("LiquidDamage"));
            return;
        }

        double planeSpeed = planez.getPlaneController().getUpgradeController().getPlaneSpeed(planeId);


        Vector vector = player.getLocation().getDirection().multiply(planeSpeed / 140D);
        vector.setY((player.getEyeLocation().getDirection().getY() / 200.0D) * planeSpeed);

        event.getVehicle().setVelocity(vector);
        planez.getFuelController().decreaseFuel(planeId);
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player))
            return;

        if (!Validation.isAPlanezVehicle(event.getVehicle()))
            return;

        if (planez.getConfig().getBoolean("UsePermissions") && !event.getEntered().hasPermission("Planez.Start"))
            return;

        Player player = (Player) event.getEntered();
        Integer planeID = event.getVehicle().getEntityId();

        if (planez.getPlaneController().isPlaneOwned(planeID)) {
            if (!planez.getPlaneController().isPlaneOwnedByPlayer(planeID, player.getName())) {
                player.sendMessage(Utils.getTranslation("Error.Owned"));
                event.setCancelled(true);
                return;
            } else {
                player.sendMessage(Utils.getTranslation("PlaneUnlocked"));
            }
        }

        if (planez.getFuelController().getFuelLevel(planeID) != null) {
            planez.getFuelController().displayFuelLevel(player);
        }

        if (planez.getConfig().getBoolean("Key.GiveOnPlaneEnter") &&
                !player.getInventory().contains(planez.getSettings().getKey())) {
            player.sendMessage(Utils.getTranslation("KeyReceived"));
            player.getInventory().addItem(new ItemStack(planez.getSettings().getKey()));
        }
    }

    @EventHandler
    public void onEngineStart(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!event.getPlayer().isInsideVehicle())
            return;

        if (!Validation.isAPlanezVehicle((Vehicle) event.getPlayer().getVehicle()))
            return;

        if (planez.getConfig().getBoolean("UsePermission") && !event.getPlayer().hasPermission("Planez.Start"))
            return;

        if (Utils.getMaterialInPlayersHand(event.getPlayer()) != planez.getSettings().getKey() &&
                planez.getConfig().getBoolean("Key.RequirePlanezKey"))
            return;

        if (!DelayTasks.getInstance().delayPlayer(event.getPlayer(), 1))
            return;

        Player player = event.getPlayer();
        Minecart minecart = (Minecart) event.getPlayer().getVehicle();

        if (planez.getPlaneController().isDriving(player.getName())) {
            planez.getPlaneController().removeDriver(player.getName());
            minecart.setMaxSpeed(0D);
            player.sendMessage(Utils.getTranslation("EngineStop"));

        } else {
            planez.getPlaneController().addDriver(player.getName(), player.getVehicle().getEntityId());
            minecart.setMaxSpeed(1000D);
            player.sendMessage(Utils.getTranslation("EngineStart"));
        }
    }

    @EventHandler
    public void onPlaneDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Minecart))
            return;

        if (!Validation.isAPlanezVehicle((Vehicle) event.getEntity()))
            return;

        planez.getPlaneController().destroyPlane((Vehicle) event.getEntity());
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (!(event.getExited() instanceof Player)) {
            return;
        }

        if (!Validation.isAPlanezVehicle(event.getVehicle()))
            return;

        Player player = (Player) event.getExited();

        if (planez.getPlaneController().isDriving(player.getName())) {
            planez.getPlaneController().removeDriver(player.getName());
            player.sendMessage(Utils.getTranslation("EngineStop"));

            if (planez.getPlaneController().isPlaneOwnedByPlayer(event.getVehicle().getEntityId(), player.getName())) {
                player.sendMessage(Utils.getTranslation("PlaneLocked"));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.getVehicle().getPassengers().size() < 0) {
            event.setCancelled(true);
        }

        //TODO if the plane is owned, or has people in, set it to non-breaking
    }
}
