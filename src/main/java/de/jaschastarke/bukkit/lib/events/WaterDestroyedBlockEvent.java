package de.jaschastarke.bukkit.lib.events;

import org.bukkit.block.Block;

public class WaterDestroyedBlockEvent extends BlockDestroyedEvent {
    private Block flowingBlock;
    public WaterDestroyedBlockEvent(final Block theBlock, final Block source) {
        super(theBlock);
        flowingBlock = source;
    }

    public Block getLiquid() {
        return flowingBlock;
    }
}
