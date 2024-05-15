package net.kozibrodka.wolves.api;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry created so other mods can add new blocks for the hibachi to ignite. Not used by Better Than Wolves itself.
 */
public class HibachiIgnitionRegistry {

    private static final HibachiIgnitionRegistry INSTANCE = new HibachiIgnitionRegistry();
    private Map<Integer, Integer> recipes = new HashMap<>();

    public static final HibachiIgnitionRegistry getInstance() {
        return INSTANCE;
    }

    public void addBlockIgnition(int unignitedBlockID, int ignitedBlockID) {
        this.recipes.put(unignitedBlockID, ignitedBlockID);
    }

    public int getIgnitedID(int i) {
        if (this.recipes.get(i) == null) return 0;
        return (int)this.recipes.get(i);
    }

}
