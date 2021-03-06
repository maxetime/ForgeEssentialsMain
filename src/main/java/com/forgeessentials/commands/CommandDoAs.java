package com.forgeessentials.commands;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.commands.util.FEcmdModuleCommands;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.UserIdent;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandDoAs extends FEcmdModuleCommands {
    @Override
    public String getCommandName()
    {
        return "doas";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        StringBuilder cmd = new StringBuilder(args.toString().length());
        for (int i = 1; i < args.length; i++)
        {
            cmd.append(args[i]);
            cmd.append(" ");
        }
        EntityPlayerMP player = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
        if (player != null)
        {
            OutputHandler.chatWarning(player, String.format("Player %s is attempting to issue a command as you.", sender.getCommandSenderName()));
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, cmd.toString());
            OutputHandler.chatConfirmation(sender, String.format("Successfully issued command as %s", args[0]));
        }
        else
        {
            OutputHandler.chatError(sender, String.format("Player %s does not exist, or is not online.", args[0]));
        }
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
        }
        else
        {
            return null;
        }
    }

    @Override
    public RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.OP;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {

        return "/doas <player> <command> Run a command as another player.";
    }
}
