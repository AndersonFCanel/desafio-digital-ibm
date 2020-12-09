package desafioIBM.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import desafioIBM.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

  boolean existsByUsername(String username);

  User findByUsername(String username);
}
