import type {ThemeDefinition} from "vuetify"
import {createVuetify} from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles'
import {VDateInput, VTreeview} from "vuetify/labs/components"
import colors from "vuetify/util/colors"

const defaultTheme: ThemeDefinition = {
  dark: false,
  colors: {
    background: '#000000',
    primary: colors.blueGrey.darken4, // #263238
    'primary-opacity': colors.blueGrey.darken4 + '32',
    surface: colors.grey.darken4,
    secondary: colors.blueGrey.darken3,
    success: colors.green.accent3,
    info: colors.blue.lighten3,
    warning: colors.amber.base,
    error: colors.deepOrange.accent4,
    'blue-accent-3': colors.blue.accent3,
    'login-primary': '#2979FF',
  },
}

export const vuetify = createVuetify({
  components: {
    ...components,
    VDateInput,
    VTreeview,
  },
  directives,
  locale: {
    locale: 'en'
  },
  date: {
    locale: {
      en: 'zh-TW'
    }
  },
  theme: {
    defaultTheme: 'defaultTheme',
    themes: {
      defaultTheme,
    }
  },
})
