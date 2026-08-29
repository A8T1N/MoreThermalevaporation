package morethermalevaporation.common.content.blocktype;

import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import morethermalevaporation.common.registries.MoreThermalEvaporationBlocks;
import morethermalevaporation.common.tier.MoreThermalEvaporationTier;

import java.util.function.Supplier;

public class MoreThermalEvaporationMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {

    public MoreThermalEvaporationMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description, MoreThermalEvaporationTier tier) {
        super(tileEntityRegistrar, description);
        if (tier.ordinal() < MoreThermalEvaporationTier.values().length - 1) {
            add(new AttributeUpgradeable(() -> MoreThermalEvaporationBlocks.COMPACTS.get(MoreThermalEvaporationTier.values()[tier.ordinal() + 1])));
        }
    }

    public static class MoreThermalEvaporationMachineBuilder<MACHINE extends MoreThermalEvaporationMachine<TILE>, TILE extends TileEntityMekanism, T extends MoreThermalEvaporationMachine.MoreThermalEvaporationMachineBuilder<MACHINE, TILE, T>> extends BlockTypeTile.BlockTileBuilder<MACHINE, TILE, T> {

        protected MoreThermalEvaporationMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> MoreThermalEvaporationMachine.MoreThermalEvaporationMachineBuilder<MoreThermalEvaporationMachine<TILE>, TILE, ?> createMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description, MoreThermalEvaporationTier tier) {
            return new MoreThermalEvaporationMachine.MoreThermalEvaporationMachineBuilder<>(new MoreThermalEvaporationMachine<>(tileEntityRegistrar, description, tier));
        }
    }
}
