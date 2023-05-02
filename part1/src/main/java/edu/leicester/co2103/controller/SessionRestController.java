package edu.leicester.co2103.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.SessionRepository;

@RestController
@RequestMapping("sessions")
public class SessionRestController {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionRestController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return (List<Session>) sessionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Session> getSessionById(@PathVariable long id) {
        return sessionRepository.findById(id);
    }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionRepository.save(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable long id, @RequestBody Session updatedSession) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (!optionalSession.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Session session = optionalSession.get();
        session.setTopic(updatedSession.getTopic());
        session.setDatetime(updatedSession.getDatetime());
        session.setDuration(updatedSession.getDuration());
        sessionRepository.save(session);

        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable long id) {
        sessionRepository.deleteById(id);
    }
}
