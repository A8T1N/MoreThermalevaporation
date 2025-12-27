package morethermalevaporation.common.config;

import morethermalevaporation.MoreThermalEvaporation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreThermalEvaporation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MoreThermalEvaporationConfig {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec configSpec;

    public static final ForgeConfigSpec.IntValue BasicEvaporationOutputTankCapacity;
    public static final ForgeConfigSpec.IntValue BasicEvaporationPlantHeight;

    public static final ForgeConfigSpec.IntValue AdvancedEvaporationOutputTankCapacity;
    public static final ForgeConfigSpec.IntValue AdvancedEvaporationPlantHeight;

    public static final ForgeConfigSpec.IntValue EliteEvaporationOutputTankCapacity;
    public static final ForgeConfigSpec.IntValue EliteEvaporationPlantHeight;

    public static final ForgeConfigSpec.IntValue UltimateEvaporationOutputTankCapacity;
    public static final ForgeConfigSpec.IntValue UltimateEvaporationPlantHeight;

    public final static ForgeConfigSpec.BooleanValue RenderFluid;

    static {
        builder.comment("More Thermal Evaporation Settings");
        builder.push("Tier");

        // Basic
        builder.push("Basic");
        builder.comment("Settings for the Basic Tier");
        BasicEvaporationOutputTankCapacity = builder
                .comment("Amount of output fluid (mB) that the Basic Evaporation Plant can store.")
                .defineInRange("BasicEvaporationOutputTankCapacity", 20000, 1, 2147483646);

        BasicEvaporationPlantHeight = builder
                .comment("Buildable Height (in blocks) for the Basic Evaporation Plant.")
                .defineInRange("BasicEvaporationPlantHeight", 18, 18, 36);
        builder.pop();

        // Advanced
        builder.push("Advanced");
        builder.comment("Settings for the Advanced Tier");
        AdvancedEvaporationOutputTankCapacity = builder
                .comment("Amount of output fluid (mB) that the Advanced Evaporation Plant can store.")
                .defineInRange("AdvancedEvaporationOutputTankCapacity", 80000, 1, 2147483646);

        AdvancedEvaporationPlantHeight = builder
                .comment("Buildable Height (in blocks) for the Advanced Evaporation Plant.")
                .defineInRange("AdvancedEvaporationPlantHeight", 18, 18, 72);
        builder.pop();

        // Elite
        builder.push("Elite");
        builder.comment("Settings for the Elite Tier");
        EliteEvaporationOutputTankCapacity = builder
                .comment("Amount of output fluid (mB) that the Elite Evaporation Plant can store.")
                .defineInRange("EliteEvaporationOutputTankCapacity", 640000, 1, 2147483646);

        EliteEvaporationPlantHeight = builder
                .comment("Buildable Height (in blocks) for the Elite Evaporation Plant.")
                .defineInRange("EliteEvaporationPlantHeight", 18, 18, 144);
        builder.pop();

        // Ultimate
        builder.push("Ultimate");
        builder.comment("Settings for the Ultimate Tier");
        UltimateEvaporationOutputTankCapacity = builder
                .comment("Amount of output fluid (mB) that the Ultimate Evaporation Plant can store.")
                .defineInRange("UltimateEvaporationOutputTankCapacity", 10240000, 1, 2147483646);

        UltimateEvaporationPlantHeight = builder
                .comment("Buildable Height (in blocks) for the Ultimate Evaporation Plant.")
                .defineInRange("UltimateEvaporationPlantHeight", 18, 18, 288);
        builder.pop();

        builder.pop(); // Tier

        builder.push("Render");
        builder.comment("Settings for the Render fluid");
        RenderFluid = builder
                .comment("Render fluid inside of Thermal Evaporation Plants.")
                .define("RenderFluid", true);
        builder.pop(); // Render


        configSpec = builder.build();
    }
}