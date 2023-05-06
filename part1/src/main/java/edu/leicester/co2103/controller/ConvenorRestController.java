package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
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
        if (convenors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(convenors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convenor> getConvenorById(@PathVariable("id") Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(new ErrorInfo("Convenor with id " + id + " not found"), HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<?> createConvenor(@RequestBody Convenor convenor) {
        Convenor createdConvenor = convenorRepository.save(convenor);
        return new ResponseEntity<>(createdConvenor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConvenor(@PathVariable("id") Long id, @RequestBody Convenor updatedConvenor) {

        if (convenorRepository.findById(id).isPresent()) {
            Convenor currentConvenor = convenorRepository.findById(id).get();
            currentConvenor.setName(updatedConvenor.getName());
            currentConvenor.setPosition(updatedConvenor.getPosition());
            convenorRepository.save(currentConvenor);
            return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id "+ id + " not found"), HttpStatus.NOT_FOUND);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("id") Long id) {
        if (convenorRepository.findById(id).isPresent()) {
            convenorRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id "+ id +" not found"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> listConvenorModules(@PathVariable("id") Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value.getModules(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity(new ErrorInfo("Convenor's modules with id " + id + " not found"), HttpStatus.NOT_FOUND));
    }
}
