package JETS.bot;

//import org.alicebot.ab.Bot;
//import org.alicebot.ab.Chat;
//
//import java.nio.file.Path;
//
//public class BotManager {
//    private Chat chat;
//
//    private void initBot() {
//        Bot bot = new Bot("LongTalk", Path.of(System.getProperty("user.dir")).resolve("src/main/resources/bot").toString());
//        chat = new Chat(bot);
//    }
//
//    public String getResponse(String message) {
//        if (chat == null) {
//            initBot();
//        }
//        return chat.multisentenceRespond(message);
//    }
//}
