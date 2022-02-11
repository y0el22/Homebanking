package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.ClientSeguro;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientSeguroRepository extends JpaRepository<ClientSeguro, Long> {
}
