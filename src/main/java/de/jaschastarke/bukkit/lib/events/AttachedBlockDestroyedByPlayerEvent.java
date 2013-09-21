package de.jaschastarke.bukkit.lib.events;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class AttachedBlockDestroyedByPlayerEvent extends AttachedBlockDestroyedEvent {
    private BlockBreakEventData data;
    public AttachedBlockDestroyedByPlayerEvent(final Block theBlock, final BlockBreakEventData parentEventData) {
        super(theBlock);
        data = parentEventData;
    }
    public Player getPlayer() {
        return data.getPlayer();
    }
    public Block getBlockDestroyedByPlayer() {
        return data.getBlock();
    }
    
    public static class BlockBreakEventData implements Serializable {
        private static final long serialVersionUID = -4292889136669384284L;
        private WeakReference<Player> player;
        private Block block;
        
        public BlockBreakEventData(final BlockBreakEvent event) {
            player = new WeakReference<Player>(event.getPlayer());
            block = event.getBlock();
        }
        public Block getBlock() {
            return block;
        }
        public Player getPlayer() {
            return player.get();
        }
    }
}
