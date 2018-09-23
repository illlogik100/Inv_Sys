/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rk
 */
public class Inventory {
    
    
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    //observable list used as an array btof the listview
    public static ObservableList<Part> getPartInventory() {
        return parts;
    }
    
    public static ObservableList<Product> getProductInventory() {
        return products;
    }
    
    
    //add product to inventory
    public static void addProduct(Product name){
        products.add(name);
        
    }
    
    //code to remove product
    public static void removeProduct(Product product){
        products.remove(product);
        
    }
    
    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }
    
    //search function for products
    public static int lookUpProduct(String searchFor){
       {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchFor)) {
            for (int i = 0; i < products.size(); i++) {
                if (Integer.parseInt(searchFor) == products.get(i).getProductID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < products.size(); i++) {
                if (searchFor.equals(products.get(i).getProductName())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No products found.");
            return -1;
        }
    }
    }
    
    //update the product inventory
    public static void updateProduct(int index, Product product) {
        products.set(index, product);
    }
    
    //adds a part to the inventory
    public static void addPart(Part part){
        parts.add(part);
    }
        
        //deletes a part
    public static void deletePart(Part part) {
        parts.remove(part);
    }
    
    //search function for parts
    public static int lookupPart(String searchFor) {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchFor)) {
            for (int i = 0; i < parts.size(); i++) {
                if (Integer.parseInt(searchFor) == parts.get(i).getPartID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < parts.size(); i++) {
                searchFor = searchFor.toLowerCase();
                if (searchFor.equals(parts.get(i).getPartName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No parts found.");
            return -1;
        }
    }
    
    
        //update the part inventory
    public static void updatePart(int index, Part part) {
        parts.set(index, part);
    }

    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
        public static boolean validatePartDelete(Part part) {
        boolean isFound = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }

    public static boolean validateProductDelete(Product product) {
        boolean isFound = false;
        int productID = product.getProductID();
        for (int i=0; i < products.size(); i++) {
            if (products.get(i).getProductID() == productID) {
                if (!products.get(i).getProductParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }
    
}
