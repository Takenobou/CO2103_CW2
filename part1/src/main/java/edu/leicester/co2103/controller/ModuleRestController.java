package edu.leicester.co2103.controller;

import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.repo.ConvenorRepository;
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
    @Autowired
    ConvenorRepository convenorRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> getAllModules() {
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        if (modules.isEmpty()) {
            return new ResponseEntity<>(new ErrorInfo("Modules not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Module> getModuleByCode(@PathVariable("code") String code) {
        Optional<Module> module = moduleRepository.findById(code);
        return module.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity(new ErrorInfo("Module with code " + code + " not found"), HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createModule(@RequestBody Module module) {
        if (module.getLevel() > 3) {
            return new ResponseEntity<>(new ErrorInfo("Invalid level"), HttpStatus.BAD_REQUEST);
        }

        Module createdModule = moduleRepository.save(module);
        return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
    }


    @PatchMapping("/{code}")
    public ResponseEntity<?> updateModule(@PathVariable("code") String code, @RequestBody Map<String, Object> updates) {
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
                }
            });

            Module updatedModule = moduleRepository.save(module);
            return new ResponseEntity<Module>(module, HttpStatus.OK);
        } else {
            ErrorInfo errorInfo = new ErrorInfo("Module with code "+ code + " not found");
            return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteModule(@PathVariable("code") String code) {
        Optional<Module> optionalModule = moduleRepository.findById(code);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();

            // Remove the module from any convenors who have it
            for (Convenor convenor : convenorRepository.findAll()) {
                convenor.getModules().remove(module);
                convenorRepository.save(convenor);
            }

            // Remove the session from any module
            Iterator<Session> sessionIterator = module.getSessions().iterator();
            while (sessionIterator.hasNext()) {
                Session session = sessionIterator.next();
                sessionIterator.remove();
                sessionRepository.delete(session);
            }

            // Delete the module
            moduleRepository.delete(module);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(new ErrorInfo("Module with code " + code + " not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{code}/sessions")
    public ResponseEntity<List<Session>> getModuleSessions(@PathVariable("code") String code) {
        Optional<Module> optionalModule = moduleRepository.findById(code);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            List<Session> sessions = module.getSessions();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        } else {
            return new ResponseEntity(new ErrorInfo("Module with code "+ code + " sessions are not found"),HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{code}/sessions")
    public ResponseEntity<?> createSession(@PathVariable("code") String code, @RequestBody Session session) {
        Module module = moduleRepository.findById(code)
                .orElse(null);

        if (module == null) {
            return new ResponseEntity(new ErrorInfo("Module with code "+ code + " sessions are not found"),HttpStatus.NOT_FOUND);
        }

        module.getSessions().add(session);
        moduleRepository.save(module);

        return new ResponseEntity<>(session, HttpStatus.OK);

    }

    @GetMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable("code") String code, @PathVariable("id") Long id) {
        Session session = sessionRepository.findById(id).orElse(null);
        if (session == null) {
            return new ResponseEntity<>(new ErrorInfo("Session with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(session, HttpStatus.OK);
    }



    @PutMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> updateSession(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(new ErrorInfo("Session with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }

        Session existingSession = sessionRepository.findById(id).orElse(null);
        existingSession.setTopic(session.getTopic());
        existingSession.setDatetime(session.getDatetime());
        existingSession.setDuration(session.getDuration());
        sessionRepository.save(existingSession);

        return new ResponseEntity<>(existingSession, HttpStatus.OK);

    }


    @PatchMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> partiallyUpdateSession(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(new ErrorInfo("Session with id " + id + " not found"), HttpStatus.NOT_FOUND);
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

        return new ResponseEntity<>(existingSession, HttpStatus.OK);
    }

    @DeleteMapping("/{code}/sessions/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable("code") String code, @PathVariable("id") Long id) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(new ErrorInfo("Session with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }

        sessionRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}