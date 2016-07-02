package ru.xtime_1_9R1;

import org.bukkit.Material;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class XListener implements Listener{  
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onDamage(EntityDamageByEntityEvent event){
		if(event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.TIPPED_ARROW) {
			TippedArrow arrow = (TippedArrow) event.getDamager();
			if (((Entity) arrow.getShooter()).getType() == EntityType.PLAYER ) {
				Player p =  (Player) arrow.getShooter();
				if (!p.hasPermission("itemfixer.bypass")) {
					if (arrow.hasCustomEffects()) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		if (event.getDamager().getType() == EntityType.AREA_EFFECT_CLOUD) {
			AreaEffectCloud arc = (AreaEffectCloud) event.getDamager();
			if (arc.hasCustomEffects()) {
				event.setCancelled(true);
				arc.remove();
				return;
			}
		}
		if (event.getDamager().getType() == EntityType.PLAYER) {
			Player player2 = (Player) event.getDamager();
			if (!player2.hasPermission("itemfixer.bypass")) {
				event.setCancelled(Checks.checkAttributes(player2.getInventory().getItemInMainHand()));
				if (Checks.removeEnt(player2.getInventory().getItemInMainHand())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPickupItem(final PlayerPickupItemEvent e) {
		if (!e.getPlayer().hasPermission("itemfixer.bypass")) {
			e.setCancelled(Checks.checkAttributes(e.getItem().getItemStack()));
			if (Checks.removeEnt(e.getItem().getItemStack())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void InventoryClick(final InventoryClickEvent e) {
		if (e.getCurrentItem() != null) {
			if (!e.getWhoClicked().hasPermission("itemfixer.bypass")) {
				e.setCancelled(Checks.checkAttributes(e.getCurrentItem()));
				if (Checks.removeEnt(e.getCurrentItem())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent e) {
		if (!e.getPlayer().hasPermission("itemfixer.bypass") && e.getItem() != null) {
			e.setCancelled(Checks.checkAttributes(e.getItem()));
			if (Checks.removeEnt(e.getItem())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onItemDrop(final PlayerDropItemEvent e) {
		if (!e.getPlayer().hasPermission("itemfixer.bypass")) {
			e.setCancelled(Checks.checkAttributes(e.getItemDrop().getItemStack()));
			if (Checks.removeEnt(e.getItemDrop().getItemStack())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void OnUse(PlayerItemConsumeEvent e ){
		if (!e.getPlayer().hasPermission("itemfixer.bypass")) {
			if (e.getItem().getType() == Material.POTION) {
				e.setCancelled(Checks.checkAttributes(e.getItem()));
				if (Checks.removeEnt(e.getItem())) {
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void OnSwap(PlayerSwapHandItemsEvent e ){
		if (!e.getPlayer().hasPermission("itemfixer.bypass") && e.getOffHandItem() != null) {
			e.setCancelled(Checks.checkAttributes(e.getOffHandItem()));
			if (Checks.removeEnt(e.getOffHandItem())) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void OnSwap2(PlayerSwapHandItemsEvent e ){
		if (!e.getPlayer().hasPermission("itemfixer.bypass") && e.getMainHandItem() != null) {
			e.setCancelled(Checks.checkAttributes(e.getMainHandItem()));
			if (Checks.removeEnt(e.getMainHandItem())) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void OnLaunch2 (BlockDispenseEvent e){
		e.setCancelled(Checks.checkAttributes(e.getItem()));
		if (Checks.removeEnt(e.getItem())) {
			e.setCancelled(true);
		}
	}
}