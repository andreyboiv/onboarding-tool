package com.boivalenko.businessapp.web.service;

import com.boivalenko.businessapp.web.entity.Stat;
import com.boivalenko.businessapp.web.repository.StatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatService {
    private final StatRepository statRepository;

    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    public ResponseEntity<Stat> findStat(String email) {
        Stat stat = this.statRepository.findByEmployeeToStatEmail(email);

        if (stat == null) {
            return new ResponseEntity("Keine Statistik gefunden. Email:" + email,
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(stat);
    }
}
