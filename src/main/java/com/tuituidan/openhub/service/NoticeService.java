package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.NoticeDto;
import com.tuituidan.openhub.bean.dto.SortDto;
import com.tuituidan.openhub.bean.entity.Notice;
import com.tuituidan.openhub.bean.vo.NoticeVo;
import com.tuituidan.openhub.repository.NoticeRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.ListUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * NoticeService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2020/10/2
 */
@Service
public class NoticeService {

    @Resource
    private NoticeRepository noticeRepository;

    /**
     * select
     *
     * @return List
     */
    public List<NoticeVo> select(Boolean status) {
        Predicate<Notice> filterFunc = item -> true;
        if (BooleanUtils.isTrue(status)) {
            filterFunc = item -> Objects.isNull(item.getEndTime())
                    || item.getEndTime().isAfter(LocalDateTime.now());
        }
        return noticeRepository.findAll(Sort.by("sort"))
                .stream().filter(filterFunc)
                .map(item -> {
                    NoticeVo noticeVo = BeanExtUtils.convert(item, NoticeVo::new);
                    noticeVo.setContent(HtmlUtils.htmlUnescape(noticeVo.getContent()));
                    return noticeVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 保存
     *
     * @param id id
     * @param dto dto
     */
    public void save(String id, NoticeDto dto) {
        String formatTxt = dto.getContent().replaceAll("(?i)(<SCRIPT)[\\s\\S]*?((</SCRIPT>)|(/>))", "");
        dto.setContent(HtmlUtils.htmlEscape(formatTxt));
        Notice notice = BeanExtUtils.convert(dto, Notice::new);
        if (StringUtils.isBlank(id)) {
            notice.setId(StringExtUtils.getUuid());
            notice.setSort(noticeRepository.getMaxSort() + 1);
        } else {
            notice.setId(id);
        }
        noticeRepository.save(notice);
    }

    /**
     * 修改排序
     *
     * @param sortDto sortDto
     */
    public void changeSort(SortDto sortDto) {
        List<Notice> saveList = ListUtils.changeSort(noticeRepository.findAll(Sort.by("sort")), sortDto);
        if (CollectionUtils.isEmpty(saveList)) {
            return;
        }
        noticeRepository.saveAll(saveList);
    }

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(String[] id) {
        noticeRepository.deleteAllById(Arrays.asList(id));
    }

}
