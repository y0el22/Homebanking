package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
* Luego de modificar el archivo debes hacer que el repositorio sea un RestRepository
*  colocando la anotación @RepositoryRestResource que le indica a Spring
*  que debe genera el código necesario para que se pueda administrar la data
* de la aplicación desde el navegador usando JSON, es decir se crea una API REST automática
* que expone los recursos de cada repositorio anotado con @RepositoryRestResource.*/
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

    //spring se encarga en la creacion de consultas
    Client findByEmail(String email);

    //Este se ocupa para traer todos
    //List<Client> findByEmail(String email);
}
