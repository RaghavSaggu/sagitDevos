package com.sagitDevos.repository;

import com.sagitDevos.model.enitities.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ILaptopJpaRepo extends JpaRepository<Laptop, Integer> {
    List<Laptop> findAllByBrandName(String brandName);

    List<Laptop> findAllByBrandNameContaining(String brandName);

    List<Laptop> findAllByLidGreaterThanEqual(Integer id);

    @Query("FROM LAPTOP ORDER BY lid")
    List<Laptop> findAllSortedByBrandName();

    List<Laptop> findAllByLidInOrderByBrandName(List<Integer> ids);

    List<Laptop> findAllByLidIn(List<Integer> ids);
}
