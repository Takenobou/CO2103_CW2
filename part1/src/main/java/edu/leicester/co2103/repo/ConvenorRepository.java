package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Convenor;

import java.util.List;


public interface ConvenorRepository extends CrudRepository<Convenor, Long> {

}
