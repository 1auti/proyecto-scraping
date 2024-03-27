package com.chatbotconscraping.Repository;

import com.chatbotconscraping.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {

    Producto findProducto(String nombre);
}
