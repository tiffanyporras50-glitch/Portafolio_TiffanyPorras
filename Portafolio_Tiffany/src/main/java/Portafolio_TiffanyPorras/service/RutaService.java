package Portafolio_TiffanyPorras.service;

import Portafolio_TiffanyPorras.domain.Ruta;
import Portafolio_TiffanyPorras.repository.RutaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Semana 10
@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @Transactional(readOnly = true)
    public List<Ruta> getRutas() {
        return rutaRepository.findAllByOrderByRequiereRolAsc();
    }
}
