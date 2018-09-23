/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View_Comtroller;

import Model.Inventory;
import static Model.Inventory.deletePart;
import static Model.Inventory.getPartInventory;
import Model.Part;
import Model.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import java.io.IOException;
import java.util.Optional;

import static Model.Inventory.getProductInventory;
import static Model.Inventory.validatePartDelete;
import static Model.Inventory.validateProductDelete;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;





/**
 * FXML Controller class
 *
 * @author rk
 */
public class MainScreenController implements Initializable {
    
    private static Part modPart;
    private static int modPartIndex;
    private static Product modProduct;
    private static int modProductIndex;

    @FXML
    private AnchorPane MainScreen;

    @FXML
    private Button PartsSearchButton;
    
    @FXML
    private TextField searchPartsTxt;

    @FXML
    private TableView<Part> PartsTable;

    @FXML
    private TableColumn<Part, Integer> PartIDColumnPARTS;

    @FXML
    private TableColumn<Part, String> PartNMEColumnPARTS;

    @FXML
    private TableColumn<Part, Integer> InvLVLColumnPARTS;

    @FXML
    private TableColumn<Part, Double> Price_CstColumnPARTS;
    
    @FXML 
    private TextField partID;
    
    @FXML 
    private TextField partName;
    
    @FXML 
    private TextField InventoryLevel;
    
    @FXML 
    private TextField Price_Cost;

    @FXML
    private Button AddPartMainButton;

    @FXML
    private Button ModPartMainButton;

    @FXML
    private Button DelePartMainButton;
    
   
    ////Products 
    @FXML
    private TextField searchProductsTxt;

    @FXML
    private TableView<Product> ProductTable;
    
    @FXML
    private Button ProdSearchButton;

    @FXML
    private TableColumn<Product, Integer> ProductIDClmnProducts;

    @FXML
    private TableColumn<Product, String> PDCNmeClmnProds;

    @FXML
    private TableColumn<Product, Integer> InvLvLClmnProds;

    @FXML
    private TableColumn<Product, Double> PricePerClmnProds;

    @FXML
    private Button AddProdMainButton;

    @FXML
    private Button ModProdMainButton;

    @FXML
    private Button DeleProdMainButton;

    @FXML
    private Button ExitMainButton;




    
    public static int partToModifyIndex() {
        return modPartIndex;
    }

    public static int productToModifyIndex() {
        return modProductIndex;
    }
    
    
    
//     @FXML
//    void AddPartMainButton(ActionEvent event) {
//    //isOutsourced = false;
//    //DynAddPartLabel.setText("Machine ID");
//    Parent But_AddPartMainButton = FMXLLoader.load(getClass().getResource("AddInsourcedPart.fxml"));
//    
//    Scene Butt_Add_Part_Scene = new Scene(But_AddPartMainButton);
//    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//    
//    window.setScene(Butt_Add_Part_Scene);
//    window.show();
//    
//    }
//    
//    @FXML
//    void ModPartMainButton(ActionEvent event) {
//    //isOutsourced = false;
//    //DynAddPartLabel.setText("Machine ID");
//    Parent But_ModPartMainButton = FMXLLoader.load(getClass().getResource("ModifyInsourcedPart.fxml"));
//    
//    Scene Butt_Mod_Part_Scene = new Scene(But_ModPartMainButton);
//    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//    
//    window.setScene(Butt_Mod_Part_Scene);
//    window.show();
//    
//    }
    
    @FXML
    private void clearPartsSearch(ActionEvent event) {
        updatePartsTableView();
        searchPartsTxt.setText("");
    }

    @FXML
    private void PartsSearchButton(ActionEvent event) {
        String searchPart = searchPartsTxt.getText();
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
            PartsTable.setItems(tempPartList);
        }
    }

    @FXML
    private void DelePartMainButton(ActionEvent event) {
        Part part = PartsTable.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getPartName() + " has been deleted.");
            }
            else {
                System.out.println("Part " + part.getPartName() + " has not been deleted.");
            }
        }
    }

    @FXML
    private void AddPartMainButton(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddInsourcedPart.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }

    @FXML
    private void ModPartMainButton(ActionEvent event) throws IOException {
        modPart = PartsTable.getSelectionModel().getSelectedItem();
        modPartIndex = getPartInventory().indexOf(modPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyInsourcedPart.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyPartStage.setScene(modifyPartScene);
        modifyPartStage.show();
    }

    //// Products section
    @FXML
    private void clearProductsSearch(ActionEvent event) {
        updateProductsTableView();
        searchProductsTxt.setText("");
    }

    @FXML
    private void ProdSearchButton(ActionEvent event) {
        String searchProduct = searchProductsTxt.getText();
        int prodIndex = -1;
        if (Inventory.lookUpProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any known products.");
            alert.showAndWait();
        }
        else {
            prodIndex = Inventory.lookUpProduct(searchProduct);
            Product tempProduct = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            ProductTable.setItems(tempProductList);
        }
    }

    @FXML
    private void DeleProdMainButton(ActionEvent event) {
        Product product = ProductTable.getSelectionModel().getSelectedItem();
        if (validateProductDelete(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product cannot be deleted!");
            alert.setContentText("Product contains one or more parts.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Inventory.removeProduct(product);
                updateProductsTableView();
                System.out.println("Product " + product.getProductName() + " has been deleted.");
            } else {
                System.out.println("Product " + product.getProductName() + " has not been deleted.");
            }
        }
    }

    @FXML
    private void AddProdMainButton(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void ModProdMainButton(ActionEvent event) throws IOException {
        modProduct = ProductTable.getSelectionModel().getSelectedItem();
        modProductIndex = getProductInventory().indexOf(modProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
    }


    //// Update table views
    public void updatePartsTableView() {
        PartsTable.setItems(getPartInventory());
    }

    public void updateProductsTableView() {
        ProductTable.setItems(getProductInventory());
    }


    //// Confirm exit on Main screen
    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }


    /* public void setMainApp(Main mainApp) {
        updatePartsTableView();
        updateProductsTableView();
    } */


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PartIDColumnPARTS.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        PartNMEColumnPARTS.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        InvLVLColumnPARTS.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        Price_CstColumnPARTS.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        ProductIDClmnProducts.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        PDCNmeClmnProds.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        InvLvLClmnProds.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        PricePerClmnProds.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
    }
    

    
    
    
    
    
    
  
    

