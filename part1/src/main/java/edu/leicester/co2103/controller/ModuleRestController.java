package edu.leicester.co2103.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ModuleRepository;

@RestController
@RequestMapping("/api/modules")
public class ModuleRestController {

    private final ModuleRepository moduleRepository;

    @Autowired
    public ModuleRestController(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @GetMapping
    public List<Module> getAllModules() {
        return (List<Module>) moduleRepository.findAll();
    }

    @GetMapping("/{code}")
    public Optional<Module> getModuleByCode(@PathVariable String code) {
        return moduleRepository.findById(code);
    }

    @PostMapping
    public Module createModule(@RequestBody Module module) {
        return moduleRepository.save(module);
    }

    @PutMapping("/{code}")
    public Module updateModule(@PathVariable String code, @RequestBody Module updatedModule) {
        return moduleRepository.findById(code).map(module -> {
            module.setTitle(updatedModule.getTitle());
            module.setLevel(updatedModule.getLevel());
            module.setOptional(updatedModule.isOptional());
            module.setSessions(updatedModule.getSessions());
            return moduleRepository.save(module);
        }).orElseGet(() -> {
            updatedModule.setCode(code);
            return moduleRepository.save(updatedModule);
        });
    }

    @DeleteMapping("/{code}")
    public void deleteModule(@PathVariable String code) {
        moduleRepository.deleteById(code);
    }
}
