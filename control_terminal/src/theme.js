/**
 * 全局主题管理
 * 用法：import { useTheme } from '@/theme.js'
 */
import {reactive} from 'vue'

const STORAGE_KEY = 'pjdm_theme'

const state = reactive({
    isDark: localStorage.getItem(STORAGE_KEY) !== 'light'
})

export function useTheme() {
    function apply() {
        document.documentElement.setAttribute('data-theme', state.isDark ? 'dark' : 'light')
        localStorage.setItem(STORAGE_KEY, state.isDark ? 'dark' : 'light')
    }

    function toggle() {
        state.isDark = !state.isDark
        apply()
    }

    function init() {
        apply()
    }

    return {state, toggle, init}
}
