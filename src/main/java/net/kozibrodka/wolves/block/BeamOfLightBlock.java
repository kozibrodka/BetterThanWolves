package net.kozibrodka.wolves.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.kozibrodka.wolves.events.BlockListener;
import net.kozibrodka.wolves.events.TextureListener;
import net.kozibrodka.wolves.utils.CustomBlockRendering;
import net.kozibrodka.wolves.utils.UnsortedUtils;
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
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithWorldRenderer.class)
//@EnvironmentInterface(value = EnvType.CLIENT, itf = BlockWithInventoryRenderer.class)
public class BeamOfLightBlock extends TemplateBlock implements BlockWithWorldRenderer {
    public BeamOfLightBlock(Identifier identifier) {
        super(identifier, Material.AIR);
        setOpacity(0);
        setDefaultState(getDefaultState()
                .with(Properties.FACING, Direction.UP)
                .with(FINAL, 0)
                .with(MAX_DISTANCE, 0)
        );
        setLuminance((BlockState blockState) -> blockState.get(FINAL) > 1 ? 15 : 0);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(Properties.FACING);
        builder.add(FINAL);
        builder.add(MAX_DISTANCE);
    }

    private static final int m_iLensMaxRange = 127; /// mogę zmienić MAX range dowolnie, nie wchodzi w konflikt z wysokością świata.
    private static final IntProperty FINAL = IntProperty.of("final", 0, 2);
    public static final IntProperty MAX_DISTANCE = IntProperty.of("max_distance", 0, m_iLensMaxRange);


    @Override
    public int getTextureId(BlockView blockAccess, int i, int j, int k, int iSide) {
        return TextureListener.beam_25;
    }

    @Override
    public int getTexture(int iSide) {
        return TextureListener.beam_25;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) { /// piasek przechodzi / brak kolizcji przy chodzeniu
        return null;
    }

    @Override
    public boolean hasCollision() {
        return false;
    } /// nie można kliknąć myszą, rozwalić

//    @Override
//    public boolean isFullCube() {
//        return false;
//    }

    @Override
    public int getDroppedItemCount(Random random) {
        return 0;
    }


    //TODO MAIN: czas na render
    //todo lens: aplikacja TileEntity - jedynie aby było powolne cykliczne sprawdzanie odnośnie Pory Dnia - bo w sumie to taka main funkcja.

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
    } /// patch ala Optifine musi naprawić ten system (brak liquidsów za blokami translucent)

