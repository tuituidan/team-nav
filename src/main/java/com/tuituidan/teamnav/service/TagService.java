package com.tuituidan.teamnav.service;

import com.tuituidan.teamnav.entity.Tag;
import com.tuituidan.teamnav.repository.TagRepository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * TagService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class TagService {

    @Resource
    private TagRepository tagRepository;

    public List<Tag> select() {
        return tagRepository.findAll(Sort.by("sort"));
    }

    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }
}
