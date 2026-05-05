package tn.enis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.transaction.Transactional;
import tn.enis.dao.ClientRepository;
import tn.enis.dao.CompteDao;
import tn.enis.dao.CompteRepository;
import tn.enis.entity.Client;
import tn.enis.entity.Compte;

@SpringBootApplication
public class FirstSpringBootApplication implements CommandLineRunner {

	private final CompteRepository compteRepository;

	private final ClientRepository clientRepository;
	@Autowired
	private CompteDao compteDao;

	FirstSpringBootApplication(ClientRepository clientRepository, CompteRepository compteRepository) {
		this.clientRepository = clientRepository;
		this.compteRepository = compteRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FirstSpringBootApplication.class, args);
	}

	@Transactional // commit
	@Override
	public void run(String... args) throws Exception {


		 
	}

}
