package edu.leicester.co2103.controller;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/modules")
public class ModuleRestController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public ResponseEntity<List<Module>> listModules() {
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        Module createdModule = moduleRepository.save(module);
        return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModule(@PathVariable String id) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            return new ResponseEntity<>(module.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable String id, @RequestBody Module moduleUpdates) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            Module updatedModule = module.get();
            updatedModule.setName(moduleUpdates.getName());
            updatedModule.setConvenors(moduleUpdates.getConvenors());
            moduleRepository.save(updatedModule);
            return new ResponseEntity<>(updatedModule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable String id) {
        moduleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<Session>> listModuleSessions(@PathVariable String id) {
        Optional<Module> module = moduleRepository.findById(id);
        if (module.isPresent()) {
            return new ResponseEntity<>(module.get().getSessions(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

