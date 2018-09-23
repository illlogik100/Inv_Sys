/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View_Comtroller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

/**
 * FXML Controller class
 *
 * @author rk
 */
public class ModifyOutSourcedPartController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private RadioButton Radio_In_Add_Outsourced;

    @FXML
    private RadioButton Radio_Out_Add_OutSourced;

    @FXML
    private Button Cancel_Button_Add_Outsourced;

    @FXML
    private Button Save_Button_Add_Outsourced;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
