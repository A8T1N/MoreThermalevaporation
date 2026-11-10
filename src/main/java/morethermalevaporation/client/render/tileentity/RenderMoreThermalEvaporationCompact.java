package morethermalevaporation.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.FluidTextureType;
import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.client.render.ModelRenderer;
import mekanism.client.render.RenderResizableCuboid.FaceDisplay;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;
import morethermalevaporation.common.tile.machine.TileEntityMoreThermalEvaporationCompact;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NothingNullByDefault
public class RenderMoreThermalEvaporationCompact extends MekanismTileEntityRenderer<TileEntityMoreThermalEvaporationCompact> {

    private final MoreThermalEvaporationTier tier;
    private static final Map<FluidStack, Int2ObjectMap<Model3D>> cachedCenterFluids = new HashMap<>();

    private static final int stages = 1_400;

    public RenderMoreThermalEvaporationCompact(MoreThermalEvaporationTier tier, BlockEntityRendererProvider.Context context) {
        super(context);
        this.tier = tier;
    }

    public static void resetCachedModels() {
        cachedCenterFluids.clear();
    }

    @Override
    protected void render(TileEntityMoreThermalEvaporationCompact tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        FluidStack fluid = tile.inputTank.getFluid();
        float fluidScale = tile.prevScale;

        Lazy<VertexConsumer> buffer = Lazy.of(() -> renderer.getBuffer(Sheets.translucentCullBlockSheet()));

        if (!fluid.isEmpty() && fluidScale > 0) {
            MekanismRenderer.renderObject(getFluidModel(fluid, fluidScale), matrix, buffer.get(), MekanismRenderer.getColorARGB(fluid, fluidScale),
                    MekanismRenderer.calculateGlowLight(light, fluid), overlayLight, FaceDisplay.FRONT, getCamera(), tile.getBlockPos());
        }
    }

    @Override
    protected String getProfilerSection() {
        return this.tier.getBaseTier().getLowerName() + "ThermalEvaporationCompact";
    }

    public static Model3D getFluidModel(@NotNull FluidStack fluid, float fluidScale) {
        return cachedCenterFluids.computeIfAbsent(fluid, f -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(ModelRenderer.getStage(fluid, stages, fluidScale), stage -> new Model3D()
                        .setTexture(MekanismRenderer.getFluidTexture(fluid, FluidTextureType.STILL))
                        .setSideRender(Direction.DOWN, false)
                        .setSideRender(Direction.UP, stage < stages)
                        .xBounds(0.020F, 0.975F)
                        .yBounds(0.135F, 0.135F + 0.75F * (stage / (float) stages))
                        .zBounds(0.020F, 0.975F)
                );
    }

}
