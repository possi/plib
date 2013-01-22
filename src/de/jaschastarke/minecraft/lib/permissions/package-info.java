/**
 * This Package contains representations for 3 different types of Permission(-Groups) which assemble from 5 different
 * interfaces.
 * 
 * 1. Simple Permission: Has a full name, a parent and a default-value
 * 2. Simple Permission-Node: Has a full name, a parent and child-Permissions
 * 3. Permission-List: Only has child-Permissions, the have an other parent
 * 
 * You can combine the Interfaces to a 4th type compined of 1. and 2. (so is a container with default value)
 */
package de.jaschastarke.minecraft.lib.permissions;