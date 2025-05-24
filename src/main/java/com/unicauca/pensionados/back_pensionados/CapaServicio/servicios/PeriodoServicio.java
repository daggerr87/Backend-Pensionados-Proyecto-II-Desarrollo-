package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
//import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Autowired
    private IPCRepositorio ipcRepositorio;

    @Override
    @Transactional
    public void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte) {
        List<Periodo> periodos = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        int anioInicio = fechaInicioPension.getYear();
        int anioActual = fechaActual.getYear();

        // Obtener los IPC desde el año de inicio
        List<IPC> IPCApartirFechaPension = ipcRepositorio.findByFechaIPCGreaterThanEqual(anioInicio);

        // Mapa de acceso por año
        Map<Integer, IPC> ipcPorAnio = IPCApartirFechaPension.stream()
            .collect(Collectors.toMap(IPC::getFechaIPC, Function.identity()));

        for (int anio = anioInicio; anio <= anioActual; anio++) {
            LocalDate inicioPeriodo;
            LocalDate finPeriodo;

            if (anio == anioInicio) {
                inicioPeriodo = fechaInicioPension;
                finPeriodo = LocalDate.of(anio, 12, 31);
            } else if (anio == anioActual) {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                finPeriodo = fechaActual; // ← Usa la fecha actual exacta
            } else {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                finPeriodo = LocalDate.of(anio, 12, 31);
            }

            // Calcular mesadas
            BigDecimal numeroMesadas = calcularMesadas(inicioPeriodo, finPeriodo);

            Periodo periodo = new Periodo();
            periodo.setFechaInicioPeriodo(inicioPeriodo);
            periodo.setFechaFinPeriodo(finPeriodo);
            periodo.setNumeroMesadas(numeroMesadas);

            IPC ipc = ipcPorAnio.get(anio);
            periodo.setIPC(ipc);  // Puede ser null si no se encuentra

            // Valores aleatorios
            //CAMBIAR AQUI PARA EL CALCULO CORRECTO DE CADA PERIODO
            BigDecimal valorPension = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal cuotaParteMensual = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal cuotaParteTotalAnio = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));
            BigDecimal incrementoLey476 = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000));

            periodo.setValorPension(valorPension);
            periodo.setCuotaParteMensual(cuotaParteMensual);
            periodo.setCuotaParteTotalAnio(cuotaParteTotalAnio);
            periodo.setIncrementoLey476(incrementoLey476);
            periodo.setCuotaParte(cuotaParte);

            periodos.add(periodo);
        }

        periodoRepositorio.saveAll(periodos);
    }

    public BigDecimal calcularMesadas(LocalDate inicio, LocalDate fin) {
        BigDecimal totalMesadas = BigDecimal.ZERO;

        int anioInicio = inicio.getYear();
        int anioFin = fin.getYear();

        for (int anio = anioInicio; anio <= anioFin; anio++) {
            LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
            LocalDate finAnio = LocalDate.of(anio, 12, 31);

            LocalDate desde = (inicio.isAfter(inicioAnio)) ? inicio : inicioAnio;
            LocalDate hasta = (fin.isBefore(finAnio)) ? fin : finAnio;

            long diasPeriodo = ChronoUnit.DAYS.between(desde, hasta) + 1;
            long diasAnio = ChronoUnit.DAYS.between(inicioAnio, finAnio) + 1;

            if (diasPeriodo <= 0) continue;

            // Mesadas ordinarias
            BigDecimal mesadas = BigDecimal.valueOf(12)
                .multiply(BigDecimal.valueOf(diasPeriodo))
                .divide(BigDecimal.valueOf(diasAnio), 5, RoundingMode.HALF_UP);

            // Junio adicional
            LocalDate inicioJunio = LocalDate.of(anio, 6, 1);
            LocalDate finJunio = YearMonth.of(anio, 6).atEndOfMonth();
            if (anio >= 1994 && !hasta.isBefore(inicioJunio) && !desde.isAfter(finJunio)) {
                long diasEnJunio = ChronoUnit.DAYS.between(inicioJunio, finJunio) + 1;
                long diasCubrimiento = ChronoUnit.DAYS.between(
                    desde.isAfter(inicioJunio) ? desde : inicioJunio,
                    hasta.isBefore(finJunio) ? hasta : finJunio
                ) + 1;
                if (diasCubrimiento > 0) {
                    BigDecimal proporcion = BigDecimal.valueOf(diasCubrimiento)
                        .divide(BigDecimal.valueOf(diasEnJunio), 5, RoundingMode.HALF_UP);
                    mesadas = mesadas.add(proporcion);
                }
            }

            // Diciembre adicional
            LocalDate inicioDic = LocalDate.of(anio, 12, 1);
            LocalDate finDic = YearMonth.of(anio, 12).atEndOfMonth();
            if (!hasta.isBefore(inicioDic) && !desde.isAfter(finDic)) {
                long diasEnDic = ChronoUnit.DAYS.between(inicioDic, finDic) + 1;
                long diasCubrimiento = ChronoUnit.DAYS.between(
                    desde.isAfter(inicioDic) ? desde : inicioDic,
                    hasta.isBefore(finDic) ? hasta : finDic
                ) + 1;
                if (diasCubrimiento > 0) {
                    BigDecimal proporcion = BigDecimal.valueOf(diasCubrimiento)
                        .divide(BigDecimal.valueOf(diasEnDic), 5, RoundingMode.HALF_UP);
                    mesadas = mesadas.add(proporcion);
                }
            }

            totalMesadas = totalMesadas.add(mesadas);
        }
        return totalMesadas.setScale(2, RoundingMode.HALF_UP);
    }

}
