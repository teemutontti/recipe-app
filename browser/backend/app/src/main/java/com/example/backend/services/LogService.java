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
}