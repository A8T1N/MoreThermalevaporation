package morethermalevaporation.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.data.FluidRenderData;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.evaporation.AdvancedThermalEvaporationMultiblockData;
import morethermalevaporation.tile.multiblock.TileEntityAdvancedThermalEvaporationController;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;

@NothingNullByDefault
public class RenderAdvancedThermalEvaporationPlant extends MultiblockTileEntityRenderer<AdvancedThermalEvaporationMultiblockData, TileEntityAdvancedThermalEvaporationController> {

    public RenderAdvancedThermalEvaporationPlant(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityAdvancedThermalEvaporationController tile, AdvancedThermalEvaporationMultiblockData multiblock, float partialTick, PoseStack matrix, MultiBufferSource renderer,
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
        return "AdvancedThermalEvaporationController";
    }

    @Override
    protected boolean shouldRender(TileEntityAdvancedThermalEvaporationController tile, AdvancedThermalEvaporationMultiblockData multiblock, Vec3 camera) {
        return super.shouldRender(tile, multiblock, camera) && !multiblock.inputTank.isEmpty() && MoreThermalEvaporationConfig.RenderFluid.get();
    }
}