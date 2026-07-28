package morethermalevaporation.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.data.FluidRenderData;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import morethermalevaporation.common.config.MoreThermalEvaporationConfig;
import morethermalevaporation.common.content.evaporation.MoreThermalEvaporationMultiblockData;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.multiblock.TileEntityMoreThermalEvaporationController;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;

@NothingNullByDefault
public class RenderMoreThermalEvaporationPlant extends MultiblockTileEntityRenderer<MoreThermalEvaporationMultiblockData, TileEntityMoreThermalEvaporationController> {

    private final MoreThermalEvaporationTier tier;

    public RenderMoreThermalEvaporationPlant(MoreThermalEvaporationTier tier, BlockEntityRendererProvider.Context context) {
        super(context);
        this.tier = tier;
    }

    @Override
    protected void render(TileEntityMoreThermalEvaporationController tile, MoreThermalEvaporationMultiblockData multiblock, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        VertexConsumer buffer = renderer.getBuffer(Sheets.translucentCullBlockSheet());
        int size = multiblock.getType().getRenderSize();
        FluidRenderData data = RenderData.Builder.create(multiblock.inputTank.getFluid())
                .location(multiblock.renderLocation.offset(1, 0, 1))
                .dimensions(size, multiblock.height() - 2, size)
                .build();
        renderObject(data, multiblock.valves, tile.getBlockPos(), matrix, buffer, overlayLight, Math.min(1, multiblock.prevScale));
    }

    @Override
    protected String getProfilerSection() {
        return this.tier.getBaseTier().getLowerName() + "ThermalEvaporationController";
    }

    @Override
    protected boolean shouldRender(TileEntityMoreThermalEvaporationController tile, MoreThermalEvaporationMultiblockData multiblock, Vec3 camera) {
        return super.shouldRender(tile, multiblock, camera) && !multiblock.inputTank.isEmpty() && MoreThermalEvaporationConfig.config.renderFluid.get() && multiblock.getType() != null;
    }
}