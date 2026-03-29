import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import Heading from '@theme/Heading';
import styles from './index.module.css';

const features = [
  {
    title: 'Multiplatform',
    icon: '📱',
    description: 'Single codebase targeting Android and iOS with Kotlin Multiplatform and Compose Multiplatform.',
  },
  {
    title: 'Clean Architecture',
    icon: '🏛️',
    description: 'Every module follows data/domain/presentation layer separation for testability and maintainability.',
  },
  {
    title: 'Modular',
    icon: '🧩',
    description: 'Pick only what you need — or use the umbrella module for everything in one dependency.',
  },
  {
    title: 'Type-Safe Networking',
    icon: '🌐',
    description: 'Ktor-based HTTP client with automatic error mapping to typed AppException hierarchy.',
  },
  {
    title: 'State Management',
    icon: '⚡',
    description: 'BaseViewModel with 5 async helpers — from zero-boilerplate pipes to full state reducers.',
  },
  {
    title: 'Ready-to-Use UI',
    icon: '🎨',
    description: 'Material 3 composables with full theming, plus MiruResourceView for declarative loading states.',
  },
];

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <header className={clsx('hero hero--primary', styles.heroBanner)}>
      <div className="container">
        <Heading as="h1" className="hero__title">
          {siteConfig.title}
        </Heading>
        <p className="hero__subtitle">{siteConfig.tagline}</p>
        <div className={styles.buttons}>
          <Link className="button button--secondary button--lg" to="/docs/getting-started/installation">
            Get Started →
          </Link>
          <Link className="button button--outline button--lg" style={{color: 'white', borderColor: 'white', marginLeft: '1rem'}} href="https://github.com/wahidabd/miru-sdk">
            GitHub
          </Link>
        </div>
        <div className={styles.installSnippet}>
          <code>implementation("com.github.wahidabd.miru-sdk:miru-sdk:{'<version>'}")</code>
        </div>
      </div>
    </header>
  );
}

function Feature({title, icon, description}: {title: string; icon: string; description: string}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center padding-horiz--md" style={{padding: '2rem 1rem'}}>
        <div style={{fontSize: '2.5rem', marginBottom: '0.5rem'}}>{icon}</div>
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {features.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}

function ModuleOverview() {
  const modules = [
    { name: ':core', desc: 'Resource, AppException, extensions', color: '#607D8B', link: '/docs/modules/core' },
    { name: ':network', desc: 'Ktor HTTP client, token management', color: '#9C27B0', link: '/docs/modules/network' },
    { name: ':ui-state', desc: 'BaseViewModel, UiState, EventFlow', color: '#2196F3', link: '/docs/modules/ui-state' },
    { name: ':ui-components', desc: 'Material 3 composables, theming', color: '#2196F3', link: '/docs/modules/ui-components' },
    { name: ':navigation', desc: 'Compose Navigation wrapper', color: '#2196F3', link: '/docs/modules/navigation' },
    { name: ':firebase', desc: 'Remote Config, FCM topics', color: '#FF5722', link: '/docs/modules/firebase' },
    { name: ':auth', desc: 'Google, Apple, Facebook OAuth', color: '#E91E63', link: '/docs/modules/auth' },
    { name: ':persistent', desc: 'Room KMP + DataStore', color: '#795548', link: '/docs/modules/persistent' },
    { name: ':di', desc: 'Koin wiring, SDK initializer', color: '#FF9800', link: '/docs/modules/di' },
  ];

  return (
    <section style={{padding: '4rem 0', background: 'var(--ifm-background-surface-color)'}}>
      <div className="container">
        <Heading as="h2" className="text--center" style={{marginBottom: '2rem'}}>Modules</Heading>
        <div className="row">
          {modules.map((mod) => (
            <div key={mod.name} className="col col--4" style={{marginBottom: '1rem'}}>
              <Link to={mod.link} style={{textDecoration: 'none'}}>
                <div className="card" style={{padding: '1.2rem', borderLeft: `4px solid ${mod.color}`}}>
                  <strong><code>{mod.name}</code></strong>
                  <p style={{margin: '0.3rem 0 0', fontSize: '0.9rem', color: 'var(--ifm-font-color-secondary)'}}>{mod.desc}</p>
                </div>
              </Link>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

export default function Home(): JSX.Element {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout title={siteConfig.title} description={siteConfig.tagline}>
      <HomepageHeader />
      <main>
        <HomepageFeatures />
        <ModuleOverview />
      </main>
    </Layout>
  );
}
