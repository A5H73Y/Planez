package io.github.a5h73y.planez;

import io.github.a5h73y.planez.commands.PlanezCommands;
import io.github.a5h73y.planez.controllers.EconomyController;
import io.github.a5h73y.planez.controllers.FuelController;
import io.github.a5h73y.planez.controllers.PlaneController;
import io.github.a5h73y.planez.listeners.PlayerListener;
import io.github.a5h73y.planez.listeners.SignListener;
import io.github.a5h73y.planez.listeners.VehicleListener;
import io.github.a5h73y.planez.other.Settings;
import io.github.a5h73y.planez.other.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class Planez extends JavaPlugin {

    private static Planez instance;
    private Settings settings;

    private FuelController fuelController;
    private PlaneController planeController;
    private EconomyController economyController;

    public static Planez getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        getCommand("planez").setExecutor(new PlanezCommands(this));

        getServer().getPluginManager().registerEvents(new VehicleListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new SignListener(this), this);

        settings = new Settings(this);
        planeController = new PlaneController(this);
        fuelController = new FuelController();
        economyController = new EconomyController(this);

        getLogger().info("Planez enabled");
        getLogger().info("=============================");
        getLogger().info("Planez will soon be integrated into the Carz plugin, please update when available.");
        getLogger().info("=============================");
    }

    public void onDisable() {
        Utils.destroyAllPlanes();
    }

    public FuelController getFuelController() {
        return fuelController;
    }

    public PlaneController getPlaneController() {
        return planeController;
    }

    public EconomyController getEconomyController() {
        return economyController;
    }

    public Settings getSettings() {
        return settings;
    }

    public static String getPrefix() {
        return Utils.getTranslation("Prefix", false);
    }
}
