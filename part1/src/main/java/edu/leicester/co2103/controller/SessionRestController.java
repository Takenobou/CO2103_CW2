package edu.leicester.co2103.controller;

import java.util.List;
import java.util.stream.Collectors;

import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Session> getAllSessions(@RequestParam(required = false) Long id, @RequestParam(required = false) String code) {
        List<Session> sessions = (List<Session>) sessionRepository.findAll();

        if (id != null) {
            sessions = sessions.stream()
                    .filter(session -> convenorRepository.findById(id).get().getModules().stream()
                            .anyMatch(module1 -> module1.getSessions().contains(session)))
                    .collect(Collectors.toList());
        }

        if (code != null) {
            sessions = sessions.stream()
                    .filter(session -> moduleRepository.findById(code).get().getSessions().contains(session))
                    .collect(Collectors.toList());
        }

        return sessions;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSession() {
        sessionRepository.deleteAll();
        return ResponseEntity.ok(null);
    }
}