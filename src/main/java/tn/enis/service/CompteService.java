package tn.enis.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import tn.enis.dao.CompteRepository;
import tn.enis.entity.Compte;

@AllArgsConstructor
@Transactional
@Service
public class CompteService {

    private final CompteRepository compteRepository;

    public void save(Compte compte) {
        compteRepository.save(compte);
    }

    public List<Compte> findAll() {
        return compteRepository.findAll();
    }

    public void delete(Integer rib) {
        compteRepository.deleteById(rib);
    }

    public Compte getById(Integer rib) {
        Optional<Compte> compteOptional = compteRepository.findById(rib);
        if (compteOptional.isPresent()) {
            return compteOptional.get();
        } else {
            return null;
        }
    }

    public void update(Compte compte) {
        compteRepository.save(compte);
    }

    public List<Compte> findByClientCin(String cin) {
        return compteRepository.findByClientCin(cin);
    }
}