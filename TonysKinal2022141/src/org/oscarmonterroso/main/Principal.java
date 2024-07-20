/*
Nombre: Oscar Alberto Monterroso Vásquez
Códico Técnico: IN5AV
Carné: 2022141
Fecha de Creación: 28/3/23
Fechas de Modificación: 
28/03/23
29/03/23
17/04/23
18/04/23
19/04/23
24/04/23
25/04/23
26/04/23
28/04/23
 */
package org.oscarmonterroso.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.oscarmonterroso.controller.EmpleadoController;
import org.oscarmonterroso.controller.EmpresaController;
import org.oscarmonterroso.controller.LoginController;
import org.oscarmonterroso.controller.MenuPrincipalController;
import org.oscarmonterroso.controller.PlatoController;
import org.oscarmonterroso.controller.PresupuestoController;
import org.oscarmonterroso.controller.ProductoController;
import org.oscarmonterroso.controller.ProductosHasPlatosController;
import org.oscarmonterroso.controller.ProgramadorController;
import org.oscarmonterroso.controller.ServicioController;
import org.oscarmonterroso.controller.ServiciosHasPlatosController;
import org.oscarmonterroso.controller.TipoEmpleadoController;
import org.oscarmonterroso.controller.TipoPlatoController;
import org.oscarmonterroso.controller.UsuarioController;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/oscarmonterroso/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Tony's kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/oscarmonterroso/image/menu.png"));
        /*Parent root = FXMLLoader.load(getClass().getResource("/org/oscarmonterroso/view/MenuPrincipalView.fxml"));
        Scene escena = new Scene(root);
        escenarioPrincipal.setScene(escena);*/
        ventanaLogin();
        escenarioPrincipal.show();
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 484, 483);
            menu.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController progra = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 590, 511);
            progra.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresaController = (EmpresaController) cambiarEscena("EmpresaView.fxml", 858, 497);
            empresaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoempleadoController = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 858, 497);
            tipoempleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProducto() {
        try {
            ProductoController productoController = (ProductoController) cambiarEscena("ProductoView.fxml", 858, 497);
            productoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoplatoController = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 858, 497);
            tipoplatoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuestoController = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 858, 497);
            presupuestoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPlato() {
        try {
            PlatoController platoController = (PlatoController) cambiarEscena("PlatoView.fxml", 858, 497);
            platoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadoController empleadoController = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 998, 504);
            empleadoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicio() {
        try {
            ServicioController servicioController = (ServicioController) cambiarEscena("ServicioView.fxml", 998, 504);
            servicioController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProductosHasPlatos() {
        try {
            ProductosHasPlatosController productosHasPlatosController = (ProductosHasPlatosController) cambiarEscena("ProductosHasPlatosView.fxml", 998, 504);
            productosHasPlatosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasPlatos() {
        try {
            ServiciosHasPlatosController serviciosHasPlatosController = (ServiciosHasPlatosController) cambiarEscena("ServiciosHasPlatosView.fxml", 998, 504);
            serviciosHasPlatosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaLogin() {
        try {
            LoginController loginController = (LoginController) cambiarEscena("LoginView.fxml", 555, 478);
            loginController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaUsuario() {
        try {
            UsuarioController usuarioController = (UsuarioController) cambiarEscena("UsuarioView.fxml", 858, 497);
            usuarioController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
