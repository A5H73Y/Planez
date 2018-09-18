package me.A5H73Y.Planez.other;

import me.A5H73Y.Planez.Planez;
import org.bukkit.Material;

import java.util.ArrayList;

public class Settings {

    private Planez planez;

    public Settings(Planez planez) {
        this.planez = planez;
        
        setupConfig();
    }

    public Material getKey() {
        return Material.getMaterial(planez.getConfig().getString("Key.Material"));
    }

    public String getSignHeader() {
        return Utils.getTranslation("SignHeader", false);
    }

    public boolean isDestroyInLiquid() {
        return planez.getConfig().getBoolean("Other.DestroyInLiquid");
    }

    public Double getStartSpeed() {
        return planez.getConfig().getDouble("Speed.Start");
    }

    public Double getUpgradeSpeed() {
        return planez.getConfig().getDouble("Speed.Upgrade.Increment");
    }

    public Double getUpgradeMaxSpeed() {
        return planez.getConfig().getDouble("Speed.Upgrade.Max");
    }

    /**
     * Initialise the configuration options on startup
     */
    private void setupConfig() {
        planez.getConfig().options().header("==== Planez Config ==== #");

        planez.getConfig().addDefault("Key.Material", "STICK");
        planez.getConfig().addDefault("Key.GiveOnPlaneEnter", true);
        planez.getConfig().addDefault("Key.RequirePlanezKey", true);

        planez.getConfig().addDefault("Speed.Start", 80.0);
        planez.getConfig().addDefault("Speed.Upgrade.Increment", 20.0);
        planez.getConfig().addDefault("Speed.Upgrade.Max", 200.0);

        planez.getConfig().addDefault("Command.Spawn", true);
        planez.getConfig().addDefault("Command.Purchase", true);
        planez.getConfig().addDefault("Command.Refuel", true);
        planez.getConfig().addDefault("Command.Upgrade", true);

        planez.getConfig().addDefault("Fuel.Enable", true);
        planez.getConfig().addDefault("Fuel.StartAmount", 10000.0);
        planez.getConfig().addDefault("Fuel.GaugeScale", 40);

        planez.getConfig().addDefault("Economy.Use", true);
        planez.getConfig().addDefault("Economy.Cost.Purchase", 20.0);
        planez.getConfig().addDefault("Economy.Cost.Upgrade", 16.0);
        planez.getConfig().addDefault("Economy.Cost.Refuel", 4.0);

        planez.getConfig().addDefault("Other.DestroyInLiquid", true);
        planez.getConfig().addDefault("Other.UpdateCheck", true);
        planez.getConfig().addDefault("Other.UsePermissions", true);
        planez.getConfig().addDefault("Other.UseEffects", true);

        planez.getConfig().addDefault("Message.Prefix", "&0[&bPlanez&0]&7 ");
        planez.getConfig().addDefault("Message.SignHeader", "&0[&bPlanez&0]");
        planez.getConfig().addDefault("Message.Spawned", "Plane Spawned!");
        planez.getConfig().addDefault("Message.Purchased", "Plane purchased!");
        planez.getConfig().addDefault("Message.Refuel", "Plane Refuelled!");
        planez.getConfig().addDefault("Message.EngineStart", "You switch the engine on.");
        planez.getConfig().addDefault("Message.EngineStop", "You switch the engine off.");
        planez.getConfig().addDefault("Message.PlaneLocked", "You lock the plane.");
        planez.getConfig().addDefault("Message.PlaneUnlocked", "You unlock the plane.");
        planez.getConfig().addDefault("Message.Commands", "To Display all commands enter &f/planez cmds");
        planez.getConfig().addDefault("Message.PlayerPlane", "%PLAYER%'s plane");
        planez.getConfig().addDefault("Message.FuelEmpty", "This plane has run out of fuel!");
        planez.getConfig().addDefault("Message.KeyReceived", "You receive a key.");
        planez.getConfig().addDefault("Message.LiquidDamage", "Your plane has been destroyed by liquid!");
        planez.getConfig().addDefault("Message.UpgradeSpeed", "New top speed: %SPEED%");
        planez.getConfig().addDefault("Message.ConfigReloaded", "The config has been reloaded.");

        planez.getConfig().addDefault("Message.Error.NoPermission", "You do not have permission: &b%PERMISSION%");
        planez.getConfig().addDefault("Message.Error.SignProtected", "This sign is protected!");
        planez.getConfig().addDefault("Message.Error.UnknownCommand", "Unknown Command!");
        planez.getConfig().addDefault("Message.Error.UnknownSignCommand", "Unknown Sign Command!");
        planez.getConfig().addDefault("Message.Error.CommandDisabled", "This command has been disabled!");
        planez.getConfig().addDefault("Message.Error.InPlane", "You are already in a plane!");
        planez.getConfig().addDefault("Message.Error.NotInPlane", "You are not in a plane!");
        planez.getConfig().addDefault("Message.Error.HavePlane", "You already have a plane!");
        planez.getConfig().addDefault("Message.Error.FuelDisabled", "Fuel is disabled.");
        planez.getConfig().addDefault("Message.Error.PurchaseFailed", "Purchase failed. Cost: %COST%");
        planez.getConfig().addDefault("Message.Error.FullyUpgraded", "Your plane is already fully upgraded!");
        planez.getConfig().addDefault("Message.Error.Owned", "This plane is owned by someone else!");

        planez.getConfig().options().copyDefaults(true);
        planez.saveConfig();
    }
}
