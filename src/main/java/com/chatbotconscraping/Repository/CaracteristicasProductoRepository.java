package com.chatbotconscraping.Repository;

import com.chatbotconscraping.Entity.CaracteristicasProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicasProductoRepository extends JpaRepository<CaracteristicasProducto,Integer> {
}
