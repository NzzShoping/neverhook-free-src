package ru.neverhook.event.impl;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import ru.neverhook.event.callables.EventCancellable;

public class EventWorldLight extends EventCancellable {

    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;

    public EventWorldLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }

    public EnumSkyBlock getEnumSkyBlock() {
        return enumSkyBlock;
    }

    public BlockPos getPos() {
        return pos;
    }
}