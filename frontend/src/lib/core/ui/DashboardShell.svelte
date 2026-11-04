<script lang="ts">
    import Brand from './Brand.svelte';
    import Button from './Button.svelte';
    import type { Snippet } from 'svelte';
    import { i18n, t } from '$lib/core/i18n/index.svelte';

    let { userName, onLogout, homeHref, children }: { userName: string; onLogout: () => void; homeHref: string; children: Snippet } = $props();
    let theme = $state<'system' | 'light' | 'dark'>('system');
    let systemDark = $state(false);
    const dark = $derived(theme === 'system' ? systemDark : theme === 'dark');
    const themeLabel = $derived(t(theme === 'system' ? 'theme.system' : theme === 'light' ? 'theme.light' : 'theme.dark'));

    $effect(() => {
        const media = window.matchMedia('(prefers-color-scheme: dark)');
        const sync = () => systemDark = media.matches;
        sync();
        media.addEventListener('change', sync);
        return () => media.removeEventListener('change', sync);
    });

    function cycleTheme() {
        theme = dark ? 'light' : 'dark';
    }

</script>

<div class="dashboard" class:light={!dark}>
    <div class="dashboard-inner">
        <header>
            <a href={homeHref} class="home-link" aria-label={t('surveys.heading')}><Brand compact /></a>
            <nav aria-label={t('surveys.heading')} title={userName}>
                <button class="utility language" onclick={() => i18n.locale = i18n.locale === 'cs' ? 'en' : 'cs'} aria-label={t('nav.language')} title={t('nav.language')}>
                    {i18n.locale === 'cs' ? 'EN' : 'CS'}
                </button>
                <button class="utility theme" onclick={cycleTheme} aria-label={`${t('nav.theme')}: ${themeLabel}`} title={`${t('nav.theme')}: ${themeLabel}`}>
                    {#if dark}
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true"><path d="M20 15A9 9 0 0 1 9 4a9 9 0 1 0 11 11Z"/></svg>
                    {:else}
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true"><circle cx="12" cy="12" r="4"/><path d="M12 2v2m0 16v2M2 12h2m16 0h2M5 5l1.5 1.5m11 11L19 19M5 19l1.5-1.5m11-11L19 5"/></svg>
                    {/if}
                </button>
                <Button onclick={onLogout} color="var(--toolbar)" textColor="var(--foreground)" class="logout">{t('nav.logout')}</Button>
            </nav>
        </header>
        {@render children()}
    </div>
</div>

<style>
    .dashboard { --background: #294350; --surface: #3b5969; --panel: #304c5b; --row: #3d5e6e; --row-border: #527281; --foreground: #f3f5f6; --panel-text: #f0f5f5; --toolbar: #294350; --completed-border: #26b3ad; min-height: 100svh; padding: 1.25rem; background: var(--background); color: var(--foreground); }
    .dashboard.light { --background: #f2eedf; --surface: #fffef5; --panel: #e4ece6; --row: #f6faf6; --row-border: #d0ddd3; --foreground: #494f50; --panel-text: #344d43; --toolbar: #e8e7dd; --completed-border: transparent; }
    .dashboard-inner { max-width: 1240px; margin: auto; }
    header { min-height: 3rem; display: flex; align-items: center; justify-content: space-between; gap: 1rem; padding: .35rem .65rem; background: var(--surface); border-radius: .65rem; }
    .home-link { display: flex; align-items: center; color: inherit; text-decoration: none; padding: 0 .25rem; }
    nav { display: flex; align-items: center; gap: .6rem; }
    .utility { padding: .5rem; border-radius: .65rem; }
    .utility:hover { background: var(--toolbar); }
    .language { font-size: .75rem; font-weight: 600; min-width: 2.25rem; }
    .theme svg { width: 1.4rem; height: 1.4rem; }
    nav :global(.logout) { min-height: 2rem; font-size: .75rem; padding: .4rem .9rem; border-radius: .75rem; }
    @media (max-width: 700px) {
        .dashboard { padding: 0; }
        header { border-radius: 0; min-height: 3.5rem; padding: .5rem .75rem; }
        nav { gap: .4rem; }
        nav :global(.logout) { order: -1; }
    }
</style>
