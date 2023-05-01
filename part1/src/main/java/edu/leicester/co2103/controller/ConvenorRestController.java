package edu.leicester.co2103.controller;

import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ConvenorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/convenors")
public class ConvenorRestController {

    @Autowired
    private ConvenorRepository convenorRepository;

    @GetMapping
    public ResponseEntity<List<Convenor>> listConvenors() {
        List<Convenor> convenors = (List<Convenor>) convenorRepository.findAll();
        return new ResponseEntity<>(convenors, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Convenor> createConvenor(@RequestBody Convenor convenor) {
        Convenor createdConvenor = convenorRepository.save(convenor);
        return new ResponseEntity<>(createdConvenor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convenor> getConvenor(@PathVariable Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        if (convenor.isPresent()) {
            return new ResponseEntity<>(convenor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Convenor> updateConvenor(@PathVariable Long id, @RequestBody Convenor convenorUpdates) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        if (convenor.isPresent()) {
            Convenor updatedConvenor = convenor.get();
            updatedConvenor.setName(convenorUpdates.getName());
            updatedConvenor.setPosition(convenorUpdates.getPosition());
            updatedConvenor.setModules(convenorUpdates.getModules());
            convenorRepository.save(updatedConvenor);
            return new ResponseEntity<>(updatedConvenor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConvenor(@PathVariable Long id) {
        convenorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> listConvenorModules(@PathVariable Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        if (convenor.isPresent()) {
            return new ResponseEntity<>(convenor.get().getModules(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
