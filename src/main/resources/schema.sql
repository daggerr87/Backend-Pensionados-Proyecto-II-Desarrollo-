/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     7/04/2025 8:05:52 p.�m.                      */
/*==============================================================*/

SET FOREIGN_KEY_CHECKS=0;


DROP TABLE IF EXISTS PERIODO;
DROP TABLE IF EXISTS CUOTA_PARTE;
DROP TABLE IF EXISTS TRABAJO;
DROP TABLE IF EXISTS SUCESOR;
DROP TABLE IF EXISTS PENSIONADO;
DROP TABLE IF EXISTS IPC;
DROP TABLE IF EXISTS ENTIDAD;
DROP TABLE IF EXISTS PERSONA;
DROP TABLE IF EXISTS USUARIO;
DROP TABLE IF EXISTS ROL;
DROP TABLE IF EXISTS ROL_ACCION;
DROP TABLE IF EXISTS LOG_CAMBIO;
;
SET FOREIGN_KEY_CHECKS=1;

/*==============================================================*/
/* Table: ROL                                               */
/*==============================================================*/
CREATE TABLE ROL (
   id BIGINT NOT NULL AUTO_INCREMENT,
   nombre VARCHAR(60) NOT NULL,
   activo BOOLEAN NOT NULL DEFAULT TRUE,
   creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (id),
   UNIQUE (nombre)
);
/*==============================================================*/
/* Table: USUARIO                                               */
/*==============================================================*/
CREATE TABLE USUARIO (
   id BIGINT NOT NULL AUTO_INCREMENT,
   nombre VARCHAR(100) NOT NULL,
   apellido VARCHAR(100) NOT NULL,
   /*email VARCHAR(100) NOT NULL,*/
   username VARCHAR(100) NOT NULL,
   password VARCHAR(200) NOT NULL,


   rol_id BIGINT NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (rol_id) REFERENCES ROL(id)

);

/*==============================================================*/
/* Table: PERSONA                                               */
/*==============================================================*/
CREATE TABLE PERSONA (

   idPersona BIGINT NOT NULL AUTO_INCREMENT,              -- 1. Nuevo ID para la persona (llave primaria)
   numeroIdentificacion BIGINT NOT NULL,                  -- 2. Renombrado desde numeroIdPersona
   tipoIdentificacion VARCHAR(50) NOT NULL,               -- 3. Renombrado desde tipoIdPersona
   nombrePersona VARCHAR(50) NOT NULL,
   apellidosPersona VARCHAR(50) NOT NULL,
   fechaNacimientoPersona DATE NOT NULL,
   fechaExpedicionDocumentoIdPersona DATE NOT NULL,
   estadoPersona VARCHAR(50) NOT NULL,
   generoPersona VARCHAR(50),
   estadoCivil VARCHAR(50) NOT NULL,                      -- 4. Nuevo campo Estado Civil
   fechaDefuncionPersona DATE,
   PRIMARY KEY (idPersona),                               -- 5. Se establece el nuevo ID como llave primaria
   UNIQUE KEY uk_identificacion (tipoIdentificacion, numeroIdentificacion) -- 6. Se unifican los campos de identificacion
);

/*==============================================================*/
/* Table: ENTIDAD                                               */
/*==============================================================*/
CREATE TABLE ENTIDAD (
   nitEntidad BIGINT NOT NULL,
   nombreEntidad VARCHAR(100) NOT NULL,
   direccionEntidad VARCHAR(100) NOT NULL,
   emailEntidad VARCHAR(100) NOT NULL,
   telefonoEntidad BIGINT NOT NULL,
   estadoEntidad VARCHAR(50) NOT NULL,
   PRIMARY KEY (nitEntidad)
);

/*==============================================================*/
/* Table: IPC                                                   */
/*==============================================================*/
CREATE TABLE IPC (
   fechaIPC INT NOT NULL,
   valorIPC DECIMAL(5,2) NOT NULL,
   PRIMARY KEY (fechaIPC)
);

/*==============================================================*/
/* Table: PENSIONADO                                            */
/*==============================================================*/
CREATE TABLE PENSIONADO (
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   nitEntidad BIGINT NOT NULL,
   fechaInicioPension DATE,
   valorInicialPension DECIMAL (19,0) NOT NULL,
   resolucionPension  VARCHAR(200) NOT NULL,
   totalDiasTrabajo BIGINT,

   aplicarIPCPrimerPeriodo BOOLEAN NOT NULL DEFAULT FALSE,

   PRIMARY KEY (idPersona), -- Cambiado de numeroIdPersona
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);


