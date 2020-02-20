//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.catcoder.updatechecker.PluginUpdater;
import me.catcoder.updatechecker.UpdaterException;
import me.catcoder.updatechecker.UpdaterResult;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private ItemChecker checker;
    private ProtocolManager manager;
    public String version;
    private final PluginUpdater updater = new PluginUpdater(this, "Dimatert9", "ItemFixer");

    public Main() {
    }

    public void onEnable() {
        this.saveDefaultConfig();
        this.checkNewConfig();
        PluginManager pmanager = Bukkit.getPluginManager();
        this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        this.checker = new ItemChecker(this);
        this.manager = ProtocolLibrary.getProtocolManager();
        this.manager.addPacketListener(new NBTListener(this, this.version));
        pmanager.registerEvents(new NBTBukkitListener(this), this);
        pmanager.registerEvents(new TextureFix(this.version, this), this);
        if (this.getConfig().getBoolean("check-update")) {
            this.checkUpdate();
        }

        Bukkit.getConsoleSender().sendMessage("§b[ItemFixer] §aenabled");
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.manager.removePacketListeners(this);
        NBTListener.cancel.invalidateAll();
        NBTListener.cancel = null;
        this.checker = null;
        this.manager = null;
    }

    public boolean checkItem(ItemStack stack, Player p) {
        return this.checker.isHackedItem(stack, p, p.getWorld());
    }

    public boolean checkItem(ItemStack stack, World world) {
        return this.checker.isHackedItem(stack, (Player)null, world);
    }

    private void checkNewConfig() {
        if (!this.getConfig().isSet("ignored-tags")) {
            File config = new File(this.getDataFolder(), "config.yml");
            config.delete();
            this.saveDefaultConfig();
        }

        if (this.getConfig().isSet("max-pps")) {
            this.getConfig().set("max-pps", (Object)null);
            this.getConfig().set("max-pps-kick-msg", (Object)null);
            this.saveConfig();
        }

    }

    public boolean isUnsupportedVersion() {
        return this.version.startsWith("v1_11_R") || this.version.startsWith("v1_12_R") || this.version.startsWith("v1_13_R");
    }

    private void checkUpdate() {
        (new Thread(() -> {
            try {
                UpdaterResult result = this.updater.checkUpdates();
                if (result.hasUpdates()) {
                    Bukkit.getConsoleSender().sendMessage("§b[ItemFixer] §cНовое обновление найдено! | The new version found!");
                } else {
                    Bukkit.getConsoleSender().sendMessage("§b[ItemFixer] §aОбновлений не найдено. | No updates found.");
                }
            } catch (UpdaterException var2) {
                var2.print();
            }

        })).start();
    }
}
