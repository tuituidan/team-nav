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
    this.loadSettings();
    this.loadUserInfo();
  },
  methods: {
    loadUserInfo() {
      this.$store.dispatch('user/GetUserInfo');
    },
    loadSettings() {
      this.$store.dispatch('settings/loadSettings').then(res => {
        if (res.logoToFavicon) {
          const link = document.createElement('link')
          link.rel = 'shortcut icon';
          link.href = res.logoPath;
          document.head.removeChild(document.querySelector('link[rel=icon]'));
          document.head.appendChild(link);
        }
      })
    }
  }
};
</script>
