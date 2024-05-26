package ru.gb.Client.service;

import lombok.AllArgsConstructor;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.Client.domain.Client;
import ru.gb.Client.repository.ClientRepository;
import ru.gb.Client.aspect.TrackUserAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Получение текущего баланса на аккаунте
     * @param id - id аккаунта, на котором необходимо узнать баланс
     * @return баланс аккаунта с заданным id
     */
    @TrackUserAction
    public BigDecimal viewBalance(Long id){

        return clientRepository.findById(id).get().getAccountBalance();
    }

    /**
     * фильтрация аккаунтов по дате рождения
     * @param birthdate - дата рождения, по которой проиходит фильтрация согласно условию: "дата рождения больше чем переданный в запросе"
     * @return аккаунты прошедшие фильтрацию
     */
    @TrackUserAction
    public List<Client> filterUsersByAge(String birthdate) {
        List<Client> clients = clientRepository.findAll();
        LocalDate date = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int year = date.getYear();
        return clients.stream().filter(client -> client.getAge() < year).collect(Collectors.toList());
    }

    /**
     * поиск по email
     * @param email - email, который должен иметь искомый аккаунт
     * @return - аккаунт с email идентичным заданному
     */
    @TrackUserAction
    public Client getByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    /**
     * поиск по номеру телефона
     * @param phoneNumber - номер телефона, который должен иметь искомый аккаунт
     * @return - аккаунт с номером телефона, идентичным заданному
     */
    @TrackUserAction
    public Client getByPhoneNumber(String phoneNumber){
        return clientRepository.findByPhone(phoneNumber);
    }

    /**
     *  поиск по ФИО
     * @param fullName - ФИО, который должен иметь искомый аккаунт
     * @return - аккаунт с ФИО, идентичным заданному
     */
    @TrackUserAction
    public Client getByName(String fullName){
        return clientRepository.findByName(fullName);
    }

    /**
     * Изменение номера телефона по id и паролю с проверкой на уникальность
     * @param id - id аккаунта, на котором требуется изменить номер телефона
     * @param newPhoneNumber - дополнительный номер телефона
     * @param password - пароль от аккаунта, на котором требуется сменить номер телефона
     */
    @TrackUserAction
    public void editPhoneNumber(Long id, String newPhoneNumber, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){//проверка на совпадение паролей
            List<Client> phones = clientRepository.findAll().stream().
                    filter(client -> Objects.equals(client.getPhoneNumber(), newPhoneNumber)|| Objects.equals(client.getExtraPhoneNumber(), newPhoneNumber)).
                    toList();//проверка на уникальность
            if(phones.isEmpty()){
                clientRepository.findById(id).get().setPhoneNumber(newPhoneNumber);
                clientRepository.save(clientRepository.findById(id).get());
            }else{
                throw new RuntimeException("Phone number must be unique");
            }

        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Изменение почты по id по паролю с проверко на уникальность
     * @param id - id аккаунта, на котором требуется сменить почту
     * @param newEmail - новая уникальная почта, на которую произойдёт смена
     * @param password - пароль от аккаунта, на котором произойдёт смена
     */
    @TrackUserAction
    public void editEmail(Long id, String newEmail, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){//проверка на совпадение паролей
            List<Client> emails = clientRepository.findAll().stream().
                    filter(client -> Objects.equals(client.getEmail(), newEmail)|| Objects.equals(client.getExtraEmail(), newEmail)).
                    toList();//проверка на уникальность
            if(emails.isEmpty()){
                clientRepository.findById(id).get().setEmail(newEmail);
                clientRepository.save(clientRepository.findById(id).get());
            }else{
                throw new RuntimeException("Email must be unique");
            }

        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Добавление дополнительного номера телефона, соответствующий уникальности, по id и паролю
     * @param id - id аккаунта, на которы требуется добавить дополнительный номер телефона
     * @param phoneNumber - новый дополнительный номер телефона, который требуется добавить
     * @param password - пароль от аккаунта, на который требуется добавить дополнительный номер телефона
     */
    @TrackUserAction
    public void addExtraPhoneNumber(Long id, String phoneNumber, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){//проверка на совпадение паролей
            List<Client> phones = clientRepository.findAll().stream()
                    .filter(client -> Objects.equals(client.getPhoneNumber(), phoneNumber) || Objects.equals(client.getExtraPhoneNumber(), phoneNumber))
                    .toList();//поверка на уникальность
            if(phones.isEmpty()){
                clientRepository.findById(id).get().setExtraPhoneNumber(phoneNumber);
                clientRepository.save(clientRepository.findById(id).get());
            }else{
                throw new RuntimeException("Phone number must be unique");
            }

        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Добавление дополнительной почты, соответствующей уникальности, по id и паролю
     * @param id - id аккаунта, на который требуется добавить новую дополнительную почту
     * @param email - новая дополнительная почта, соответствующая уникальности, которую требуется добавить
     * @param password - пароль от аккаунта, на котором требуется добавить дополнительную почту
     */
    @TrackUserAction
    public void addExtraEmail(Long id, String email, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){//проверка совпеадения паролей
            List<Client> emails = clientRepository.findAll().stream().
                    filter(client -> Objects.equals(client.getEmail(), email)|| Objects.equals(client.getExtraEmail(), email)).
                    toList();//проверка на уникальность
            if(emails.isEmpty()){
                clientRepository.findById(id).get().setExtraEmail(email);
                clientRepository.save(clientRepository.findById(id).get());
            }else{
                throw new RuntimeException("Email must be unique");
            }

        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Удаление дополнительного номера телефона по id и паролю
     * @param id - id аккаунта, на котором требуется удалить дополнительный номер телефона
     * @param password - пароль от аккаунта, на котором требуется удалить дополнительный номер телефона
     */
    @TrackUserAction
    public void deleteExtraPhoneNumber(Long id, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){
                clientRepository.findById(id).get().setExtraPhoneNumber(null);
                clientRepository.save(clientRepository.findById(id).get());
        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Удаление дополнительной почты по id и паролю
     * @param id - id аккаунта, на котором надо удалить дополнительную почту
     * @param password - пароль от аккаунта, на котором требуется удалить дополнительную почту
     */
    @TrackUserAction
    public void deleteExtraEmail(Long id, String password){
        if(Objects.equals(clientRepository.findById(id).get().getPassword(), password)){
            clientRepository.findById(id).get().setExtraEmail(null);
            clientRepository.save(clientRepository.findById(id).get());
        }else{
            throw new RuntimeException("Wrong password");
        }
    }

    /**
     * Изменение дополнительного номера телефона по id и паролю с проверкой на уникальность
     * @param id - id аккаунта, на котором требуется смена дополнительного номера телефона
     * @param newPhoneNumber - новый дополнительный номер телефона, удовлетврояющий уникальности
     * @param password - пароль от аккаунта, на котором требуется изменить дополнительный номер телефона
     */
    @TrackUserAction
    public void editExtraPhoneNumber(Long id, String newPhoneNumber, String password){
        deleteExtraPhoneNumber(id, password);
        addExtraPhoneNumber(id, newPhoneNumber, password);
    }

    /**
     * Изменение дополнительной почты по id и паролю с проверкой на уникальность
     * @param id - id аккаунта, на котором требуется провести смену дополнительной почты
     * @param newEmail - новая дополнительная почта, удовлетворяющая уникальности
     * @param password - пароль от аккаунта, на котором требуется изменить дополнительную почту
     */
    @TrackUserAction
    public void editExtraEmail(Long id, String newEmail, String password){
        deleteExtraEmail(id, password);
        addExtraEmail(id, newEmail, password);
    }

    /**
     * Метод передачи денежных средств между аккаунтами с проверкой на то, что баланс не может быть отрицательным
     * @param idSender - id отправителя
     * @param idReceiver - id получателя
     * @param amount - отправляемая сумма
     */
    @TrackUserAction
    @Transactional
    public void transferMoney(long idSender, long idReceiver, BigDecimal amount) {
        Client sender = clientRepository.findById(idSender).orElse(null);
        Client receiver = clientRepository.findById(idReceiver).orElse(null);

        BigDecimal senderNewAmount = sender.getAccountBalance().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAccountBalance().add(amount);

        if (senderNewAmount.compareTo(new BigDecimal(0L))>=0){//проверка
            clientRepository.findById(sender.getId()).get().setAccountBalance(senderNewAmount);
            clientRepository.findById(receiver.getId()).get().setAccountBalance(receiverNewAmount);
            clientRepository.save(clientRepository.findById(sender.getId()).get());
            clientRepository.save(clientRepository.findById(receiver.getId()).get());
        }else{
            throw new RuntimeException("it cannot be negative balance");
        }

    }

}
