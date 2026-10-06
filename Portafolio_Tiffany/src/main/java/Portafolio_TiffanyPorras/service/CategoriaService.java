/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Portafolio_TiffanyPorras.service;
 
import Portafolio_TiffanyPorras.domain.Categoria;
import Portafolio_TiffanyPorras.repository.CategoriaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
 
/**
 *
 * @author tiffanyporrasmarin
 */
@Service
public class CategoriaService {
 
    // Los atributos son final para asegurar la inmutabilidad
    private final CategoriaRepository categoriaRepository;
    private final FirebaseStorageService firebaseStorageService;
 
    // Inyección por constructor (no requiere @Autowired en Spring moderno)
    public CategoriaService(CategoriaRepository categoriaRepository,
            FirebaseStorageService firebaseStorageService) {
        this.categoriaRepository = categoriaRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
 
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { // Solo activos
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
 
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }
 
    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) { // Si no está vacío... pasaron una imagen
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "categoria",
                        categoria.getIdCategoria());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {
                System.err.println("Error subiendo la imagen a Firebase: " + e.getMessage());
            }
        }
    }
 
    @Transactional
    public void delete(Integer idCategoria) {
        // Verifica si la categoría existe antes de intentar eliminarla
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepción para indicar que la categoría no fue encontrada
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoría. Tiene datos asociados.", e);
        }
    }
}
 
