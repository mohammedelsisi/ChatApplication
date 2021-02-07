package JETS.ui.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainScene2.fxml"));
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainScene.fxml"));
                Parent SignUP = fxmlLoader.load();
                Scene SignUPScene = new Scene(SignUP,655,610);
                SceneData SignUPSceneData = new SceneData(fxmlLoader, SignUP, SignUPScene);
                scenes.put("SignUPScene", SignUPSceneData);
                primaryStage.setScene(SignUPScene);
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'SignUP View' FXML file");
            }
        } else {
            SceneData SignUPSceneData = scenes.get("SignUP");
            Scene SignUPScene = SignUPSceneData.getScene();
            primaryStage.setScene(SignUPScene);
        }

    }
}
