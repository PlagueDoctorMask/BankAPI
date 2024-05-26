package ru.gb.Client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gb.Client.domain.Client;

import java.math.BigDecimal;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /*@Query("UPDATE clients SET accountBalance = :senderNewAmount WHERE id = :idSender")
    void changeAmount(long idSender, BigDecimal senderNewAmount);*/


    @Query("SELECT c FROM Client c WHERE c.phoneNumber = :phoneNumber")
    Client findByPhone(String phoneNumber);

    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Client findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.fullName = :fullName")
    Client findByName(String fullName);
}
