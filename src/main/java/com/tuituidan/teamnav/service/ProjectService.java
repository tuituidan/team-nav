package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.entity.Project;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * ProjectService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/2/21
 */
@Service
public class ProjectService {

    @Value(value = "#{'${projects}'.split(',')}")
    private String[] projects;

    private List<Project> projectList;

    @PostConstruct
    private void init() {
        projectList = Arrays.stream(projects)
                .map(name -> new Project().setId(DigestUtils.md5Hex(name)).setName(name))
                .collect(Collectors.toList());
    }


    public List<Project> select() {
        return projectList;
    }


}
