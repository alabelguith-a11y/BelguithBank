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
import tn.enis.dto.CompteDto;
import tn.enis.entity.Client;
import tn.enis.entity.Compte;
import tn.enis.service.ClientService;
import tn.enis.service.CompteService;

@Controller
@RequestMapping("/comptes")
@AllArgsConstructor
public class CompteController {

    private CompteService compteService;
    private ClientService clientService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("comptes", compteService.findAll());
        model.addAttribute("clients", clientService.findAll());
        return "comptes";
    }

    @PostMapping("/save")
    public String save(@RequestParam float solde,
                       @RequestParam String clientCin) {
        CompteDto dto = new CompteDto(null, solde, clientCin);
        Client client = clientService.getById(dto.clientCin());
        if (client != null) {
            compteService.save(new Compte(dto.solde(), client));
        }
        return "redirect:/comptes";
    }

    @GetMapping("/edit/{rib}")
    public String editForm(@PathVariable Integer rib, Model model) {
        Compte compte = compteService.getById(rib);
        if (compte == null) return "redirect:/comptes";
        CompteDto dto = new CompteDto(compte.getRib(), compte.getSolde(), compte.getClient().getCin());
        model.addAttribute("editing", dto);
        model.addAttribute("comptes", compteService.findAll());
        model.addAttribute("clients", clientService.findAll());
        return "comptes";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer rib,
                         @RequestParam float solde,
                         @RequestParam String clientCin) {
        CompteDto dto = new CompteDto(rib, solde, clientCin);
        Client client = clientService.getById(dto.clientCin());
        if (client != null) {
            compteService.update(new Compte(dto.rib(), dto.solde(), client));
        }
        return "redirect:/comptes";
    }

    /**
     * AJAX delete — called via POST from jQuery $.ajax().
     * Returns 200 OK on success, 404 if not found.
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> delete(@RequestParam Integer rib) {
        Compte compte = compteService.getById(rib);
        if (compte == null) {
            return ResponseEntity.notFound().build();
        }
        compteService.delete(rib);
        return ResponseEntity.ok().build();
    }
}