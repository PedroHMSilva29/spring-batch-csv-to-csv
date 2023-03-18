package br.com.pehenmo.batch.processor;

import br.com.pehenmo.batch.ResultCSV;
import br.com.pehenmo.batch.Student;
import br.com.pehenmo.postgres.entity.StudanteEntity;
import br.com.pehenmo.postgres.repository.StudanteRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@Transactional
//@EnableJpaRepositories("br.com.pehenmo.postgres.repository")
public class StudentProcessor implements ItemProcessor<Student, ResultCSV> {

    @Autowired
    StudanteRepository repository;
/**
    private final StudanteRepository repository;

    @Autowired
    StudentProcessor(StudanteRepository repository) {
        this.repository = repository;
    }
*/
    @Override
    @Transactional
    public ResultCSV process(Student student) {

        StudanteEntity entity = new StudanteEntity();


        Optional<StudanteEntity> result = repository.findByFirstName("UM");

        if(result.isPresent()){
            StudanteEntity entity1 = result.get();
            entity1.setLastName("change1");
            repository.save(entity1);
            System.out.println("achou");
        }else{
            StudanteEntity entity1 = new StudanteEntity();
            entity1.setFirstName("pedro");
            entity1.setLastName("teste-insert");
            entity1.setIdentificador("temail");
            repository.save(entity1);
        }


        return new ResultCSV(student.getId(),
                student.getFirstName().toUpperCase(),
                student.getLastName().toUpperCase(),
                student.getEmail());
    }
}
