Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresa(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa(codigoEmpresa)

);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)

);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)

);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProductos varchar(150) not null,
    cantidadProducto int not null,
    primary key PK_codigoProducto (codigoProducto)

);

Create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key (codigoTipoEmpleado) 
		references TipoEmpleado(codigoTipoEmpleado)

);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa) 
		references Empresa(codigoEmpresa)
    

);

Create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresa(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)

);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null auto_increment,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto)
		references Productos(codigoProducto)

);

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null auto_increment,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)

);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null auto_increment,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
	constraint Fk_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado)
		references Empleados(codigoEmpleado)

);

Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null,
    contrasena varchar(50) not null,
    primary key PK_codigoUsuario (codigoUsuario)

);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster (usuarioMaster)
);



-- --------------------------------------------Procedimientos Almacenados ----------------------------------------------
-- ------------------Empresa--------------------------
-- --Agregar Empresa--------------------------
Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150),direccion varchar(150),telefono varchar(8))
    begin
		Insert into Empresa(nombreEmpresa,direccion,telefono)
			values (nombreEmpresa,direccion,telefono);
    End $$
Delimiter ;

call sp_AgregarEmpresa('Tonys Kinal', '17 avenida 11-12','42568652');
call sp_AgregarEmpresa('Pepsi', '16 avenida 5-13','12345678');
call sp_AgregarEmpresa('Coca cola', '17 avenida 1-15','87654321');
call sp_AgregarEmpresa('Kinal', '14 avenida 12-14','13578246');
call sp_AgregarEmpresa('CasaMedica', '11 avenida 17-2','21325356');
call sp_AgregarEmpresa('Holandesa', '20 avenida 13-22','64564234');
call sp_AgregarEmpresa('Patsy', '21 avenida 12-12','25676585');
call sp_AgregarEmpresa('McDonals', '12 avenida 19-13','86656873');
call sp_AgregarEmpresa('KFC', '13 avenida 11-18','12135436');

-- --Listar Empresa----------------------------
Delimiter $$
	Create procedure sp_ListarEmpresas()
    begin
    select 
    E.codigoEmpresa,
    E.nombreEmpresa,
    E.direccion,
    E.telefono
    from Empresa E;
    
    End$$
Delimiter ;

call sp_ListarEmpresas();


-- --Editar Empresa ---------------------------

Delimiter $$
	Create procedure sp_EditarEmpresa(in codigoEmp int, in nombreEmp varchar(150), in direc varchar(150), in tel varchar(8))
    begin 
    Update Empresa E
    set 
    E.nombreEmpresa = nombreEmp,
    E.direccion = direc,
    E.telefono = tel
    where E.codigoEmpresa = codigoEmp;
    
    End $$

Delimiter ;

call sp_EditarEmpresa(1,'Tonys kinal', '17 avenida 18-18','42568653');

-- --Buscar Empresa -------------------------

Delimiter $$
	Create procedure sp_BuscarEmpresa (in codigoEmp int)
    begin
    Select 
    E.codigoEmpresa,
    E.nombreEmpresa,
    E.direccion,
    E.telefono
    from Empresa E
    where codigoEmpresa = codigoEmp;
    End $$
Delimiter ;

call sp_BuscarEmpresa(1);

-- --Eliminar Empresa -------------------------
Delimiter $$
	Create procedure sp_EliminarEmpresa (in codigoEmp int)
    begin
    Delete from Empresa
    where codigoEmpresa = codigoEmp;
    end$$
Delimiter ;

-- ------------------TipoEmpleado--------------------------
-- --Agregar TipoEmpleado--------------------------
Delimiter  $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
    Begin
		Insert into TipoEmpleado(descripcion)
			values(descripcion);
    End $$
Delimiter ;

call sp_AgregarTipoEmpleado('Chef');

-- --Listar TipoEmpleado ---------------------------
Delimiter $$
	Create procedure sp_ListarTipoEmpleado()
    Begin
    Select
    TE.codigoTipoEmpleado,
    TE.descripcion
    from TipoEmpleado TE;
    End$$
Delimiter ;

