package com.klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}