package morethermalevaporation;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mekanism.api.IEvaporationSolar;
import mekanism.api.NBTConstants;
import mekanism.api.heat.HeatAPI;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerFluidTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.FluidRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleFluid;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import mekanism.common.tile.multiblock.TileEntityThermalEvaporationBlock;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;


/**
 * EvaporationMultiblockData
 * <p>
 * このクラスは Mekanism の Thermal Evaporation マルチブロックの
 * 中核データクラスです。以下の主要責務を持ちます。
 * - 入力/出力の液体タンク管理
 * - 熱（温度）管理（VariableHeatCapacitor）
 * - レシピ検索・実行の監視（RecipeCacheLookupMonitor / OneInputCachedRecipe）
 * - ソーラーパネル（IEvaporationSolar）との連携（角に設置される）
 * - マルチブロック形成時/解体時やネットワーク同期処理
 * <p>
 * 注意：以下のコメントは、提供されたソースコードの振る舞いをそのまま説明
 * しており、コードに存在しない振る舞いを想像で追加していません。
 */
public class EvaporationMultiblockData extends MultiblockData implements IValveHandler, FluidRecipeLookupHandler<FluidToFluidRecipe> {

    public static final int MAX_HEIGHT = 18; // 許容される最大高さ
    public static final double MAX_MULTIPLIER_TEMP = 3_000; // 温度補正値の上限
    // レシピ処理時に注目するエラーの種類を列挙
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    // アイテムスロット（バケツなど）
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItemInput", docPlaceholder = "input side's input slot")
    final FluidInventorySlot inputInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItemOutput", docPlaceholder = "input side's output slot")
    final OutputInventorySlot outputInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItemInput", docPlaceholder = "output side's input slot")
    final FluidInventorySlot inputOutputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItemOutput", docPlaceholder = "output side's output slot")
    final OutputInventorySlot outputOutputSlot;
    // レシピ検索の監視用オブジェクト（レシピのキャッシュを自動管理）
    private final RecipeCacheLookupMonitor<FluidToFluidRecipe> recipeCacheLookupMonitor;
    // 全エラーの再評価を要求するかどうかを判定するサプライヤ（TileEntity 側で制御）
    private final BooleanSupplier recheckAllRecipeErrors;
    @ContainerSync
    private final boolean[] trackedErrors = new boolean[TRACKED_ERROR_TYPES.size()]; // エラーの現在状態を格納
    // 各コーナーに置かれるソーラーパネル能力をキャッシュするためのマップ
    private final Int2ObjectMap<NonNullConsumer<LazyOptional<IEvaporationSolar>>> cachedSolarListeners = new Int2ObjectArrayMap<>(4);
    private final Int2ObjectMap<LazyOptional<IEvaporationSolar>> cachedSolar = new Int2ObjectArrayMap<>(4);
    // レシピ入出力ハンドラ（タンクをレシピシステムに接続する）
    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull FluidStack> inputHandler;
    // ContainerSync によるクライアントとの同期対象フィールド
    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public BasicFluidTank inputTank;
    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"}, docPlaceholder = "output tank")
    public BasicFluidTank outputTank;
    @ContainerSync
    public VariableHeatCapacitor heatCapacitor; // 熱（温度）を保持・更新するオブジェクト
    public float prevScale; // GUI 等へ送る液体表示スケール
    @ContainerSync
    @SyntheticComputerMethod(getter = "getProductionAmount")
    public double lastGain; // 直近の生産（増加）量の概算（デバッグ・表示用）
    @ContainerSync
    @SyntheticComputerMethod(getter = "getEnvironmentalLoss")
    public double lastEnvironmentLoss; // 直近の環境損失（熱）
    // バイオーム環境温度（初期値は周辺の環境温から設定）
    private double biomeAmbientTemp;
    // レシピ実行速度に影響する温度倍率（tick ごとに更新される）
    private double tempMultiplier;
    private int inputTankCapacity; // マルチブロック体積から計算した入力タンク容量


