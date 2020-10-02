package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.entity.Site;
import com.tuituidan.teamnav.repository.SiteRepository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * SiteService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class SiteService {

    @Resource
    private SiteRepository siteRepository;

    public List<Site> select(String tag) {
        return siteRepository.findByTag(tag);
    }

    public void save(){

    }

}
