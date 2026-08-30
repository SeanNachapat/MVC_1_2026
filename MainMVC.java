import Controllers.ElectionController;
import Data.JsonService;
import Models.ElectionData;
import Models.ElectionService;
import Views.ElectionView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class MainMVC {
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {
        }

        ElectionView view = new ElectionView();
        JsonService jsonService = new JsonService();
        ElectionData initialData = jsonService.loadElectionData();

        ElectionService model;
        if (initialData != null) {
            model = new ElectionService(initialData);
            view.success("Initial data loaded successfully.");
        } else {
            model = new ElectionService();
            view.error("seed_data.json not found.");
        }

        ElectionController controller = new ElectionController(model, view);
        controller.start();
    }
}
