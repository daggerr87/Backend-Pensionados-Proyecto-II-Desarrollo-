--Insertar Usuario por Defecto
INSERT INTO USUARIO (id, apellido, email, nombre, password, username)
VALUES (1, 'Unicauca', 'pensiones@unicauca.edu.co', 'pensiones', '$2a$10$R6MoChK7sDHuSTrON3BC5.jENlU5fn/tMRP0v7frkmiCMfDafgKJK', 'pensiones@unicauca.edu.co');



INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES (8911500319, 'Unicauca', 'Calle 5 No. 4-70 (Popayán - Cauca', 'rectoria@unicauca.edu.co', 8209900, "Activa");

-- Insertar más entidades para el historial laboral
INSERT INTO ENTIDAD (nitEntidad, nombreEntidad, direccionEntidad, emailEntidad, telefonoEntidad, estadoEntidad)
VALUES 
(9004567281, 'Hospital San José', 'Carrera 10 No. 15-45, Popayán', 'contacto@hsanjose.com', 8220011, 'Activa'),
(8600123456, 'Alcaldía de Popayán', 'Calle 8 No. 7-30, Popayán', 'alcaldia@popayan.gov.co', 8220022, 'Activa'),
(9001234567, 'Gobernación del Cauca', 'Calle 4 No. 3-52, Popayán', 'info@cauca.gov.co', 8220033, 'Activa'),
(8300123123, 'Colegio La Salle', 'Avenida 2 No. 12-40, Popayán', 'secretaria@lasalle.edu.co', 8220044, 'Activa');

-- Insertar 5 personas
INSERT INTO PERSONA (numeroIdPersona, tipoIdPersona, nombrePersona, apellidosPersona, fechaNacimientoPersona, fechaExpedicionDocumentoIdPersona, estadoPersona, generoPersona)
VALUES 
(1001000000, 'Cedula', 'Carlos', 'Gomez Perez', '1955-03-12', '2010-05-20', 'Activo', 'Masculino'),
(1002000000, 'Cedula', 'Maria', 'Lopez Ruiz', '1960-07-25', '2012-08-15', 'Activo', 'Femenino'),
(1003000000, 'Cedula', 'Jorge', 'Martinez Diaz', '1948-11-05', '2005-10-30', 'Activo', 'Masculino'),
(1004000000, 'Cedula', 'Ana', 'Rodriguez Soto', '1952-09-18', '2008-04-22', 'Activo', 'Femenino'),
(1005000000, 'Cedula', 'Luis', 'Sanchez Mora', '1958-02-14', '2015-03-10', 'Activo', 'Masculino');

-- Insertar 5 pensionados asociados a la entidad Unicauca (nit 8911500319)
INSERT INTO PENSIONADO (numeroIdPersona, nitEntidad, fechaInicioPension, valorPension, resolucionPension, totalDiasTrabajo)
VALUES 
(1001000000, 8911500319, '2018-01-01', 2500000, 'RES-2018-001', 6935),
(1002000000, 8300123123, '2019-05-15', 1800000, 'RES-2019-045', 4015),
(1003000000, 8911500319, '2015-03-20', 3000000, 'RES-2015-012', 7665),
(1004000000, 9004567281, '2020-10-10', 2100000, 'RES-2020-087', 4745),
(1005000000, 9001234567, '2017-12-05', 2750000, 'RES-2017-032', 3650);

-- Insertar registros de trabajo para los pensionados
-- Cada pensionado tiene su trabajo principal en Unicauca (ya registrado en la tabla PENSIONADO)
-- Y trabajos adicionales en otras entidades a lo largo de su vida laboral

-- Trabajos para Carlos Gomez (1001000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1001000000, 9004567281, 1825), -- Trabajó ~5 años en el Hospital San José
(1001000000, 8600123456, 2190); -- Trabajó ~6 años en la Alcaldía de Popayán

-- Trabajos para Maria Lopez (1002000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1002000000, 8300123123, 2555), -- Trabajó ~7 años en el Colegio La Salle
(1002000000, 9001234567, 1460); -- Trabajó ~4 años en la Gobernación del Cauca

-- Trabajos para Jorge Martinez (1003000000) 
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1003000000, 8600123456, 2190), -- Trabajó ~6 años en la Alcaldía de Popayán
(1003000000, 8911500319, 2920), -- Trabajó ~8 años en Unicauca
(1003000000, 9001234567, 1460), -- Trabajó ~4 años en la Gobernación del Cauca
(1003000000, 8300123123, 1095); -- Trabajó ~3 años en el Colegio La Salle

-- Trabajos para Ana Rodriguez (1004000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1004000000, 9004567281, 2920), -- Trabajó ~8 años en el Hospital San José
(1004000000, 8300123123, 1825); -- Trabajó ~5 años en el Colegio La Salle

-- Trabajos para Luis Sanchez (1005000000)
INSERT INTO TRABAJO (numeroIdPersona, nitEntidad, diasDeServicio)
VALUES
(1005000000, 9001234567, 2555), -- Trabajó ~7 años en la Gobernación del Cauca
(1005000000, 9004567281, 1095); -- Trabajó ~3 años en el Hospital San José