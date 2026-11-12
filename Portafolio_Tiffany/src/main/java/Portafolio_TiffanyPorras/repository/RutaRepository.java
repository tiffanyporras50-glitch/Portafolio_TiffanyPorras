package Portafolio_TiffanyPorras.repository;

import Portafolio_TiffanyPorras.domain.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

//Semana 10
public interface RutaRepository extends JpaRepository<Ruta, Integer> {

    //Consulta derivada: todas las rutas, ordenadas por requiereRol
    List<Ruta> findAllByOrderByRequiereRolAsc();
}