//    @Override
//    public boolean isFullCube() {
//        return false; // Informuje silnik, że używasz niestandardowego renderera (nie standardowej kostki)
//    }


    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideVisible(BlockView blockView, int x, int y, int z, int side) {
        return true;

        //TODO logika tego w renderze? (sadzonka block na przykład)? - coś w stylu isSideVisible (....,  getOppositeFacingMeta?)
//        int var6 = blockView.getBlockId(x, y, z);
//        return !this.true && var6 == this.id ? false :
//        return     super.isSideVisible(blockView, x, y, z, side);
    }


    @Override
    public void neighborUpdate(World world, int i, int j, int k, int id) {

//        System.out.println("UPDATENEIG: " + Block.BLOCKS[id].getTranslatedName());
//        if(!checkIfCanStay(world, i, j, k)){
//            System.out.println("USUWAM SIE");
//            world.setBlock(i, j, k, 0);
//        }else{
//            /// tryExtending
//        }
        if(id != BlockListener.beamLight.id) {
            world.scheduleBlockUpdate(i, j, k, this.id, 1); //TODO dev
        }

    }

    public boolean checkIfCanStay(World world, int x, int y, int z, BlockState blockState){
//        BlockState blockState = world.getBlockState(x,y,z);
        BlockPos pos = new BlockPos(x, y, z).add(blockState.get(Properties.FACING).getOpposite().getVector());
        int neID = world.getBlockId(pos.x, pos.y, pos.z);
        if(neID == BlockListener.beamLight.id){
            BlockState neigBlockState = world.getBlockState(pos.x, pos.y, pos.z);
            if(neigBlockState.get(Properties.FACING) == blockState.get(Properties.FACING)){
                return true;
            }
        }else if(neID == BlockListener.lens.id){
            return ((LensBlock) BlockListener.lens).GetMetaFacing(world, pos.x, pos.y, pos.z) == UnsortedUtils.convertDirectionToMeta(blockState.get(Properties.FACING));
        }

        return false;
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
//        System.out.println("POSTAWIENIE ŚWIATŁA" + world.getBlockState(x, y, z).get(Properties.FACING));
        world.scheduleBlockUpdate(x, y, z, this.id, 1); //1
    }

    @Override
    public void onTick(World world, int x, int y, int z, Random random) {
//        System.out.println("onTICK ŚWIATŁA" + world.getBlockState(x, y, z).get(Properties.FACING));
        /// try extending
        BlockState blockState = world.getBlockState(x, y, z);

        if(!checkIfCanStay(world, x, y, z, blockState)){
//            System.out.println("USUWAM SIE ");
            world.setBlock(x, y, z, 0);
        }else {
            BlockPos pos = new BlockPos(x,y,z).add(blockState.get(Properties.FACING).getVector());
            if (tryExtendingLight(world, pos, blockState.get(Properties.FACING), blockState.get(MAX_DISTANCE) + 1)) {
                if (blockState.get(FINAL) > 0) {
                    world.setBlockStateWithoutNotifyingNeighbors(x, y, z, blockState.with(FINAL, 0)); /// światło zostało przedłużone
                }
            } else {
                if(world.getBlockId(pos.x, pos.y, pos.z) != BlockListener.beamLight.id){
                    if(world.getBlockId(pos.x, pos.y, pos.z) != 0 || y == world.getTopY()-1){
                        world.setBlockStateWithoutNotifyingNeighbors(x, y, z, blockState.with(FINAL, 2)); /// jjestem przy ścianie
                    }else{
                        world.setBlockStateWithoutNotifyingNeighbors(x, y, z, blockState.with(FINAL, 1)); /// jestem na końcu limitu
                    }
                }
            }
        }
    }

    public boolean tryExtendingLight(World world, BlockPos newPos, Direction geoDir, int nextDistance){
//        BlockPos newPos = new BlockPos(x,y,z).add(geoDir.getVector());
        if(world.getBlockId(newPos.x, newPos.y, newPos.z) == 0 && isInsideWorldY(world, newPos.y) && nextDistance <= m_iLensMaxRange){

            world.setBlock(newPos.x, newPos.y, newPos.z, BlockListener.beamLight.id);
            BlockState blockState = world.getBlockState(newPos);
            world.setBlockState(newPos, blockState.with(Properties.FACING, geoDir).with(MAX_DISTANCE, nextDistance));
            return true;
        }else{
            return false;
        }
        /// Ustawi się na około 141 bloków, ale gdy załaduje chunki to może nawet w nieskończoność.
        /// Zanik światła przy wyłączeniu lens też około 141 bloków przy swieżym wejściu na świat, jeżeli załaduje chunki to w nieskończoność
        /// Sensowność limitu 128 np. jest sensowne imo, bo jak wyjebie mega daleko to przy wyłączeniu gdzieś daleko światło może zostać.
    }

    public boolean isInsideWorldY(World world, int y){
        return y < world.getHeight() && y >= 0;
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
        BlockState blockState = ((BlockStateView)blockView).getBlockState(x,y,z);
        if(blockState.get(FINAL) < 2){ /// tylko przylegający do ściany renderuje
            return false;
        }

        Direction geoDir = blockState.get(Properties.FACING);
        BlockPos reflPos = new BlockPos(x, y, z).add(geoDir.getVector());
        Block blockR0 = Block.BLOCKS[blockView.getBlockId(reflPos.x, reflPos.y, reflPos.z)];
        boolean flagF = blockR0.isFullCube();

        if(!flagF){
            /// dodatkowa logika odnośnie kolejnych bloków?
        }


        int texID = TextureListener.beam_25;
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.color(1.0F, 1.0F, 1.0F); ///jasność zawsze full




        // PODŁOGA
        // box na samej górze, ściągnięty w dół o 1 blok
        if(geoDir == Direction.DOWN) {
            if(flagF) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
                tileRenderer.renderTopFace(this, x, y , z, texID);
            }
        }
        //todo UP - DOWN zostało

        // SUFIT
        // box na samym dole, podbity w górę o 1 blok
        if(geoDir == Direction.UP) {
            if(flagF) {
                this.setBoundingBox(0.0F, 0.999F, 0.0F, 1.0F, 1.0F, 1.0F);
                tileRenderer.renderBottomFace(this, x, y, z, texID);
            }
        }

        // EAST
        if(geoDir == Direction.EAST) {
            if(flagF) {
                this.setBoundingBox(0.999F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                tileRenderer.renderNorthFace(this, x, y, z, texID);
            }else{
                this.setBoundingBox(0.999F, (float) blockR0.minY, (float) blockR0.minZ, 1.0F, (float) blockR0.maxY, (float) blockR0.maxZ);
                tileRenderer.renderNorthFace(this, x + blockR0.minX, y, z, texID);
            }
        }

        // WEST
        if(geoDir == Direction.WEST) {
            if(flagF) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.001F, 1.0F, 1.0F);
                tileRenderer.renderSouthFace(this, x, y, z, texID);
            }else{
                this.setBoundingBox(0.0F, (float) blockR0.minY, (float) blockR0.minZ, 0.001F, (float) blockR0.maxY, (float) blockR0.maxZ);
                tileRenderer.renderSouthFace(this, x - (1 - blockR0.maxX), y, z, texID);
            }
        }

        // NORTH
        if(geoDir == Direction.NORTH) {
            if(flagF) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.001F);
                tileRenderer.renderWestFace(this, x, y, z, texID);
            }else{
                this.setBoundingBox((float) blockR0.minX, (float) blockR0.minY, 0.0F, (float) blockR0.maxX, (float) blockR0.maxY, 0.001F);
                tileRenderer.renderEastFace(this, x, y, z - (1 - blockR0.maxZ), texID);
            }
        }

        // SOUTH
        if(geoDir == Direction.SOUTH) {
            if(flagF) {
                this.setBoundingBox(0.0F, 0.0F, 0.999F, 1.0F, 1.0F, 1.0F);
                tileRenderer.renderEastFace(this, x, y, z, texID);
            }else{
                this.setBoundingBox((float) blockR0.minX, (float) blockR0.minY, 0.999F, (float) blockR0.maxX, (float) blockR0.maxY, 1.0F);
                tileRenderer.renderEastFace(this, x, y, z + blockR0.minZ, texID);
            }
        }

        // Resetowanie głównego Boxa kolizji/wyboru dla bloku
