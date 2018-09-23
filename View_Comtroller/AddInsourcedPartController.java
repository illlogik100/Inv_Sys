/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View_Comtroller;

import Model.InHousePart;
import Model.Inventory;
import Model.OutSourcedPart;
import Model.Part;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;






/**
 * FXML Controller class
 *
 * @author rk
 */
public class AddInsourcedPartController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private RadioButton Radio_In_INHOUSE_Add;

    @FXML
    private RadioButton Radio_Out_INHOUSE_Add;

    @FXML
    private Button Cancel_Button_INHOUSE_Add;

    @FXML
    private Button Save_Button_INHOUSE_add;
    
    @FXML
    private Label part_ID_lbl;
    
    @FXML 
    private TextField partID;
    
    @FXML
    private TextField AddCoNameTxt;

    @FXML
    private TextField AddPartNameTxt;

    @FXML
    private TextField AddInvTxt;

    @FXML
    private TextField AddPriceTxt;

    @FXML
    private TextField AddMaxTxt;

    @FXML
    private TextField AddMinTxt;

    @FXML
    private Label AddPartDynLbl;
    
    @FXML
    private TextField AddPartDynTxt;
    
    
    
    
    
    
    
    
    private boolean isOutsourced;
    private String exceptionMessage = new String();
    private int partIDnum;
    
    
    //Radio button for default of In-House part selection
    @FXML
    void Radio_In_INHOUSE_Add(ActionEvent event) {
        isOutsourced = false;
        AddPartDynLbl.setText("Machine ID");
        AddPartDynTxt.setPromptText("Machine ID");
        Radio_Out_INHOUSE_Add.setSelected(false);
    }

    @FXML
    void Radio_Out_INHOUSE_Add(ActionEvent event) {
        isOutsourced = true;
        AddPartDynLbl.setText("Company Name");
        AddPartDynTxt.setPromptText("Company Name");
        Radio_In_INHOUSE_Add.setSelected(false);
    }
    
    
    
        @FXML
    void Save_Button_INHOUSE_add(ActionEvent event) throws IOException {
        String partName = AddPartNameTxt.getText();
        String partInv = AddInvTxt.getText();
        String partPrice = AddPriceTxt.getText();
        String partMin = AddMinTxt.getText();
        String partMax = AddMaxTxt.getText();
        //used only for Modify Part
        String partDyn = AddPartDynTxt.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partIDnum);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setMachineID(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutSourcedPart outPart = new OutSourcedPart();
                    outPart.setPartID(partIDnum);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }

                Parent addPartSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void Cancel_Button_INHOUSE_Add(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        {
        partIDnum = Inventory.getPartIDCount();
        part_ID_lbl.setText("Auto-Gen: " + partIDnum);
    
        } 
    }
    
}
