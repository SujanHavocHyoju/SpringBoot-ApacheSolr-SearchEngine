package com.quertle.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quertle.demo.model.FierceNews;

@Repository
public interface FierceNewsRepository extends JpaRepository<FierceNews, Integer>{

}