    /**
     * コンストラクタ
     * - MultiblockData の初期化を行い、レシピ監視・タンク・スロット・熱コンデンサを初期化する
     * - 使い道（呼び出し側）: マルチブロックの TileEntity が新しい MultiblockData を生成する際に呼ばれる
     */
    public EvaporationMultiblockData(TileEntityThermalEvaporationBlock tile) {
        super(tile);
        recipeCacheLookupMonitor = new RecipeCacheLookupMonitor<>(this);
        recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(tile);
        //Default biome temp to the ambient temperature at the block we are at
        biomeAmbientTemp = HeatAPI.getAmbientTemp(tile.getLevel(), tile.getTilePos());

        // 可変容量の入力タンク（容量は getMaxFluid で決まる）
        fluidTanks.add(inputTank = VariableCapacityFluidTank.input(this, this::getMaxFluid, this::containsRecipe, createSaveAndComparator(recipeCacheLookupMonitor)));
        // 出力タンクは設定値ベースの固定最大容量
        fluidTanks.add(outputTank = VariableCapacityFluidTank.output(this, MekanismConfig.general.evaporationOutputTankCapacity, BasicFluidTank.alwaysTrue, this));

        // レシピの入出力ハンドラを生成（エラー種別を渡す）
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);

        // アイテムスロットを作成（GUI/コンテナの入出力）
        inventorySlots.add(inputInputSlot = FluidInventorySlot.fill(inputTank, this, 28, 20));
        inventorySlots.add(outputInputSlot = OutputInventorySlot.at(this, 28, 51));
        inventorySlots.add(inputOutputSlot = FluidInventorySlot.drain(outputTank, this, 132, 20));
        inventorySlots.add(outputOutputSlot = OutputInventorySlot.at(this, 132, 51));
        inputInputSlot.setSlotType(ContainerSlotType.INPUT);
        inputOutputSlot.setSlotType(ContainerSlotType.INPUT);

