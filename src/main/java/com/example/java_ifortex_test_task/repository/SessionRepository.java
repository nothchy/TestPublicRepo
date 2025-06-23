package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = """
        SELECT id, device_type - 1 AS device_type, ended_at_utc, started_at_utc, user_id FROM sessions
        WHERE device_type = 2
        ORDER BY started_at_utc DESC
        LIMIT 1""", nativeQuery = true)
    Session getFirstDesktopSession(DeviceType deviceType);

    @Query(value = """
        SELECT s.id, device_type - 1 AS device_type, ended_at_utc, started_at_utc, user_id FROM sessions AS s
        JOIN users AS u ON s.user_id = u.id
        WHERE u.deleted = false AND s.ended_at_utc < '2025-01-01'
        ORDER BY s.started_at_utc DESC""", nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(LocalDateTime endDate);
}