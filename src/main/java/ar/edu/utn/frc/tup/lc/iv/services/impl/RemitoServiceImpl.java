package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.ComprasClient;
import ar.edu.utn.frc.tup.lc.iv.dtos.request.CrearRemitoRequest;
import ar.edu.utn.frc.tup.lc.iv.dtos.response.RemitoDto;
import ar.edu.utn.frc.tup.lc.iv.entities.DetalleRemitoEntity;
import ar.edu.utn.frc.tup.lc.iv.entities.RemitoEntity;
import ar.edu.utn.frc.tup.lc.iv.repositories.DetalleRemitoRepository;
import ar.edu.utn.frc.tup.lc.iv.repositories.RemitoRepository;
import ar.edu.utn.frc.tup.lc.iv.services.RemitoService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementación de servicio para operaciones relacionadas con remitos.
 */
@Service
public class RemitoServiceImpl implements RemitoService {

    /**
     * Componente utilizado para realizar mapeo de objetos.
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Repositorio para acceder y gestionar
     * entidades de remito en la base de datos.
     */
    @Autowired
    private RemitoRepository remitoRepository;

    /**
     * Repositorio para acceder y gestionar entidades
     * de detalles de remito en la base de datos.
     */
    @Autowired
    private DetalleRemitoRepository detalleRemitoRepository;

    /**
     * Cliente para realizar solicitudes de compras
     * y gestionar la recuperación de errores.
     */
    @Autowired
    private ComprasClient comprasClient;
    /**
     * Crea un nuevo remito a partir de una solicitud
     * y lo almacena en la base de datos.
     *
     * @param remito La solicitud para crear el remito.
     * @return El remito creado.
     */
    @Override
    public RemitoDto crearRemito(final CrearRemitoRequest remito) {
        if (remitoRepository.findByNroRemitoAndNombreProveedor(
                remito.getNroRemito(),
                remito.getNombreProveedor()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Remito with nro: "
                    + remito.getNroRemito()
                    + " and supplier " + remito.getNombreProveedor()
                    + " already exists");
        }

        RemitoEntity remitoEntity =
                modelMapper.map(remito, RemitoEntity.class);
        remitoEntity.getDetalles().forEach(
                detalle -> detalle.setRemito(remitoEntity));
        RemitoEntity remitoEntitySaved = remitoRepository.save(remitoEntity);
        comprasClient.postRemitoId(remitoEntitySaved.getId());

        return modelMapper.map(remitoEntitySaved, RemitoDto.class);
    }

    /**
     * Obtiene una lista de todos los remitos almacenados en la base de datos.
     *
     * @return Una lista de remitos.
     */
    @Override
    public List<RemitoDto> listarRemitos() {
        return List.of(modelMapper.map(
                remitoRepository.findAll(), RemitoDto[].class));
    }

    /**
     * Modifica un remito existente con la información proporcionada.
     *
     * @param remito El remito modificado.
     * @param id     El ID del remito a modificar.
     * @return El remito modificado.
     */
    @Override
    public RemitoDto modificarRemito(final
        CrearRemitoRequest remito, final Long id) {
        Optional<RemitoEntity> remitoActualOptional =
                remitoRepository.findById(id);

        if (remitoActualOptional.isEmpty()) {
            throw new EntityNotFoundException("Remito with ID: " + id
                    + " does not exist");
        }

        RemitoEntity remitoActual = remitoActualOptional.get();

        remitoActual.setNroRemito(remito.getNroRemito());
        remitoActual.setFechaLlegada(remito.getFechaLlegada());
        remitoActual.setNombreProveedor(remito.getNombreProveedor());
        remitoActual.setNroOrdenCompra(remito.getNroOrdenCompra());
        remitoActual.setObservaciones(remito.getObservaciones());

        remitoActual.getDetalles().clear();
        List<DetalleRemitoEntity> detallesNuevos =
                List.of(modelMapper.map(remito.getDetalles(),
                        DetalleRemitoEntity[].class));
        detallesNuevos.forEach(detalle -> {
            detalle.setRemito(remitoActual);
        });
        remitoActual.getDetalles().addAll(detallesNuevos);

        RemitoEntity remitoModified = remitoRepository.save(remitoActual);

        return modelMapper.map(remitoModified, RemitoDto.class);
    }
}

