package com.skiba.myNotesApplication.api.controller;

import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.api.service.BirthdayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Set;

@RestController
public class BirthdayController {

    public static final String MESSAGE_AFTER_BIRTHDAY_DELETION = "BirthdayInfo with ID: %d has been deleted successfully!!!";

    BirthdayService birthdayService;

    @Autowired
    public BirthdayController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

    @GetMapping(value = "/api/birthdays")
    public ResponseEntity<Set<BirthdayDisplay>> getAllBirthdays(Principal principal) {

        Set<BirthdayDisplay> birthdayDisplays = birthdayService.getAllBirthdays(principal.getName());
        return ResponseEntity.ok(birthdayDisplays);
    }

    @GetMapping(value = "/api/birthdays/{birthdayId}")
    public ResponseEntity<BirthdayDisplay> getSingleBirthday(@PathVariable Long birthdayId, Principal principal) {

        String email = principal.getName();

        BirthdayDisplay birthdayDisplay = birthdayService.getSingleBirthday(birthdayId, email);
        return ResponseEntity.ok(birthdayDisplay);
    }

    @PostMapping("/api/birthdays")
    public ResponseEntity<BirthdayDisplay> addBirthdayInfo(
            @RequestBody @Valid BirthdayInfoCreation birthdayInfoCreation,
                                                           Principal principal) {
        String email = principal.getName();

        BirthdayDisplay saved = birthdayService.addBirthdayForUser(birthdayInfoCreation, email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/api/birthdays/{birthdayId}")
    public ResponseEntity<BirthdayDisplay> updateBirthdayInfo(@RequestBody @Valid BirthdayInfoCreation birthdayInfoCreation,
                                                              @PathVariable Long birthdayId, Principal principal) {
        String email = principal.getName();

        BirthdayDisplay updatedBirthday = birthdayService.updateBirthday(birthdayInfoCreation, birthdayId, email);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBirthday);
    }

    @DeleteMapping("/api/birthdays/{birthdayId}")
    public ResponseEntity<String> deleteBirthdayInfo(@PathVariable Long birthdayId, Principal principal) {

        String email = principal.getName();

        birthdayService.removeBirthday(birthdayId, email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format(MESSAGE_AFTER_BIRTHDAY_DELETION, birthdayId));
    }


}
