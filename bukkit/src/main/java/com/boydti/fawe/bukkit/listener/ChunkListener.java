package com.boydti.fawe.bukkit.listener;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;

import com.boydti.fawe.Fawe;
import com.boydti.fawe.bukkit.v0.BukkitQueue_0;
import com.boydti.fawe.util.MathMan;

public class ChunkListener implements Listener {

    @EventHandler
    public static void onWorldLoad(WorldInitEvent event) {
	if (BukkitQueue_0.disableChunkLoad) {
	    World world = event.getWorld();
	    world.setKeepSpawnInMemory(false);
	}
    }

    @EventHandler
    public static void onChunkLoad(ChunkLoadEvent event) {
	Chunk chunk = event.getChunk();
	long pair = MathMan.pairInt(chunk.getX(), chunk.getZ());
	BukkitQueue_0.keepLoaded.putIfAbsent(pair, Fawe.get().getTimer().getTickStart());
    }

    @EventHandler
    public static void onChunkUnload(ChunkUnloadEvent event) {
	Chunk chunk = event.getChunk();
	long pair = MathMan.pairInt(chunk.getX(), chunk.getZ());
	Long lastLoad = BukkitQueue_0.keepLoaded.get(pair);
	if (lastLoad != null) {
	    if (Fawe.get().getTimer().getTickStart() - lastLoad < 10000) {
		event.setCancelled(true);
	    } else {
		BukkitQueue_0.keepLoaded.remove(pair);
	    }
	}
    }

}
