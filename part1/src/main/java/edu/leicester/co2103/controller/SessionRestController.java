package edu.leicester.co2103.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.SessionRepository;

@RestController
@RequestMapping("sessions")
public class SessionRestController {

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionRestController(SessionRepository sessionRepository, ModuleRepository moduleRepository, ConvenorRepository convenorRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return (List<Session>) sessionRepository.findAll();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSession() {
        sessionRepository.deleteAll();
        return ResponseEntity.ok(null);
    }
}