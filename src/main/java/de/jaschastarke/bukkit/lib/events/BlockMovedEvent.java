package de.jaschastarke.bukkit.lib.events;

import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockMovedEvent extends BlockEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private Block source;
    private FallingBlock entity;
    
    public BlockMovedEvent(final Block from, final Block to, final FallingBlock entity) {
        super(to);
        source = from;
        this.entity = entity;
    }
    public Block getSource() {
        return source;
    }
    public FallingBlock getEntity() {
        return entity;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
