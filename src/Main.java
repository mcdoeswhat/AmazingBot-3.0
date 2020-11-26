import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入机器人QQ号码");
        long qq = Long.parseLong(scanner.next());
        System.out.println("请输入机器人QQ密码");
        String password = scanner.next();
        BotConfiguration configuration = new BotConfiguration() {
            {
                fileBasedDeviceInfo("deviceInfo.json");
            }
        };
        Bot bot = BotFactoryJvm.newBot(qq, password, configuration);
        bot.loginAsync();
        Events.registerEvents(bot, new SimpleListenerHost() {
            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                if (event.getMessage().contentToString().equalsIgnoreCase("测试")) {
                    event.getGroup().sendMessage("测试成功");
                }
                return ListeningStatus.LISTENING;
            }

            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });
        bot.join();
    }
}
