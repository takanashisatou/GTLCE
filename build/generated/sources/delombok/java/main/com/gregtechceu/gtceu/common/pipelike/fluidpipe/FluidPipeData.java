package com.gregtechceu.gtceu.common.pipelike.fluidpipe;

import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.pipenet.IAttachData;
import net.minecraft.core.Direction;
import java.util.Objects;

/**
 * @author KilaBash
 * @date 2023/3/1
 * @implNote CableData
 */
public class FluidPipeData implements IAttachData {
    FluidPipeProperties properties;
    byte connections;

    public FluidPipeData(FluidPipeProperties properties, byte connections) {
        this.properties = properties;
        this.connections = connections;
    }

    @Override
    public boolean canAttachTo(Direction side) {
        return (connections & (1 << side.ordinal())) != 0;
    }

    @Override
    public boolean setAttached(Direction side, boolean attach) {
        var result = canAttachTo(side);
        if (result != attach) {
            if (attach) {
                connections |= (1 << side.ordinal());
            } else {
                connections &= ~(1 << side.ordinal());
            }
        }
        return result != attach;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FluidPipeData cableData) {
            return cableData.properties.equals(properties) && connections == cableData.connections;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties, connections);
    }

    public FluidPipeProperties properties() {
        return this.properties;
    }

    public byte connections() {
        return this.connections;
    }
}
