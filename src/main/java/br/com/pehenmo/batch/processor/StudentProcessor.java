package br.com.pehenmo.batch.processor;

import br.com.pehenmo.batch.entity.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<Student, Student> {
    @Override
    public Student process(Student student) throws Exception {

        if("pedro".equals(student.getFirstName())) return null;

        Student student1 = new Student(student.getId(), student.getFirstName().toUpperCase(), student.getLastName().toUpperCase(), student.getEmail());
        return student1;
    }
}
