package ru.sfedu.HospitalityNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sfedu.HospitalityNetwork.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
