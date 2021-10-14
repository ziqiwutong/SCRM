package com.scrm.service.service.impl;

import com.scrm.service.dao.LabelDao;
import com.scrm.service.service.LabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelDao labelDao;

}
