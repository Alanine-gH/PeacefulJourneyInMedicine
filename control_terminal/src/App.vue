<template>
  <router-view/>
</template>

<script>
import {useTheme} from '@/theme.js'

export default {
  name: 'App',
  setup() {
    const {init} = useTheme()
    init()
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@400;500;700&display=swap');
</style>
