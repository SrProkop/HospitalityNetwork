package ru.sfedu.HospitalityNetwork.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sfedu.HospitalityNetwork.models.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {
}
