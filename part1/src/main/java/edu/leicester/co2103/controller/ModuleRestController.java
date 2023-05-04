package edu.leicester.co2103.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;

@RestController
@RequestMapping("/modules")
public class ModuleRestController {

    private final ModuleRepository moduleRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public ModuleRestController(ModuleRepository moduleRepository, ObjectMapper objectMapper) {
        this.moduleRepository = moduleRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Module> getModuleByCode(@PathVariable String code) {
        Optional<Module> module = moduleRepository.findById(code);
        return module.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        Module createdModule = moduleRepository.save(module);
        return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<Module> updateModule(@PathVariable String code, @RequestBody Map<String, Object> updates) {
        Optional<Module> optionalModule = moduleRepository.findById(code);

        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "title":
                        module.setTitle((String) value);
                        break;
                    case "level":
                        module.setLevel((Integer) value);
                        break;
                    case "optional":
                        module.setOptional((Boolean) value);
                        break;
                    case "sessions":
                        List<Session> sessions = objectMapper.convertValue(value, new TypeReference<>() {
                        });
                        module.setSessions(sessions);
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected update field: " + key);
                }
            });

            Module updatedModule = moduleRepository.save(module);
            return ResponseEntity.ok(updatedModule);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteModule(@PathVariable String code) {
        moduleRepository.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
