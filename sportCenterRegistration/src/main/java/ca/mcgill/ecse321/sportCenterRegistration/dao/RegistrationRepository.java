package ca.mcgill.ecse321.sportCenterRegistration.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.sportCenterRegistration.model.Registration;
import ca.mcgill.ecse321.sportCenterRegistration.model.Account;
import ca.mcgill.ecse321.sportCenterRegistration.model.Session;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {
    public Registration findRegistrationById(int id);

    public Registration findRegistrationByAccountAndSession(Account account, Session session);

    public List<Registration> findRegistrationByAccount(Account account);

    public List<Registration> findRegistrationBySession(Session session);
}
