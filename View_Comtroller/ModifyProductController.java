/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View_Comtroller;

import Model.Inventory;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;
import Model.Part;
import Model.Product;
import static View_Comtroller.MainScreenController.productToModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rk
 */
public class ModifyProductController implements Initializable {
    @FXML
    private Button Mod_Prod_Search;
    
    @FXML
    private TableView<Part> Mod_Prod_TabV_Add;
    
    @FXML
    private TableColumn<Part, Integer> Mod_Prod_Add_Pid_COL;
    
    @FXML
    private TableColumn<Part, String> Mod_Prod_Add_PNME_COL;
    
    @FXML
    private TableColumn<Part, Integer> Mod_Prod_Add_InvLVL_COL;
    
    @FXML
    private TableColumn<Part, Double> Mod_Prod_Add_PPU_COL;
    
    @FXML
    private TableView<Part> Mod_Prod_TabV_Delete;
    
    @FXML
    private TableColumn<Part, Integer> Mod_Prod_Del_Pid_COL;
    
    @FXML
    private TableColumn<Part, String> Mod_Prod_Del_PNME_COL;
    
    @FXML
    private TableColumn<Part, Integer> Mod_Prod_Del_InvLVL_COL;
    
    @FXML
    private TableColumn<Part, Double> Mod_Prod_Del_PPU_COL;
    
    @FXML
    private Button Mod_Prod_Add;
    
    @FXML
    private Button Mod_Prod_Delete;
    
    @FXML
    private Button Mod_Prod_Cancel;
    
    @FXML
    private Button Mod_Prod_Save;
    
    @FXML
    private Label ModProdIDlbl;
    
    @FXML
    private TextField modProdNameTxt;
    
    @FXML
    private TextField modProdInvTxt;
    
    @FXML
    private TextField modProdPriceTxt;
    
    @FXML
    private TextField modProdMinTxt;
    
    @FXML
    private TextField modProdMaxTxt;
    
    @FXML
    private TextField modProdSearchTxt;

    
    
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;



    /**
     * Initializes the controller class.
     */
    
    
    
    @FXML
    void handleClearSearch(ActionEvent event) {
        updateAddPartsTableView();
        modProdSearchTxt.setText("");
    }

    @FXML
    void Mod_Prod_Search(ActionEvent event) {
        String searchPart = modProdSearchTxt.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            Mod_Prod_TabV_Add.setItems(tempPartList);
        }
    }

    @FXML
    void Mod_Prod_Add(ActionEvent event) {
        Part part = Mod_Prod_TabV_Add.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTableView();
    }

    @FXML
    void Mod_Prod_Delete(ActionEvent event) {
        Part part = Mod_Prod_TabV_Delete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm");
        alert.setContentText("Delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        }
        else {
            System.out.println("Cancel");
        }
    }

    @FXML
    private void Mod_Prod_Save(ActionEvent event) throws IOException {
        String productName = modProdNameTxt.getText();
        String productInv = modProdInvTxt.getText();
        String productPrice = modProdPriceTxt.getText();
        String productMin = modProdMinTxt.getText();
        String productMax = modProdMaxTxt.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setProductInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void Mod_Prod_Cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
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
        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();
        ModProdIDlbl.setText("Auto-Gen: " + productID);
        modProdNameTxt.setText(product.getProductName());
        modProdInvTxt.setText(Integer.toString(product.getProductInStock()));
        modProdPriceTxt.setText(Double.toString(product.getProductPrice()));
        modProdMinTxt.setText(Integer.toString(product.getProductMin()));
        modProdMaxTxt.setText(Integer.toString(product.getProductMax()));
        currentParts = product.getProductParts();
        Mod_Prod_Add_Pid_COL.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        Mod_Prod_Add_PNME_COL.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        Mod_Prod_Add_InvLVL_COL.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        Mod_Prod_Add_PPU_COL.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        Mod_Prod_Del_Pid_COL.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        Mod_Prod_Del_PNME_COL.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        Mod_Prod_Del_InvLVL_COL.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        Mod_Prod_Del_PPU_COL.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
    }

    public void updateAddPartsTableView() {
        Mod_Prod_TabV_Add.setItems(getPartInventory());
    }

    public void updateDeletePartsTableView() {
        Mod_Prod_TabV_Delete.setItems(currentParts);
    }    
    
}
