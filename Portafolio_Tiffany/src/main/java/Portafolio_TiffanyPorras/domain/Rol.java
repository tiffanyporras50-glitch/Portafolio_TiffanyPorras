package Portafolio_TiffanyPorras.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

//Semana 10 - Mapea la tabla rol (ADMIN, VENDEDOR, USER)
@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "rol", unique = true, length = 25)
    private String rol;
}
