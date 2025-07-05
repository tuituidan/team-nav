package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.entity.SysDatasource;
import com.tuituidan.openhub.bean.vo.SysDatasourceVo;
import com.tuituidan.openhub.consts.DbTypeEnum;
import com.tuituidan.openhub.repository.DatasourceRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import com.tuituidan.openhub.util.StringExtUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * SysDatasourceService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/7/5
 */
@Service
public class DatasourceService {

    @Resource
    private DatasourceRepository datasourceRepository;

    @Resource
    private CacheService cacheService;

    /**
     * select
     *
     * @return List
     */
    public List<SysDatasourceVo> select() {
        return datasourceRepository.findAll().stream()
                .map(it -> BeanExtUtils.convert(it, SysDatasourceVo::new)).collect(Collectors.toList());
    }

    /**
     * save
     *
     * @param id id
     * @param datasource datasource
     */
    public void save(String id, SysDatasource datasource) {
        if (StringUtils.isBlank(id)) {
            datasource.setId(StringExtUtils.getUuid());
        }
        DbTypeEnum dbTypeEnum = DbTypeEnum.getByType(datasource.getType());
        datasource.setDesc(buildDesc(datasource.getJdbcUrl()))
                .setDriverClassName(dbTypeEnum.getDriverClassName());
        datasourceRepository.save(datasource);
        cacheService.getJdbcTemplateCache().invalidate(datasource.getId());
    }

    /**
     * delete
     *
     * @param id id
     */
    public void delete(String id) {
        datasourceRepository.deleteById(id);
        cacheService.getJdbcTemplateCache().invalidate(id);
    }

    private String buildDesc(String jdbcUrl) {
        int end = jdbcUrl.indexOf("?");
        end = end < 0 ? jdbcUrl.length() : end;
        return jdbcUrl.substring(jdbcUrl.indexOf("//") + 2, end);
    }

}
