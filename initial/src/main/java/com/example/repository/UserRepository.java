package com.example.repository;
import org.springframework.data.repository.CrudRepository;
import com.example.springboot.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
}