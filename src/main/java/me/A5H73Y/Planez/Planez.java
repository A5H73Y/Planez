package me.A5H73Y.Planez;

import me.A5H73Y.Planez.commands.PlanezCommands;
import me.A5H73Y.Planez.controllers.EconomyController;
import me.A5H73Y.Planez.listeners.VehicleListener;
import me.A5H73Y.Planez.listeners.PlayerListener;
import me.A5H73Y.Planez.listeners.SignListener;
import me.A5H73Y.Planez.controllers.PlaneController;
import me.A5H73Y.Planez.controllers.FuelController;
import me.A5H73Y.Planez.other.Settings;
import me.A5H73Y.Planez.other.Updater;
import me.A5H73Y.Planez.other.Utils;
import org.bstats.bukkit.Metrics;
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
        new Metrics(this);
        updatePlugin();
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

    private void updatePlugin() {
        if (instance.getConfig().getBoolean("Other.UpdateCheck"))
            new Updater(this, 53024, this.getFile(), Updater.UpdateType.DEFAULT, true);
    }
}
