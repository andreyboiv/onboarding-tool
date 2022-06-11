package com.boivalenko.businessapp.web.app.controller;

import com.boivalenko.businessapp.web.app.entity.Stat;
import com.boivalenko.businessapp.web.app.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stat")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class StatController {

    public final StatService statService;

    @PostMapping("/findByEmail")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {
        return this.statService.findStat(email);
    }

}
