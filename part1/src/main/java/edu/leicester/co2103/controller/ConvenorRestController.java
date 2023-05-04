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

    private final ConvenorRepository convenorRepository;

    @Autowired
    public ConvenorRestController(ConvenorRepository convenorRepository) {
        this.convenorRepository = convenorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Convenor>> listConvenors() {
        List<Convenor> convenors = (List<Convenor>) convenorRepository.findAll();
        return new ResponseEntity<>(convenors, HttpStatus.OK);
    }

    @GetMapping(params = "id")
    public ResponseEntity<Convenor> getConvenorById(@RequestParam Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<Convenor> createConvenor(@RequestBody Convenor convenor) {
        Convenor createdConvenor = convenorRepository.save(convenor);
        return new ResponseEntity<>(createdConvenor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Convenor> updateConvenor(@RequestBody Convenor updatedConvenor, @PathVariable("id") Long id) {
        return convenorRepository.findById(id).map(convenor -> {
            convenor.setName(updatedConvenor.getName());
            convenor.setPosition(updatedConvenor.getPosition());
            Convenor savedConvenor = convenorRepository.save(convenor);
            return ResponseEntity.ok(savedConvenor);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConvenor(@RequestParam Long id) {
        convenorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> listConvenorModules(@PathVariable Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value.getModules(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
