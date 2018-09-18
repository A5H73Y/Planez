package me.A5H73Y.Planez.listeners;

import me.A5H73Y.Planez.Planez;
import me.A5H73Y.Planez.enums.Permissions;
import me.A5H73Y.Planez.other.DelayTasks;
import me.A5H73Y.Planez.other.Utils;
import me.A5H73Y.Planez.other.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final Planez planez;

    public PlayerListener(Planez planez) {
        this.planez = planez;
    }

    @EventHandler
    public void onPlaceMinecart(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (Utils.getMaterialInPlayersHand(event.getPlayer()) != Material.MINECART)
            return;

        if (event.getClickedBlock().getType() == XMaterial.RAIL.parseMaterial()
                || event.getClickedBlock().getType() == Material.POWERED_RAIL
                || event.getClickedBlock().getType() == Material.DETECTOR_RAIL)
            return;

        Player player = event.getPlayer();

        if (!Utils.hasPermission(player, Permissions.PLACE))
            return;

        if (!DelayTasks.getInstance().delayPlayer(player, 3))
            return;

        ItemStack planeInHand = Utils.getItemStackInPlayersHand(player);

        if (planeInHand.getItemMeta().hasDisplayName()) {
            if (planeInHand.getItemMeta().getDisplayName().contains(player.getName())) {
                Utils.spawnOwnedPlane(event.getClickedBlock().getLocation(), player);
            } else {
                player.sendMessage(Utils.getTranslation("Error.Owned"));
                return;
            }
        } else {
            Utils.spawnPlane(event.getClickedBlock().getLocation());
        }

        Utils.reduceItemStackInPlayersHand(player);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
//            if (planez.getPlaneController().isDriving(event.getEntity().getName()) &&
            if (event.getEntity().isInsideVehicle() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }
}
