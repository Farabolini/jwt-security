package br.com.marcello.Security.service;

import br.com.marcello.Security.model.Role;
import br.com.marcello.Security.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role findRoleById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findRoleByName(String roleName) {
        return this.roleRepository.findRoleByName(roleName);
    }
}
