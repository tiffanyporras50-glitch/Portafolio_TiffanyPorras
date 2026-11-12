package Portafolio_TiffanyPorras.repository;

import Portafolio_TiffanyPorras.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

//Semana 10
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Consulta derivada: busca el usuario por su username (el campo del login) y que esté activo
    Optional<Usuario> findByUsernameAndActivoTrue(String username);
}
