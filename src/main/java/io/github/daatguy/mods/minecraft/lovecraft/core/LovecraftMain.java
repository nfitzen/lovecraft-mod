package io.github.daatguy.mods.minecraft.lovecraft.core;

import io.github.daatguy.mods.minecraft.lovecraft.container.AlchemyRecipes;
import io.github.daatguy.mods.minecraft.lovecraft.container.LovecraftTab;
import io.github.daatguy.mods.minecraft.lovecraft.entity.EntitySpell;
import io.github.daatguy.mods.minecraft.lovecraft.event.ProfessionHandler;
import io.github.daatguy.mods.minecraft.lovecraft.generator.DecorationGenerator;
import io.github.daatguy.mods.minecraft.lovecraft.generator.OreGenerator;
import io.github.daatguy.mods.minecraft.lovecraft.generator.TombGenerator;
import io.github.daatguy.mods.minecraft.lovecraft.world.WorldProviderRoom;
import io.github.daatguy.mods.minecraft.lovecraft.world.potion.PotionDrugged;
import io.github.daatguy.mods.minecraft.lovecraft.world.potion.PotionSimple;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockAltar;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockBasRelief;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockCarvedBlock;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockChargedObelisk;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockDesk;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockFlowerDrug;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockFossil;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockHookah;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockMortar;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockObelisk;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockObeliskCap;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockResolvedObeliskCap;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockUnderstructure;
import io.github.daatguy.mods.minecraft.lovecraft.block.BlockWeirdedBrick;
import io.github.daatguy.mods.minecraft.lovecraft.book.DeskHandler;
import io.github.daatguy.mods.minecraft.lovecraft.book.spell.SpellHandler;
//import daatguy.lovecraft.generator.LengGenerator;
import io.github.daatguy.mods.minecraft.lovecraft.generator.village.VillageCreationHandler;
import io.github.daatguy.mods.minecraft.lovecraft.generator.village.VillageOpiumDen;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemBeaker;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemBook;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemEmptyBeaker;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemFossil;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemFossilDust;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemRubbing;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemSimple;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemSimpleBlock;
import io.github.daatguy.mods.minecraft.lovecraft.item.ItemTome;
import io.github.daatguy.mods.minecraft.lovecraft.item.SubItemsHandler;
import io.github.daatguy.mods.minecraft.lovecraft.networking.PacketHandler;
import io.github.daatguy.mods.minecraft.lovecraft.tileentity.TileEntityAltar;
import io.github.daatguy.mods.minecraft.lovecraft.tileentity.TileEntityCarving;
import io.github.daatguy.mods.minecraft.lovecraft.tileentity.TileEntityChargedObelisk;
import io.github.daatguy.mods.minecraft.lovecraft.tileentity.TileEntityHookah;

@Mod(modid = "lovecraft", name = "Lovecraft Mod", version = "Alpha-1.0")
public class LovecraftMain {
	
	//Potion Id Addition
	//Should probably be changeable via config (todo)
	public static int potionIdStart = 4000;

	//Initialization of tabs, handlers, other engine classes, etc.
	public static CreativeTabs lovecraftTab = new LovecraftTab();

	public static SubItemsHandler subItemsHandler = new SubItemsHandler();
	
	public static SpellHandler spellHandler; //initialize items first

	public static DeskHandler deskHandler = new DeskHandler();
	
	public static PacketHandler packetHandler = new PacketHandler();
	
	public static AlchemyRecipes alchemyRecipes = new AlchemyRecipes();
	
	//public static LengGenerator lengGenerator = new LengGenerator();
	
	//For use for the potionDrugged
	//Only changed client-side
	//See PotionDruggedMessage.java for more info
	public int motionBlurShader = 0;
	
	//Potion Refs
	public static Potion potionDread;
	public static Potion potionAwake;
	public static Potion potionDrugged;
	
	//Item declarations
	public static Item itemRubbingKit;
	public static Item itemEmptyBeaker;
	public static Item itemBeaker;
	public static Item itemCoin;
	public static Item itemWeirdShards;
	public static Item itemWeirdDust;
	public static Item itemFossilDust;
	public static Item itemMummyDust;
	public static Item itemFleshDust;
	public static Item itemMummyChunk;
	public static Item itemMagnifyingGlass;
	public static Item itemTome;
	public static Item itemBook;
	public static Item itemFossil;
	public static Item itemDriedFlower;
	public static Item itemRubbing;

