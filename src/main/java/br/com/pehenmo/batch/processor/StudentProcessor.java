package br.com.pehenmo.batch.processor;

import br.com.pehenmo.batch.entity.ResultCSV;
import br.com.pehenmo.batch.entity.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentProcessor implements ItemProcessor<Student, ResultCSV> {
    @Override
    public ResultCSV process(Student student) {

        if("pedro".equals(student.getFirstName())) return null;

        return new ResultCSV(student.getId(),
                student.getFirstName().toUpperCase(),
                student.getLastName().toUpperCase(),
                student.getEmail());
    }
}
