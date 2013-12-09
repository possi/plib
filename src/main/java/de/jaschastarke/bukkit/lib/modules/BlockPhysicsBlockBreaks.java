package de.jaschastarke.bukkit.lib.modules;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Attachable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.events.AttachedBlockDestroyedByPlayerEvent;
import de.jaschastarke.bukkit.lib.events.AttachedBlockDestroyedEvent;
import de.jaschastarke.bukkit.lib.events.AttachedBlockDestroyedByPlayerEvent.BlockBreakEventData;

/**
 * Advanced version of AdditionBlockBreaks, that makes use of BlockPhysics for more accurate event sending.
 */
public class BlockPhysicsBlockBreaks extends AdditionalBlockBreaks implements Listener {
    private static final String EVENT_DATA_KEY = "plib.blockbreak.attached";
    
    public BlockPhysicsBlockBreaks(final Core plugin) {
        super(plugin);
    }

    private void sendAttachedBlockDestroyedEvent(final Block block) {
        List<MetadataValue> metadata = block.getMetadata(EVENT_DATA_KEY);
        for (MetadataValue md : metadata) {
            if (md.getOwningPlugin() == plugin && md.value() instanceof AttachedBlockDestroyedByPlayerEvent.BlockBreakEventData) {
                plugin.getLog().debug(block.getLocation().toString() + " - BlockBreakEventData found");
                AttachedBlockDestroyedEvent customEvent = new AttachedBlockDestroyedByPlayerEvent(block, (BlockBreakEventData) md.value());
                plugin.getServer().getPluginManager().callEvent(customEvent);
                block.removeMetadata(EVENT_DATA_KEY, plugin);
                return;
            }
        }
        AttachedBlockDestroyedEvent customEvent = new AttachedBlockDestroyedEvent(block);
        plugin.getServer().getPluginManager().callEvent(customEvent);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPhysics(final BlockPhysicsEvent event) {
        /*if (plugin.isDebug())
            plugin.getLog().debug("Physics: " + event.getBlock().getType().toString() + ": " + event.getBlock().getState().getData().toString());//*/
        if (event.getBlock().getState().getData() instanceof Attachable) {
            BlockFace face = ((Attachable) event.getBlock().getState().getData()).getAttachedFace();
            if (event.getBlock().getRelative(face).getType() == Material.AIR) {
                sendAttachedBlockDestroyedEvent(event.getBlock());
            }
        } else if (groundedItems.contains(event.getBlock().getType())) {
            if (event.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                sendAttachedBlockDestroyedEvent(event.getBlock());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final List<Block> breakingBlockList = getAttachedBlocks(event.getBlock());
        for (Block breakingBlock : breakingBlockList) {
            if (plugin.isDebug())
                plugin.getLog().debug("Setting BlockBreakEventData to " + event.getBlock().getLocation().toString() + " - " + event.getBlock().getType().toString());
            breakingBlock.setMetadata(EVENT_DATA_KEY, new FixedMetadataValue(plugin, new AttachedBlockDestroyedByPlayerEvent.BlockBreakEventData(event)));
        }
        if (breakingBlockList.size() > 0) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() { // cleanup MetaData in 1 Tick
                @Override
                public void run() {
                    if (plugin.isDebug())
                        plugin.getLog().debug("Scheduler: Synchronous Task run: Cleanup BlockbreakData");
                    for (Block breakingBlock : breakingBlockList) {
                        breakingBlock.removeMetadata(EVENT_DATA_KEY, plugin);
                    }
                }
            }, 1L);
        }
    }

}
