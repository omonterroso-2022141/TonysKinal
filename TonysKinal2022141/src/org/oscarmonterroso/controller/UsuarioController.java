package org.oscarmonterroso.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import  javafx.scene.image.Image;
import org.oscarmonterroso.bean.Usuario;
import org.oscarmonterroso.db.Conexion;
import org.oscarmonterroso.main.Principal;

public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        NINGUNO, GUARDAR
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private TextField txtCodigoUsuario;
    @FXML
    private TextField txtNombreUsuario;
    @FXML
    private TextField txtApellidoUsuario;
    @FXML
    private TextField txtUsuarioLogin;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;

    public void desactivarControles() {
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuarioLogin.setEditable(false);
        txtPassword.setEditable(false);

    }

    public void activarControles() {
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuarioLogin.setEditable(true);
        txtPassword.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuarioLogin.clear();
        txtPassword.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void nuevo() {
        activarControles();
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/oscarmonterroso/image/save.png"));
                imgEliminar.setImage(new Image("/org/oscarmonterroso/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                imgNuevo.setImage(new Image("/org/oscarmonterroso/image/nuevo.png"));
                imgEliminar.setImage(new Image("/org/oscarmonterroso/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }

    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuarioLogin.getText());
        registro.setContrasena(txtPassword.getText());
       
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
      public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
      
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
      
    

}
