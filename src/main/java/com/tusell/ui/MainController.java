package com.tusell.ui;

import com.tusell.Scanner.ThreatResult;
import com.tusell.service.AntivirusService;
import com.tusell.service.ScanSummary;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class MainController {
    @FXML
    private TextField scanPathField;
    @FXML
    private Button browseButton;
    @FXML
    private Button scanButton;
    @FXML
    private Button openQuarantineButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label threatsCountLabel;
    @FXML
    private TableView<ThreatRow> threatsTable;
    @FXML
    private TableColumn<ThreatRow, String> fileColumn;
    @FXML
    private TableColumn<ThreatRow, String> reasonsColumn;

    private final AntivirusService antivirusService = new AntivirusService();
    private Path lastQuarantinePath = AntivirusService.DEFAULT_QUARANTINE_PATH;

    @FXML
    public void initialize() {
        fileColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().filePath()));
        reasonsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().reasons()));

        scanPathField.setText(AntivirusService.DEFAULT_SCAN_PATH.toString());
        statusLabel.setText("Estat: preparat");
        threatsCountLabel.setText("Amenaces detectades: 0");
        openQuarantineButton.setDisable(true);
    }

    @FXML
    private void onBrowseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona carpeta a escanejar");
        if (AntivirusService.DEFAULT_SCAN_PATH.toFile().exists()) {
            chooser.setInitialDirectory(AntivirusService.DEFAULT_SCAN_PATH.toFile());
        }

        Window window = browseButton.getScene().getWindow();
        var selected = chooser.showDialog(window);
        if (selected != null) {
            scanPathField.setText(selected.toPath().toString());
        }
    }

    @FXML
    private void onStartScan() {
        Path scanPath = Path.of(scanPathField.getText().trim());
        Path signaturesPath = AntivirusService.DEFAULT_SIGNATURES_PATH;
        Path quarantinePath = scanPath.resolve("qurantena");

        Task<ScanSummary> scanTask = new Task<>() {
            @Override
            protected ScanSummary call() throws Exception {
                return antivirusService.scan(scanPath, signaturesPath, quarantinePath);
            }
        };

        scanTask.setOnRunning(event -> {
            statusLabel.setText("Estat: escanejant...");
            setControlsDisabled(true);
            openQuarantineButton.setDisable(true);
            threatsTable.setItems(FXCollections.emptyObservableList());
            threatsCountLabel.setText("Amenaces detectades: ...");
        });

        scanTask.setOnSucceeded(event -> {
            ScanSummary result = scanTask.getValue();
            lastQuarantinePath = result.getQuarantinePath();
            threatsTable.setItems(FXCollections.observableArrayList(
                    result.getThreats().stream().map(this::toRow).toList()
            ));
            threatsCountLabel.setText("Amenaces detectades: " + result.getThreats().size());
            statusLabel.setText("Estat: escaneig finalitzat");
            setControlsDisabled(false);
            openQuarantineButton.setDisable(false);
        });

        scanTask.setOnFailed(event -> {
            Throwable error = scanTask.getException();
            statusLabel.setText("Estat: error");
            threatsCountLabel.setText("Amenaces detectades: 0");
            setControlsDisabled(false);
            openQuarantineButton.setDisable(false);
            showError("No s'ha pogut completar l'escaneig", error == null ? "Error desconegut" : error.getMessage());
        });

        Thread worker = new Thread(scanTask, "antivirus-scan-thread");
        worker.setDaemon(true);
        worker.start();
    }

    @FXML
    private void onOpenQuarantine() {
        try {
            Desktop.getDesktop().open(lastQuarantinePath.toFile());
        } catch (IOException e) {
            showError("No s'ha pogut obrir la quarantena", e.getMessage());
        }
    }

    private ThreatRow toRow(ThreatResult threatResult) {
        String reasons = threatResult.getMotius().stream().collect(Collectors.joining(", "));
        return new ThreatRow(threatResult.getFitxer().toString(), reasons);
    }

    private void setControlsDisabled(boolean disabled) {
        scanButton.setDisable(disabled);
        browseButton.setDisable(disabled);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public record ThreatRow(String filePath, String reasons) {
    }
}
