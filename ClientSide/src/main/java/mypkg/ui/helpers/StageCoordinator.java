package mypkg.ui.helpers;

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
                Scene mainScene = new Scene(main,650,520);
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


    public Parent getSignInScene() {
        if (primaryStage == null) {
            throw new RuntimeException("Stage Coordinator should be initialized with a Stage before it could be used");
        }
        if (!scenes.containsKey("signIn")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SignIn.fxml"));
                Parent signin = fxmlLoader.load();
                Scene signinScene = new Scene(signin);
                SceneData signInData = new SceneData(fxmlLoader, signin, signinScene);
                scenes.put("signIn", signInData);
                return signin;
            } catch (IOException e) {
                System.out.println("IO Exception: Couldn't load 'Login View' FXML file");
            }
        }
            SceneData loginSceneData = scenes.get("signIn");
            Parent signin = loginSceneData.getView();
            return signin;

    }
}
