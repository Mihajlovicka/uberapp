package com.example.demo.repository;

import com.example.demo.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatsRepository extends JpaRepository<Chat, Long> {
}
