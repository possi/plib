package de.jaschastarke.bukkit.lib.events;

import org.bukkit.block.Block;

public class AttachedBlockDestroyedEvent extends BlockDestroyedEvent {
    public AttachedBlockDestroyedEvent(final Block theBlock) {
        super(theBlock);
    }
}