/*==============================================================*/
/* Table: TRABAJO                                               */
/*==============================================================*/
CREATE TABLE TRABAJO (
   idTrabajo BIGINT NOT NULL AUTO_INCREMENT,
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   nitEntidad BIGINT NOT NULL,
   diasDeServicio BIGINT NOT NULL,
   PRIMARY KEY (idTrabajo),
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria de Persona
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);

/*==============================================================*/
/* Table: SUCESOR                                               */
/*==============================================================*/
CREATE TABLE SUCESOR (
   idPersona BIGINT NOT NULL, -- Cambiado de numeroIdPersona
   idPensionado BIGINT NOT NULL, -- Renombrado por claridad (era numeroIdPensionado)
   fechaInicioSucesion DATE NOT NULL,
   porcentajePension DOUBLE NOT NULL,
   PRIMARY KEY (idPersona), -- Cambiado de numeroIdPersona
   FOREIGN KEY (idPersona) REFERENCES PERSONA(idPersona), -- Apunta a la nueva llave primaria
   FOREIGN KEY (idPensionado) REFERENCES PENSIONADO(idPersona) -- Apunta a la llave primaria de Pensionado
);

/*==============================================================*/
/* Table: CUOTA_PARTE                                           */
/*==============================================================*/
CREATE TABLE CUOTA_PARTE (
   idCuotaParte BIGINT NOT NULL AUTO_INCREMENT,
   idTrabajo BIGINT NOT NULL,
   valorCuotaParte DECIMAL (19,2) NOT NULL,
   porcentajeCuotaParte DECIMAL(5,4) NOT NULL,
   fechaGeneracion DATE,
   notas VARCHAR(200) NOT NULL,
   cuotaParteTotal DECIMAL (19,2),
   PRIMARY KEY (idCuotaParte),
   FOREIGN KEY (idTrabajo) REFERENCES TRABAJO(idTrabajo)
);

/*==============================================================*/
/* Table: PERIODO                                               */
/*==============================================================*/
CREATE TABLE PERIODO (
   idPeriodo BIGINT NOT NULL AUTO_INCREMENT,
   fechaIPC INT NOT NULL,
   idCuotaParte BIGINT NOT NULL,
   fechaInicioPeriodo DATE NOT NULL,
   fechaFinPeriodo DATE NOT NULL,
   numeroMesadas DECIMAL(5,2) NOT NULL,
   valorPension DECIMAL (19,0) NOT NULL,
   cuotaParteMensual DECIMAL (19,0) NOT NULL,
   cuotaParteTotalAnio DECIMAL (19,0) NOT NULL,
   incrementoLey476 DECIMAL (19,2),
   PRIMARY KEY (idPeriodo),
   FOREIGN KEY (fechaIPC) REFERENCES IPC(fechaIPC),
   FOREIGN KEY (idCuotaParte) REFERENCES CUOTA_PARTE(idCuotaParte)
);



/*==============================================================*/
/* Table: ROL_ACCION                                               */
/*==============================================================*/
CREATE TABLE ROL_ACCION (
   rol_id BIGINT NOT NULL,
   accion ENUM(
     'EJECUCION_PAGOS',
     'REGISTRO_PENSIONADO',
     'PAGO_CUOTA_PARTE',
     'GENERAR_REPORTE',
     'CONSULTAR_HISTORIAL'
   ) NOT NULL,
   PRIMARY KEY (rol_id, accion),
   FOREIGN KEY (rol_id) REFERENCES ROL(id)
     ON UPDATE CASCADE
     ON DELETE CASCADE
);
/*==============================================================*/
/* Table: LOG_CAMBIO                                               */
/*==============================================================*/
CREATE TABLE LOG_CAMBIO (
   id BIGINT NOT NULL AUTO_INCREMENT,
   entidad VARCHAR(80) NOT NULL,
   accion ENUM('CREAR','ACTUALIZAR','ELIMINAR','CONSULTAR') NOT NULL,
   valor_anterior JSON NULL,
   valor_nuevo JSON NULL,
   fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   usuario_id BIGINT NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
     ON UPDATE CASCADE
     ON DELETE CASCADE,
   INDEX idx_log_usuario (usuario_id),
   INDEX idx_log_fecha (fecha)
);


