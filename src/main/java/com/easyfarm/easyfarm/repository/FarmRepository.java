package com.easyfarm.easyfarm.repository;

import com.easyfarm.easyfarm.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

}
