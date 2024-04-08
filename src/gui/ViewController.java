package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ViewController implements Initializable {
	
	@FXML
	private ComboBox<String> methodComboBox;
	
	@FXML
	private TextField httpRequestTextField;
	
	@FXML
	private Button sendRequestButton;
	
	@FXML
	private Button copyAllButton;
	
	@FXML
	private TextArea resultTextArea;
	
	@FXML
	private TextArea paramsTextArea;
	
	@FXML
    private Label lineCountLabel;
	
	@FXML
	private TabPane tabResponseParams;
	
	@FXML
	public void onSendRequestButtonAction() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String method = methodComboBox.getValue();
		
		if (method == null) { 
			Alerts.showAlert("No method selected", null, "No HTTP method was selected.", AlertType.WARNING);
		}
		
		if (httpRequestTextField.getText() == null) {
			Alerts.showAlert("No URL", null, "No URL was passed.", AlertType.WARNING);
		}
		
		try {
			URL url = new URL(httpRequestTextField.getText());
			
			switch (method) {
			case "GET":
				try {
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						content.append(inputLine);
					}
					JsonElement je = JsonParser.parseString(content.toString());
					resultTextArea.setText(gson.toJson(je));
					in.close();
				} catch (IOException e) {
					Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
				}
				break;
			
			case "POST":
				try {
					String params = paramsTextArea.getText();
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json");
					con.setDoOutput(true);

					try (java.io.OutputStream os = con.getOutputStream()) {
			            byte[] input = params.getBytes("utf-8");
			            os.write(input, 0, input.length);
			        }
					
					StringBuilder answer = new StringBuilder();;
					try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							answer.append(inputLine);
						}
					} catch (Exception e) {
			        	Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			        }
					
					JsonElement je = JsonParser.parseString(answer.toString());
					resultTextArea.setText(gson.toJson(je));
					
				} catch (IOException e) {
					Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
				}
				break;
			
			case "PUT":
				try {
					String params = paramsTextArea.getText();
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("PUT");
					con.setRequestProperty("Content-Type", "application/json");
					con.setDoOutput(true);
					
					try (java.io.OutputStream os = con.getOutputStream()) {
			            byte[] input = params.getBytes("utf-8");
			            os.write(input, 0, input.length);
			        } catch (Exception e) {
			        	Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			        }
					
					StringBuilder answer = new StringBuilder();
					try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							answer.append(inputLine);
						}
					}
					
					JsonElement je = JsonParser.parseString(answer.toString());
					resultTextArea.setText(gson.toJson(je));
					
				} catch (IOException e) {
					Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
				}
				break;
			
			case "DELETE":
				try {
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("DELETE");
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						content.append(inputLine);
					}
					JsonElement je = JsonParser.parseString(content.toString());
					resultTextArea.setText(gson.toJson(je));
					in.close();
				} catch (IOException e) {
					Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
				}
				
				break;
			}
			tabResponseParams.getSelectionModel().select(0);
		} catch (MalformedURLException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateStyles();
		methodComboBox.setItems(FXCollections.observableArrayList("GET", "POST", "PUT", "DELETE"));
		
		resultTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updateLineCount(newValue);
        });
	
		paramsTextArea.setText("{\n    \"key\": \"value\",\n}");
	}
	
	@FXML
	private void onCopyAllButtonAction() {
		int index = tabResponseParams.getSelectionModel().getSelectedIndex();
		String result = "";
		if (index == 0) {
			result = resultTextArea.getText();
		} else if (index == 1) {
			result = paramsTextArea.getText();
		}
		
		if (result != "") {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(result), null);
			Alerts.showAlert("Text copied", null, "All text was successfully copied to the clipboard.", AlertType.INFORMATION);
		} else {
			Alerts.showAlert("Nothing to copy", null, "There is no content to copy.", AlertType.WARNING);
		}
		
	}
	
	private void updateLineCount(String text) {
        String[] lines = text.split("\n");
        int lineCount = lines.length;
        lineCountLabel.setText("Lines: " + lineCount);
    }
	
	private void updateStyles() {
		final String HOVERED_BUTTON_STYLE = "-fx-background-color: #0854bc; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;";
		final String IDLE_BUTTON_STYLE = "-fx-background-color: #0864d4; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;";
		final String HOVERED_SMALL_BUTTON_STYLE = HOVERED_BUTTON_STYLE.replace("-fx-font-size: 16px", "-fx-font-size: 12px");
		final String IDLE_SMALL_BUTTON_STYLE = IDLE_BUTTON_STYLE.replace("-fx-font-size: 16px", "-fx-font-size: 12px");
		
		
		sendRequestButton.setStyle(IDLE_BUTTON_STYLE);
		sendRequestButton.setOnMouseEntered(e -> sendRequestButton.setStyle(HOVERED_BUTTON_STYLE));
		sendRequestButton.setOnMouseExited(e -> sendRequestButton.setStyle(IDLE_BUTTON_STYLE));
		copyAllButton.setStyle(IDLE_SMALL_BUTTON_STYLE);
		copyAllButton.setOnMouseEntered(e -> copyAllButton.setStyle(HOVERED_SMALL_BUTTON_STYLE));
		copyAllButton.setOnMouseExited(e -> copyAllButton.setStyle(IDLE_SMALL_BUTTON_STYLE));
		tabResponseParams.setTabMinHeight(35);
		tabResponseParams.setTabMinWidth(60);
	}
}
