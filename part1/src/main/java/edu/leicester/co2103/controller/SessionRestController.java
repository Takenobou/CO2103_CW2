package edu.leicester.co2103.controller;

import java.util.List;
import java.util.stream.Collectors;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sessions")
public class SessionRestController {

    private final SessionRepository sessionRepository;
    private final ModuleRepository moduleRepository;
    private final ConvenorRepository convenorRepository;

    @Autowired
    public SessionRestController(SessionRepository sessionRepository, ModuleRepository moduleRepository, ConvenorRepository convenorRepository) {
        this.sessionRepository = sessionRepository;
        this.moduleRepository = moduleRepository;
        this.convenorRepository = convenorRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllSessions(@RequestParam(value = "convenor", required = false) Long id, @RequestParam(value = "module", required = false) String code) {
        List<Session> sessions = (List<Session>) sessionRepository.findAll();

        if (id != null) {
            if (convenorRepository.findById(id).isPresent()) {
                sessions = sessions.stream()
                        .filter(session -> convenorRepository.findById(id).get().getModules().stream()
                                .anyMatch(module1 -> module1.getSessions().contains(session)))
                        .collect(Collectors.toList());
            } else {
                return new ResponseEntity<>(new ErrorInfo("Convenor with id " + id + " not found"), HttpStatus.NOT_FOUND);
            }
        }

        if (code != null) {
            if (moduleRepository.findById(code).isPresent()) {
                sessions = sessions.stream()
                        .filter(session -> moduleRepository.findById(code).get().getSessions().contains(session))
                        .collect(Collectors.toList());
            } else {
                return new ResponseEntity<>(new ErrorInfo("Module with code " + code + " not found"), HttpStatus.NOT_FOUND);
            }
        }

        if (sessions.isEmpty()) {
            return new ResponseEntity<>(new ErrorInfo("Session not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sessions, HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteSession() {
        long numSessions = sessionRepository.count();
        if (numSessions == 0) {
            return new ResponseEntity<>(new ErrorInfo("No sessions found"), HttpStatus.NOT_FOUND);
        }
        sessionRepository.deleteAll();
        return ResponseEntity.ok(null);
    }


}
