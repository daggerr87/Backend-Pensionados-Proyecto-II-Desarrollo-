/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     7/04/2025 8:05:52 p.�m.                      */
/*==============================================================*/

DROP TABLE IF EXISTS SUCESOR;

DROP TABLE IF EXISTS TRABAJO;

DROP TABLE IF EXISTS PERIODO;

DROP TABLE IF EXISTS PENSIONADO;

DROP TABLE IF EXISTS ENTIDADCUOTAPARTE;

DROP TABLE IF EXISTS CUOTA_PARTE;

DROP TABLE IF EXISTS ENTIDAD;

DROP TABLE IF EXISTS IPC;

DROP TABLE IF EXISTS SALARIO_MINIMO;

DROP TABLE IF EXISTS PERSONA;


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
/* Table: PENSIONADO                                            */
/*==============================================================*/
CREATE TABLE PENSIONADO (
   numeroIdPersona BIGINT NOT NULL,
   nitEntidad BIGINT NOT NULL,
   fechaInicioPension DATE,
   valorPension DOUBLE NOT NULL,
   resolucionPension  VARCHAR(200) NOT NULL,
   totalDiasTrabajo BIGINT,
   PRIMARY KEY (numeroIdPersona),
   FOREIGN KEY (numeroIdPersona) REFERENCES PERSONA(numeroIdPersona),
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);

/*==============================================================*/
/* Table: SALARIO_MINIMO                                        */
/*==============================================================*/
CREATE TABLE SALARIO_MINIMO (
   anioSalarioMinimo YEAR NOT NULL,
   salarioMinimoMensual DOUBLE NOT NULL,
   PRIMARY KEY (anioSalarioMinimo)
);

/*==============================================================*/
/* Table: IPC                                                   */
/*==============================================================*/
CREATE TABLE IPC (
   fechaIpc DATE NOT NULL,
   inflacionTotalFinMes DOUBLE NOT NULL,
   PRIMARY KEY (fechaIpc)
);

/*==============================================================*/
/* Table: PERIODO                                               */
/*==============================================================*/
CREATE TABLE PERIODO (
   idPeriodo BIGINT NOT NULL,
   fechaInicioPeriodo DATE NOT NULL,
   fechaFinPeriodo DATE NOT NULL,
   numeroMesadas BIGINT NOT NULL,
   valorPension DOUBLE NOT NULL,
   cuotaParteMensual DOUBLE NOT NULL,
   porcentajeIncremento DOUBLE NOT NULL,
   fechaIpc DATE NOT NULL,
   PRIMARY KEY (idPeriodo),
   FOREIGN KEY (fechaIpc) REFERENCES IPC(fechaIpc)
);

/*==============================================================*/
/* Table: CUOTA_PARTE                                           */
/*==============================================================*/
CREATE TABLE CUOTA_PARTE (
   idCuotaParte BIGINT NOT NULL,
   valorCuotaParte DOUBLE NOT NULL,
   porcentajeCuotaParte DOUBLE NOT NULL,
   valorTotalCuotaParte DOUBLE NOT NULL,
   fechaGeneracion DATE NOT NULL,
   notas VARCHAR(200),
   numeroIdPersona BIGINT NOT NULL,
   idPeriodo BIGINT NOT NULL,
   PRIMARY KEY (idCuotaParte),
   FOREIGN KEY (numeroIdPersona) REFERENCES PENSIONADO(numeroIdPersona),
   FOREIGN KEY (idPeriodo) REFERENCES PERIODO(idPeriodo)
);

/*==============================================================*/
/* Table: ENTIDADCUOTAPARTE                                     */
/*==============================================================*/
CREATE TABLE ENTIDADCUOTAPARTE (
   idCuotaParte BIGINT NOT NULL,
   nitEntidad BIGINT NOT NULL,
   PRIMARY KEY (idCuotaParte, nitEntidad),
   FOREIGN KEY (idCuotaParte) REFERENCES CUOTA_PARTE(idCuotaParte),
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
/* Table: TRABAJO                                               */
/*==============================================================*/
CREATE TABLE TRABAJO (
   numeroIdPersona BIGINT NOT NULL,
   nitEntidad BIGINT NOT NULL,
   diasDeServicio BIGINT NOT NULL,
   PRIMARY KEY (numeroIdPersona, nitEntidad),
   FOREIGN KEY (numeroIdPersona) REFERENCES PENSIONADO(numeroIdPersona),
   FOREIGN KEY (nitEntidad) REFERENCES ENTIDAD(nitEntidad)
);