	//More 'item' declarations, ItemBlocks
	public static Item itemBlockUnderstructure;
	public static Item itemBlockFlowerDrug;
	public static Item itemBlockFossil;
	public static Item itemBlockAetherOre;
	public static Item itemBlockWeirdedBrick;
	public static Item itemBlockBasRelief;
	public static Item itemBlockHookah;
	public static Item itemBlockAltar;
	public static Item itemBlockMortar;
	public static Item itemBlockDesk;
	public static Item itemBlockObelisk;
	public static Item itemBlockCarvedObelisk;
	public static Item itemBlockChargedObelisk;
	public static Item itemBlockObeliskCap;
	public static Item itemBlockResolvedObeliskCap;
	public static Item itemBlockCarved; //remove when I bother to add a chisel, probably

	//Block declarations
	public static Block blockUnderstructure;
	public static BlockBush blockFlowerDrug;
	public static Block blockFossil;
	//public static Block blockAetherOre;
	public static Block blockWeirdedBrick;
	public static Block blockBasRelief;
	public static Block blockHookah;
	public static Block blockAltar;
	public static Block blockMortar;
	public static Block blockDesk;
	public static Block blockObelisk;
	public static Block blockCarvedObelisk;
	public static Block blockChargedObelisk;
	public static Block blockObeliskCap;
	public static Block blockResolvedObeliskCap;
	public static Block blockCarved;
	
	//Room dimension
	public static final String ROOM_NAME = "room";
	public static final int ROOM_DIM_ID = nextDimID();
	public static final DimensionType ROOM_DIM_TYPE = DimensionType.register(ROOM_NAME, "_"+ROOM_NAME, ROOM_DIM_ID, WorldProviderRoom.class, true);

	@Instance
	public static LovecraftMain instance = new LovecraftMain();

