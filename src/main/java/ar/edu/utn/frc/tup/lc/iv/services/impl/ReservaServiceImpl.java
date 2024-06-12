package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.DetalleCrearReservaRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleReservaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.DetalleResumenReserva;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ExistenciaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.ReservaResumenResponse;
import ar.edu.utn.frc.tup.lc.iv.entities.DetalleReservaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ExistenciaEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.LoteEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.ReservaEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ExistenciaRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.LoteRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.ReservaRepository;
import ar.edu.utn.frc.tup.lc.iv.services.LoteService;
import ar.edu.utn.frc.tup.lc.iv.services.ReservaService;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * service de reservas.
 */
@Service
public class ReservaServiceImpl implements ReservaService {
    /**
     * reservaRepository.
     */
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * loteRepository.
     */
    @Autowired
    private LoteRepository loteRepository;

    /**
     * existenciaRepository.
     */
    @Autowired
    private ExistenciaRepository existenciaRepository;

    /**
     * detalleReservaRepository.
     */
    @Autowired
    private DetalleReservaRepository detalleReservaRepository;

    /**
     * modelMapper.
     */
    @Autowired
    private ModelMapper modelMapper;
    /**
     * loteService.
     */
    @Autowired
    private LoteService loteService;

    /**
     * crearReserva.
     *
     * @return ReservaResumenResponse
     */
    @Override
    public ReservaResumenResponse crearReserva(final
                         CrearReservaRequest crearReservaRequest) {
        ReservaEntity reservaEntity = new ReservaEntity();

        reservaEntity.setDetalles(crearDetallesReserva(
                crearReservaRequest.getStockReservado(), reservaEntity));

        ReservaEntity nueva = reservaRepository.save(reservaEntity);
        nueva.setDetalles(reservaEntity.getDetalles());
        return resumirReserva(nueva);
    }

    /**
     * consultarReserva.
     *
     * @param id id de consultar reserva
     * @return ReservaDto
     */
    @Override
    public ReservaDto consultarReserva(final Long id) {
        Optional<ReservaEntity> r = reservaRepository.findById(id);
        ReservaDto reservaDto = new ReservaDto();
        if (!r.isEmpty()) {
            reservaDto = modelMapper.map(r, ReservaDto.class);
            List<DetalleReservaDto> rdDto =
                    modelMapper.map(r.get().getDetalles(),
                    new TypeToken<List<DetalleReservaDto>>() {
                    }.getType());
            reservaDto.setDetalles(rdDto);
        } else {
            return null;
        }
        return reservaDto;
    }

    /**
     * listarReservas.
     *
     * @return ReservaResumenResponse
     */
    @Override
    public List<ReservaResumenResponse> listarReservas() {
        List<ReservaEntity> reservas = reservaRepository.findAll();
        List<ReservaResumenResponse> reservasNoConfirmadas = new ArrayList<>();
        if (!reservas.isEmpty()) {
            for (ReservaEntity r : reservas) {
                ReservaResumenResponse reservaDto = resumirReserva(r);
                reservasNoConfirmadas.add(reservaDto);
            }
        } else {
            throw new EntityNotFoundException("No reservations found");
        }
        return reservasNoConfirmadas;
    }

    /**
     * modificarReserva.
     *
     * @return ReservaResumenResponse
     */
    @Override
    public ReservaResumenResponse modificarReserva(final
            CrearReservaRequest crearReservaRequest, final
    Long reservaId) {
        ReservaEntity reservaEntityActual = buscarReservaPorId(reservaId);

        reservaEntityActual.getDetalles().clear();

        reservaEntityActual.getDetalles().addAll(
                crearDetallesReserva(crearReservaRequest.getStockReservado(),
                reservaEntityActual));
        reservaRepository.save(reservaEntityActual);

        return resumirReserva(reservaEntityActual);
    }

    /**
     * eliminarReserva.
     *
     * @return ReservaResumenResponse
     */
    @Override
    public ReservaResumenResponse eliminarReserva(final
                                                      Long reservaId) {
        ReservaEntity reservaEntityActual = buscarReservaPorId(reservaId);

        reservaRepository.delete(reservaEntityActual);

        return resumirReserva(reservaEntityActual);
    }

    /**
     * confirmarReserva.
     *
     * @return ReservaResumenResponse
     */
    @Override
    public ReservaResumenResponse confirmarReserva(final
                                                       Long reservaId) {
        ReservaEntity reservaEntityActual = buscarReservaPorId(reservaId);


        reservaRepository.delete(reservaEntityActual);
        for (DetalleReservaEntity detalle : reservaEntityActual.getDetalles()) {
            loteService.restarStock(detalle.getLote(),
                    (int) detalle.getCantidad());
        }

        return resumirReserva(reservaEntityActual);
    }

