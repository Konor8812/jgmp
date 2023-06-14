package com.illia.module03chatgpt.persistance.repository;

import com.illia.module03chatgpt.persistance.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

}