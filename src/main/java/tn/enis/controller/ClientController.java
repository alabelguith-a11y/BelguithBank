package tn.enis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import tn.enis.dto.ClientDto;
import tn.enis.entity.Client;
import tn.enis.service.ClientService;

@Controller
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @PostMapping("/save")
    public String save(@RequestParam String cin,
                       @RequestParam String nom,
                       @RequestParam String prenom) {
        ClientDto dto = new ClientDto(cin, nom, prenom);
        clientService.save(new Client(dto.cin(), dto.nom(), dto.prenom()));
        return "redirect:/clients";
    }

    @GetMapping("/edit/{cin}")
    public String editForm(@PathVariable String cin, Model model) {
        Client client = clientService.getById(cin);
        if (client == null) return "redirect:/clients";
        ClientDto dto = new ClientDto(client.getCin(), client.getNom(), client.getPrenom());
        model.addAttribute("editing", dto);
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @PostMapping("/update")
    public String update(@RequestParam String cin,
                         @RequestParam String nom,
                         @RequestParam String prenom) {
        ClientDto dto = new ClientDto(cin, nom, prenom);
        clientService.update(new Client(dto.cin(), dto.nom(), dto.prenom()));
        return "redirect:/clients";
    }

    /**
     * AJAX delete — called via POST from jQuery $.ajax().
     * Returns 200 OK on success, 404 if not found.
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> delete(@RequestParam String cin) {
        Client client = clientService.getById(cin);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        clientService.delete(cin);
        return ResponseEntity.ok().build();
    }
}