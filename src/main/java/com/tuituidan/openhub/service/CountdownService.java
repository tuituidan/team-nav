package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.CountdownDto;
import com.tuituidan.openhub.bean.entity.Countdown;
import com.tuituidan.openhub.bean.vo.CountdownVo;
import com.tuituidan.openhub.repository.CountdownRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * CategoryService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class CountdownService {

    @Resource
    private CountdownRepository countdownRepository;

    /**
     * select
     *
     * @return List
     */
    public List<CountdownVo> select(Boolean status) {
        Predicate<Countdown> filterFunc = item -> true;
        if (BooleanUtils.isTrue(status)) {
            filterFunc = item -> item.getEndTime().isAfter(LocalDateTime.now());
        }
        return countdownRepository.findAll(Sort.by("endTime"))
                .stream().filter(filterFunc)
                .map(item -> BeanExtUtils.convert(item, CountdownVo::new))
                .collect(Collectors.toList());
    }

    /**
     * 保存
     *
     * @param id id
     * @param dto dto
     */
    public void save(String id, CountdownDto dto) {
        Countdown countdown = BeanExtUtils.convert(dto, Countdown::new);
        countdown.setId(StringUtils.isBlank(id) ? StringExtUtils.getUuid() : id);
        countdownRepository.save(countdown);
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String id) {
        countdownRepository.deleteById(id);
    }

}
