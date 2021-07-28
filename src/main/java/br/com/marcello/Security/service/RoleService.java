package br.com.marcello.Security.service;

import br.com.marcello.Security.model.Role;
import org.springframework.data.repository.query.Param;

public interface RoleService {

    Role saveRole(Role role);

    Role findRoleById(Long id);

    Role findRoleByName(@Param("roleName") String roleName);

}
