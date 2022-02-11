package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SeguroRepository extends JpaRepository<Seguro, Long> {
}
