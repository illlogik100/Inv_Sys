/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

/**
 *
 * @author rk
 */
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutSourcedPart extends Part  {
    
    String companyName;
    private final StringProperty partCompanyName;
    
    public OutSourcedPart() {
        super();
        partCompanyName = new SimpleStringProperty();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    
    
}
