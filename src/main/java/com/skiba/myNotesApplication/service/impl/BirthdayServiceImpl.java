package com.skiba.myNotesApplication.service.impl;

import com.skiba.myNotesApplication.api.dto.BirthdayInfoCreation;
import com.skiba.myNotesApplication.api.dto.BirthdayDisplay;
import com.skiba.myNotesApplication.model.BirthdayInfo;
import com.skiba.myNotesApplication.model.User;
import com.skiba.myNotesApplication.repository.BirthdayRepository;
import com.skiba.myNotesApplication.repository.UserRepository;
import com.skiba.myNotesApplication.api.service.BirthdayService;
import com.skiba.myNotesApplication.service.mapper.BirthdayCreationToBirthdayInfoMapper;
import com.skiba.myNotesApplication.service.mapper.BirthdayInfoToBirthdayDisplayMapper;
import com.skiba.myNotesApplication.validation.BirthdayInfoNotFoundException;
import com.skiba.myNotesApplication.validation.UserByEmailNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BirthdayServiceImpl implements BirthdayService {

    private BirthdayRepository birthdayRepository;
    private UserRepository userRepository;
    private BirthdayInfoToBirthdayDisplayMapper birthdayInfoToBirthdayDisplayMapper;
    private BirthdayCreationToBirthdayInfoMapper birthdayCreationToBirthdayInfoMapper;

    @Autowired
    public BirthdayServiceImpl(BirthdayRepository birthdayRepository, UserRepository userRepository,
                               BirthdayInfoToBirthdayDisplayMapper birthdayInfoToBirthdayDisplayMapper,
                               BirthdayCreationToBirthdayInfoMapper birthdayCreationToBirthdayInfoMapper) {
        this.birthdayRepository = birthdayRepository;
        this.userRepository = userRepository;
        this.birthdayInfoToBirthdayDisplayMapper = birthdayInfoToBirthdayDisplayMapper;
        this.birthdayCreationToBirthdayInfoMapper = birthdayCreationToBirthdayInfoMapper;
    }

    @Override
    public Set<BirthdayDisplay> getAllBirthdays(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            return user.getBirthdayInfos()
                    .stream()
                    .map(birthdayInfoToBirthdayDisplayMapper::map)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public BirthdayDisplay getSingleBirthday(Long birthdayId, String email) {

        Optional<BirthdayInfo> birthdayInfoOptional = getBirthdayInfoIfUserIsItsOwnerOptional(birthdayId, email);

        if (!birthdayInfoOptional.isPresent()) {
            throw new BirthdayInfoNotFoundException(birthdayId);
        } else {
            return birthdayInfoToBirthdayDisplayMapper.map(birthdayInfoOptional.get());
        }
    }

    @Override
    public BirthdayDisplay addBirthdayForUser(BirthdayInfoCreation birthdayInfoCreation, String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            BirthdayInfo birthdayInfo = birthdayCreationToBirthdayInfoMapper.map(birthdayInfoCreation);

            Set<BirthdayInfo> birthdayInfoSet = user.getBirthdayInfos();
            birthdayInfoSet.add(birthdayInfo);
            userRepository.save(user);

            return birthdayInfoToBirthdayDisplayMapper.map(birthdayInfo);
        }
    }

    @Override
    public BirthdayDisplay updateBirthday(BirthdayInfoCreation birthdayInfoCreation, Long birthdayId, String email) {

        Optional<BirthdayInfo> birthdayInfoOptional = getBirthdayInfoIfUserIsItsOwnerOptional(birthdayId, email);

        if (!birthdayInfoOptional.isPresent()) {
            throw new BirthdayInfoNotFoundException(birthdayId);
        } else {
            BirthdayInfo updatedBirthdayInfo = birthdayCreationToBirthdayInfoMapper.map(birthdayInfoCreation);
            BirthdayInfo oldBirthdayInfo = birthdayInfoOptional.get();

            oldBirthdayInfo.setBirthdayPerson(updatedBirthdayInfo.getBirthdayPerson());
            oldBirthdayInfo.setDateOfBirth(updatedBirthdayInfo.getDateOfBirth());
            BirthdayInfo savedInfo = birthdayRepository.save(oldBirthdayInfo);

            return birthdayInfoToBirthdayDisplayMapper.map(savedInfo);
        }
    }

    @Override
    public void removeBirthday(Long birthdayId, String email) {

        Optional<BirthdayInfo> birthdayInfoOptional = getBirthdayInfoIfUserIsItsOwnerOptional(birthdayId, email);

        if (!birthdayInfoOptional.isPresent()) {
            throw new BirthdayInfoNotFoundException(birthdayId);
        } else {
            birthdayRepository.deleteBirthdayInfoById(birthdayId);
        }
    }

    private Optional<BirthdayInfo> getBirthdayInfoIfUserIsItsOwnerOptional(Long birthdayId, String email) {

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException(email);
        } else {
            return user
                    .getBirthdayInfos()
                    .stream()
                    .filter(b -> b.getId().equals(birthdayId))
                    .findFirst();
        }
    }
}
