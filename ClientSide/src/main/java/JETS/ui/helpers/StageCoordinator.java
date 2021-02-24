package JETS.ui.helpers;

import JETS.ClientServices.ClientServicesFactory;
import JETS.net.ClientProxy;
import JETS.ui.controllers.ChatController;
import Models.ChatEntitiy;
import Models.CurrentUser;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StageCoordinator {

    private static final StageCoordinator stageCoordinator = new StageCoordinator();
    private static Stage primaryStage;
    private final Map<String, SceneData> scenes = new HashMap<>();

    private StageCoordinator() {

    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }

    public Map<String, SceneData> getScenes() {
        return scenes;
    }

    public void initStage(Stage stage) {
        if (primaryStage != null) {
            throw new IllegalArgumentException("The Stage Already been initialized");
        }
        primaryStage = stage;

        stage.setOnCloseRequest((e) -> {
            if (ModelsFactory.getInstance().getCurrentUser() != null) {
                try {

                    ClientProxy.getInstance().disconnect(ClientServicesFactory.getClientServicesImp());
                } catch (RuntimeException s) {

                }
            }
        });
        //if the user closes the application, call the method that keeps him logged in.(basiony)
    }

    public void switchToLoginScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("MainScene")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/LoginView01.fxml"));
                Parent main = fxmlLoader.load();
                Scene mainScene = new Scene(main, 655, 610);
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
                Scene SignUPScene = new Scene(SignUP, 655, 610);
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

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/HomeScreen.fxml"));
            Parent Chat = fxmlLoader.load();
            Scene ChatScene = new Scene(Chat, 655, 610);
            SceneData ChatSceneData = new SceneData(fxmlLoader, Chat, ChatScene);
            scenes.put("Chat", ChatSceneData);
            primaryStage.setScene(ChatScene);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception: Couldn't load 'Chat View' FXML file");
        }

    }

    public HBox createChatLayout(ChatEntitiy chatEntitiy) {
        List<String> participants = chatEntitiy.getParticipantsPhoneNumbers()
                .stream().filter((e) -> !e.equals(ModelsFactory.getInstance().getCurrentUser().getPhoneNumber()))
                .collect(Collectors.toList());
        StringBuilder receiver = new StringBuilder(FriendsManager.getInstance().getFriendName(participants.get(0)));

        for (int i = 1; i < participants.size(); i++) {
            receiver.append(", ");
            receiver.append(FriendsManager.getInstance().getFriendName(participants.get(i)));
        }
        Label name = new Label(receiver.toString());
        Circle circle = new Circle(25);
        if (participants.size() == 1) {

            circle.setFill(new ImagePattern(new Image(new ByteArrayInputStream(FriendsManager.getInstance().getFriendPhoto(participants.get(0))))));
        } else {
            try (FileInputStream file = new FileInputStream("groupIcon.jpg")) {
                circle.setFill(new ImagePattern(new Image(new ByteArrayInputStream(file.readAllBytes()))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        HBox hBox = new HBox(circle, name);
        return hBox;

    }

}


