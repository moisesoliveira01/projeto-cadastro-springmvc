package project.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.springboot.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long>{

	@Query(value = "select t from Telefone t where t.pessoa.id = ?1")
	List<Telefone> findTelefones(Long pessoaid);
}
