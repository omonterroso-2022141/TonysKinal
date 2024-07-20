package org.oscarmonterroso.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.oscarmonterroso.bean.Empleado;
import org.oscarmonterroso.bean.Servicio;
import org.oscarmonterroso.bean.ServiciosHasEmpleados;
import org.oscarmonterroso.db.Conexion;
import org.oscarmonterroso.main.Principal;

public class ServiciosHasEmpleadosController implements Initializable {

    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasEmpleados> listaServiciosHasEmpleados;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;

    private enum operaciones {
        GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO
    };

    @FXML
    private TextField txtServiciosCodigoServicio;
    @FXML
    private TextField txtHoraEvento;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private ComboBox cmbCodigoEmpleado;
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
    private GridPane grpFecha;
    @FXML
    private TableView tblServiciosHasEmpleados;
    @FXML
    private TableColumn colServiciosCodigoServicio;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colCodigoServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/oscarmonterroso/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 2);
        cmbCodigoServicio.setItems(getServicios());
        cmbCodigoEmpleado.setItems(getEmpleado());

    }

    public void cargarDatos() {
        desactivarControles();
        tblServiciosHasEmpleados.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
        colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaServicio"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("tipoServicio"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("horaServicio"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("telefonoContacto"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpresa"));

    }

    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaServicio = FXCollections.observableList(lista);
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
                tipoDeOperacion = ServiciosHasEmpleadosController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                if (fecha.getSelectedDate() == null) {
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
                    tipoDeOperacion = ServiciosHasEmpleadosController.operaciones.NINGUNO;
                    imgEditar.setOpacity(1);
                    imgReporte.setOpacity(1);
                    cargarDatos();
                    break;
                }

        }
    }

    public void guardar() {
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        registro.setCodigoServicio(((Servicio) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado) cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Empleados(?,?,?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(2, registro.getHoraEvento());
            procedimiento.setString(3, registro.getLugarEvento());
            procedimiento.setInt(4, registro.getCodigoServicio());
            procedimiento.setInt(5, registro.getCodigoEmpleado());
            procedimiento.execute();
            listaServiciosHasEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void seleccionarElemento() {
        if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null) {
            txtServiciosCodigoServicio.setText(String.valueOf(((ServiciosHasEmpleados) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((ServiciosHasEmpleados) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            txtLugarEvento.setText(String.valueOf(((ServiciosHasEmpleados) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
            txtHoraEvento.setText(String.valueOf(((ServiciosHasEmpleados) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicio) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicios(((Servicio) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
                tipoDeOperacion = ServiciosHasEmpleadosController.operaciones.NINGUNO;
                imgEditar.setOpacity(1);
                imgReporte.setOpacity(1);
                break;
            default:
                if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta segur@ de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios(?)");
                            procedimiento.setInt(1, ((Servicio) tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServiciosHasEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServiciosHasEmpleados.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        try {
                            tblServiciosHasEmpleados.getSelectionModel().clearSelection();
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
                if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/oscarmonterroso/image/save.png"));
                    imgReporte.setImage(new Image("/org/oscarmonterroso/image/cancelar.png"));
                    activarControles();
                    tipoDeOperacion = ServiciosHasEmpleadosController.operaciones.ACTUALIZAR;
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
                tipoDeOperacion = ServiciosHasEmpleadosController.operaciones.NINGUNO;
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

    public Servicio buscarServicios(int codigoServicio) {
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

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleados(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

   public ObservableList<Servicio> getServicios() {
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

    public void desactivarControles() {
        fecha.setDisable(true);
        

    }

    public void activarControles() {
        fecha.setDisable(false);
        
    }

    public void limpiarControles() {
        
        fecha.setSelectedDate(null);
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuProncipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

}
