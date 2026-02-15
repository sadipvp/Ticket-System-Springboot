package com.ticketing.ticketsystem.Repository;

import com.ticketing.ticketsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);

    List<User> findByRole(String role);
}
