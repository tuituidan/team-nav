package com.tuituidan.openhub.service.setting;

import com.tuituidan.openhub.bean.dto.Nginx;
import com.tuituidan.openhub.bean.entity.Setting;
import com.tuituidan.openhub.repository.SettingRepository;
import com.tuituidan.openhub.util.thread.CompletableUtils;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private List<ISettingListener> settingListeners;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.settingChange(get());
    }

    /**
     * get
     *
     * @return Setting
     */
    public Setting get() {
        return settingRepository.findAll().stream().findFirst().orElse(new Setting());
    }

    /**
     * save
     *
     * @param nginx nginx
     */
    public void saveNginx(Nginx nginx) {
        Setting setting = new Setting().setId("setting-id");
        setting.setNginxOpen(nginx.getOpen());
        setting.setNginxUrl(StringUtils.stripEnd(nginx.getUrl(), "/"));
        settingRepository.save(setting);
        this.settingChange(setting);
    }

    private void settingChange(Setting setting) {
        for (ISettingListener listener : settingListeners) {
            CompletableUtils.runAsync(() -> listener.settingChange(setting));
        }
    }

}
