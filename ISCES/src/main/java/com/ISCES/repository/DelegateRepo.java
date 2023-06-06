package com.ISCES.repository;

import com.ISCES.entities.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegateRepo extends JpaRepository<Delegate,Long> {
}
