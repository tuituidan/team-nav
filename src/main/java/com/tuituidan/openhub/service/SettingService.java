package com.tuituidan.openhub.service;

import com.tuituidan.openhub.bean.dto.SettingDto;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.consts.Consts;
import com.tuituidan.openhub.repository.SettingRepository;
import com.tuituidan.openhub.util.BeanExtUtils;
import javax.annotation.Resource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * SettingService.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/27 0027
 */
@Service
public class SettingService implements ApplicationRunner {

    @Resource
    private SettingRepository settingRepository;

    private Setting settingCache;

    @Value("${nav-name}")
    private String navName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Setting setting = get();
        boolean needSave = false;
        if (StringUtils.isBlank(setting.getNavName())) {
            // 加载默认配置，兼容老版本
            setting.setNavName(navName);
            needSave = true;
        }
        if (setting.getCutOverSpeed() == null) {
            setting.setCutOverSpeed(10 * 1000);
            needSave = true;
        }
        if (StringUtils.isBlank(setting.getLogoPath())) {
            setting.setLogoPath(Consts.DEFAULT_LOGO_PATH);
            needSave = true;
        }
        if (setting.getLogoToFavicon() == null) {
            setting.setLogoToFavicon(false);
            needSave = true;
        }
        if (setting.getNginxOpen() == null) {
            setting.setNginxOpen(false);
            needSave = true;
        }
        if (needSave) {
            settingRepository.save(setting);
        }
        this.settingChange(setting);
    }

    /**
     * get
     *
     * @return Setting
     */
    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting().setId("setting-id"));
    }

    /**
     * save
     *
     * @param settingDto nginx
     */
    public void saveSetting(SettingDto settingDto) {
        Setting setting = BeanExtUtils.convert(settingDto, Setting::new).setId("setting-id");
        setting.setNginxUrl(StringUtils.stripEnd(settingDto.getNginxUrl(), "/"));
        if (StringUtils.isBlank(setting.getLogoPath())) {
            setting.setLogoPath(Consts.DEFAULT_LOGO_PATH);
        }
        settingRepository.save(setting);
        this.settingChange(setting);
    }

    /**
     * 获取经过一些处理能直接使用的设置
     *
     * @return Setting
     */
    public Setting getSettingCache() {
        return this.settingCache;
    }

    private void settingChange(Setting setting) {
        this.settingCache = BeanExtUtils.convert(setting, Setting::new);
        this.settingCache.setNginxUrl(BooleanUtils.isTrue(setting.getNginxOpen())
                && StringUtils.isNotBlank(setting.getNginxUrl())
                ? setting.getNginxUrl() : StringUtils.EMPTY);
        settingCache.setNavName(setting.getNavName());
        settingCache.setLogoToFavicon(BooleanUtils.isTrue(setting.getLogoToFavicon()));
        settingCache.setCutOverSpeed((setting.getCutOverSpeed() == null ? 10 : setting.getCutOverSpeed()) * 1000);
        settingCache.setLogoPath(setting.getLogoPath());
    }

}
