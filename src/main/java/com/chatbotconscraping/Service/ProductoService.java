package com.chatbotconscraping.Service;

import com.chatbotconscraping.Entity.Producto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductoService {

   Producto obtenerProductoJumbo(String producto);
   List<Producto> traerProductos();
   Producto findProducto(String nombre);

}