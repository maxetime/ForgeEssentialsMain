package com.forgeessentials.playerlogger.rollback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.forgeessentials.playerlogger.ModulePlayerLogger;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.selections.Point;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent()
    public void playerInteractEvent(PlayerInteractEvent e)
    {
        /*
         * Lb info!
		 */

        if (e.entityPlayer.getEntityData().getBoolean("lb"))
        {
            e.setCanceled(true);
            try
            {
                int limit = e.entityPlayer.getEntityData().getInteger("lb_limit");
                Date date = new Date();
                new Timestamp(date.getTime());
                Connection connection = DriverManager.getConnection(ModulePlayerLogger.url, ModulePlayerLogger.username, ModulePlayerLogger.password);
                Statement st = connection.createStatement();
                Point p = getPoint(e.entityPlayer);
                st.execute(
                        "SELECT * FROM  `blockChange` WHERE  `Dim` = " + e.entityPlayer.dimension + " AND  `X` = " + p.getX() + " AND  `Y` = " + p.getY() + " AND  `Z` = "
                                + p.getZ() + " ORDER BY id DESC LIMIT " + limit);
                ResultSet res = st.getResultSet();

                OutputHandler.chatNotification(e.entityPlayer, "Results: " + p.getX() + ", " + p.getY() + ", " + p.getZ());

                while (res.next())
                {
                    OutputHandler.chatNotification(e.entityPlayer,
                            res.getString("player") + " " + res.getString("category") + " block " + res.getString("block") + " at " + res.getTimestamp("time"));
                }
                res.close();
                st.close();
                connection.close();
            }
            catch (SQLException e1)
            {
                OutputHandler.chatError(e.entityPlayer, "Connection error!");
                e1.printStackTrace();
            }
        }
    }

    private Point getPoint(EntityPlayer player)
    {
        float var4 = 1.0F;
        float var5 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * var4;
        float var6 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * var4;
        double var7 = player.prevPosX + (player.posX - player.prevPosX) * var4;
        double var9 = player.prevPosY + (player.posY - player.prevPosY) * var4 + 1.62D - player.yOffset;
        double var11 = player.prevPosZ + (player.posZ - player.prevPosZ) * var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 500D;
        Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);

        MovingObjectPosition mo = player.worldObj.rayTraceBlocks(var13, var23);

        if (mo == null)
        {
            return null;
        }

        Point p = new Point(mo.blockX, mo.blockY, mo.blockZ);

        if (!player.isSneaking())
        {
            return p;
        }

        switch (mo.sideHit)
        {
        case 0:
        	p.setY(p.getY() - 1);
            break;
        case 1:
        	p.setY(p.getY() + 1);
            break;
        case 2:
        	p.setZ(p.getZ() - 1);
            break;
        case 3:
        	p.setZ(p.getZ() + 1);
            break;
        case 4:
        	p.setX(p.getX() - 1);
            break;
        case 5:
        	p.setX(p.getX() + 1);
            break;
        }

        return p;
    }
}
