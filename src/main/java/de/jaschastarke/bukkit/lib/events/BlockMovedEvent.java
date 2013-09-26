package de.jaschastarke.bukkit.lib.events;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class BlockMovedEvent extends BlockEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private Block source;
    
    public BlockMovedEvent(final Block from, final Block to) {
        super(to);
        source = from;
    }
    public Block getSource() {
        return source;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
