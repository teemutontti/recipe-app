package com.example.backend.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;

@Service
public class LogService extends BaseService<Log> {

    @Autowired
    private LogRepository logRepository;

    @Override
    protected LogRepository getRepository() {
        return logRepository;
    }

    @Override
    public Log update(Long id, Log log) {
        Log existingLog = getRepository().findById(id).orElse(null);

        System.out.println("\n\n" + log + "\n\n");
        System.out.println("\n\n" + existingLog + "\n\n");

        if (existingLog != null) {
            // TODO: Implement update logic
            return getRepository().save(existingLog);
        }
        return null;
    }
}