package JETS.ui.helpers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StageCoordinator {

    private static Stage primaryStage;
    private static final StageCoordinator stageCoordinator = new StageCoordinator();

    public Map<String, SceneData> getScenes() {
        return scenes;
    }

    private final Map<String, SceneData> scenes = new HashMap<>();

    private StageCoordinator() {
    }

    public void initStage(Stage stage) {
        if (primaryStage != null) {
            throw new IllegalArgumentException("The Stage Already been initialized");
        }
        primaryStage = stage;
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/Pics/logo.png").toString()));
        primaryStage.setTitle("ServerSide");
    }

    public static StageCoordinator getInstance() {
        return stageCoordinator;
    }

    public void switchToMainScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }

        if (!scenes.containsKey("MainScene")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Server.fxml"));
                Parent main = fxmlLoader.load();
                Scene mainScene = new Scene(main,655,610);
                SceneData MainSceneData = new SceneData(fxmlLoader, main, mainScene);
                scenes.put("MainScene", MainSceneData);
                primaryStage.setScene(mainScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            SceneData loginSceneData = scenes.get("MainScene");
            Scene loginScene = loginSceneData.getScene();
            primaryStage.setScene(loginScene);
        }

    }



}
