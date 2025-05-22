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

SET FOREIGN_KEY_CHECKS=1;

/*==============================================================*/
/* Table: USUARIO                                               */
/*==============================================================*/
CREATE TABLE USUARIO (
   id BIGINT NOT NULL,
   nombre VARCHAR(100) NOT NULL,
   apellido VARCHAR(100) NOT NULL,
   email VARCHAR(100) NOT NULL,
   username VARCHAR(100) NOT NULL,
   password VARCHAR(200) NOT NULL,
   PRIMARY KEY (id)
);

/*==============================================================*/
/* Table: PERSONA                                               */
/*==============================================================*/
CREATE TABLE PERSONA (
   numeroIdPersona BIGINT NOT NULL,
   tipoIdPersona VARCHAR(50) NOT NULL,
   nombrePersona VARCHAR(50) NOT NULL,
   apellidosPersona VARCHAR(50) NOT NULL,
   fechaNacimientoPersona DATE NOT NULL,
   fechaExpedicionDocumentoIdPersona DATE NOT NULL,
   estadoPersona VARCHAR(50) NOT NULL,
   generoPersona VARCHAR(50),
   fechaDefuncionPersona DATE,
   PRIMARY KEY (numeroIdPersona)
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
   numeroIdPersona BIGINT NOT NULL,
   nitEntidad BIGINT NOT NULL,
   fechaInicioPension DATE,
   valorInicialPension DOUBLE NOT NULL,
   resolucionPension  VARCHAR(200) NOT NULL,
   totalDiasTrabajo BIGINT,
   PRIMARY KEY (numeroIdPersona),
   FOREIGN KEY (numeroIdPersona) REFERENCES PERSONA(numeroIdPersona),
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);

/*==============================================================*/
/* Table: TRABAJO                                               */
/*==============================================================*/
CREATE TABLE TRABAJO (
   idTrabajo BIGINT NOT NULL AUTO_INCREMENT,
   numeroIdPersona BIGINT NOT NULL,
   nitEntidad BIGINT NOT NULL,
   diasDeServicio BIGINT NOT NULL,
   PRIMARY KEY (idTrabajo),
   FOREIGN KEY (numeroIdPersona) REFERENCES PENSIONADO(numeroIdPersona),
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);

/*==============================================================*/
/* Table: SUCESOR                                               */
/*==============================================================*/
CREATE TABLE SUCESOR (
    numeroIdPersona BIGINT NOT NULL,
    numeroIdPensionado BIGINT NOT NULL,
    fechaInicioSucesion DATE NOT NULL,
    PRIMARY KEY (numeroIdPersona),
    FOREIGN KEY (numeroIdPersona) REFERENCES PERSONA(numeroIdPersona),
    FOREIGN KEY (numeroIdPensionado) REFERENCES PENSIONADO(numeroIdPersona)
);

/*==============================================================*/
/* Table: CUOTA_PARTE                                           */
/*==============================================================*/
CREATE TABLE CUOTA_PARTE (
   idCuotaParte BIGINT NOT NULL AUTO_INCREMENT,
   idTrabajo BIGINT NOT NULL,
   valorCuotaParte DOUBLE NOT NULL,
   porcentajeCuotaParte DOUBLE NOT NULL,
   fechaGeneracion DATE,
   notas VARCHAR(200) NOT NULL,
   cuotaParteTotal DOUBLE NOT NULL,
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
   numeroMesadas BIGINT NOT NULL,
   valorPension DOUBLE NOT NULL,
   cuotaParteMensual DOUBLE NOT NULL,
   cuotaParteTotalAnio DOUBLE NOT NULL,
   incrementoAdicionalLey476 DOUBLE,
   PRIMARY KEY (idPeriodo),
   FOREIGN KEY (fechaIPC) REFERENCES IPC(fechaIPC), 
   FOREIGN KEY (idCuotaParte) REFERENCES CUOTA_PARTE(idCuotaParte)
);

