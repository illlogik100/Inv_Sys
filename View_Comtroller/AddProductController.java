/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View_Comtroller;

/**
 * FXML Controller class
 *
 * @author rk
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import Model.Inventory;
import static Model.Inventory.getPartInventory;
import Model.Part;
import Model.Product;

public class AddProductController implements Initializable {
    @FXML
    private Button Add_Prod_Search;
    
    @FXML
    private TableView<Part> Add_Prod_TabV_Add;
    
    @FXML
    private TableColumn<Part, Integer> Add_Prod_Add_Pid_COL;
    
    @FXML
    private TableColumn<Part, String> Add_Prod_Add_PNME_COL;
    
    @FXML
    private TableColumn<Part, Integer> Add_Prod_Add_InvLVL_COL;
    
    @FXML
    private TableColumn<Part, Double> Add_Prod_Add_PPU_COL;
    
    @FXML
    private TableView<Part> Add_Prod_TabV_Delete;
    
    @FXML
    private TableColumn<Part, Integer> Add_Prod_Delete_Pid_COL;
    
    @FXML
    private TableColumn<Part, String> Add_Prod_Del_PNME_COL;
    
    @FXML
    private TableColumn<Part, Integer> Add_Prod_Del_InvLVL_COL;
    
    @FXML
    private TableColumn<Part, Double> Add_Prod_Del_PPU_COL;
    
    @FXML
    private Button Add_Prod_Add;
    
    @FXML
    private Button Add_Prod_Delete;
    
    @FXML
    private Button Add_Prod_Cancel;
    
    @FXML
    private Button Add_Prod_Save;
    
    @FXML
    private Label lblAddProductIDNumber;
    
    @FXML
    private TextField txtAddProductName;
    
    @FXML
    private TextField txtAddProductInv;
    
    @FXML
    private TextField txtAddProductPrice;
    
    @FXML
    private TextField txtAddProductMin;
    
    @FXML
    private TextField txtAddProductMax;
    
    @FXML
    private TextField txtAddProductSearch;
    

    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();
    private int productID;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    void Add_Prod_Search(ActionEvent event) {
        String searchPart = txtAddProductSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == partIndex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            Add_Prod_TabV_Add.setItems(tempPartList);
        }
    }

    @FXML
    void Add_Prod_Add(ActionEvent event) {
        Part part = Add_Prod_TabV_Add.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        deletePartTableView();
    }

    @FXML
    void Add_Prod_Delete(ActionEvent event) {
        Part part = Add_Prod_TabV_Delete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            currentParts.remove(part);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @FXML
    void Add_Prod_Save(ActionEvent event) throws IOException {
        String productName = txtAddProductName.getText();
        String productInv = txtAddProductInv.getText();
        String productPrice = txtAddProductPrice.getText();
        String productMin = txtAddProductMin.getText();
        String productMax = txtAddProductMax.getText();

        try{
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productMin), Integer.parseInt(productMax), Integer.parseInt(productInv), Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
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
                Inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void Add_Prod_Cancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel adding a new product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Add_Prod_Add_Pid_COL.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        Add_Prod_Add_PNME_COL.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        Add_Prod_Add_InvLVL_COL.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        Add_Prod_Add_PPU_COL.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        Add_Prod_Delete_Pid_COL.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        Add_Prod_Del_PNME_COL.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        Add_Prod_Del_InvLVL_COL.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        Add_Prod_Del_PPU_COL.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        addPartTableView();
        deletePartTableView();
        productID = Inventory.getProductIDCount();
        lblAddProductIDNumber.setText("Auto-Gen: " + productID);
    }

    public void addPartTableView() {
        Add_Prod_TabV_Add.setItems(getPartInventory());
    }

    public void deletePartTableView() {
        Add_Prod_TabV_Delete.setItems(currentParts);
    }
   
    
}
