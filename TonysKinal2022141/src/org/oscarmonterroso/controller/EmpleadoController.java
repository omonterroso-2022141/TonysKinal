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
import org.oscarmonterroso.bean.Empleado;
import org.oscarmonterroso.bean.TipoEmpleado;
import org.oscarmonterroso.db.Conexion;
import org.oscarmonterroso.main.Principal;

public class EmpleadoController implements Initializable {

    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    };

    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNumeroEmpleado;
    @FXML
    private TextField txtApellidosEmpleado;
    @FXML
    private TextField txtNombresEmpleado;
    @FXML
    private TextField txtDireccionEmpleado;
    @FXML
    private TextField txtTelefonoContacto;
    @FXML
    private TextField txtGradoCocinero;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private ComboBox cmbCodigoTipoEmpleado;
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
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colNumeroEmpleados;
    @FXML
    private TableColumn colApellidosEmpleados;
    @FXML
    private TableColumn colNombresEmpleados;
    @FXML
    private TableColumn colDireccionEmpleados;
    @FXML
    private TableColumn colTelefonoContacto;
    @FXML
    private TableColumn colGradoCocinero;
    @FXML
    private TableColumn colCodigoTipoEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
    }

    public void cargarDatos() {
        desactivarControles();
        tblEmpleados.setItems(getEmpleado());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroEmpleados.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosEmpleados.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("apellidosEmpleado"));
        colNombresEmpleados.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("nombresEmpleado"));
        colDireccionEmpleados.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));

    }

    public ObservableList<Empleado> getEmpleado() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaEmpleado = FXCollections.observableArrayList(lista);
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
                tipoDeOperacion = EmpleadoController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (txtNumeroEmpleado.getText().isEmpty() || txtApellidosEmpleado.getText().isEmpty() || txtNombresEmpleado.getText().isEmpty() || txtDireccionEmpleado.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() || txtGradoCocinero.getText().isEmpty() || cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null) {
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
                    tipoDeOperacion = EmpleadoController.operaciones.NINGUNO;
                    imgEditar.setOpacity(1);
                    imgReporte.setOpacity(1);
                    cargarDatos();
                    break;
                }

        }
    }

    public void guardar() {
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
        registro.setNombresEmpleado(txtNombresEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado) cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleados(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seleccionarElemento() {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtCodigoEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            txtNumeroEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtApellidosEmpleado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtNombresEmpleado.setText((((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
            txtDireccionEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
            txtTelefonoContacto.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
            txtGradoCocinero.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero()));
            cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
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
                tipoDeOperacion = EmpleadoController.operaciones.NINGUNO;
                imgEditar.setOpacity(1);
                imgReporte.setOpacity(1);
                break;
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta segur@ de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleados(?)");
                            procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblEmpleados.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        try {
                            tblEmpleados.getSelectionModel().clearSelection();
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/oscarmonterroso/image/save.png"));
                    imgReporte.setImage(new Image("/org/oscarmonterroso/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = EmpleadoController.operaciones.ACTUALIZAR;
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
                tipoDeOperacion = EmpleadoController.operaciones.NINGUNO;
                btnNuevo.setOpacity(1);
                btnEliminar.setOpacity(1);
                break;

        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleados(?,?,?,?,?,?,?,?)");
            Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidosEmpleado.getText());
            registro.setNombresEmpleado(txtNombresEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado) {
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                        registro.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public void desactivarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidosEmpleado.setEditable(false);
        txtNombresEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setEditable(false);

    }

    public void activarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidosEmpleado.setEditable(true);
        txtNombresEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setEditable(false);
    }

    public void limpiarControles() {
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidosEmpleado.clear();
        txtNombresEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoContacto.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.getSelectionModel().clearSelection();
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

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

}
