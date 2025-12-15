package morethermalevaporation.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.data.FluidRenderData;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import mekanism.common.base.ProfilerConstants;
import morethermalevaporation.common.evaporation.BasicThermalEvaporationMultiblockData;
import morethermalevaporation.tile.multiblock.TileEntityBasicThermalEvaporationController;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;

@NothingNullByDefault
public class RenderBasicThermalEvaporationPlant extends MultiblockTileEntityRenderer<BasicThermalEvaporationMultiblockData, TileEntityBasicThermalEvaporationController> {

    public RenderBasicThermalEvaporationPlant(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityBasicThermalEvaporationController tile, BasicThermalEvaporationMultiblockData multiblock, float partialTick, PoseStack matrix, MultiBufferSource renderer,
                          int light, int overlayLight, ProfilerFiller profiler) {
        VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
        FluidRenderData data = RenderData.Builder.create(multiblock.inputTank.getFluid())
                .of(multiblock)
                .height(multiblock.height() - 2)    // TODO なぜか液体の描画が1ブロック分多い
                .build();
        renderObject(data, multiblock.valves, tile.getBlockPos(), matrix, buffer, overlayLight, Math.min(1, multiblock.prevScale));
    }

    @Override
    protected String getProfilerSection() {
        return "BasicThermalEvaporationController";
    }

    @Override
    protected boolean shouldRender(TileEntityBasicThermalEvaporationController tile, BasicThermalEvaporationMultiblockData multiblock, Vec3 camera) {
        return super.shouldRender(tile, multiblock, camera) && !multiblock.inputTank.isEmpty();
    }
}