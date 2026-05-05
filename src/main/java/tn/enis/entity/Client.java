package tn.enis.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "t_client")
public class Client {
	@Id // PK
	@Include
	private String cin;
	private String nom;
	private String prenom;

	public Client(String cin, String nom, String prenom) {
		super();
		this.cin = cin;
		this.nom = nom;
		this.prenom = prenom;
	}

	@JsonIgnore // cassé la boucle Json
	@Exclude // cassé la boucle toString
	// bidirectionenlle
	// mappedBy --> n'ajoute rien dans le model relationnel
	@OneToMany(mappedBy = "client") // a elle seule --> JT
	private List<Compte> comptes;

}
