package tn.enis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import tn.enis.dao.ClientRepository;
import tn.enis.entity.Client;

@AllArgsConstructor
@Transactional
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public void save(Client client) {
        clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void delete(String cin) {
        clientRepository.deleteById(cin);
    }

    public Client getById(String cin) {
        Optional<Client> clientOptional = clientRepository.findById(cin);
        if (clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            return null;
        }
    }

    public void update(Client client) {
        clientRepository.save(client);
    }
}