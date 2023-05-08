package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Position;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/convenors")
public class ConvenorRestController {
    @Autowired
    ConvenorRepository convenorRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    SessionRepository sessionRepository;

    @GetMapping
    public ResponseEntity<?> listConvenors() {
        List<Convenor> convenors = (List<Convenor>) convenorRepository.findAll();
        if (convenors.isEmpty()) {
            return new ResponseEntity<>(new ErrorInfo("Convenors not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(convenors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convenor> getConvenorById(@PathVariable("id") Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(new ErrorInfo("Convenor with id " + id + " not found"), HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<?> createConvenor(@RequestBody Map<String, Object> convenorInput) {
        String positionString = (String) convenorInput.get("position");

        Position position;
        try {
            position = Position.valueOf(positionString.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(new ErrorInfo("Invalid position"), HttpStatus.BAD_REQUEST);
        }

        String name = (String) convenorInput.get("name");

        Convenor convenor = new Convenor();
        convenor.setName(name);
        convenor.setPosition(position);

        Convenor createdConvenor = convenorRepository.save(convenor);
        return new ResponseEntity<>(createdConvenor, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateConvenor(@PathVariable("id") Long id, @RequestBody Map<String, Object> updatedConvenorInput) {

        Optional<Convenor> optionalConvenor = convenorRepository.findById(id);
        if (optionalConvenor.isPresent()) {
            Convenor currentConvenor = optionalConvenor.get();

            String name = (String) updatedConvenorInput.get("name");
            if (name != null) {
                currentConvenor.setName(name);
            }

            String positionString = (String) updatedConvenorInput.get("position");
            if (positionString != null) {
                Position position;
                try {
                    position = Position.valueOf(positionString.toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    return new ResponseEntity<>(new ErrorInfo("Invalid position"), HttpStatus.BAD_REQUEST);
                }
                currentConvenor.setPosition(position);
            }

            convenorRepository.save(currentConvenor);
            return new ResponseEntity<>(currentConvenor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorInfo("Convenor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("id") Long id) {
        Optional<Convenor> optionalConvenor = convenorRepository.findById(id);
        if (optionalConvenor.isPresent()) {
            Convenor convenor = optionalConvenor.get();
            List<Module> modulesTaughtByConvenor = convenor.getModules();
            List<Module> modulesCopy = new ArrayList<>(modulesTaughtByConvenor);

            for (Module module : modulesCopy) {
                List<Session> convenorsTeachingModule = module.getSessions();
                if (convenorsTeachingModule != null && convenorsTeachingModule.size() == 1 && convenorsTeachingModule.get(0).equals(convenor)) {
                    List<Session> sessionsInModule = module.getSessions();
                    sessionRepository.deleteAll(sessionsInModule);
                    convenor.getModules().remove(module);
                    moduleRepository.delete(module);
                } else {
                    convenor.getModules().remove(module);
                }
            }

            convenorRepository.delete(convenor);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new ErrorInfo("Convenor with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> listConvenorModules(@PathVariable("id") Long id) {
        Optional<Convenor> convenor = convenorRepository.findById(id);
        return convenor.map(value -> new ResponseEntity<>(value.getModules(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity(new ErrorInfo("Convenor's modules with id " + id + " not found"), HttpStatus.NOT_FOUND));
    }
}
