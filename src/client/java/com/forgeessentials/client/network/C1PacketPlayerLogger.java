package com.forgeessentials.client.network;

import com.forgeessentials.client.ForgeEssentialsClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class C1PacketPlayerLogger implements IMessageHandler<C1PacketPlayerLogger.Message, IMessage> {

    @Override public IMessage onMessage(C1PacketPlayerLogger.Message message, MessageContext ctx)
    {
        return null;
    }

    public static class Message implements IMessage
    {
        private EntityPlayer player;

        public Message()
        {
        }

        public Message(EntityPlayer player)
        {
            this.player = player;
        }

        @Override public void fromBytes(ByteBuf buf)
        {
            ForgeEssentialsClient.getInfo().playerLogger = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf)
        {

        }
    }
}
