package com.easyfarm.easyfarm.service;

import com.easyfarm.easyfarm.repository.FarmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FarmService {
    @Autowired
    private FarmRepository farmRepository;


}