    /**
     * metodo para buscar una reserva por id.
     *
     * @param reservaId id de la reserva a buscar
     * @return ReservaEntity
     */
    private ReservaEntity buscarReservaPorId(final Long reservaId) {
        Optional<ReservaEntity> reservaEntityOptional =
                reservaRepository.findById(reservaId);
        if (reservaEntityOptional.isEmpty()) {
            throw new EntityNotFoundException("Reservation with id:"
                    + reservaId + " dosen't exist");
        }
        return reservaEntityOptional.get();
    }

    /**
     * verificarReserva.
     *
     * @param detallesRequest datos de los detalles
     * @return boolean
     */
    private boolean verificarReserva(final
                    List<DetalleCrearReservaRequest> detallesRequest) {
        List<DetalleCrearReservaRequest> duplicates = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (DetalleCrearReservaRequest i : detallesRequest) {
            if (set.contains(i.getCodigoExistencia())) {
                return false;
            } else {
                set.add(i.getCodigoExistencia());
            }
        }
        return true;
    }

    /**
     * crearDetallesReserva.
     * @param detallesRequest
     * @param reservaEntity
     * @return List<DetalleReservaEntity>
     */
    private List<DetalleReservaEntity> crearDetallesReserva(
            final List<DetalleCrearReservaRequest> detallesRequest,
            final ReservaEntity reservaEntity) {
        List<DetalleReservaEntity> detalleList = new ArrayList<>();

        if (!verificarReserva(detallesRequest)) {
            throw new IllegalArgumentException("Existance can't be reapeated");
        }

        for (DetalleCrearReservaRequest detalleRequest : detallesRequest) {
            Optional<ExistenciaEntity> existenciaEntity =
                    existenciaRepository.findById(
                            detalleRequest.getCodigoExistencia());
            if (existenciaEntity.isEmpty()) {
                throw new EntityNotFoundException("Existance:"
                        + detalleRequest.getCodigoExistencia()
                        + " not  found");
            }

            List<LoteEntity> loteEntityList =
                    loteRepository.findByExistencia(existenciaEntity.get(),
                            Sort.by(Sort.Direction.ASC, "fechaVencimiento"));
            if (loteEntityList.isEmpty()) {
                throw new IllegalArgumentException("Existance:"
                        + detalleRequest.getCodigoExistencia()
                        + " don't have any stock");
            }

            List<LoteEntity> lotesElegidos = new ArrayList<>();
            double cantidadDisponible = 0;
            for (LoteEntity lote : loteEntityList) {

                List<DetalleReservaEntity> reservasAnteriores =
                        detalleReservaRepository.findAllByLote(lote);
                double cantidadAnteriorReservada = 0;
                for (DetalleReservaEntity detalleReservaEntity
                        : reservasAnteriores) {
                    if (detalleReservaEntity.getReserva() != reservaEntity) {
                        cantidadAnteriorReservada +=
                                detalleReservaEntity.getCantidad();
                    }
                }

                cantidadDisponible +=
                        lote.getCantidad() - cantidadAnteriorReservada;
                lotesElegidos.add(lote);
            }
            if (cantidadDisponible < detalleRequest.getCantidad()) {
                throw new IllegalArgumentException("Existance:"
                        + detalleRequest.getCodigoExistencia()
                        + " don't have stock enough");
            }

            double cantidadReserva = detalleRequest.getCantidad();

            for (LoteEntity lote : lotesElegidos) {
                DetalleReservaEntity entity = new DetalleReservaEntity();
                entity.setReserva(reservaEntity);
                entity.setLote(lote);
                if (lote.getCantidad() > cantidadReserva) {
                    entity.setCantidad(cantidadReserva);
                } else {
                    entity.setCantidad(lote.getCantidad());
                }
                detalleList.add(entity);
                cantidadReserva -= entity.getCantidad();
                if (cantidadReserva <= 0) {
                    break;
                }
            }
        }
        return detalleList;
    }

    /**
     * resumirReserva.
     *
     * @param entidad datos de la reserva a resumir
     * @return ReservaResumenResponse
     */
    private ReservaResumenResponse resumirReserva(final
                                                  ReservaEntity entidad) {

        ReservaResumenResponse responce =
                new ReservaResumenResponse(
                        entidad.getId(), null);
        List<DetalleResumenReserva> detalles = new ArrayList<>();
        for (DetalleReservaEntity detalleReservaEntity
                : entidad.getDetalles()) {
            Optional<DetalleResumenReserva> encontrarExistencia =
                    detalles.stream().
                    filter(d -> d.getExistencia().getCodigo()
                            ==
                    detalleReservaEntity.getLote().getExistencia().getCodigo())
                    .findFirst();
            if (encontrarExistencia.isPresent()) {
                encontrarExistencia.get()
                        .setCantidad(encontrarExistencia.get().getCantidad()
                        + detalleReservaEntity.getCantidad());
            } else {
                detalles.add(new DetalleResumenReserva(
                        modelMapper.map(
                                detalleReservaEntity.getLote().getExistencia(),
                                ExistenciaDto.class),
                        detalleReservaEntity.getCantidad()));
            }
        }
        responce.setStockReservado(detalles);
        return responce;
    }

}
