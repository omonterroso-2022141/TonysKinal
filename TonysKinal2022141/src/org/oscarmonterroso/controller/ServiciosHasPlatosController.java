package org.oscarmonterroso.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.oscarmonterroso.bean.Plato;
import org.oscarmonterroso.bean.Servicio;
import org.oscarmonterroso.bean.ServiciosHasPlatos;
import org.oscarmonterroso.db.Conexion;
import org.oscarmonterroso.main.Principal;

public class ServiciosHasPlatosController implements Initializable {
    
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasPlatos> listaProductosHasPlatos;
    private ObservableList<Plato> listaPlato;
    private ObservableList<Servicio> listaServicio;
    

    @FXML
    private TextField txtServiciosCodigoServicio;
    @FXML
    private ComboBox cmbCodigoPlato;
    @FXML
    private ComboBox cmbCodigoServicio;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TableView tblServiciosHasPlatos;
    @FXML
    private TableColumn colServiciosCodigoServicio;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoPlato.setItems(getPlato());
    }
    
    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    };
    
    public void cargarDatos() {
        desactivarControles();
        tblServiciosHasPlatos.setItems(getProductosHasPlatos());
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));

    }

    public ObservableList<ServiciosHasPlatos> getProductosHasPlatos() {
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosHasPlatos(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaProductosHasPlatos = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/oscarmonterroso/image/save.png"));
                imgEliminar.setImage(new Image("/org/oscarmonterroso/image/cancelar.png"));
                imgEditar.setOpacity(0.45);
                imgReporte.setOpacity(0.45);
                tipoDeOperacion = ServiciosHasPlatosController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (cmbCodigoPlato.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Tiene que llenar todos los campos");
                } else {
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/oscarmonterroso/image/nuevo.png"));
                    imgEliminar.setImage(new Image("/org/oscarmonterroso/image/eliminar.png"));
                    tipoDeOperacion = ServiciosHasPlatosController.operaciones.NINGUNO;
                    imgEditar.setOpacity(1);
                    imgReporte.setOpacity(1);
                    cargarDatos();
                    break;
                }

        }
    }

    public void guardar() {
        ServiciosHasPlatos registro = new ServiciosHasPlatos();
        registro.setCodigoPlato(((Plato) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Platos(?,?)");
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.execute();
            listaProductosHasPlatos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seleccionarElemento() {
        if (tblServiciosHasPlatos.getSelectionModel().getSelectedItem() != null) {
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        } else {
            JOptionPane.showMessageDialog(null, "No ha seleccionado una fila");
        }

    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/oscarmonterroso/image/nuevo.png"));
                imgEliminar.setImage(new Image("/org/oscarmonterroso/image/eliminar.png"));
                tipoDeOperacion = ServiciosHasPlatosController.operaciones.NINGUNO;
                imgEditar.setOpacity(1);
                imgReporte.setOpacity(1);
                break;
            default:
                if (tblServiciosHasPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta segur@ de eliminar el registro?", "Eliminar Has", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios_has_Platos(?)");
                            procedimiento.setInt(1, ((ServiciosHasPlatos) tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaProductosHasPlatos.remove(tblServiciosHasPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServiciosHasPlatos.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        try {
                            tblServiciosHasPlatos.getSelectionModel().clearSelection();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServiciosHasPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/oscarmonterroso/image/save.png"));
                    imgReporte.setImage(new Image("/org/oscarmonterroso/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = ServiciosHasPlatosController.operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/oscarmonterroso/image/editar.png"));
                imgReporte.setImage(new Image("/org/oscarmonterroso/image/reporte.png"));
                cargarDatos();
                tipoDeOperacion = ServiciosHasPlatosController.operaciones.NINGUNO;
                btnNuevo.setOpacity(1);
                btnEliminar.setOpacity(1);
                break;

        }
    }

    public void actualizar() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Servicio buscarServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicios(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getString("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                         resultado.getString("tipoServicio"),
                         resultado.getString("horaServicio"),
                         resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlatos(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public ObservableList<Plato> getPlato() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public void desactivarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoPlato.setEditable(false);

    }

    public void activarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoServicio.setEditable(false);
        cmbCodigoPlato.setEditable(false);
    }

    public void limpiarControles() {
        txtServiciosCodigoServicio.clear();
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoPlato.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}
