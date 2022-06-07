package com.boivalenko.businessapp.web.controller;

import com.boivalenko.businessapp.web.entity.Stat;
import com.boivalenko.businessapp.web.service.StatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
public class StatController {

    public final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/findByEmail")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {
        return this.statService.findStat(email);
    }

}