        // 熱コンデンサを追加。初期値ではバイオーム温度を参照する関数を与える
        heatCapacitors.add(heatCapacitor = VariableHeatCapacitor.create(MekanismConfig.general.evaporationHeatCapacity.get() * 3, () -> biomeAmbientTemp, this));
    }


    /**
     * onCreated
     * - マルチブロックがワールド上で形成された直後に呼ばれる
     * - ここではバイオームの平均温度を再計算し、熱容量を高さに応じて更新し、ソーラーを更新する
     * - 使い道: マルチブロックが生成された直後に一度だけ呼び、可変要素（高さ依存など）を確定させる
     */
    @Override
    public void onCreated(Level world) {
        super.onCreated(world);
        biomeAmbientTemp = calculateAverageAmbientTemperature(world);
        // update the heat capacity now that we've read
        heatCapacitor.setHeatCapacity(MekanismConfig.general.evaporationHeatCapacity.get() * height(), true);
        updateSolars(world);
    }


    /**
     * tick
     * - 毎 tick 呼ばれるメインループ
     * - 行う処理:
     * 1) 外部へ放熱（simulateEnvironment）
     * 2) 熱コンデンサの更新（updateHeatCapacitors）
     * 3) 温度に基づくレシピ速度係数 tempMultiplier の更新
     * 4) アイテムスロット経由の入出力処理（fill/drain）
     * 5) レシピキャッシュの更新と処理
     * 6) GUI のスケール変化判定（prevScale）
     * - 戻り値: ネットワーク同期が必要なら true を返す
     * - 使い道: マルチブロック TileEntity の毎 tick 更新から呼ばれる
     */
    @Override
    public boolean tick(Level world) {
        boolean needsPacket = super.tick(world);
        // external heat dissipation
        lastEnvironmentLoss = simulateEnvironment();
        // update temperature
        updateHeatCapacitors(null);
        //After we update the heat capacitors, update our temperature multiplier
        // Note: We use the ambient temperature without taking our biome into account as we want to have a consistent multiplier
        tempMultiplier = (Math.min(MAX_MULTIPLIER_TEMP, getTemperature()) - HeatAPI.AMBIENT_TEMP) * MekanismConfig.general.evaporationTempMultiplier.get() *
                ((double) height() / MAX_HEIGHT);
        // アイテムスロットによる入出力（バケツ等）
        inputOutputSlot.drainTank(outputOutputSlot);
        inputInputSlot.fillTank(outputInputSlot);
        // レシピ検索/実行の更新
        recipeCacheLookupMonitor.updateAndProcess();
        // GUI 用スケールを計算し、変化があればパケットが必要
        float scale = MekanismUtils.getScale(prevScale, inputTank);
        if (scale != prevScale) {
            prevScale = scale;
            needsPacket = true;
        }
        return needsPacket;
    }


    /**
     * readUpdateTag
     * - ネットワークやクライアント側で受け取る更新タグを読み込む
     * - ここでは inputTank の中身や GUI 用スケール、バルブ情報を復元している
     * - 使い道: クライアントがサーバからの同期パケットを適用する際に呼ばれる
     */
    @Override
    public void readUpdateTag(CompoundTag tag) {
        super.readUpdateTag(tag);
        NBTUtils.setFluidStackIfPresent(tag, NBTConstants.FLUID_STORED, fluid -> inputTank.setStack(fluid));
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> prevScale = scale);
        readValves(tag);
    }


    /**
     * writeUpdateTag
     * - サーバがクライアントへ送る更新タグを生成する
     * - inputTank の内容や GUI スケール、バルブ情報をタグへ書き込む
     * - 使い道: ネットワーク同期を行う際にサーバ側で呼ばれる
     */
    @Override
    public void writeUpdateTag(CompoundTag tag) {
        super.writeUpdateTag(tag);
        tag.put(NBTConstants.FLUID_STORED, inputTank.getFluid().writeToNBT(new CompoundTag()));
        tag.putFloat(NBTConstants.SCALE, prevScale);
        writeValves(tag);
    }


    /**
     * simulateEnvironment
     * - 外気との熱交換を計算する
     * - ソーラーからの加熱を適用し、環境温に近づけるための放熱/吸熱を計算して heatCapacitor に反映する
     * - 戻り値: 正の値は（外部へ放出した）環境損失の量を表す（呼び出し側は lastEnvironmentLoss に格納している）
     * - 使い道: 毎 tick の熱処理の一部として呼ばれる
     */
    @Override
    public double simulateEnvironment() {
        double currentTemperature = getTemperature();
        double heatCapacity = heatCapacitor.getHeatCapacity();
        // 太陽パネルの数に応じた加熱を先に適用
        heatCapacitor.handleHeat(getActiveSolars() * MekanismConfig.general.evaporationSolarMultiplier.get() * heatCapacity);
        if (Math.abs(currentTemperature - biomeAmbientTemp) < 0.001) {
            // 温度差がほぼゼロならバイオーム温度に一致させる
            heatCapacitor.handleHeat(biomeAmbientTemp * heatCapacity - heatCapacitor.getHeat());
        } else {
            // 温度差の平方根に基づいて段階的に放熱/吸熱する（現実的な減衰を模す）
            double incr = MekanismConfig.general.evaporationHeatDissipation.get() * Math.sqrt(Math.abs(currentTemperature - biomeAmbientTemp));
            if (currentTemperature > biomeAmbientTemp) {
                incr = -incr; // 高温なら放熱方向
            }
            heatCapacitor.handleHeat(heatCapacity * incr);
            if (incr < 0) {
                return -incr; // 実際に放熱があった場合は正の値を返す
            }
        }
        return 0;
    }


    /**
     * getTemperature
     * - heatCapacitor の現在温度を返す単純な getter
     * - ComputerMethod アノテーションにより外部のコンピュータ統合からも呼べる
     */
    @ComputerMethod
    public double getTemperature() {
        return heatCapacitor.getTemperature();
    }


    /**
     * setVolume
     * - マルチブロックの体積が変化した際に呼ばれる
     * - 入力タンク容量（inputTankCapacity）を内部体積に基づいて更新する
     * - 使い道: 構造が形成/解体された時にフレームワーク側から呼ばれる
     */
    @Override
    public void setVolume(int volume) {
        if (getVolume() != volume) {
            super.setVolume(volume);
            //Note: We only count the inner volume for the tank capacity for the evap tower
            inputTankCapacity = (volume / 4) * MekanismConfig.general.evaporationFluidPerTank.get();
        }
    }


    /**
     * getMaxFluid
     * - 可変容量タンクが参照する最大流体量を返す
     * - 使い道: VariableCapacityFluidTank の容量取得コールバックとして使用される
     */
    public int getMaxFluid() {
        return inputTankCapacity;
    }


    /**
     * getRecipeType
     * - このマルチブロックが扱うレシピタイプ（蒸発）を返す
     * - 使い道: レシピ検索システムがどのタイプのレシピを検索すべきかを判定するために呼ばれる
     */
    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<FluidToFluidRecipe, SingleFluid<FluidToFluidRecipe>> getRecipeType() {
        return MekanismRecipeType.EVAPORATING;
    }


    /**
     * getRecipe
     * - 指定されたキャッシュインデックスに対応するレシピを返す（実装では入力ハンドラに基づいて最初のレシピを見つける）
     * - 使い道: レシピキャッシュが現在有効なレシピを問い合わせるときに呼ばれる
     */
    @Nullable
    @Override
    public FluidToFluidRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }


    /**
     * clearRecipeErrors
     * - 指定されたキャッシュインデックスのエラー追跡配列をクリアする
     * - 使い道: レシピエラーの再評価やリセットを行う場合に呼ばれる
     */
    @Override
    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(trackedErrors, false);
    }


    /**
     * createNewCachedRecipe
     * - 新しい CachedRecipe を生成し、各種コールバック（エラー更新、アクティブ状態、必要tick、基準最大操作数）を設定する
     * - 重要点（使い道）:
     * ・setErrorsChanged: レシピエラーの状態が変わったときに trackedErrors 配列を更新する
     * ・setActive: レシピが稼働中かどうかのコールバックで、稼働中なら lastGain を温度に応じて設定する
     * ・setRequiredTicks / setBaselineMaxOperations: 温度倍率 tempMultiplier に応じて必要 tick や一度に行う操作数を決める
     * - ここでの実装は tempMultiplier が 0 < x < 1 の場合に特別な扱いをしており、その場合は
     * 1 操作を複数 tick に分割して進める（1 / tempMultiplier tick ごとに 1 mB 相当などを処理する想定）
     */
    @NotNull
    @Override
    public CachedRecipe<FluidToFluidRecipe> createNewCachedRecipe(@NotNull FluidToFluidRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.fluidToFluid(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(errors -> {
                    for (int i = 0; i < trackedErrors.length; i++) {
                        trackedErrors[i] = errors.contains(TRACKED_ERROR_TYPES.get(i));
                    }
                })
                .setActive(active -> {
                    //TODO: Make the numbers for lastGain be based on how much the recipe provides as an output rather than "assuming" it is 1 mB
                    // Also fix that the numbers don't quite accurately reflect the values as we modify number of operations, and not have a fractional
                    // amount
                    if (active) {
                        if (tempMultiplier > 0 && tempMultiplier < 1) {
                            lastGain = 1F / (int) Math.ceil(1 / tempMultiplier);
                        } else {
                            lastGain = tempMultiplier;
                        }
                    } else {
                        lastGain = 0;
                    }
                })
                .setRequiredTicks(() -> tempMultiplier > 0 && tempMultiplier < 1 ? (int) Math.ceil(1 / tempMultiplier) : 1)
                .setBaselineMaxOperations(() -> tempMultiplier > 0 && tempMultiplier < 1 ? 1 : (int) tempMultiplier);
    }


    /**
     * hasWarning
     * - 指定された RecipeError が trackedErrors 配列で検出されているかを返す
     * - 使い道: GUI やログ表示で特定の警告を表示するか判定する
     */
    public boolean hasWarning(RecipeError error) {
        int errorIndex = TRACKED_ERROR_TYPES.indexOf(error);
        if (errorIndex == -1) {
            //Something went wrong
            return false;
        }
        return trackedErrors[errorIndex];
    }


    /**
     * getHandlerWorld
     * - レシピ処理や能力取得で必要になるワールドを返す
     * - 使い道: レシピ検索処理などがワールド参照を必要とするときに呼ぶ
     */
    @Override
    public Level getHandlerWorld() {
        return getWorld();
    }


    /**
     * getActiveSolars
     * - キャッシュされたソーラーパネル能力のうち、太陽が見えているものの数を返す
     * - 使い道: simulateEnvironment での追加加熱量計算に利用される
     */
    @ComputerMethod
    int getActiveSolars() {
        int ret = 0;
        for (LazyOptional<IEvaporationSolar> capability : cachedSolar.values()) {
            if (capability.map(IEvaporationSolar::canSeeSun).orElse(false)) {
                ret++;
            }
        }
        return ret;
    }


    /**
     * updateSolarSpot（private）
     * - 指定した角（corner インデックス）に対応する位置のタイルエンティティから
     * IEvaporationSolar capability を取得し、キャッシュとリスナーを更新する
     * - 使い道: 周囲のソーラーパネル設置/破壊時に呼び出してキャッシュを最新化する
     */
    private void updateSolarSpot(Level world, BlockPos pos, int corner) {
        //If we have the corner cached remove it
        cachedSolar.remove(corner);
        BlockEntity tile = WorldUtils.getTileEntity(world, pos);
        if (tile != null && !tile.isRemoved()) {
            LazyOptional<IEvaporationSolar> capability = CapabilityUtils.getCapability(tile, Capabilities.EVAPORATION_SOLAR, Direction.DOWN);
            if (capability.isPresent()) {
                capability.addListener(cachedSolarListeners.computeIfAbsent(corner, c -> new RefreshListener(this, c)));
                cachedSolar.put(corner, capability);
            }
        }
    }


    /**
     * updateSolarSpot（public）
     * - 外部から与えられた座標 pos が、マルチブロックの最上層角の位置かを判定し
     * 対応する corner インデックスを計算して private updateSolarSpot を呼ぶ
     * - 使い道: ソーラーパネルが設置/破壊された際の TileEntity 側から呼ばれる
     */
    public void updateSolarSpot(Level world, BlockPos pos) {
        BlockPos maxPos = getMaxPos();
        //Validate it is actually one of the spots solar panels can go
        if (pos.getY() == maxPos.getY() && getBounds().isOnCorner(pos)) {
            int i = 0;
            if (pos.getX() + 3 == maxPos.getX()) {
                //If we are westwards our index goes up by one
                i++;
            }
            if (pos.getZ() + 3 == maxPos.getZ()) {
                //If we are northwards it goes up by two
                i += 2;
            }
            updateSolarSpot(world, pos, i);
        }
    }


    /**
     * updateSolars
     * - マルチブロックの4角（最上層）に対応する全てのソーラースポットを更新する
     * - 使い道: マルチブロックが生成されたときや、周囲チャンクがロードされた際に初期化する
     */
    private void updateSolars(Level world) {
        BlockPos maxPos = getMaxPos();
        updateSolarSpot(world, maxPos, 0);
        updateSolarSpot(world, maxPos.west(3), 1);
        updateSolarSpot(world, maxPos.north(3), 2);
        updateSolarSpot(world, maxPos.offset(-3, 0, -3), 3);
    }


    /**
     * getMultiblockRedstoneLevel
     * - レッドストーン出力レベルを決める（タンクの充填率から計算）
     * - 使い道: ブロックが出力するアナログ信号の計算に利用される
     */
    @Override
    protected int getMultiblockRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getFluidAmount(), inputTank.getCapacity());
    }


    /**
     * remove
     * - マルチブロックが解体されるときに呼ばれる
     * - キャッシュしたソーラーパネル参照をクリアしてメモリリークを防ぐ
     */
    @Override
    public void remove(Level world) {
        //Clear the cached solar panels so that we don't hold references to them and prevent them from being able to be garbage collected
        cachedSolar.clear();
        super.remove(world);
    }


    /**
     * RefreshListener 内部クラス
     * - LazyOptional のリスナーとして登録され、能力の変化（たとえばブロック破壊）を検知する
     * - WeakReference によって親の EvaporationMultiblockData を参照し、親が GC 対象になれるようにする
     * - accept の実装では、対応角の位置がロードされていれば updateSolarSpot を呼んで再読み込みする
     */
    private static class RefreshListener implements NonNullConsumer<LazyOptional<IEvaporationSolar>> {

        // 親 multiblock への弱参照（リスナーが残っていても multiblock が解体されたら参照が切れる）
        private final WeakReference<EvaporationMultiblockData> multiblock;
        private final int corner;

        private RefreshListener(EvaporationMultiblockData multiblock, int corner) {
            this.multiblock = new WeakReference<>(multiblock);
            this.corner = corner;
        }

        @Override
        public void accept(@NotNull LazyOptional<IEvaporationSolar> ignored) {
            EvaporationMultiblockData multiblockData = multiblock.get();
            //Check to make sure the multiblock is still valid and that the position we are going to check is actually still loaded
            if (multiblockData != null && multiblockData.isFormed()) {
                BlockPos maxPos = multiblockData.getMaxPos();
                BlockPos pos = switch (corner) {
                    case 1 -> maxPos.west(3);
                    case 2 -> maxPos.north(3);
                    case 3 -> maxPos.offset(-3, 0, -3);
                    default -> maxPos;//Corner 0
                };
                if (WorldUtils.isBlockLoaded(multiblockData.getWorld(), pos)) {
                    //Refresh the solar
                    multiblockData.updateSolarSpot(multiblockData.getWorld(), pos, corner);
                }
            }
        }
    }
}
