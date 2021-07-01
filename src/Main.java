import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;

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
        configuration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, configuration);

        bot.getEventChannel().subscribeAlways(MessageEvent.class, messageEvent -> {
            if (messageEvent.getMessage().contentToString().equalsIgnoreCase("测试")) {
                messageEvent.getSubject().sendMessage("测试成功");
            }
        });
        bot.login();
    }

}
