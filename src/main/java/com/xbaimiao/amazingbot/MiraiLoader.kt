package com.xbaimiao.amazingbot

import io.izzel.taboolib.loader.util.ILoader
import io.izzel.taboolib.loader.util.IO
import me.albert.amazingbot.AmazingBot
import java.io.File

object MiraiLoader {

    private val folder = "plugins${File.separator}TabooLib${File.separator}libs"

    private val libs = ArrayList<Lib>().also {
        val miraiVersion = "2.6.5"
        it.add(Lib("https://maven.aliyun.com/repository/public/net/mamoe/mirai-core-all/$miraiVersion/mirai-core-all-$miraiVersion-all.jar"))
        it.add(Lib("https://maven.aliyun.com/repository/public/org/bouncycastle/bcprov-jdk15on/1.64/bcprov-jdk15on-1.64.jar"))
    }

    @JvmStatic
    fun start() {
        for (lib in libs) {
            if (!lib.file.exists()) {
                AmazingBot.getInstance().plugin.logger.info("download ${lib.file.name} ...")
                IO.downloadFile(lib.url, lib.file)
                AmazingBot.getInstance().plugin.logger.info("download ${lib.file.name} success!")
            }
            ILoader.addPath(lib.file)
        }
    }

    class Lib(val url: String) {

        private val args = url.split("/")

        val file = File(folder, args[args.size - 1])

    }

}