call sp_ListarTipoEmpleado();

-- --Editar TipoEmpleado --------------------------
Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codigoTipoEm int,in descrip varchar(50))
    begin
    Update TipoEmpleado TE
    set 
    TE.descripcion = descrip
    where TE.codigoTipoEmpleado = codigoTipoEm;
    End$$
Delimiter ;

call sp_EditarTipoEmpleado(1,'Cheff');

-- --Buscar TipoEmpleado -------------------------

Delimiter $$
	Create procedure sp_BuscarTipoEmpleado (in codigoTipoEm int)
    begin
    Select 
    TE.codigoTipoEmpleado,
    TE.descripcion
    from TipoEmpleado TE
    where codigoTipoEmpleado = codigoTipoEm;
    End $$
Delimiter ;

call sp_BuscarTipoEmpleado(1);

-- --Eliminar TipoEmpleado -------------------------
Delimiter $$
	Create procedure sp_EliminarTipoEmpleado (in codigoTipoEm int)
    begin
    Delete from TipoEmpleado
    where codigoTipoEmpleado = codigoTipoEm;
    end$$
Delimiter ;

-- ------------------TipoPlato--------------------------
-- --Agregar TipoPlato--------------------------
Delimiter  $$
	Create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(50))
    Begin
		Insert into TipoPlato(descripcionTipo)
			values(descripcionTipo);
    End $$
Delimiter ;

call sp_AgregarTipoPlato('Premium');

-- --Listar TipoPlato ---------------------------
Delimiter $$
	Create procedure sp_ListarTipoPlato()
    Begin
    Select
    TP.codigoTipoPlato,
    TP.descripcionTipo
    from TipoPlato TP;
    End$$
Delimiter ;

call sp_ListarTipoPlato();

-- --Editar TipoPlato --------------------------
Delimiter $$
	Create procedure sp_EditarTipoPlato(in codigoTipoPl int,in descripT varchar(50))
    begin
    Update TipoPlato TP
    set 
    TP.descripcionTipo = descripT
    where TP.codigoTipoPlato = codigoTipoPl;
    End$$
Delimiter ;

call sp_EditarTipoPlato(1,'Basico');

-- --Buscar TipoPlato -------------------------

Delimiter $$
	Create procedure sp_BuscarTipoPlato (in codigoTipoPl int)
    begin
    Select 
    TP.codigoTipoPlato,
    TP.descripcionTipo
    from TipoPlato TP
    where codigoTipoPlato = codigoTipoPl;
    End $$
Delimiter ;

call sp_BuscarTipoPlato(1);

-- --Eliminar TipoPlato -------------------------
Delimiter $$
	Create procedure sp_EliminarTipoPlato (in codigoTipoPl int)
    begin
    Delete from TipoPlato
    where codigoTipoPlato = codigoTipoPl;
    end$$
Delimiter ;

-- ------------------Productos--------------------------
-- --Agregar Productos--------------------------
Delimiter  $$
	Create procedure sp_AgregarProductos(in nombreProductos varchar(150), in cantidadProducto int)
    Begin
		Insert into Productos(nombreProductos,cantidadProducto)
			values(nombreProductos,cantidadProducto);
    End $$
Delimiter ;

call sp_AgregarProductos('Carne Asada','2');

-- --Listar Productos ---------------------------
Delimiter $$
	Create procedure sp_ListarProductos()
    Begin
    Select
    P.codigoProducto,
    P.nombreProductos,
    P.cantidadProducto
    from Productos P;
    End$$
Delimiter ;

call sp_ListarProductos();

-- --Editar Productos --------------------------
Delimiter $$
	Create procedure sp_EditarProductos(in codigoPro int,in nombrePro varchar(150),canti int)
    begin
    Update Productos P
    set 
    P.nombreProductos = nombrePro,
    P.cantidadProducto = canti
    where P.codigoProducto = codigoPro;
    End$$
Delimiter ;

call sp_EditarProductos(1,'Pan', 3);

-- --Buscar Productos -------------------------

