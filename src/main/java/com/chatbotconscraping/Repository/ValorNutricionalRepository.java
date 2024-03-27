package com.chatbotconscraping.Repository;

import com.chatbotconscraping.Entity.ValorNutricional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorNutricionalRepository extends JpaRepository<ValorNutricional,Integer> {
}
