package de.jaschastarke.bukkit.lib.modules;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import de.jaschastarke.bukkit.lib.Core;
import de.jaschastarke.bukkit.lib.SimpleModule;
import de.jaschastarke.bukkit.lib.events.BlockMovedEvent;

public class BlockFall extends SimpleModule<Core> implements Listener {
    private static final String ENTITY_DATA_KEY = "plib.blockfall.source";
    private static BlockFall registeredInstance = null;
    
    public BlockFall(final Core plugin) {
        super(plugin);
    }
    
    @Override
    public void onEnable() {
        if (registeredInstance == null) {
            registeredInstance = this;
            super.onEnable();
        }
    }
    @Override
    public void onDisable() {
        if (registeredInstance == this) {
            registeredInstance = null;
        }
        super.onDisable();
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if (event.getTo() == Material.AIR) {
                event.getEntity().setMetadata(ENTITY_DATA_KEY, new FixedMetadataValue(plugin, event.getBlock()));
            } else {
                for (MetadataValue md : event.getEntity().getMetadata(ENTITY_DATA_KEY)) {
                    if (md.value() instanceof Block) {
                        BlockMovedEvent ce = new BlockMovedEvent((Block) md.value(), event.getBlock(), (FallingBlock) event.getEntity());
                        plugin.getServer().getPluginManager().callEvent(ce);
                        event.getEntity().removeMetadata(ENTITY_DATA_KEY, md.getOwningPlugin()); // don't fire event twice
                        break;
                    }
                }
            }
        }
    }
}