Delimiter $$
	Create procedure sp_BuscarProductos (in codigoPro int)
    begin
    Select 
    P.codigoProducto,
    P.nombreProductos,
    P.cantidadProducto
    from Productos P
    where codigoProducto = codigoPro;
    End $$
Delimiter ;

call sp_BuscarProductos(1);

-- --Eliminar Productos -------------------------
Delimiter $$
	Create procedure sp_EliminarProductos(in codigoPro int)
    begin
    Delete from Productos
    where codigoProducto = codigoPro;
    end$$
Delimiter ;


-- ------------------Empleados--------------------------
-- --Agregar Empleados--------------------------
Delimiter  $$
	Create procedure sp_AgregarEmpleados(in numeroEmpleado int, in apellidosEmpleado varchar(150), in nombresEmpleado varchar(150),
								in direccionEmpleado varchar(150),in telefonoContacto varchar(8),in gradoCocinero varchar(50),
                                in codigoTipoEmpleado int)
    Begin
		Insert into Empleados(numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,telefonoContacto,gradoCocinero,codigoTipoEmpleado)
			values(numeroEmpleado,apellidosEmpleado,nombresEmpleado,direccionEmpleado,telefonoContacto,gradoCocinero,codigoTipoEmpleado);
    End $$
Delimiter ;

call sp_AgregarEmpleados('2','Monterroso Vasquez','Oscar Alberto','17 avenida zona 20','59159599','PrimerCheff','1');

-- --Listar Empleados ---------------------------
Delimiter $$
	Create procedure sp_ListarEmpleados()
    Begin
    Select
    EM.codigoEmpleado,
    EM.numeroEmpleado,
    EM.apellidosEmpleado,
    EM.nombresEmpleado,
    EM.direccionEmpleado,
    EM.telefonoContacto,
    EM.gradoCocinero,
    EM.codigoTipoEmpleado
    from Empleados EM;
    End$$
Delimiter ;

call sp_ListarEmpleados();

-- --Editar Empleados --------------------------
Delimiter $$
	Create procedure sp_EditarEmpleados(in codigoEm int,in numeroEm int, apellidosEm varchar(150),nombresEm varchar(150),
										in direccionEm varchar(150),in telefonoContac varchar(8),in gradoCoci varchar(50),
                                        in codigoTipoEm int)
    begin
    Update Empleados EM
    set 
    EM.numeroEmpleado = numeroEm,
    EM.apellidosEmpleado = apellidosEm,
    EM.nombresEmpleado = nombresEm,
    EM.direccionEmpleado = direccionEm,
    EM.telefonoContacto = telefonoContac,
    EM.gradoCocinero = gradoCoci,
    EM.codigoTipoEmpleado =  codigoTipoEm
    where EM.codigoEmpleado = codigoEm;
    End$$
Delimiter ;

call sp_EditarEmpleados(1,'2','Monterroso Vasquez','Luis Alberto','17 avenida zona 22','59159599','SegundoCheff','1');

-- --Buscar Empleados -------------------------

Delimiter $$
	Create procedure sp_BuscarEmpleados (in codigoEm int)
    begin
    Select 
    EM.codigoEmpleado,
    EM.numeroEmpleado,
    EM.apellidosEmpleado,
    EM.nombresEmpleado,
    EM.direccionEmpleado,
    EM.telefonoContacto,
    EM.gradoCocinero,
    EM.codigoTipoEmpleado
    from Empleados EM
    where EM.codigoEmpleado = codigoEm;
    End $$
Delimiter ;

call sp_BuscarEmpleados(2);

-- --Eliminar Empleados -------------------------
Delimiter $$
	Create procedure sp_EliminarEmpleados(in codigoEm int)
    begin
    Delete from Empleados
    where codigoEmpleado = codigoEm;
    end$$
Delimiter ;
-- ------------------Servicios--------------------------
-- --Agregar Servicios--------------------------
Delimiter  $$
	Create procedure sp_AgregarServicios(in fechaServicio date, in tipoServicio varchar(150), in horaServicio time,
								in lugarServicio varchar(150),in telefonoContacto varchar(8),in codigoEmpresa int)
    Begin
		Insert into Servicios(fechaServicio,tipoServicio,horaServicio,lugarServicio,telefonoContacto,codigoEmpresa)
			values(fechaServicio,tipoServicio,horaServicio,lugarServicio,telefonoContacto,codigoEmpresa);
    End $$
