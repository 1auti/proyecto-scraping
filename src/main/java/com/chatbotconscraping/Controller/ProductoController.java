package com.chatbotconscraping.Controller;

import com.chatbotconscraping.Entity.Producto;
import com.chatbotconscraping.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/informacion")
    public ResponseEntity<Producto> obtenerPrecioProducto(@RequestParam String producto) {
        Producto producto1 = productoService.obtenerProductoJumbo(producto);
        return ResponseEntity.ok(producto1);
    }

    @GetMapping("/traerProductos")
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productoList = productoService.traerProductos();
        return ResponseEntity.ok(productoList);
    }

    @GetMapping("/taerProducto")
    public ResponseEntity<Producto> buscarProducto(String nombreProducto){
        Producto productobuscado = productoService.findProducto(nombreProducto);
        return ResponseEntity.ok(productobuscado);
    }





}
