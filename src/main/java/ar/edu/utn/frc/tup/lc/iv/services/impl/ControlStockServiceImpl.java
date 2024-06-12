package ar.edu.utn.frc.tup.lc.iv.services.impl;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.ModificarControlStockRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ControlStockResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleEstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.EstadisticaResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.ControlDeStockEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.ControlStockRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.services.ControlStockService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


/**
 * Implementacion del service de controlStock.
 */
@Service
public class ControlStockServiceImpl implements ControlStockService {

    /**
     * Repositorio para acceder y gestionar
     * entidades de control de stock en la base de datos.
     */
    @Autowired
    private ControlStockRepository controlStockRepository;

    /**
     * Repositorio para acceder y gestionar
     * entidades de lotes en la base de datos.
     */
    @Autowired
    private LoteRepository loteRepository;

    /**
     * Componente utilizado para realizar mapeo de objetos.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Crea un informe de control de stock.
     *
     * @param request La solicitud para crear un informe de control de stock.
     * @return El informe de control de stock creado.
     */
    @Override
    public ControlStockResponse crearControlStock(final
            CrearControlStockRequest request) {
        Optional<LoteEntity> loteEntityOptional =
                loteRepository.findById(request.getLoteId());
        if (loteEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Batch with id: "
                    + request.getLoteId() + " doesn't founded");
        }

        ControlDeStockEntity controlDeStockEntity =
                modelMapper.map(request, ControlDeStockEntity.class);
        controlDeStockEntity.setId(null);
        controlDeStockEntity.setLote(loteEntityOptional.get());

