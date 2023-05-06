package edu.leicester.co2103.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.repo.SessionRepository;
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
    private final SessionRepository sessionRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public ModuleRestController(ModuleRepository moduleRepository, SessionRepository sessionRepository, ObjectMapper objectMapper) {
        this.moduleRepository = moduleRepository;
        this.sessionRepository = sessionRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Module> getModuleByCode(@PathVariable("code") String code) {
        Optional<Module> module = moduleRepository.findById(code);
        return module.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        Module createdModule = moduleRepository.save(module);
        return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<Module> updateModule(@PathVariable("code") String code, @RequestBody Map<String, Object> updates) {
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
    public ResponseEntity<Void> deleteModule(@PathVariable("code") String code) {
        moduleRepository.deleteById(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{code}/sessions")
    public ResponseEntity<List<Session>> getModuleSessions(@PathVariable("code") String code) {
        Optional<Module> optionalModule = moduleRepository.findById(code);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            List<Session> sessions = module.getSessions();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{code}/sessions")
    public ResponseEntity<?> createSession(@PathVariable("code") String code, @RequestBody Session session) {
        Module module = moduleRepository.findById(code)
                .orElse(null);

        if (module == null) {
            return ResponseEntity.notFound().build();
        }

        module.getSessions().add(session);
        moduleRepository.save(module);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable("code") String code, @PathVariable("id") Long id) {
        Session session = sessionRepository.findById(id).orElse(null);
        return ResponseEntity.ok(session);
    }


    @PutMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> updateSession(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session) {
        if (!sessionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Session existingSession = sessionRepository.findById(id).orElse(null);
        existingSession.setTopic(session.getTopic());
        existingSession.setDatetime(session.getDatetime());
        existingSession.setDuration(session.getDuration());
        sessionRepository.save(existingSession);

        return ResponseEntity.ok(existingSession);
    }

    @PatchMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> partiallyUpdateSession(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session) {
        if (!sessionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Session existingSession = sessionRepository.findById(id).orElse(null);

        if (Objects.nonNull(session.getDuration())) {
            existingSession.setDuration(session.getDuration());
        }
        if (session.getTopic() != null) {
            existingSession.setTopic(session.getTopic());
        }

        if (session.getDatetime() != null) {
            existingSession.setDatetime(session.getDatetime());
        }


        sessionRepository.save(existingSession);

        return ResponseEntity.ok(existingSession);
    }

    @DeleteMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable("code") String code, @PathVariable("id") Long id) {
        if (!sessionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        sessionRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }


}
