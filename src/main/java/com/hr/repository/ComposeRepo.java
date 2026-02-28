package com.hr.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.hr.entity.Compose;

@Repository
public interface ComposeRepo extends JpaRepository<Compose, Integer> {

    long countByStatus(String status);

    List<Compose> findTop5ByOrderByCreatedDateDesc(); 
    
    List<Compose> findByParentUkid(Integer parentUkid); 
    
    
    List<Compose> findByParentUkidAndStatus(Integer parentUkid, String status);
    
    
    long countByParentUkidAndStatus(Integer parentUkid, String status);
    long countByParentUkid(Integer parentUkid);
}