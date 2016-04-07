package ru.xtime_1_9R1;

import org.bukkit.Material;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class XListener implements Listener{  
    boolean b;
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event){
		if (event.isCancelled()) {
			return;
		}
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (!player.hasPermission("itemfixer.bypass")) {
				if(event.getDamager() instanceof TippedArrow){
					TippedArrow arrow = (TippedArrow) event.getDamager();
					if (arrow.hasCustomEffects()) {
						event.setCancelled(true);
						return;
					}
				}
				if (event.getDamager() instanceof AreaEffectCloud) {
					AreaEffectCloud arc = (AreaEffectCloud) event.getDamager();
					if (arc.hasCustomEffects()) {
						event.setCancelled(true);
						arc.remove();
					}
				}
			}
		}
	}
    
	@EventHandler
	public void onPickupItem(final PlayerPickupItemEvent e) {
		if (e.isCancelled()) {
			return;
		}
		Player player = e.getPlayer();
		if (!player.hasPermission("itemfixer.bypass")) {
			final Item item = e.getItem();
			final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(item.getItemStack());
			if (a) {
				e.setCancelled(true);
				item.remove();
			}
			ru.xtime_1_9R1.Checks.removeEnt(item.getItemStack());
		}
	}
    
	@EventHandler
	public void InventoryClick(final InventoryClickEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (e.getCurrentItem() != null) {
			Player player = (Player) e.getWhoClicked();
			if (!player.hasPermission("itemfixer.bypass")) {
				final ItemStack item = e.getCurrentItem();
				final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(item);
				if (a) {
					e.getWhoClicked().getInventory().remove(item);
					e.setCancelled(true);
				}
				ru.xtime_1_9R1.Checks.removeEnt(item);
			}
		}
	}
    
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (e.getItem() != null) {
			Player player = e.getPlayer();
			if (!player.hasPermission("itemfixer.bypass")) {
				final ItemStack item = e.getItem();
				final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(item);
				if (a) {
					e.getPlayer().getInventory().remove(item);
					e.setCancelled(true);
				}
				final boolean b = ru.xtime_1_9R1.Checks.removeEnt(item);
				e.setCancelled(b);
			}
		}
	}
    
	@EventHandler
	public void onItemDrop(final PlayerDropItemEvent e) {
		if (e.isCancelled()) {
			return;
		}
		Player player = e.getPlayer();
		if (!player.hasPermission("itemfixer.bypass")) {
			final Item item = e.getItemDrop();
			final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(item.getItemStack());
			if (a) {
				e.setCancelled(true);
			}
			ru.xtime_1_9R1.Checks.removeEnt(item.getItemStack());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnSpawn (EntitySpawnEvent e ){
		if (e.isCancelled()) {
			return;
		}
		if (e.getEntity() instanceof ArmorStand) {
			ArmorStand as = (ArmorStand) e.getEntity();
			if (as.getCustomName() != null) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnUse(PlayerItemConsumeEvent e ){
		if (e.isCancelled()) {
			return;
		}
		Player player = e.getPlayer();
		if (!player.hasPermission("itemfixer.bypass")) {
			ItemStack hand = e.getItem();
			if (hand.getType() == Material.POTION) {
				final PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();
				if (meta.hasCustomEffects()) {
					e.setCancelled(true);
					player.getInventory().remove(hand);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnSwap(PlayerSwapHandItemsEvent e ){
		if (e.isCancelled()) {
			return;
		}
		Player player = e.getPlayer();
		if (!player.hasPermission("itemfixer.bypass")) {
			ItemStack hand = e.getOffHandItem();
			final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(hand);
			if (a) {
				e.setCancelled(true);
				player.getInventory().remove(hand);
			}
			ru.xtime_1_9R1.Checks.removeEnt(hand);
		}
	}
       
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnLaunch (ProjectileLaunchEvent e){
		if (e.isCancelled()) {
			return;
		}
		if (e.getEntity() instanceof LingeringPotion || e.getEntity() instanceof SplashPotion){
			ThrownPotion potion = (ThrownPotion) e.getEntity();
			for (PotionEffect pe : potion.getEffects()) {
				if (pe.getAmplifier() > 1) {
					e.setCancelled(true);
				}
			}
		}
		if (e.getEntity() instanceof TippedArrow){
			TippedArrow arrow = (TippedArrow) e.getEntity();
			if (arrow.hasCustomEffects()) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnLaunch2 (BlockDispenseEvent e){
		if (e.isCancelled()) {
			return;
		}
		final ItemStack item = e.getItem();
		final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(item);
		if (a) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void lol(final PlayerInteractEvent e) {
		final ItemStack its = e.getItem();
		if(e.getPlayer().hasPermission("itemfixer.bypass")) {
			this.b = false;
		} else {
			if (its != null && its.getType() == Material.MONSTER_EGG) {
				final boolean a = ru.xtime_1_9R1.Checks.checkAttributes(its);
				if (a) {
					this.b = true;
				}else {
					this.b = false;
				}
			}
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onSpawn(final CreatureSpawnEvent e) {
    	if (!(e.getEntity() instanceof ArmorStand)) {
    		if (e.getSpawnReason() == SpawnReason.SPAWNER_EGG) {
    		e.setCancelled(this.b);
    		}
    	}
    }
}
