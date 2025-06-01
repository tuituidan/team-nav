import http from './http'
import {Message} from 'element-ui'
import {saveAs} from 'file-saver'

export default {
  download(url, downloadName) {
    return http.get(url, {responseType: 'blob'})
      .then((res) => {
        // 从content-disposition中获取文件名
        const disposition = res.headers['content-disposition'];
        const name = downloadName || decodeURIComponent(disposition.split('filename*=utf-8\'\'')[1]);
        const blob = new Blob([res.data]);
        this.saveAs(blob, name);
      })
      .catch(err => {
        if (err.response.data.type === 'application/json') {
          const reader = new FileReader();
          reader.readAsText(err.response.data);
          reader.onload = () => {
            const {message} = JSON.parse(reader.result);
            Message.error(message || '下载失败');
          };
          return;
        }
        Message.error('下载失败');
      })
  },
  saveAs(text, name, opts) {
    saveAs(text, name, opts);
  },
}

