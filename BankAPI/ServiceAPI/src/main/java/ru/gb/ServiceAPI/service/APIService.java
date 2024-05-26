package ru.gb.ServiceAPI.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import ru.gb.ServiceAPI.aspect.TrackUserAction;
import ru.gb.ServiceAPI.domain.Client;
import ru.gb.ServiceAPI.repository.APIRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class APIService extends Thread{

    private final APIRepository apiRepository;

    /**
     * возвращает список всех аккаунтов
     * @return список всех аккаунтов
     */
    @TrackUserAction
    public List<Client> getAllClients(){
        return apiRepository.findAll();
    }

    /**
     * Создание нового аккаунта
     * @param client - данные, которые должны быть у созданного аккаунта
     * @return - созданный аккаунт
     */
    @TrackUserAction
    public Client createClient(Client client){
        List<Client> clients = apiRepository.findAll();
        long AN;
        if (clients.isEmpty()){
            AN = 0L;
        }else{
            AN = clients.get(clients.size()-1).getAccount_number() + 2L;
        }
        client.setAccount_number(AN);
        client.setAccountBalance(new BigDecimal(500));
        apiRepository.save(client);
        return client;
    }

    /**
     * удаление аккаунта
     * @param id - id аккаунта, который надо удалить
     */
    @TrackUserAction
    public void deleteById(Long id){
        apiRepository.deleteById(id);
    }

    /**
     * фильтрация аккаунтов по дате рождения
     * @param birthdate - дата рождения, по которой проиходит фильтрация согласно условию: "дата рождения больше чем переданный в запросе"
     * @return аккаунты прошедшие фильтрацию
     */
    @TrackUserAction
    public List<Client> filterUsersByAge(String birthdate) {
        List<Client> clients = apiRepository.findAll();
        LocalDate date = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int age = 2024 - date.getYear();
        return clients.stream().filter(client -> client.getAge() < age).collect(Collectors.toList());
    }

    /**
     * поиск по email
     * @param email - email, который должен иметь искомый аккаунт
     * @return - аккаунт с email идентичным заданному
     */
    @TrackUserAction
    public Client getByEmail(String email){
        return apiRepository.findByEmail(email);
    }

    /**
     * поиск по номеру телефона
     * @param phoneNumber - номер телефона, который должен иметь искомый аккаунт
     * @return - аккаунт с номером телефона, идентичным заданному
     */
    @TrackUserAction
    public Client getByPhoneNumber(String phoneNumber){
        return apiRepository.findByPhone(phoneNumber);
    }

    /**
     *  поиск по ФИО
     * @param fullName - ФИО, который должен иметь искомый аккаунт
     * @return - аккаунт с ФИО, идентичным заданному
     */
    @TrackUserAction
    public Client getByName(String fullName){
        return apiRepository.findByName(fullName);
    }

    /**
     * Поток увеличивающий баланс всех аккаунтов согласно условию задания
     */
    @TrackUserAction
    @Transactional
    @Override
    public void run() {
        List<Client> clients = apiRepository.findAll();
        while(!clients.isEmpty()){
            for (int i = 0; i < clients.size(); i++){
                if(clients.get(i).getAccountBalance().multiply(new BigDecimal("0.05")).compareTo(new BigDecimal("500").multiply(new BigDecimal(2.07))) < 0){
                    clients.get(i).setAccountBalance(clients.get(i).getAccountBalance().add(clients.get(i).getAccountBalance().multiply(new BigDecimal("0.05"))));
                    apiRepository.save(clients.get(i));
                }else{
                    clients.remove(clients.get(i));
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
