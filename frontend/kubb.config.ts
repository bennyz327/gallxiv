import { defineConfig } from '@kubb/core'
import { pluginOas } from '@kubb/plugin-oas'
import { pluginVueQuery } from '@kubb/plugin-vue-query'
import { pluginTs } from '@kubb/plugin-ts'

export default defineConfig({
  input: {
    path: './temp/input.json',
  },
  output: {
    path: './src/api/generated',
  },
  plugins: [
    pluginOas(),
    pluginTs(),
    pluginVueQuery({
      output: {
        path: './hooks',
      },
      group: {
        type: 'tag',
        name: ({ group }) => `${group}Hooks`,
      },
      client: {
        importPath: '@/api/app-axios',
      },
    }),
  ],
})
