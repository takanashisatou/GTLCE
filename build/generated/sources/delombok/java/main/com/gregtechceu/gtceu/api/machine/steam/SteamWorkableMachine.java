package com.gregtechceu.gtceu.api.machine.steam;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.IMufflableMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author KilaBash
 * @date 2023/3/14
 * @implNote SteamWorkableMachine
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SteamWorkableMachine extends SteamMachine implements IRecipeLogicMachine, IMufflableMachine, IMachineLife {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SteamWorkableMachine.class, SteamMachine.MANAGED_FIELD_HOLDER);
    @Nullable
    private ICleanroomProvider cleanroom;
    @Persisted
    @DescSynced
    public final RecipeLogic recipeLogic;
    public final GTRecipeType[] recipeTypes;
    public int activeRecipeType;
    @Persisted
    @DescSynced
    @RequireRerender
    protected Direction outputFacing;
    @Persisted
    @DescSynced
    protected boolean isMuffled;
    protected boolean previouslyMuffled = true;
    protected final Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy;
    protected final List<ISubscription> traitSubscriptions;

    public SteamWorkableMachine(IMachineBlockEntity holder, boolean isHighPressure, Object... args) {
        super(holder, isHighPressure, args);
        this.recipeTypes = getDefinition().getRecipeTypes();
        this.activeRecipeType = 0;
        this.recipeLogic = createRecipeLogic(args);
        this.capabilitiesProxy = Tables.newCustomTable(new EnumMap<>(IO.class), IdentityHashMap::new);
        this.traitSubscriptions = new ArrayList<>();
        this.outputFacing = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.UP;
    }

    //////////////////////////////////////
    // ***** Initialization *****//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for (MachineTrait trait : getTraits()) {
            if (trait instanceof IRecipeHandlerTrait<?> handlerTrait) {
                if (!capabilitiesProxy.contains(handlerTrait.getHandlerIO(), handlerTrait.getCapability())) {
                    capabilitiesProxy.put(handlerTrait.getHandlerIO(), handlerTrait.getCapability(), new ArrayList<>());
                }
                var handlers = capabilitiesProxy.get(handlerTrait.getHandlerIO(), handlerTrait.getCapability());
                if (handlers != null) handlers.add(handlerTrait);
                traitSubscriptions.add(handlerTrait.addChangedListener(recipeLogic::updateTickSubscription));
            }
        }
    }

    protected RecipeLogic createRecipeLogic(@SuppressWarnings("unused") Object... args) {
        return new RecipeLogic(this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        traitSubscriptions.forEach(ISubscription::unsubscribe);
        traitSubscriptions.clear();
        recipeLogic.inValid();
    }

    /**
     * @param outputFacing the facing to set
     */
    public void setOutputFacing(@NotNull Direction outputFacing) {
        if (!hasFrontFacing() || this.outputFacing != getFrontFacing()) {
            this.outputFacing = outputFacing;
        }
    }

    @Override
    protected InteractionResult onWrenchClick(Player playerIn, InteractionHand hand, Direction gridSide, BlockHitResult hitResult) {
        if (!playerIn.isShiftKeyDown() && !isRemote()) {
            if (hasFrontFacing() && gridSide == getFrontFacing()) return InteractionResult.PASS;
            setOutputFacing(gridSide);
            return InteractionResult.CONSUME;
        }
        return super.onWrenchClick(playerIn, hand, gridSide, hitResult);
    }

    @Override
    public boolean keepSubscribing() {
        return false;
    }

    @NotNull
    @Override
    public GTRecipeType getRecipeType() {
        return recipeTypes[activeRecipeType];
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (previouslyMuffled != isMuffled) {
            previouslyMuffled = isMuffled;
            if (recipeLogic != null) recipeLogic.updateSound();
        }
    }

    //////////////////////////////////////
    // ******* Rendering ********//
    //////////////////////////////////////
    @Override
    public ResourceTexture sideTips(Player player, BlockPos pos, BlockState state, Set<GTToolType> toolTypes, Direction side) {
        if (toolTypes.contains(GTToolType.WRENCH)) {
            if (!player.isShiftKeyDown()) {
                if (!hasFrontFacing() || side != getFrontFacing()) {
                    return GuiTextures.TOOL_IO_FACING_ROTATION;
                }
            }
        }
        return super.sideTips(player, pos, state, toolTypes, side);
    }

    @Nullable
    public ICleanroomProvider getCleanroom() {
        return this.cleanroom;
    }

    public void setCleanroom(@Nullable final ICleanroomProvider cleanroom) {
        this.cleanroom = cleanroom;
    }

    public RecipeLogic getRecipeLogic() {
        return this.recipeLogic;
    }

    public GTRecipeType[] getRecipeTypes() {
        return this.recipeTypes;
    }

    public int getActiveRecipeType() {
        return this.activeRecipeType;
    }

    public void setActiveRecipeType(final int activeRecipeType) {
        this.activeRecipeType = activeRecipeType;
    }

    public Direction getOutputFacing() {
        return this.outputFacing;
    }

    public boolean isMuffled() {
        return this.isMuffled;
    }

    public void setMuffled(final boolean isMuffled) {
        this.isMuffled = isMuffled;
    }

    public Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> getCapabilitiesProxy() {
        return this.capabilitiesProxy;
    }
}
