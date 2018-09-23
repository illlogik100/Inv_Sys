/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Comtroller;

import Model.InHousePart;
import Model.Inventory;
import static Model.Inventory.getPartInventory;
import Model.OutSourcedPart;
import Model.Part;
import static View_Comtroller.MainScreenController.partToModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rk
 */
public class ModifyInsourcedPartController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private RadioButton Radio_In_Mod_INHOUSE;

    @FXML
    private RadioButton Radio_Out_Mod_INHOUSE;

    @FXML
    private Button Cancel_Mod_INHOUSE;

    @FXML
    private Button Save_Mod_INHOUSE;

    @FXML
    private Label lblModifyPartIDNumber;

    @FXML
    private TextField ModifyPartNameTxt;

    @FXML
    private TextField ModifyPartInvTxt;

    @FXML
    private TextField ModifyPartPriceTxt;

    @FXML
    private TextField ModifyPartMinTxt;

    @FXML
    private TextField ModifyPartMaxTxt;

    @FXML
    private Label lblModifyPartDyn;

    @FXML
    private TextField ModifyPartDynTxt;

    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    void Radio_In_Mod_INHOUSE(ActionEvent event) {
        isOutsourced = false;
        Radio_In_Mod_INHOUSE.setSelected(false);
        lblModifyPartDyn.setText("Machine ID");
        ModifyPartDynTxt.setText("");
        ModifyPartDynTxt.setPromptText("Machine ID");
    }

    @FXML
    void Radio_Out_Mod_INHOUSE(ActionEvent event) {
        isOutsourced = true;
        Radio_Out_Mod_INHOUSE.setSelected(false);
        lblModifyPartDyn.setText("Company Name");
        ModifyPartDynTxt.setText("");
        ModifyPartDynTxt.setPromptText("Company Name");
    }

    @FXML
    void Save_Mod_INHOUSE(ActionEvent event) throws IOException {
        String partName = ModifyPartNameTxt.getText();
        String partInv = ModifyPartInvTxt.getText();
        String partPrice = ModifyPartPriceTxt.getText();
        String partMin = ModifyPartMinTxt.getText();
        String partMax = ModifyPartMaxTxt.getText();
        String partDyn = ModifyPartDynTxt.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutSourcedPart outPart = new OutSourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outPart);
                }

                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void Cancel_Mod_INHOUSE(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        lblModifyPartIDNumber.setText("Auto-Gen: " + partID);
        ModifyPartNameTxt.setText(part.getPartName());
        ModifyPartInvTxt.setText(Integer.toString(part.getPartInStock()));
        ModifyPartPriceTxt.setText(Double.toString(part.getPartPrice()));
        ModifyPartMinTxt.setText(Integer.toString(part.getPartMin()));
        ModifyPartMaxTxt.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InHousePart) {
            lblModifyPartDyn.setText("Machine ID");
            ModifyPartDynTxt.setText(Integer.toString(((InHousePart) getPartInventory().get(partIndex)).getMachineID()));
            Radio_In_Mod_INHOUSE.setSelected(true);
        } else {
            lblModifyPartDyn.setText("Company Name");
            ModifyPartDynTxt.setText(((OutSourcedPart) getPartInventory().get(partIndex)).getCompanyName());
            Radio_Out_Mod_INHOUSE.setSelected(true);
        }
    }

}
