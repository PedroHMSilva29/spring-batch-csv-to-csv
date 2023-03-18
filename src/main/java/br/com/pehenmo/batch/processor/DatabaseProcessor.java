package br.com.pehenmo.batch.processor;

import br.com.pehenmo.batch.ResultCSV;
import br.com.pehenmo.postgres.entity.StudanteEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class DatabaseProcessor implements ItemProcessor<ResultCSV, StudanteEntity> {
    @Override
    public StudanteEntity process(ResultCSV resultCSV) {
        return resultCSV.mapToEntity();
    }
}