Delimiter ;

call sp_AgregarServicios('2022/03/02','Premium','1:33','Salon Puente Dorado','59159599','1');


-- --Listar Servicios ---------------------------
Delimiter $$
	Create procedure sp_ListarServicios()
    Begin
    Select
    S.codigoServicio,
    S.fechaServicio,
    S.tipoServicio,
    S.horaServicio,
    S.lugarServicio,
    S.telefonoContacto,
    S.codigoEmpresa
    from Servicios S;
    End$$
Delimiter ;

call sp_ListarServicios();

-- --Editar Servicios --------------------------
Delimiter $$
	Create procedure sp_EditarServicios(in codigoSer int,in fechaSer date, tipoSer varchar(150),horaSer time,
										in lugarSer varchar(150),in telefonoContac varchar(8),in codigoEm int)
                                        
    begin
    Update Servicios S
    set 
    S.fechaServicio = fechaSer,
    S.tipoServicio = tipoSer,
    S.horaServicio = horaSer,
    S.lugarServicio = lugarSer,
    S.telefonoContacto = telefonoContac,
    S.codigoEmpresa = codigoEm
    where S.codigoServicio = codigoSer;
    End$$
Delimiter ;

call sp_EditarServicios(1,'2022/03/02','Basico','1:33','Salon Puente Verde','59159599','1');

-- --Buscar Servicios -------------------------

Delimiter $$
	Create procedure sp_BuscarServicios (in codigoSer int)
    begin
    Select 
    S.codigoServicio,
    S.fechaServicio,
    S.tipoServicio,
    S.horaServicio,
    S.lugarServicio,
    S.telefonoContacto,
    S.codigoEmpresa
    from Servicios S
    where S.codigoServicio = codigoSer;
    End $$
Delimiter ;

call sp_BuscarServicios(1);

-- --Eliminar Servicios -------------------------
Delimiter $$
	Create procedure sp_EliminarServicios(in codigoSer int)
    begin
    Delete from Servicios
    where codigoServicio = codigoSer;
    end$$
