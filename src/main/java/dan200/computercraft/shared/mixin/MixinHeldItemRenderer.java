/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2019. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.computercraft.shared.mixin;

import dan200.computercraft.client.render.ItemPocketRenderer;
import dan200.computercraft.client.render.ItemPrintoutRenderer;
import dan200.computercraft.shared.media.items.ItemPrintout;
import dan200.computercraft.shared.mixed.MixedFirstPersonRenderer;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Mixin (HeldItemRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinHeldItemRenderer implements MixedFirstPersonRenderer {
    @Override
    public void renderArmFirstPerson_CC(MatrixStack stack, VertexConsumerProvider consumerProvider, int light, float equip, float swing, Arm hand) {
        this.renderArmHoldingItem(stack, consumerProvider, light, equip, swing, hand);
    }

    @Shadow
    private void renderArmHoldingItem(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm) {
    }

    @Override
    public float getMapAngleFromPitch_CC(float pitch) {
        return this.getMapAngle(pitch);
    }

    @Shadow
    private float getMapAngle(float pitch) {
        return 0;
    }

    @Inject (method = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At ("HEAD"),
        cancellable = true)
    public void renderFirstPersonItem_Injected(AbstractClientPlayerEntity player, float var2, float pitch, Hand hand, float swingProgress,
                                               ItemStack stack, float equipProgress, MatrixStack matrixStack, VertexConsumerProvider provider, int light, CallbackInfo callback) {
        if (stack.getItem() instanceof ItemPrintout) {
            ItemPrintoutRenderer.INSTANCE.renderItemFirstPerson(matrixStack, provider, light, hand, pitch, equipProgress, swingProgress, stack);
            callback.cancel();
        } else if (stack.getItem() instanceof ItemPocketComputer) {
            ItemPocketRenderer.INSTANCE.renderItemFirstPerson(matrixStack, provider, light, hand, pitch, equipProgress, swingProgress, stack);
            callback.cancel();
        }
    }
}
