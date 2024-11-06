package com.gregtechceu.gtceu.common.network.packets.hazard;

import com.gregtechceu.gtceu.client.EnvironmentalHazardClientHandler;
import com.gregtechceu.gtceu.common.capability.EnvironmentalHazardSavedData;
import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

public class SPacketAddHazardZone implements IPacket {
    private ChunkPos pos;
    private EnvironmentalHazardSavedData.HazardZone zone;

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeChunkPos(pos);
        zone.toNetwork(buf);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        pos = buf.readChunkPos();
        zone = EnvironmentalHazardSavedData.HazardZone.fromNetwork(buf);
    }

    @Override
    public void execute(IHandlerContext handler) {
        if (handler.isClient()) {
            EnvironmentalHazardClientHandler.INSTANCE.addHazardZone(pos, zone);
        }
    }

    public SPacketAddHazardZone() {
    }

    public SPacketAddHazardZone(final ChunkPos pos, final EnvironmentalHazardSavedData.HazardZone zone) {
        this.pos = pos;
        this.zone = zone;
    }
}