Delimiter ;
-- ------------------Presupuesto--------------------------
-- --Agregar Presupuesto--------------------------
Delimiter  $$
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
    Begin
		Insert into Presupuesto(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
			values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
    End $$
Delimiter ;

call sp_AgregarPresupuesto('2023/04/03','5500.00','1');


-- --Listar Presupuesto ---------------------------
Delimiter $$
	Create procedure sp_ListarPresupuesto()
    Begin
    Select
    PR.codigoPresupuesto,
    PR.fechaSolicitud,
    PR.cantidadPresupuesto,
    PR.codigoEmpresa
    from Presupuesto PR;
    End$$
Delimiter ;

call sp_ListarPresupuesto();

-- --Editar Presupuesto --------------------------
Delimiter $$
	Create procedure sp_EditarPresupuesto(in codigoPre int,in fechaSol date, cantidadPre decimal(10,2))
                                        
    begin
    Update Presupuesto PR
    set 
    PR.fechaSolicitud = fechaSol,
    PR.cantidadPresupuesto = cantidadPre
    where PR.codigoPresupuesto = codigoPre;
    End$$
Delimiter ;

-- --Buscar Presupuesto -------------------------

Delimiter $$
	Create procedure sp_BuscarPresupuesto (in codigoPre int)
    begin
    Select 
    PR.codigoPresupuesto,
	PR.fechaSolicitud,
    PR.cantidadPresupuesto,
    PR.codigoEmpresa
    from Presupuesto PR
    where PR.codigoPresupuesto = codigoPre;
    End $$
Delimiter ;

call sp_BuscarPresupuesto(1);

-- --Eliminar Presupuesto -------------------------
Delimiter $$
	Create procedure sp_EliminarPresupuesto(in codigoPre int)
    begin
    Delete from Presupuesto
    where codigoPresupuesto = codigoPre;
    end$$
Delimiter ;
-- ------------------Platos--------------------------
-- --Agregar Platos--------------------------
Delimiter  $$
	Create procedure sp_AgregarPlatos(in cantidad int, in nombrePlato varchar(50), in descripcionPlato varchar(150),
										in precioPlato decimal(10,2), in codigoTipoPlato int)
    Begin
		Insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
			values(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
    End $$
Delimiter ;

call sp_AgregarPlatos('300','Caviar','Huevas de pescado de la mas alta calidad','300.00','1');


-- --Listar Platos ---------------------------
Delimiter $$
	Create procedure sp_ListarPlatos()
    Begin
    Select
    PL.codigoPlato,
    PL.cantidad,
    PL.nombrePlato,
    PL.descripcionPlato,
    PL.precioPlato,
    PL.codigoTipoPlato
    from Platos PL;
    End$$
Delimiter ;

call sp_ListarPlatos();

-- --Editar Platos --------------------------
Delimiter $$
	Create procedure sp_EditarPlatos(in codigoPla int,in cant int, nombrePla varchar(50),descripcionPla varchar(150),
										in precioPla decimal(10,2), in codigoTipoPl int)
                                        
    begin
    Update Platos PL
    set 
    PL.cantidad= cant,
    PL.nombrePlato = nombrePla,
    PL.descripcionPlato = descripcionPla,
    PL.precioPlato = precioPla,
    PL.codigoTipoPlato = codigoTipoPl
    where PL.codigoPlato = codigoPla;
    End$$
Delimiter ;

call sp_EditarPlatos(1,'200','Caviar','Huevas de pescado de la mas alta calidad','300.00','1');
-- --Buscar Platos -------------------------

Delimiter $$
	Create procedure sp_BuscarPlatos(in codigoPla int)
    begin
    Select 
	PL.codigoPlato,
    PL.cantidad,
    PL.nombrePlato,
    PL.descripcionPlato,
    PL.precioPlato,
    PL.codigoTipoPlato
    from Platos PL
    where PL.codigoPlato = codigoPla;
    End $$
Delimiter ;

call sp_BuscarPlatos(1);

-- --Eliminar Platos -------------------------
Delimiter $$
	Create procedure sp_EliminarPlatos(in codigoPla int)
    begin
    Delete from Platos
    where codigoPlato = codigoPla;
    end$$
Delimiter ;
-- ------------------Productos_has_Platos--------------------------
-- --Agregar Productos_has_Platos--------------------------
Delimiter  $$
	Create procedure sp_AgregarProductos_has_Platos( in codigoPlato int, in codigoProducto int)
    Begin
		Insert into Productos_has_Platos(codigoPlato, codigoProducto)
			values(codigoPlato, codigoProducto);
    End $$
Delimiter ;

call sp_AgregarProductos_has_Platos('1','1');


-- --Listar Productos_has_Platos ---------------------------
Delimiter $$
	Create procedure sp_ListarProductos_has_Platos()
    Begin
    Select
    PHP.Productos_codigoProducto,
    PHP.codigoPlato,
    PHP.codigoProducto
    from Productos_has_platos PHP;
    End$$
Delimiter ;

call sp_ListarProductos_has_Platos();

-- --Editar Productos_has_Platos --------------------------
Delimiter $$
	Create procedure sp_EditarProductos_has_Platos(in Productos_codigoPro int,in codigoPla int, in codigoPro int)
                                        
    begin
    Update Productos_has_Platos PHP
    set 
    PHP.codigoPlato = codigoPla,
    PHP.codigoProducto = codigoPro
    where PHP.Productos_codigoProducto = Productos_codigoPro;
    End$$
Delimiter ;
call sp_EditarProductos_has_Platos(1,'1','1');
-- --Buscar Productos_has_Platos -------------------------

Delimiter $$
	Create procedure sp_BuscarProductos_has_Platos(in Productos_codigoPro int)
    begin
    Select 
	PHP.Productos_codigoProducto,
    PHP.codigoPlato,
	PHP.codigoProducto
    from Productos_has_Platos PHP
    where PHP.Productos_codigoProducto = Productos_codigoPro;
    End $$
Delimiter ;

call sp_BuscarProductos_has_Platos(1);

-- --Eliminar Productos_has_Platos -------------------------
Delimiter $$
	Create procedure sp_EliminarProductos_has_Platos(in Productos_codigoPro int)
    begin
    Delete from Productos_has_Platos
    where Productos_codigoProducto = Productos_codigoPro;
    end$$
Delimiter ;
-- ------------------Servicios_has_Platos--------------------------
-- --Agregar Servicios_has_Platos--------------------------
Delimiter  $$
	Create procedure sp_AgregarServicios_has_Platos( in codigoPlato int, in codigoServicio int)
    Begin
		Insert into Servicios_has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
			values(Servicios_codigoServicio, codigoPlato, codigoServicio);
    End $$
Delimiter ;

call sp_AgregarServicios_has_Platos('1','1');


-- --Listar Servicios_has_Platos ---------------------------
Delimiter $$
	Create procedure sp_ListarServicios_has_Platos()
    Begin
    Select
    SHP.Servicios_codigoServicio,
    SHP.codigoPlato,
    SHP.codigoServicio
    from Servicios_has_Platos SHP;
    End$$
Delimiter ;

call sp_ListarServicios_has_Platos();

-- --Editar Servicios_has_Platos --------------------------
Delimiter $$
	Create procedure sp_EditarServicios_has_Platos(in Servicios_codigoSer int,in codigoPla int, in codigoSer int)
                                        
    begin
    Update Servicios_has_Platos SHP
    set 
    SHP.codigoPlato = codigoPla,
    SHP.codigoServicio = codigoSer
    where SHP.Servicios_codigoServicio = Servicios_codigoSer;
    End$$
Delimiter ;
call sp_EditarServicios_has_Platos(1,'1','1');
-- --Buscar Servicios_has_Platos -------------------------

Delimiter $$
	Create procedure sp_BuscarServicios_has_Platos(in Servicios_codigoSer int)
    begin
    Select 
	SHP.Servicios_codigoServicio,
    SHP.codigoPlato,
	SHP.codigoServicio
    from Servicios_has_Platos SHP
    where SHP.Servicios_codigoServicio = Servicios_codigoSer;
    End $$
Delimiter ;

call sp_BuscarServicios_has_Platos(1);

-- --Eliminar Servicios_has_Platos -------------------------
Delimiter $$
	Create procedure sp_EliminarServicios_has_Platos(in Servicios_codigoSer int)
    begin
    Delete from Servicios_has_Platos
    where Servicios_codigoServicio = Servicios_codigoSer;
    end$$
Delimiter ;
-- ------------------Servicios_has_Empleados--------------------------
-- --Agregar Servicios_has_Empleados--------------------------
Delimiter  $$
	Create procedure sp_AgregarServicios_has_Empleados(in codigoServicio int,in codigoEmpleado int,
														in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
    Begin
		Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento )
			values(Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
    End $$
Delimiter ;

call sp_AgregarServicios_has_Empleados('1','1','2023/04/11','1:00','Cayala');


-- --Listar Servicios_has_Empleados ---------------------------
Delimiter $$
	Create procedure sp_ListarServicios_has_Empleados()
    Begin
    Select
    SHE.Servicios_codigoServicio,
    SHE.codigoServicio,
    SHE.codigoEmpleado,
    SHE.fechaEvento,
    SHE.horaEvento,
    SHE.lugarEvento
    from Servicios_has_Empleados SHE;
    End$$
Delimiter ;

call sp_ListarServicios_has_Empleados();

-- --Editar Servicios_has_Empleados --------------------------
Delimiter $$
	Create procedure sp_EditarServicios_has_Empleados(in Servicios_codigoSer int,in codigoSer int,in codigoEm int,
														in fechaEv date, in horaEv time, in lugarEv varchar(150))
                                        
    begin
    Update Servicios_has_Empleados SHE
    set 
    SHE.codigoServicio = codigoSer,
    SHE.codigoEmpleado = codigoEm,
    SHE.fechaEvento = fechaEv,
    SHE.horaEvento = horaEv,
    SHE.lugarEvento = lugarEv
    where SHE.Servicios_codigoServicio = Servicios_codigoSer;
    End$$
Delimiter ;
call sp_EditarServicios_has_Empleados(1,'1','1','2023/04/11','1:00','Salon El rosario');
-- --Buscar Servicios_has_Empleados -------------------------

Delimiter $$
	Create procedure sp_BuscarServicios_has_Empleados(in Servicios_codigoSer int)
    begin
    Select 
	SHE.Servicios_codigoServicio,
    SHE.codigoServicio,
	SHE.codigoEmpleado,
    SHE.fechaEvento,
    SHE.horaEvento,
    SHE.lugarEvento
    from Servicios_has_Empleados SHE
    where SHE.Servicios_codigoServicio = Servicios_codigoSer;
    End $$
Delimiter ;

call sp_BuscarServicios_has_Empleados(1);

-- --Eliminar Servicios_has_Empleados -------------------------
Delimiter $$
	Create procedure sp_EliminarServicios_has_Empleados(in Servicios_codigoSer int)
    begin
    Delete from Servicios_has_Empleados
    where Servicios_codigoServicio = Servicios_codigoSer;
    end$$
Delimiter ;

select * from Empresa;
select * from Presupuesto;
select * from Servicios;

Select * from Empresa E inner Join Servicios S on
 E.codigoEmpresa = S.codigoEmpresa where E.codigoEmpresa = 1;
 
 -- --
 
 Delimiter $$
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100),in apellidoUsuario varchar(100)
    ,in usuarioLogin varchar(50),in contrasena varchar(50))
    
    Begin 
    Insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
    values (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    
    end $$
 
 Delimiter ;
 
 
 Delimiter $$
	Create procedure sp_ListarUsuarios()
    Begin
		Select 
        U.codigoUsuario,
        U.nombreUsuario,
        U.apellidoUsuario,
        U.usuarioLogin,
        U.contrasena
        from Usuario U;
	End$$
 Delimiter ;
 
 
 Delimiter $$
    Create procedure sp_ReporteGeneral(in codigoEmp int)
        Begin
            select
                E.nombreEmpresa,
                E.direccion,
                E.telefono,
                PR.fechaSolicitud,
                PR.cantidadPresupuesto,
                S.FechaServicio, 
                S.tipoServicio, 
                S.horaServicio,
                SHE.fechaEvento,
                SHE.horaEvento,
                SHE.lugarEvento,
                EM.nombresEmpleado,
                EM.apellidosEmpleado,
                TE.descripcion,
                PL.cantidad,
                PL.nombrePlato,
                PL.descripcionPlato,
                PL.precioPlato,
                TP.descripcionTipo,
                P.nombreProductos,
                P.cantidadProducto
                from Empresa E
                inner join Presupuesto PR
                on E.codigoEmpresa = PR.codigoEmpresa
                inner join Servicios S
                on S.codigoEmpresa = E.codigoEmpresa
                inner join Servicios_has_Empleados SHE
                on SHE.codigoServicio = S.codigoServicio
                inner join Empleados EM
                on EM.codigoEmpleado = SHE.codigoEmpleado
                inner join TipoEmpleado TE
                on TE.codigoTipoEmpleado = EM.codigoTipoEmpleado
                inner join Servicios_has_Platos SHP
                on SHP.codigoServicio = S.codigoServicio
                inner join Platos PL
                on PL.codigoPlato = SHP.codigoPlato
                inner join TipoPlato TP
                on TP.codigoTipoPlato = PL.codigoTipoPlato
                inner join Productos_has_Platos PHP
                on PHP.codigoPlato = PL.codigoPlato
                inner join Productos P
                on P.codigoProducto = PHP.codigoProducto
                where E.codigoEmpresa = codigoEmp;
        End$$
Delimiter ;







 