        return modelMapper.map(
                controlStockRepository.save(controlDeStockEntity),
                ControlStockResponse.class);
    }

    /**
     * Modifica un informe de control de stock existente.
     *
     * @param idControl El ID del informe de control de stock a modificar.
     * @param request    La solicitud para modificar.
     * @return El informe de control de stock modificado.
     */
    @Override
    public ControlStockResponse modificarControlStock(final Long idControl,
                  final ModificarControlStockRequest request) {
        Optional<ControlDeStockEntity> controlDeStockOptional =
                controlStockRepository.findById(idControl);
        if (controlDeStockOptional.isEmpty()) {
            throw new EntityNotFoundException("Report with id: "
                        + idControl + " doesn't founded");
        }

        ControlDeStockEntity controlDeStockEntity =
                controlDeStockOptional.get();
        controlDeStockEntity.setDescripcion(request.getDescripcion());
        controlDeStockEntity.setCantidadDaniada(request.getCantidadDaniada());
        controlDeStockEntity.setLoteVencido(request.isLoteVencido());
        controlDeStockEntity.setCantidadInspeccionada(
                request.getCantidadInspeccionada());

        Optional<LoteEntity> loteEntityOptional =
                loteRepository.findById(request.getLoteId());
        if (loteEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Lote with id "
                    + request.getLoteId()
                    + " doesn't founded");
        }

        LoteEntity loteEntity = loteEntityOptional.get();
        controlDeStockEntity.setLote(loteEntity);

        return modelMapper.map(
                controlStockRepository.save(controlDeStockEntity),
                ControlStockResponse.class);
    }

    /**
     * Elimina un informe de control de stock existente.
     *
     * @param idControl El ID del informe de control de stock a eliminar.
     * @return El informe de control de stock eliminado.
     */
    @Override
    public ControlStockResponse eliminarControlStock(
            final Long idControl) {
        Optional<ControlDeStockEntity> controlDeStockEntityOptional =
                controlStockRepository.findById(idControl);
        if (controlDeStockEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Report with id: "
                    + idControl + " doesn't founded");
        }
        ControlDeStockEntity deletedReport = controlDeStockEntityOptional.get();
        controlStockRepository.delete(controlDeStockEntityOptional.get());

        return modelMapper.map(deletedReport,
                ControlStockResponse.class);
    }

    /**
     * Lista los informes de control de stock del último mes.
     *
     * @return Una lista de informes de control de stock del último mes.
     */
    @Override
    public List<ControlStockResponse> listarControlesDelUltimoMes() {

        LocalDateTime fechaActual = LocalDateTime.now();

        List<ControlDeStockEntity> listadoControlStock =
                controlStockRepository.findAll();
        List<ControlStockResponse> controlesUltimoMes = new ArrayList<>();

        for (int i = 0; i < listadoControlStock.size(); i++) {
            LocalDateTime fechaControl = listadoControlStock.get(i).getFecha();
            if (fechaControl.getMonthValue() == fechaActual.getMonthValue()
                    && fechaControl.getYear() == fechaActual.getYear()) {
                controlesUltimoMes.add(
                        modelMapper.map(listadoControlStock.get(i),
                        ControlStockResponse.class));
            }
        }
        return controlesUltimoMes;
    }

    /**
     * metodo para obtener controles de stock.
     *
     * @return ControlStockResponse[]
     */
    @Override
    public ControlStockResponse[] getControlesDeStock() {
        return modelMapper.map(controlStockRepository.findAll(
                Sort.by(Sort.Direction.ASC, "fecha")),
                ControlStockResponse[].class);
    }



    /**
     * informe de estadisticas.
     *
     * @return ControlStockResponse[]
     */
    @Override
    public EstadisticaResponse getEstadisticas(final String tipo) {
        final int primerPeriodo = 5;
        final int periodo = 6;
        final int escalaDecimal = 4;
        List<ControlDeStockEntity> controlDeStock =
                controlStockRepository.findAll();
        EstadisticaResponse responce = new EstadisticaResponse();
        if (LocalDateTime.now().getMonthValue() <= primerPeriodo) {
            LocalDateTime fecha = LocalDateTime.now();
            responce.setSeasonStart(fecha.with(firstDayOfYear()));
            responce.setSeasonStart(
                    fecha.with(firstDayOfYear()).plusMonths(periodo));
        } else {
            LocalDateTime fecha = LocalDateTime.now();
            responce.setSeasonStart(
                    fecha.with(lastDayOfYear()).minusMonths(periodo));
            responce.setSeasonStart(fecha.with(lastDayOfYear()));
        }

        double total = 0;
        List<DetalleEstadisticaResponse> responceDetalles = new ArrayList();
        for (ControlDeStockEntity control : controlDeStock) {
            Optional<DetalleEstadisticaResponse> repetido =
                    responceDetalles.stream().filter(c -> control.getLote()
                            .getExistencia().getNombre()
                            .equals(c.getName())).findFirst();

            double cantidad = (tipo.toLowerCase().equals("vencida"))
                    ? control.getCantidadInspeccionada()
                    : control.getCantidadDaniada();
            if (repetido.isPresent()) {
                repetido.get().setControls(repetido.get().getControls() + 1);
                repetido.get().setUnits(repetido.get().getUnits() + cantidad);
            } else {
                DetalleEstadisticaResponse detalleNuevo =
                        new DetalleEstadisticaResponse();
                detalleNuevo.setName(control.getLote()
                        .getExistencia().getNombre());
                detalleNuevo.setControls(1);
                detalleNuevo.setUnits(cantidad);
                responceDetalles.add(detalleNuevo);
            }
            total += cantidad;
        }
        for (DetalleEstadisticaResponse detalle : responceDetalles) {
            BigDecimal valor = BigDecimal.valueOf(detalle.getUnits() / total)
                    .setScale(escalaDecimal, RoundingMode.FLOOR);
            detalle.setPercent(valor);
        }
        responceDetalles.sort(
                Comparator.comparing(DetalleEstadisticaResponse::getPercent));
        responce.setItems(modelMapper.map(responceDetalles,
                DetalleEstadisticaResponse[].class));
        return responce;
    }

}
