INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES (8911500319, 'Unicauca', 'Calle 5 No. 4-70 (Popayán - Cauca', 'rectoria@unicauca.edu.co', 8209900, "Activa");

-- Insertar 5 personas
INSERT INTO PERSONA (numeroIdPersona, tipoIdPersona, nombrePersona, apellidosPersona, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
VALUES 
(1001, 'Cedula', 'Carlos', 'Gomez Perez', '1955-03-12', '2010-05-20', 'Activo', 'Masculino'),
(1002, 'Cedula', 'Maria', 'Lopez Ruiz', '1960-07-25', '2012-08-15', 'Activo', 'Femenino'),
(1003, 'Cedula', 'Jorge', 'Martinez Diaz', '1948-11-05', '2005-10-30', 'Activo', 'Masculino'),
(1004, 'Cedula', 'Ana', 'Rodriguez Soto', '1952-09-18', '2008-04-22', 'Activo', 'Femenino'),
(1005, 'Cedula', 'Luis', 'Sanchez Mora', '1958-02-14', '2015-03-10', 'Activo', 'Masculino');

-- Insertar 5 pensionados asociados a la entidad Unicauca (nit 8911500319)
INSERT INTO PENSIONADO (numeroIdPersona, nitEntidad, fechaInicioPension, valorPension, resolucionPension, totalDiasTrabajo)
VALUES 
(1001, 8911500319, '2018-01-01', 2500000, 'RES-2018-001', 9000),
(1002, 8911500319, '2019-05-15', 1800000, 'RES-2019-045', 8500),
(1003, 8911500319, '2015-03-20', 3000000, 'RES-2015-012', 9500),
(1004, 8911500319, '2020-10-10', 2100000, 'RES-2020-087', 8000),
(1005, 8911500319, '2017-12-05', 2750000, 'RES-2017-032', 9200);