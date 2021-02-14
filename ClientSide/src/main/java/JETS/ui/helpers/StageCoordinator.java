package JETS.ui.helpers;

import Models.ChatEntitiy;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StageCoordinator {

    private static Stage primaryStage;
    private static final StageCoordinator stageCoordinator = new StageCoordinator();
    private final Map<String, SceneData> scenes = new HashMap<>();

    private StageCoordinator() {
    }

    public void initStage(Stage stage) {
        if (primaryStage != null) {
            throw new IllegalArgumentException("The Stage Already been initialized");
        }
        primaryStage = stage;
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }

    public void switchToLoginScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("MainScene")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoginView01.fxml"));
                Parent main = fxmlLoader.load();
                Scene mainScene = new Scene(main,655,610);
                SceneData MainSceneData = new SceneData(fxmlLoader, main, mainScene);
                scenes.put("MainScene", MainSceneData);
                primaryStage.setScene(mainScene);
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'Login View' FXML file");
            }
        } else {
            SceneData loginSceneData = scenes.get("MainScene");
            Scene loginScene = loginSceneData.getScene();
            primaryStage.setScene(loginScene);
        }

    }


    public void switchToSignUPScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("SignUP")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/regView.fxml"));
                Parent SignUP = fxmlLoader.load();
                Scene SignUPScene = new Scene(SignUP,655,610);
                SceneData SignUPSceneData = new SceneData(fxmlLoader, SignUP, SignUPScene);
                scenes.put("SignUPScene", SignUPSceneData);
                primaryStage.setScene(SignUPScene);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO Exception: Couldn't load 'SignUP View' FXML file");
            }
        } else {
            SceneData SignUPSceneData = scenes.get("SignUP");
            Scene SignUPScene = SignUPSceneData.getScene();
            primaryStage.setScene(SignUPScene);
        }
    }

    public void switchToChatScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("Chat")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/HomeScreen.fxml"));
                Parent Chat = fxmlLoader.load();
                Scene ChatScene = new Scene(Chat,655,610);
                SceneData ChatSceneData = new SceneData(fxmlLoader, Chat, ChatScene);
                scenes.put("ChatScene", ChatSceneData);
                primaryStage.setScene(ChatScene);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO Exception: Couldn't load 'Chat View' FXML file");
            }
        } else {
            SceneData ChatSceneData = scenes.get("Chat");
            Scene ChatScene = ChatSceneData.getScene();
            primaryStage.setScene(ChatScene);
        }
    }

    public  HBox createChatLayout(ChatEntitiy chatEntitiy)  {
        if(chatEntitiy.getParticipantsPhoneNumbers().size()==2) {


            List<String> participants = chatEntitiy.getParticipantsPhoneNumbers()
                    .stream().filter((e) -> !e.equals(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()))
                    .collect(Collectors.toList());
            String friendPhone = participants.get(0);
            Label name = new Label(FriendsManager.getInstance().getFriendName(friendPhone));
            Circle circle = new Circle(25);
            circle.setFill(new ImagePattern(new Image(new ByteArrayInputStream(FriendsManager.getInstance().getFriendPhoto(friendPhone)))));
            HBox hBox = new HBox(circle, name);
            return hBox;
        }else{
            return null;
        }

    }

}
