package de.jaschastarke.bukkit.lib.events;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public abstract class BlockDestroyedEvent extends BlockEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    
    public BlockDestroyedEvent(final Block theBlock) {
        super(theBlock);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
