import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars: SidebarsConfig = {
  docs: [
    'intro',
    {
      type: 'category',
      label: 'Getting Started',
      collapsed: false,
      items: [
        'getting-started/installation',
        'getting-started/quick-start',
        'getting-started/architecture',
      ],
    },
    {
      type: 'category',
      label: 'Modules',
      collapsed: false,
      items: [
        'modules/core',
        'modules/network',
        'modules/ui-state',
        'modules/ui-components',
        'modules/navigation',
        'modules/di',
        'modules/firebase',
        'modules/auth',
        'modules/persistent',
      ],
    },
    {
      type: 'category',
      label: 'Guides',
      items: [
        'guides/viewmodel-patterns',
        'guides/theming',
        'guides/error-handling',
        'guides/clean-architecture',
        'guides/migration',
      ],
    },
    {
      type: 'category',
      label: 'API Reference',
      items: [
        'api/resource',
        'api/base-viewmodel',
        'api/miru-resource-view',
        'api/app-exception',
        'api/api-service',
        'api/paging-state',
      ],
    },
  ],
};

export default sidebars;
