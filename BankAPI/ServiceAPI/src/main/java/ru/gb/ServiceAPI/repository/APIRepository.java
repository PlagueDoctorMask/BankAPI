package ru.gb.ServiceAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gb.ServiceAPI.domain.Client;

@Repository
public interface APIRepository extends JpaRepository<Client, Long>{


    @Query("SELECT c FROM Client c WHERE c.phoneNumber = :phoneNumber")
    Client findByPhone(String phoneNumber);

    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Client findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.fullName = :fullName")
    Client findByName(String fullName);

}
