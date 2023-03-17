package br.com.pehenmo.batch.processor;

import br.com.pehenmo.batch.entity.ResultCSV;
import br.com.pehenmo.batch.entity.Student;
import br.com.pehenmo.batch.entity.Worker;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class WorkerProcessor implements ItemProcessor<Worker, ResultCSV> {
    @Override
    public ResultCSV process(Worker worker) {

        if("pedro".equals(worker.getFirstName())) return null;


        return new ResultCSV(worker.getId(),
                worker.getFirstName().toUpperCase(),
                worker.getLastName().toUpperCase(),
                worker.getCnpj());
    }
}
