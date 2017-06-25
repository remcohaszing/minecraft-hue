package com.haszing.hue.commands;

import com.haszing.hue.actions.HueAction;
import com.haszing.hue.actions.Pair;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class HueCommand extends CommandBase {
    private List<HueAction> actions = new ArrayList<HueAction>();

    public HueCommand() {
        super();
        this.actions.add(new Pair());
    }

    @Override
    public String getCommandName() {
        return "hue";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        String usage = "HueCommand with a Philips Hue bridge.";
        for (HueAction a : this.actions) {
            usage += "\n" + a.getName();
        }
        return usage;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if (args.length == 0) {
            sendErrorMessage(player);
            return;
        }
        for (HueAction action : this.actions) {
            if (args[0].equals(action.getName())) {
                action.setPlayer(player);
                action.run(args);
                return;
            }
        }
        sendErrorMessage(player);
    }

    private void sendErrorMessage(EntityPlayerMP player) {
        TextComponentString msg = new TextComponentString("this is not a command");
        player.addChatComponentMessage(msg, false);
    }
}
