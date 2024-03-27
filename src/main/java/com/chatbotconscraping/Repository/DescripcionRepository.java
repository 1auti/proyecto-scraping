package com.chatbotconscraping.Repository;

import com.chatbotconscraping.Entity.Descripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescripcionRepository extends JpaRepository<Descripcion,Integer> {
}
