//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer;

import com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class NBTBukkitListener implements Listener {
    private final Main plugin;
    private Boolean cc;

    public NBTBukkitListener(Main Main) {
        this.plugin = Main;

        try {
            Class.forName("com.gmail.filoghost.chestcommands.internal.MenuInventoryHolder");
            this.cc = true;
        } catch (ClassNotFoundException var3) {
            this.cc = false;
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onInvClick(InventoryClickEvent event) {
        if (!this.cc || !(event.getInventory().getHolder() instanceof MenuInventoryHolder)) {
            if (event.getWhoClicked().getType() == EntityType.PLAYER) {
                Player p = (Player)event.getWhoClicked();
                if (event.getCurrentItem() != null) {
                    if (this.plugin.checkItem(event.getCurrentItem(), p)) {
                        event.setCancelled(true);
                        p.updateInventory();
                    }

                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (event.getItemDrop() != null) {
            if (this.plugin.checkItem(event.getItemDrop().getItemStack(), p)) {
                event.setCancelled(true);
                p.updateInventory();
            }

        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onSlotChange(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        ItemStack stack = p.getInventory().getItem(event.getNewSlot());
        if (this.plugin.checkItem(stack, p)) {
            event.setCancelled(true);
            p.updateInventory();
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void on(PlayerAttemptPickupItemEvent event) {
        Item item = event.getItem();
        if (item != null) {
            if (this.plugin.checkItem(item.getItemStack(), event.getPlayer())) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void on(PlayerInteractEvent event) {
        if (event.hasItem() && this.plugin.checkItem(event.getItem(), event.getPlayer())) {
            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
            event.setCancelled(true);
            event.getPlayer().getInventory().remove(event.getItem());
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void on(ItemSpawnEvent event) {
        Item item = event.getEntity();
        if (item != null) {
            if (this.plugin.checkItem(item.getItemStack(), item.getWorld())) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler(
            priority = EventPriority.LOW,
            ignoreCancelled = true
    )
    public void on(BlockDispenseEvent event) {
        ItemStack item = event.getItem();
        if (item != null) {
            if (this.plugin.checkItem(item, event.getBlock().getWorld())) {
                event.setCancelled(true);
                Inventory inventory = ((InventoryHolder)event.getBlock().getState()).getInventory();
                inventory.remove(item);
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ItemStack[] var2 = event.getPlayer().getInventory().getContents();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack stack = var2[var4];
            this.plugin.checkItem(stack, event.getPlayer());
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        NBTListener.cancel.invalidate(event.getPlayer());
    }
}
