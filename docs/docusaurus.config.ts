import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'Miru SDK',
  tagline: 'Kotlin Multiplatform SDK for accelerating mobile development',
  favicon: 'img/favicon.svg',

  url: 'https://wahidabd.github.io',
  baseUrl: '/miru-sdk/',

  organizationName: 'wahidabd',
  projectName: 'miru-sdk',
  deploymentBranch: 'gh-pages',
  trailingSlash: false,

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  markdown: {
    mermaid: true,
  },
  themes: ['@docusaurus/theme-mermaid'],

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          editUrl: 'https://github.com/wahidabd/miru-sdk/tree/main/docs/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    colorMode: {
      defaultMode: 'light',
      disableSwitch: false,
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: 'Miru SDK',
      logo: {
        alt: 'Miru SDK Logo',
        src: 'img/logo.svg',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docs',
          position: 'left',
          label: 'Docs',
        },
        {
          href: '/docs/api/resource',
          label: 'API',
          position: 'left',
        },
        {
          href: 'https://jitpack.io/#wahidabd/miru-sdk',
          label: 'JitPack',
          position: 'right',
        },
        {
          href: 'https://github.com/wahidabd/miru-sdk',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            { label: 'Getting Started', to: '/docs/getting-started/installation' },
            { label: 'Modules', to: '/docs/modules/core' },
            { label: 'API Reference', to: '/docs/api/resource' },
          ],
        },
        {
          title: 'Guides',
          items: [
            { label: 'ViewModel Patterns', to: '/docs/guides/viewmodel-patterns' },
            { label: 'Theming', to: '/docs/guides/theming' },
            { label: 'Error Handling', to: '/docs/guides/error-handling' },
          ],
        },
        {
          title: 'More',
          items: [
            { label: 'GitHub', href: 'https://github.com/wahidabd/miru-sdk' },
            { label: 'JitPack', href: 'https://jitpack.io/#wahidabd/miru-sdk' },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Miru SDK. Built with Docusaurus.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['kotlin', 'groovy', 'bash'],
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
