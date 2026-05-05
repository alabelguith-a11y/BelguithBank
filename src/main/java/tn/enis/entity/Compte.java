package tn.enis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "t_compte")
public class Compte {
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private Integer rib;
	private float solde;
	
	@ManyToOne // à elle seule, FK
	@JoinColumn(name="id_client")
	private Client client;
	// Fetch: manière de chargement des données
	// EAGER --> le client se charge dès le chargement d'un compte
	//LAZY --> Le client ne se charge pas au chargement du compte,
	// il se charge lors de l'appelle de 'compte'.getClient()
	//Default Fetch depuis JPA2.0
	// 1--> EAGER
	// * --> LAZY
	public Compte(float solde, Client client) {
		super();
		this.solde = solde;
		this.client = client;
	}
	
	

}
