/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2020. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.network.container;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An extension over the basic
 * hooks, with a more convenient way of reading and writing data.
 */
public interface ContainerData
{
    void toBytes( PacketByteBuf buf );

    default void open( PlayerEntity player, NamedScreenHandlerFactory owner )
    {
        player.openHandledScreen(owner);
    }

    static <C extends ScreenHandler, T extends ContainerData> ScreenHandlerType<C> toType(Identifier identifier, Function<PacketByteBuf, T> reader, Factory<C, T> factory )
    {
        return ScreenHandlerRegistry.registerExtended(identifier, (id, playerInventory, packetByteBuf) -> factory.create(id, playerInventory, reader.apply(packetByteBuf)));
    }

    interface Factory<C extends ScreenHandler, T extends ContainerData>
    {
        C create( int id, @Nonnull PlayerInventory inventory, T data );
    }
}
