<script lang="ts">
    import { onMount } from 'svelte';
    import { t } from '$lib/core/i18n/index.svelte';
    import type { PollSummary } from '$lib/core/api/contracts';
    import { pollPhase } from '../model/phase';

    let { polls, failed = false }: { polls: PollSummary[]; failed?: boolean } = $props();
    let now = $state(Date.now());
    const columns = $derived([
        { phase: 'inactive', title: t('teacher.inactive'), empty: t('teacher.inactiveEmpty') },
        { phase: 'active', title: t('surveys.active'), empty: t('surveys.activeEmpty') },
        { phase: 'closed', title: t('surveys.completed'), empty: t('surveys.completedEmpty') }
    ] as const);
    onMount(() => {
        const timer = setInterval(() => now = Date.now(), 30_000);
        return () => clearInterval(timer);
    });
</script>

<svelte:head><title>{t('surveys.heading')} · Anketa GYKAS</title></svelte:head>

{#snippet addSurveyLink()}
    <a class="add-survey" href="/surveys/create"><span aria-hidden="true">＋</span>{t('teacher.add')}</a>
{/snippet}

<main>
    <h1 class="sr-only">{t('surveys.heading')}</h1>
    {#if failed}
        <p class="load-error" role="alert">{t('surveys.loadFailed')}</p>
    {:else}
        <div class="page-actions mobile-actions">
            {@render addSurveyLink()}
        </div>
        <div class="columns">
            {#each columns as column (column.phase)}
                {@const items = polls.filter(poll => pollPhase(poll, now) === column.phase)}
                <section aria-labelledby={`title-${column.phase}`}>
                    <div class="column-heading"><h2 id={`title-${column.phase}`}>{column.title}</h2><span class="count">{items.length}</span></div>
                    <div class="panel">
                        <div class="survey-list">
                            {#each items as poll (poll.id)}
                                <article class="survey-row">
                                    <a class="survey-name" href={`/surveys/${poll.id}`}>{poll.name}</a>
                                    <a class="action" href={column.phase === 'closed' ? `/surveys/${poll.id}/results` : `/surveys/${poll.id}`} aria-label={`${t(column.phase === 'closed' ? 'teacher.results' : 'teacher.detail')}: ${poll.name}`}>
                                        {t(column.phase === 'closed' ? 'teacher.results' : 'teacher.detail')}
                                    </a>
                                </article>
                            {:else}
                                <p class="empty">{column.empty}</p>
                            {/each}
                        </div>
                    </div>
                </section>
            {/each}
        </div>
        <div class="page-actions desktop-actions">
            {@render addSurveyLink()}
        </div>
    {/if}
</main>

<style>
    main { margin-top: 1.25rem; }
    .columns { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 1.25rem; background: var(--surface); padding: 1.25rem; border-radius: .85rem; }
    section { min-width: 0; display: flex; flex-direction: column; }
    .column-heading { display: flex; align-items: center; gap: .6rem; margin: 0 .25rem .8rem; }
    h2 { font-size: clamp(.85rem, 1.45vw, 1.2rem); font-weight: 600; text-transform: uppercase; line-height: 1.4; }
    .count { display: inline-grid; place-items: center; min-width: 1.4rem; height: 1.4rem; padding: 0 .3rem; border: 1px solid currentColor; border-radius: 50%; font-size: .65rem; opacity: .7; }
    .panel { display: flex; flex: 1; flex-direction: column; min-height: clamp(350px, 57svh, 590px); gap: 2rem; background: var(--panel); color: var(--panel-text); padding: 1rem .85rem; border-radius: .85rem; }
    .survey-list { display: flex; flex: 1; flex-direction: column; gap: .75rem; }
    .survey-row { display: flex; align-items: center; justify-content: space-between; gap: .75rem; background: var(--row); border: 1px solid var(--row-border); border-radius: .65rem; padding: .75rem; font-size: .875rem; line-height: 1.5; }
    .survey-name { min-width: 0; overflow-wrap: anywhere; text-decoration: none; }
    .survey-name:hover { text-decoration: underline; text-underline-offset: 3px; }
    .action { flex-shrink: 0; font-size: .75rem; font-weight: 600; padding: .35rem .6rem; border-radius: .5rem; background: var(--panel); text-decoration: none; }
    .action:hover { text-decoration: underline; text-underline-offset: 3px; }
    .empty { margin: auto; padding: 1.5rem .5rem; text-align: center; font-size: .85rem; line-height: 1.6; }
    .page-actions { display: flex; justify-content: center; margin-top: 1.5rem; }
    .mobile-actions { display: none; }
    .add-survey { width: 100%; max-width: 280px; display: flex; align-items: center; justify-content: center; gap: .5rem; min-height: 3.25rem; padding: .75rem; border-radius: .9rem; background: #355e76; color: #fff; font-size: .85rem; font-weight: 600; text-decoration: none; }
    .add-survey:hover { background: #2c5066; }
    .load-error { padding: 2rem; border-radius: .6rem; background: var(--surface); text-align: center; }
    @media (max-width: 900px) {
        .columns { gap: .8rem; padding: 1rem; }
        .survey-row { align-items: flex-start; flex-direction: column; gap: .25rem; }
        h2 { font-size: .85rem; }
    }
    @media (max-width: 700px) {
        main { margin-top: 1.25rem; padding: 0 .75rem 1.5rem; }
        .mobile-actions { display: flex; margin: 0 0 1.25rem; }
        .desktop-actions { display: none; }
        .add-survey { max-width: none; }
        .columns { grid-template-columns: 1fr; gap: 1.5rem; padding: 1rem .75rem; }
        .panel { min-height: 180px; }
        .survey-row { flex-direction: row; align-items: center; gap: .75rem; }
        .column-heading { margin-bottom: .6rem; }
        h2 { font-size: .9rem; }
    }
</style>
