package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Session;
import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends CrudRepository<Module, String> {

}
