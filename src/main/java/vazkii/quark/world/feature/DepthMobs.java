/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [02/07/2016, 20:21:15 (GMT)]
 */
package vazkii.quark.world.feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderAshen;
import vazkii.quark.world.client.render.RenderDweller;
import vazkii.quark.world.entity.EntityAshen;
import vazkii.quark.world.entity.EntityDweller;

public class DepthMobs extends Feature {

	public static int upperBound;
	boolean enableAshen, enableDweller;
	int ashenWeight, dwellerWeight;
	int ashenMaxPack, dwellerMaxPack, ashenMinPack, dwellerMinPack;

	@Override
	public void setupConfig() {
		upperBound = loadPropInt("Highest Y level for spawns", "", 20);
		enableAshen = loadPropBool("Enable Ashen", "", true);
		enableDweller = loadPropBool("Enable Dweller", "", true);
		ashenWeight = loadPropInt("Ashen Spawn Weight", "(Skeleton is 100)", 10);
		dwellerWeight = loadPropInt("Dweller Spawn Weight", "(Zombie is 100)", 10);
		ashenMaxPack = loadPropInt("Largest Ashen spawn group", "", 2);
		dwellerMaxPack = loadPropInt("Largest Dweller spawn group", "", 2);
		ashenMinPack = loadPropInt("Smallest Ashen spawn group", "", 1);
		dwellerMinPack = loadPropInt("Smallest Dweller spawn group", "", 1);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableDweller) {
			EntityRegistry.registerModEntity(EntityDweller.class, "dweller", LibEntityIDs.DWELLER, Quark.instance, 80, 3, true, 0x4a5944, 0x3f2020);
			EntityRegistry.addSpawn(EntityDweller.class, dwellerWeight, dwellerMinPack, dwellerMaxPack, EnumCreatureType.MONSTER, getBiomesWithMob(EntityZombie.class));
		}

		if(enableAshen) {
			EntityRegistry.registerModEntity(EntityAshen.class, "ashen", LibEntityIDs.ASHEN, Quark.instance, 80, 3, true, 0x838376, 0x533d3c);
			EntityRegistry.addSpawn(EntityAshen.class, ashenWeight, ashenMinPack, ashenMaxPack, EnumCreatureType.MONSTER, getBiomesWithMob(EntitySkeleton.class));
		}
	}

	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		if(enableDweller)
			RenderingRegistry.registerEntityRenderingHandler(EntityDweller.class, RenderDweller.FACTORY);
		if(enableAshen)
			RenderingRegistry.registerEntityRenderingHandler(EntityAshen.class, RenderAshen.FACTORY);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

	private Biome[] getBiomesWithMob(Class<? extends Entity> clazz) {
		List<Biome> biomes = new ArrayList();
		Iterator<Biome> bIter = Biome.REGISTRY.iterator();
		while(bIter.hasNext()) {
			Biome b = bIter.next();
			List<SpawnListEntry> spawnList = b.getSpawnableList(EnumCreatureType.MONSTER);
			for(SpawnListEntry e : spawnList)
				if(e.entityClass == clazz) {
					biomes.add(b);
					break;
				}
		}

		return biomes.toArray(new Biome[biomes.size()]);
	}

}

