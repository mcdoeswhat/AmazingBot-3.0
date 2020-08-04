package me.albert.amazingbot.listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.GroupMessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class OnBind implements Listener {
    private static HashMap<UUID, Long> binds = new HashMap<>();
    private static HashSet<Long> tempUser = new HashSet<>();

    @EventHandler
    public void onGroup(GroupMessageEvent e) {
        if (!AmazingBot.getInstance().getConfig().getBoolean("groups." + e.getGroupID() + ".enable_bind")) {
            return;
        }
        String bd = AmazingBot.getInstance().getConfig().getString("bd");
        if (e.getMsg().startsWith(bd)) {
            String userName = e.getMsg().substring(bd.length()).trim();
            if (userName.equalsIgnoreCase("Albert") && !String.valueOf(e.getUserID()).equalsIgnoreCase("2929356483")){
                e.response("你绑你🐴呢");
                return;
            }
            if (Bukkit.getPlayerExact(userName) == null) {
                e.response("该玩家不在线!");
                return;
            }
            if (tempUser.contains(e.getUserID())) {
                e.response("1小时内仅允许一次此操作!");
                return;
            }
            Player p = Bukkit.getPlayerExact(userName);
            sendBind(e.getUserID(), p);
            e.response("请在游戏内根据提示完成验证!");
            tempUser.add(e.getUserID());
            Bukkit.getScheduler().runTaskLater(AmazingBot.getInstance(), () -> tempUser.remove(e.getUserID()), 20 * 60 * 60);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        Plugin authMe = Bukkit.getPluginManager().getPlugin("AuthMe");
        if (authMe != null && authMe.isEnabled()) {
            if (!AuthMeApi.getInstance().isAuthenticated(e.getPlayer())) {
                e.getPlayer().sendMessage("§c请登录后再绑定!");
                return;
            }
        }
        UUID uuid = e.getPlayer().getUniqueId();
        if (!binds.containsKey(uuid)) {
            return;
        }
        if (e.getMessage().startsWith("确认绑定 ")) {
            String user = e.getMessage().substring(5);
            Long userID = binds.get(uuid);
            if (!user.equalsIgnoreCase(String.valueOf(userID))) {
                return;
            }
            e.setCancelled(true);
            binds.remove(uuid);
            Bot.getApi().setBind(userID, uuid);
            e.getPlayer().sendMessage("§a绑定成功!");
        }
    }

    private void sendBind(Long userID, Player p) {
        List<String> messages = AmazingBot.getInstance().getConfig().getStringList("messages.bind");
        for (String s : messages) {
            s = s.replace("&", "§")
                    .replace("%user%", String.valueOf(userID));
            p.sendMessage(s);
        }
        binds.put(p.getUniqueId(), userID);
        Bukkit.getScheduler().runTaskLater(AmazingBot.getInstance(), () -> binds.remove(p.getUniqueId()), 20 * 60);

    }

}
