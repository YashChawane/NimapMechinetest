package com.core.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Category;

@Repository

public interface CategoryDao extends JpaRepository<Category, Long>{

}
