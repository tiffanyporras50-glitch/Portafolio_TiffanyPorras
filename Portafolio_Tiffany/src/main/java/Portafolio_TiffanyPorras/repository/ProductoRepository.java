package Portafolio_TiffanyPorras.repository;

import Portafolio_TiffanyPorras.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    //Semana 8 - Consulta derivada: recupera los productos de un rango de precio y los ordena por precio ascendentemente
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioInf, BigDecimal precioSup);

    //Semana 8 - Consulta JPQL: misma consulta, escrita sobre la entidad Producto y su atributo precio
    @Query(value = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(BigDecimal precioInf, BigDecimal precioSup);

    //Semana 8 - Consulta SQL nativa: misma consulta, escrita sobre la tabla producto de MySQL
    @Query(nativeQuery = true,
            value = "SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(BigDecimal precioInf, BigDecimal precioSup);
}
