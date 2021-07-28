package br.com.marcello.Security.repository;

import br.com.marcello.Security.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    @Query(value = "SELECT * FROM user_accounts ua WHERE ua.username = :username", nativeQuery = true)
    UserAccount findAccountByUsername(@Param("username") String username);

    @Query(value = "DELETE FROM user_accounts ua WHERE ua.username = :username", nativeQuery = true)
    void deleteAccountByUsername(@Param("username") String username);

}