	//Proxy creations
	@SidedProxy(clientSide = "daatguy.lovecraft.core.ClientProxy", serverSide = "daatguy.lovecraft.core.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		//Initialize items, set properties
		itemFossilDust = new ItemFossilDust();
		itemFossilDust.setUnlocalizedName("fossil_dust");
		itemFossilDust.setRegistryName("fossil_dust");
		itemFossilDust.setCreativeTab(lovecraftTab);

		itemRubbingKit = new ItemSimple();
		itemRubbingKit.setUnlocalizedName("rubbing_kit");
		itemRubbingKit.setRegistryName("rubbing_kit");
		itemRubbingKit.setCreativeTab(lovecraftTab);
		
		itemEmptyBeaker = new ItemEmptyBeaker();
		itemEmptyBeaker.setUnlocalizedName("empty_beaker");
		itemEmptyBeaker.setRegistryName("empty_beaker");
		itemEmptyBeaker.setCreativeTab(lovecraftTab);
		
		itemBeaker = new ItemBeaker();
		itemBeaker.setUnlocalizedName("beaker");
		itemBeaker.setRegistryName("beaker");
		itemBeaker.setCreativeTab(lovecraftTab);
		
		itemCoin = new ItemSimple();
		itemCoin.setUnlocalizedName("coin");
		itemCoin.setRegistryName("coin");
		itemCoin.setCreativeTab(lovecraftTab);
		
		itemWeirdShards = new ItemSimple();
		itemWeirdShards.setUnlocalizedName("weird_shards");
		itemWeirdShards.setRegistryName("weird_shards");
		itemWeirdShards.setCreativeTab(lovecraftTab);
		
		itemWeirdDust = new ItemSimple();
		itemWeirdDust.setUnlocalizedName("weird_dust");
		itemWeirdDust.setRegistryName("weird_dust");
		itemWeirdDust.setCreativeTab(lovecraftTab);
		
		itemMummyDust = new ItemSimple();
		itemMummyDust.setUnlocalizedName("mummy_dust");
		itemMummyDust.setRegistryName("mummy_dust");
		itemMummyDust.setCreativeTab(lovecraftTab);
		
		itemFleshDust = new ItemSimple();
		itemFleshDust.setUnlocalizedName("flesh_dust");
		itemFleshDust.setRegistryName("flesh_dust");
		itemFleshDust.setCreativeTab(lovecraftTab);
		
		itemMummyChunk = new ItemSimple();
		itemMummyChunk.setUnlocalizedName("mummy_chunk");
		itemMummyChunk.setRegistryName("mummy_chunk");
		itemMummyChunk.setCreativeTab(lovecraftTab);
		
		itemMagnifyingGlass = new ItemSimple();
		itemMagnifyingGlass.setUnlocalizedName("magnifying_glass");
		itemMagnifyingGlass.setRegistryName("magnifying_glass");
		itemMagnifyingGlass.setCreativeTab(lovecraftTab);

		itemFossil = new ItemFossil();
		itemFossil.setRegistryName("fossil");
		itemFossil.setCreativeTab(lovecraftTab);
		
		itemTome = new ItemTome();
		itemTome.setRegistryName("tome");
		itemTome.setCreativeTab(lovecraftTab);

		itemBook = new ItemBook();
		itemBook.setRegistryName("book");
		itemBook.setCreativeTab(lovecraftTab);
		
		itemRubbing = new ItemRubbing();
		itemRubbing.setUnlocalizedName("rubbing");
		itemRubbing.setRegistryName("rubbing");
		
		itemDriedFlower = new ItemSimple();
		itemDriedFlower.setUnlocalizedName("dried_flower");
		itemDriedFlower.setRegistryName("dried_flower");
		itemDriedFlower.setCreativeTab(lovecraftTab);
		
		blockFlowerDrug = new BlockFlowerDrug(Material.PLANTS);
		blockFlowerDrug.setHardness(0.0f);
		blockFlowerDrug.setUnlocalizedName("flower_drug");
		blockFlowerDrug.setRegistryName("flower_drug");
		blockFlowerDrug.setCreativeTab(lovecraftTab);
		
		/*blockAetherOre = new BlockSimpleDrop(Material.ROCK, itemFossilDust, 8);
		blockAetherOre.setHardness(1.4f);
		blockAetherOre.setUnlocalizedName("aether_ore");
		blockAetherOre.setRegistryName("aether_ore");
		blockAetherOre.setLightLevel(8f);
		blockAetherOre.setHarvestLevel("pickaxe", 0);
		blockAetherOre.setCreativeTab(lovecraftTab);*/
		
		//Initialize blocks, set properties

		blockUnderstructure = new BlockUnderstructure(Material.ROCK);
		blockUnderstructure.setLightLevel(16f);
		blockUnderstructure.setUnlocalizedName("understructure");
		blockUnderstructure.setRegistryName("understructure");
		
		blockWeirdedBrick = new BlockWeirdedBrick(Material.ROCK);
		blockWeirdedBrick.setHardness(60f);
		blockWeirdedBrick.setResistance(18000000.0f);
		blockWeirdedBrick.setUnlocalizedName("weirded_brick");
		blockWeirdedBrick.setRegistryName("weirded_brick");
		blockWeirdedBrick.setCreativeTab(lovecraftTab);
		
		blockBasRelief = new BlockBasRelief(Material.ROCK);
		blockBasRelief.setHardness(2.0f);
		blockBasRelief.setUnlocalizedName("bas_relief");
		blockBasRelief.setRegistryName("bas_relief");
		blockBasRelief.setCreativeTab(lovecraftTab);
		
		blockHookah = new BlockHookah(Material.GLASS);
		blockHookah.setHardness(1.0f);
		blockHookah.setUnlocalizedName("hookah");
		blockHookah.setRegistryName("hookah");
		blockHookah.setCreativeTab(lovecraftTab);
		
		blockFossil = new BlockFossil(Material.ROCK, itemFossil, 1);
		blockFossil.setHardness(6.0f);
		blockFossil.setHarvestLevel("pickaxe", 2);
		blockFossil.setUnlocalizedName("fossil_block");
		blockFossil.setRegistryName("fossil_block");
		blockFossil.setCreativeTab(lovecraftTab);

		blockAltar = new BlockAltar(Material.ROCK);
		blockAltar.setHardness(1.5f);
		blockAltar.setHarvestLevel("pickaxe", 0);
		blockAltar.setUnlocalizedName("altar");
		blockAltar.setRegistryName("altar");
		blockAltar.setCreativeTab(lovecraftTab);
		
		blockMortar = new BlockMortar(Material.ROCK);
		blockMortar.setHardness(1.5f);
		blockMortar.setUnlocalizedName("mortar");
		blockMortar.setRegistryName("mortar");
		blockMortar.setCreativeTab(lovecraftTab);
		
		blockDesk = new BlockDesk(Material.WOOD);
		blockDesk.setHardness(2.5f);
		blockDesk.setUnlocalizedName("desk");
		blockDesk.setRegistryName("desk");
		blockDesk.setCreativeTab(lovecraftTab);
		
		blockObelisk = new BlockObelisk(Material.ROCK);
		blockObelisk.setHardness(1.5f);
		blockObelisk.setHarvestLevel("pickaxe", 0);
		blockObelisk.setUnlocalizedName("obelisk");
		blockObelisk.setRegistryName("obelisk");
		blockObelisk.setCreativeTab(lovecraftTab);
		
		blockCarvedObelisk = new BlockObelisk(Material.ROCK);
		blockCarvedObelisk.setHardness(1.5f);
		blockCarvedObelisk.setHarvestLevel("pickaxe", 0);
		blockCarvedObelisk.setUnlocalizedName("carved_obelisk");
		blockCarvedObelisk.setRegistryName("carved_obelisk");
		blockCarvedObelisk.setCreativeTab(lovecraftTab);
		
		blockChargedObelisk = new BlockChargedObelisk(Material.ROCK);
		blockChargedObelisk.setHardness(1.5f);
		blockChargedObelisk.setHarvestLevel("pickaxe", 0);
		blockChargedObelisk.setUnlocalizedName("charged_obelisk");
		blockChargedObelisk.setRegistryName("charged_obelisk");
		blockChargedObelisk.setLightLevel(8.0f);
		blockChargedObelisk.setCreativeTab(lovecraftTab);
		
		blockObeliskCap = new BlockObeliskCap(Material.ROCK);
		blockObeliskCap.setHardness(1.5f);
		blockObeliskCap.setHarvestLevel("pickaxe", 0);
		blockObeliskCap.setUnlocalizedName("obelisk_cap");
		blockObeliskCap.setRegistryName("obelisk_cap");
		blockObeliskCap.setCreativeTab(lovecraftTab);
		
		blockResolvedObeliskCap = new BlockResolvedObeliskCap(Material.ROCK);
		blockResolvedObeliskCap.setHardness(1.5f);
		blockResolvedObeliskCap.setHarvestLevel("pickaxe", 0);
		blockResolvedObeliskCap.setUnlocalizedName("resolved_obelisk_cap");
		blockResolvedObeliskCap.setRegistryName("resolved_obelisk_cap");
		blockResolvedObeliskCap.setLightLevel(8.0f);
		blockResolvedObeliskCap.setCreativeTab(lovecraftTab);
		
		blockCarved = new BlockCarvedBlock(Material.ROCK); //probably add another parameter later
		blockCarved.setHardness(1.5f);
		blockCarved.setHarvestLevel("pickaxe", 0);
		blockCarved.setUnlocalizedName("carved_block");
		blockCarved.setRegistryName("carved_block");
		blockCarved.setCreativeTab(lovecraftTab);
		
		//Initialize itemBlocks
		itemBlockUnderstructure = new ItemSimpleBlock(blockUnderstructure).setRegistryName(blockUnderstructure.getRegistryName());
		itemBlockFlowerDrug = new ItemSimpleBlock(blockFlowerDrug).setRegistryName(blockFlowerDrug.getRegistryName());
		itemBlockFossil = new ItemSimpleBlock(blockFossil).setRegistryName(blockFossil.getRegistryName());
		itemBlockWeirdedBrick = new ItemSimpleBlock(blockWeirdedBrick).setRegistryName(blockWeirdedBrick.getRegistryName());
		//itemBlockAetherOre = new ItemSimpleBlock(blockAetherOre).setRegistryName(blockAetherOre.getRegistryName());
		itemBlockBasRelief = new ItemSimpleBlock(blockBasRelief).setRegistryName(blockBasRelief.getRegistryName());
		itemBlockHookah = new ItemSimpleBlock(blockHookah).setRegistryName(blockHookah.getRegistryName());
		itemBlockAltar = new ItemSimpleBlock(blockAltar).setRegistryName(blockAltar.getRegistryName());
		itemBlockMortar = new ItemSimpleBlock(blockMortar).setRegistryName(blockMortar.getRegistryName());
		itemBlockDesk = new ItemSimpleBlock(blockDesk).setRegistryName(blockDesk.getRegistryName());
		itemBlockObelisk = new ItemSimpleBlock(blockObelisk).setRegistryName(blockObelisk.getRegistryName());
		itemBlockCarvedObelisk = new ItemSimpleBlock(blockCarvedObelisk).setRegistryName(blockCarvedObelisk.getRegistryName());
		itemBlockChargedObelisk = new ItemSimpleBlock(blockChargedObelisk).setRegistryName(blockChargedObelisk.getRegistryName());
		itemBlockObeliskCap = new ItemSimpleBlock(blockObeliskCap).setRegistryName(blockObeliskCap.getRegistryName());
		itemBlockResolvedObeliskCap = new ItemSimpleBlock(blockResolvedObeliskCap).setRegistryName(blockResolvedObeliskCap.getRegistryName());
		itemBlockCarved = new ItemSimpleBlock(blockCarved).setRegistryName(blockCarved.getRegistryName());


		//Initialize potions
		potionDread = new PotionSimple(true, 14611199, 0, 0).setPotionName("effect.dread").setRegistryName("lovecraft:dread");
		potionAwake = new PotionSimple(true, 16777113, 1, 0).setPotionName("effect.awake").setRegistryName("lovecraft:awake");
		potionDrugged = new PotionDrugged(true, 13369497, 2, 0).setPotionName("effect.drugged").setRegistryName("lovecraft:drugged");
		
		//Add world generators
		GameRegistry.registerWorldGenerator(new OreGenerator(), 30);
		GameRegistry.registerWorldGenerator(new DecorationGenerator(), 300);
		GameRegistry.registerWorldGenerator(new TombGenerator(), 20);
		//GameRegistry.registerWorldGenerator(lengGenerator, 0);
		//GameRegistry.registerWorldGenerator(new ZigguratGenerator(), 4);
		
		DimensionManager.registerDimension(ROOM_DIM_ID, ROOM_DIM_TYPE);
		
		//Add smelting recipes
		GameRegistry.addSmelting(itemBlockFlowerDrug, itemDriedFlower.getDefaultInstance(), 0);

		//Add tile entities //TODO: Registry events or whatever else this is deprecated in favor of
		GameRegistry.registerTileEntity(TileEntityHookah.class, "lovecraft:hookah");
		GameRegistry.registerTileEntity(TileEntityAltar.class, "lovecraft:altar");
		GameRegistry.registerTileEntity(TileEntityChargedObelisk.class, "lovecraft:chargedObelisk");
		GameRegistry.registerTileEntity(TileEntityCarving.class, "lovecraft:carvedBlock");
		
		//Register entities
		EntityRegistry.registerModEntity(new ResourceLocation("lovecraft:spell"), EntitySpell.class, "lovecraftSpell", 0, "lovecraft", 0, 1, false);
		
		spellHandler = new SpellHandler();
		
		//Proxy Pre-Init
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		//Init deskHandler and alchemyRecipes
		//(Reliant on pre-init defining of items, etc.)
		deskHandler.init();
		alchemyRecipes.InitRecipes();
		
		//Register village careers & associate professions
		ProfessionHandler.associateCareersAndTrades();
		
		//Register village pieces
		//
		MapGenStructureIO.registerStructureComponent(VillageOpiumDen.class, "opiumden");
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageCreationHandler(VillageOpiumDen.class, 120, 1));
		
		//Proxy Init
		proxy.init(event);
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		//Add loot tables
		LootTableList.register(new ResourceLocation("lovecraft","chests/tomb"));
		
		//Proxy Post-Init
		proxy.postInit(event);
	}

	private static int nextDimID() {
		for (int i = 0; i != -1; i++) {
			if (!DimensionManager.isDimensionRegistered(i)) {
				return i;
			}
		}
		throw new RuntimeException("No free dimension IDs!");
	}
	
}
