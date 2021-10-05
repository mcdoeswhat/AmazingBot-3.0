package me.albert.amazingbot.utils;

import me.albert.amazingbot.AmazingBot;
import me.albert.amazingbot.events.GroupMessageEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class ConsoleSender implements ConsoleCommandSender {
    private BukkitTask task = null;
    private final Server server;
    private final GroupMessageEvent e;
    private final ArrayList<String> output = new ArrayList<>();
    private final ArrayList<String> tempOutPut = new ArrayList<>();
    private final ConsoleSender instance;

    public ConsoleSender(Server server, GroupMessageEvent e) {
        this.server = server;
        this.e = e;
        instance = this;
    }

    private Optional<ConsoleCommandSender> get() {
        return Optional.ofNullable(this.server.getConsoleSender());
    }


    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        if (task != null) {
            task.cancel();
        }
        synchronized (tempOutPut) {
            tempOutPut.add(message);
        }
        task = Bukkit.getScheduler().runTaskLaterAsynchronously(AmazingBot.getInstance(), () -> {
            synchronized (output) {
                synchronized (tempOutPut) {
                    output.addAll(tempOutPut);
                    tempOutPut.clear();
                }
                StringBuilder response = new StringBuilder();
                for (String s : output) {
                    response.append(s.replaceAll("§\\S", "")).append("\n");
                }
                String msg = response.toString();
                if (!msg.isEmpty()) {
                    e.response(msg);
                    output.clear();
                }
            }
        }, 4L);
    }


    @Override
    public void sendMessage(String[] messages) {
        for (String msg : messages) {
            sendMessage(msg);
        }
    }

    @Override
    public boolean isPermissionSet(String s) {
        return get().map(c -> c.isPermissionSet(s)).orElse(true);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return get().map(c -> c.isPermissionSet(permission)).orElse(true);
    }

    @Override
    public boolean hasPermission(String s) {
        return get().map(c -> c.hasPermission(s)).orElse(true);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return get().map(c -> c.hasPermission(permission)).orElse(true);
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spigot spigot() {
        return new Spigot() {
            public void sendMessage(BaseComponent component) {
                instance.sendMessage(component.toPlainText());
            }

            public void sendMessage(BaseComponent... components) {
                for (BaseComponent baseComponent : components) {
                    sendMessage(baseComponent);
                }
            }
        };
    }

    @Override
    public boolean isConversing() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void acceptConversationInput(String s) {
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent conversationAbandonedEvent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendRawMessage(String s) {
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }
}
