<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>

export default {
  name: "App",
  metaInfo() {
    return {
      title: this.$store.state.settings.title,
    }
  },
  mounted() {
    this.$http.get('/api/v1/setting')
    .then(res=>{
      this.$store.dispatch('settings/setSetting', res)
      if(res.logoToFavicon){
        const link = document.createElement('link')
        link.rel = 'shortcut icon';
        link.href=res.logoPath;
        document.head.removeChild(document.querySelector('link[rel=icon]'));
        document.head.appendChild(link);
      }
    })
  },
};
</script>
