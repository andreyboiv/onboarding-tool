package com.boivalenko.businessapp.web.app.service;

import com.boivalenko.businessapp.web.app.entity.Stat;
import com.boivalenko.businessapp.web.app.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;

    public ResponseEntity<Stat> findStat(String email) {
        Stat stat = this.statRepository.findByEmployeeToStatEmail(email);

        if (stat == null) {
            return new ResponseEntity("Keine Statistik gefunden. Email:" + email,
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(stat);
    }
}
