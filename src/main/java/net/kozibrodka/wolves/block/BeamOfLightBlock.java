package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.BlockPosition;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class BeamOfLightBlock extends TemplateBlock implements BlockWithWorldRenderer, BlockWithInventoryRenderer {
    public BeamOfLightBlock(Identifier identifier) {
        super(identifier, Material.SNOW_LAYER);
        setOpacity(0);
        setDefaultState(getDefaultState()
                .with(Properties.FACING, Direction.UP)
                .with(FINAL, false)
        );
        setLuminance((BlockState blockState) -> blockState.get(FINAL) ? 15 : 0);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(Properties.FACING);
        builder.add(FINAL);
    }

    private static final BooleanProperty FINAL = BooleanProperty.of("final");


    @Override
    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide) {
        return 49;
    }

    @Override
    public int getTexture(int iSide) {
        return 49;
    }


    @Override
    public boolean isOpaque() {
        return false;
    }

//    @Override
//    public boolean isFullCube() {
//        return false;
//    }

    @Override
    public int getDroppedItemCount(Random random) {
        return 0;
    }

    //todo mixin canFallThrough(World world, int x, int y, int z) in FallingBlockEntity

    @Override
    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        //todo
        int var7 = Item.SNOWBALL.id;
        float var8 = 0.7F;
        double var9 = (double)(world.random.nextFloat() * var8) + (double)(1.0F - var8) * (double)0.5F;
        double var11 = (double)(world.random.nextFloat() * var8) + (double)(1.0F - var8) * (double)0.5F;
        double var13 = (double)(world.random.nextFloat() * var8) + (double)(1.0F - var8) * (double)0.5F;
        ItemEntity var15 = new ItemEntity(world, (double)x + var9, (double)y + var11, (double)z + var13, new ItemStack(var7, 1, 0));
        var15.pickupDelay = 10;
        world.spawnEntity(var15);
        world.setBlock(x, y, z, 0);
        playerEntity.increaseStat(Stats.MINE_BLOCK[this.id], 1);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getRenderLayer() {
        return 1;
    } /// ZASTANÓW SIE

//    @Override
//    public boolean isFullCube() {
//        return false; // Informuje silnik, że używasz niestandardowego renderera (nie standardowej kostki)
//    }

    public boolean transparent = true;


    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideVisible(BlockView blockView, int x, int y, int z, int side) {
        return true;

//        int var6 = blockView.getBlockId(x, y, z);
//        return !this.transparent && var6 == this.id ? false : super.isSideVisible(blockView, x, y, z, side);
    }


    @Override
    public void neighborUpdate(World world, int i, int j, int k, int id) {

        System.out.println("UPDATENEIG: " + Block.BLOCKS[id].getTranslatedName());
//        if(!checkIfCanStay(world, i, j, k)){
//            System.out.println("USUWAM SIE");
//            world.setBlock(i, j, k, 0);
//        }else{
//            /// tryExtending
//        }
        if(id != BlockListener.beamLight.id) {
            world.scheduleBlockUpdate(i, j, k, this.id, 1);
        }

    }

    public boolean checkIfCanStay(World world, int x, int y, int z, BlockState blockState){
//        BlockState blockState = world.getBlockState(x,y,z);
        BlockPos pos = new BlockPos(x, y, z).add(blockState.get(Properties.FACING).getOpposite().getVector());
        int neID = world.getBlockId(pos.x, pos.y, pos.z);
        if(neID == BlockListener.beamLight.id || neID == BlockListener.lens.id){
            BlockState neigBlockState = world.getBlockState(pos.x, pos.y, pos.z);
            if(neigBlockState.get(Properties.FACING) == blockState.get(Properties.FACING)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        System.out.println("POSTAWIENIE ŚWIATŁA" + world.getBlockState(x, y, z).get(Properties.FACING));
        world.scheduleBlockUpdate(x, y, z, this.id, 1); //1
    }

    @Override
    public void onTick(World world, int x, int y, int z, Random random) {
        System.out.println("onTICK ŚWIATŁA" + world.getBlockState(x, y, z).get(Properties.FACING));
        /// try extending
        BlockState blockState = world.getBlockState(x, y, z);

        if(!checkIfCanStay(world, x, y, z, blockState)){
            System.out.println("USUWAM SIE ");
            world.setBlock(x, y, z, 0);
        }else {


            //todo.
//            jeżeli blockNastępny to jest śwatło nawet nie próbuj.

            BlockPos pos = new BlockPos(x,y,z).add(blockState.get(Properties.FACING).getVector());

            if (tryExtendingLight(world, x, y, z, blockState.get(Properties.FACING))) {
                if (blockState.get(FINAL) == true) {
                    world.setBlockStateWithoutNotifyingNeighbors(x, y, z, blockState.with(FINAL, false)); /// światło zostało przedłużone
                }

            } else {
                if(world.getBlockId(pos.x, pos.y, pos.z) != BlockListener.beamLight.id){
                    world.setBlockStateWithoutNotifyingNeighbors(x, y, z, blockState.with(FINAL, true)); /// jestem przy ścianie
                }
            }
        }
    }

    public boolean tryExtendingLight(World world, int x, int y, int z, Direction geoDir){
        BlockPos newPos = new BlockPos(x,y,z).add(geoDir.getVector());
        if(world.getBlockId(newPos.x, newPos.y, newPos.z) == 0){
            // TODO tutaj dać w IF, czy setBlock przejdzie.
            world.setBlock(newPos.x, newPos.y, newPos.z, BlockListener.beamLight.id);
            BlockState blockState = world.getBlockState(newPos);
            world.setBlockState(newPos, blockState.with(Properties.FACING, geoDir));
            return true;
        }else{
            return false;
        }
    }

    /// Kierunki Geo
    /// old (vanilla) -> new (stapi)
    /// NORTH -> WEST
    /// SOUTH -> EAST
    /// WEST  -> SOUTH
    /// EAST  -> NORTH
    ///

    @Override
    public boolean renderWorld(BlockRenderManager tileRenderer, BlockView blockView, int x, int y, int z) {

//        int texID = TextureListener.lens_front;
        int texID = TextureListener.beam_25;
//        int texID = 67;


        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.color(1.0F, 1.0F, 1.0F); ///jasność zawsze full

        BlockPos pos = new BlockPos(x, y, z);


        // PODŁOGA
        // box na samej górze, ściągnięty w dół o 1 blok
        this.setBoundingBox(0.0F, 0.999F, 0.0F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderTopFace(this, x, y - 1.0D, z, texID);

        // SUFIT
        // box na samym dole, podbity w górę o 1 blok
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
        tileRenderer.renderBottomFace(this, x, y + 0.999D, z, texID);


        // EAST
        this.setBoundingBox(0.999F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderNorthFace(this, x, y, z, texID);

        // WEST
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.001F, 1.0F, 1.0F);
        tileRenderer.renderSouthFace(this, x, y, z, texID);

        // NORTH
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.001F);
        tileRenderer.renderWestFace(this, x, y, z, texID);

        // SOUTH
        this.setBoundingBox(0.0F, 0.0F, 0.999F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderEastFace(this, x, y, z, texID);

        // Resetowanie głównego Boxa kolizji/wyboru dla bloku
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void renderInventory(BlockRenderManager tileRenderer, int i) {
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.beam_25);
    }
}
