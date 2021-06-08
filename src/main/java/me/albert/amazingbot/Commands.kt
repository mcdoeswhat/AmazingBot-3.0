package me.albert.amazingbot

import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import io.izzel.taboolib.module.command.base.SubCommand
import me.albert.amazingbot.AmazingBot
import me.albert.amazingbot.bot.Bot
import org.bukkit.command.CommandSender

@BaseCommand(name = "amazingbot", aliases = ["amb"])
class Commands : BaseMainCommand() {

    @SubCommand(description = "重载机器人", permission = "amb.op")
    fun reload(sender: CommandSender, args: Array<String>) {
        AmazingBot.getinstance().reloadConfig()
        AmazingBot.getData().reload()
        Bot.start()
        sender.sendMessage("§a所有配置文件已经重新载入!")
    }

}