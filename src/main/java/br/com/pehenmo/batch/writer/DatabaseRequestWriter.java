package br.com.pehenmo.batch.writer;

import br.com.pehenmo.postgres.entity.StudanteEntity;
import br.com.pehenmo.postgres.repository.StudanteRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class DatabaseRequestWriter implements ItemWriter<StudanteEntity> {

    @Autowired
    StudanteRepository repository;

    @Override
    @Transactional
    public void write(List<? extends StudanteEntity> list) {
        repository.saveAll(list);
    }
}
