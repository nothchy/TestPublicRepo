package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
        SELECT id, first_name, last_name, middle_name, email, deleted
        FROM (
           SELECT DISTINCT ON (u.id)
              u.*, s.started_at_utc
           FROM users AS u
           RIGHT JOIN sessions AS s ON u.id = s.user_id
           WHERE s.device_type = 1
           ORDER BY u.id, s.started_at_utc DESC
        )
        ORDER BY started_at_utc DESC""", nativeQuery = true)
    List<User> getUsersWithAtLeastOneMobileSession();

    @Query(value = """
        SELECT u.* FROM users AS u
        RIGHT JOIN sessions AS s ON u.id = s.user_id
        GROUP BY u.id
        ORDER BY COUNT(*) DESC
        LIMIT 1""", nativeQuery = true)
    User getUserWithMostSessions();
}
