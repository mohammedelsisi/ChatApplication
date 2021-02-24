package JETS.bot;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import java.io.File;
import java.nio.file.Path;

public class BotManager {
    private Chat chat;

    private void initBot() {
//        Bot bot = new Bot("LongTalk", Path.of(System.getProperty("user.dir")).resolve("src/main/resources/bot").toString());

        String botname = "Long Talk";
        String path = getResourcesPath();
        Bot bot = new Bot(botname, path);
        chat = new Chat(bot);
    }

    public String getResponse(String message) {
        if (chat == null) {
            initBot();
        }
        return chat.multisentenceRespond(message);
    }



    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath+"\\bot";
    }

}
