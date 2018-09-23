/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rk
 */
public abstract class Part {
    
    IntegerProperty partID;
    StringProperty name;
    DoubleProperty price;
    IntegerProperty inStock;
    IntegerProperty max;
    IntegerProperty min;
    

    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        price = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    
    }
    
    

    public int getPartID() {
        return this.partID.get();
    }

    public void setPartID(int partID) {
        this.partID.set(partID);
    }

    public String getPartName() {
        return this.name.get();
    }

    public void setPartName(String name) {
        this.name.set(name);
    }

    public double getPartPrice() {
        return this.price.get();
    }

    public void setPartPrice(double price) {
        this.price.set(price);
    }

    public int getPartInStock() {
        return this.inStock.get();
    }

    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }

    public int getPartMax() {
        return this.max.get();
    }

    public void setPartMax(int max) {
        this.max.set(max);
    }

    public int getPartMin() {
        return this.min.get();
    }

    public void setPartMin(int min) {
        this.min.set(min);
    }
    
    
    
        //Properties
    public IntegerProperty partIDProperty() {
        return partID;
    }

    public StringProperty partNameProperty() {
        return name;
    }

    public DoubleProperty partPriceProperty() {
        return price;
    }

    public IntegerProperty partInvProperty() {
        return inStock;
    }

    public IntegerProperty partMinProperty() {
        return min;
    }

    public IntegerProperty partMaxProperty() {
        return max;
    }
    
    
    
    // A method to use as a check to see if the values inputed are valid.
    public static String isPartValid(String name, int min, int max, int inv, double price, String errorMessage){
        if (name == null) {
            errorMessage = errorMessage + "The name field is required. ";
        }
        if (inv < 1) {
            errorMessage = errorMessage + "The inventory count cannot be less than 1. ";
        }
        if (price <= 0) {
            errorMessage = errorMessage + "The price must be greater than $0. ";
        }
        if (max < min) {
            errorMessage = errorMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + "The inventory must be between the Min and Max values. ";
        }
        return errorMessage;
    }
    
    
}
