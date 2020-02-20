//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ru.leymooo.fixer;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TextureFix implements Listener {
    private HashMap<Material, Integer> limit = new HashMap();
    private HashSet<Material> ignore = new HashSet();
    private Main plugin;

    public TextureFix(String version, Main main) {
        this.plugin = main;
        this.limit.put(Material.STONE, 6);
        this.limit.put(Material.DIRT, 2);
        this.limit.put(Material.WOOD, 5);
        this.limit.put(Material.SAPLING, 5);
        this.limit.put(Material.SAND, 1);
        this.limit.put(Material.LOG, 3);
        this.limit.put(Material.LEAVES, 3);
        this.limit.put(Material.SPONGE, 1);
        this.limit.put(Material.SANDSTONE, 2);
        this.limit.put(Material.LONG_GRASS, 2);
        this.limit.put(Material.WOOL, 15);
        this.limit.put(Material.RED_ROSE, 8);
        this.limit.put(Material.DOUBLE_STEP, 7);
        this.limit.put(Material.STEP, 7);
        this.limit.put(Material.STAINED_GLASS, 15);
        this.limit.put(Material.MONSTER_EGGS, 5);
        this.limit.put(Material.SMOOTH_BRICK, 3);
        this.limit.put(Material.WOOD_DOUBLE_STEP, 5);
        this.limit.put(Material.WOOD_STEP, 5);
        this.limit.put(Material.COBBLE_WALL, 1);
        this.limit.put(Material.QUARTZ_BLOCK, 2);
        this.limit.put(Material.STAINED_CLAY, 15);
        this.limit.put(Material.STAINED_GLASS, 15);
        this.limit.put(Material.STAINED_GLASS_PANE, 15);
        this.limit.put(Material.LEAVES_2, 1);
        this.limit.put(Material.LOG_2, 1);
        this.limit.put(Material.PRISMARINE, 2);
        this.limit.put(Material.CARPET, 15);
        this.limit.put(Material.DOUBLE_PLANT, 5);
        this.limit.put(Material.RED_SANDSTONE, 2);
        this.limit.put(Material.COAL, 1);
        this.limit.put(Material.RAW_FISH, 3);
        this.limit.put(Material.COOKED_FISH, 1);
        this.limit.put(Material.INK_SACK, 15);
        this.limit.put(Material.SKULL_ITEM, 5);
        this.limit.put(Material.GOLDEN_APPLE, 1);
        this.limit.put(Material.BANNER, 15);
        this.limit.put(Material.ANVIL, 2);
        if (version.startsWith("v1_12_R")) {
            this.limit.put(Material.CONCRETE, 15);
            this.limit.put(Material.CONCRETE_POWDER, 15);
            this.limit.put(Material.BED, 15);
        }

        this.ignore.addAll(Arrays.asList(Material.MAP, Material.EMPTY_MAP, Material.CARROT_STICK, Material.BOW, Material.FISHING_ROD, Material.FLINT_AND_STEEL, Material.SHEARS));
        if (version.startsWith("v1_8_R")) {
            this.ignore.add(Material.MONSTER_EGG);
            this.ignore.add(Material.POTION);
            this.limit.put(Material.SKULL_ITEM, 4);
        }

        if (Material.matchMaterial("SHIELD") != null) {
            this.ignore.add(Material.SHIELD);
            this.ignore.add(Material.ELYTRA);
        }

        this.ignore.addAll(Arrays.asList(Material.WOOD_SPADE, Material.WOOD_PICKAXE, Material.WOOD_AXE, Material.WOOD_SWORD, Material.WOOD_HOE));
        this.ignore.addAll(Arrays.asList(Material.GOLD_SPADE, Material.GOLD_PICKAXE, Material.GOLD_AXE, Material.GOLD_SWORD, Material.GOLD_HOE));
        this.ignore.addAll(Arrays.asList(Material.STONE_SPADE, Material.STONE_PICKAXE, Material.STONE_AXE, Material.STONE_SWORD, Material.STONE_HOE));
        this.ignore.addAll(Arrays.asList(Material.IRON_SPADE, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD, Material.IRON_HOE));
        this.ignore.addAll(Arrays.asList(Material.DIAMOND_SPADE, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_HOE));
        this.ignore.addAll(Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS));
        this.ignore.addAll(Arrays.asList(Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS));
        this.ignore.addAll(Arrays.asList(Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET, Material.GOLD_LEGGINGS));
        this.ignore.addAll(Arrays.asList(Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS));
        this.ignore.addAll(Arrays.asList(Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS));
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onHold(PlayerItemHeldEvent e) {
        ItemStack it = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (this.isInvalide(it)) {
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(it);
            e.getPlayer().updateInventory();
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onInteract(PlayerInteractEvent e) {
        ItemStack it = e.getItem();
        if (this.isInvalide(it)) {
            e.setCancelled(true);
            e.getPlayer().getInventory().remove(it);
            e.getPlayer().updateInventory();
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onClick(InventoryClickEvent e) {
        ItemStack it = e.getCurrentItem();
        if (e.getWhoClicked().getType() == EntityType.PLAYER && this.isInvalide(it)) {
            e.setCancelled(true);
            e.getWhoClicked().getInventory().remove(it);
            ((Player)e.getWhoClicked()).updateInventory();
        }

    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = false
    )
    public void onPickup(PlayerPickupItemEvent e) {
        ItemStack it = e.getItem().getItemStack();
        if (this.isInvalide(it)) {
            e.setCancelled(true);
            e.getItem().remove();
        }

    }

    private boolean isInvalide(ItemStack it) {
        if (it != null && it.getType() != Material.AIR && it.getDurability() != 0) {
            if (this.limit.containsKey(it.getType())) {
                return it.getDurability() < 0 || it.getDurability() > (Integer)this.limit.get(it.getType());
            } else {
                return !this.ignore.contains(it.getType());
            }
        } else {
            return false;
        }
    }
}