//        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }


    public boolean renderFullBeam(BlockRenderManager tileRenderer, BlockView blockView, int x, int y, int z){
        int texID = TextureListener.beam_25;

        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.color(1.0F, 1.0F, 1.0F); ///jasność zawsze full

        BlockPos pos = new BlockPos(x, y, z);

        // PODŁOGA
        // box na samej górze, ściągnięty w dół o 1 blok
//        this.setBoundingBox(0.0F, 0.999F, 0.0F, 1.0F, 1.0F, 1.0F);
//        tileRenderer.renderTopFace(this, x, y - 1.0D, z, texID);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
        tileRenderer.renderTopFace(this, x, y , z, texID);

        // SUFIT
        // box na samym dole, podbity w górę o 1 blok
//        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.001F, 1.0F);
//        tileRenderer.renderBottomFace(this, x, y + 0.999D, z, texID);
        this.setBoundingBox(0.0F, 0.999F, 0.0F, 1.0F, 1.0F, 1.0F);
        tileRenderer.renderBottomFace(this, x, y , z, texID);

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

//    @Override
//    public void renderInventory(BlockRenderManager tileRenderer, int i) {
//        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        CustomBlockRendering.RenderInvBlockWithTexture(tileRenderer, this, -0.5F, -0.5F, -0.5F, TextureListener.beam_25);
//    }

    @Override
    public void setupRenderBoundingBox() { /// Iventory Render Box
        setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
