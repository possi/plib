package de.jaschastarke.bukkit.lib.configuration.command;

import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.configuration.IConfigurationNode;

public interface ICommandConfigCallback {
    public void onConfigCommandChange(Callback cb);
    
    public static class Callback {
        private boolean cancelled = false;
        private IConfigurationNode node;
        private CommandContext context;
        private Object value;
        private String[] args;
        private String[] chain;
        public Callback(final IConfigurationNode node, final Object value, final CommandContext context, final String[] args, final String[] chain) {
            this.node = node;
            this.value = value;
            this.context = context;
            this.args = args;
            this.chain = chain;
        }
        public boolean isCancelled() {
            return cancelled;
        }
        public void setCancelled(final boolean bool) {
            this.cancelled = bool;
        }
        public Object getValue() {
            return value;
        }
        public void setValue(final Object val) {
            this.value = val;
        }
        public CommandContext getContext() {
            return context;
        }
        public IConfigurationNode getNode() {
            return node;
        }
        public String[] getArgs() {
            return args;
        }
        public String[] getChain() {
            return chain;
        }
    }
}
