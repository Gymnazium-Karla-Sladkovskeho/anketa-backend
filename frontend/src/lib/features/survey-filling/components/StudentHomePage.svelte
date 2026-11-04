<script lang="ts">
    import { onMount } from 'svelte';
    import { t } from '$lib/core/i18n/index.svelte';
    import type { PollSummary } from '$lib/core/api/contracts';

    let { polls, failed = false }: { polls: PollSummary[]; failed?: boolean } = $props();
    let now = $state(Date.now());
    const active = $derived(polls.filter(poll => Date.parse(poll.from) <= now && Date.parse(poll.to) > now));
    const closed = $derived(polls.filter(poll => Date.parse(poll.to) <= now));
    onMount(() => {
        const timer = setInterval(() => now = Date.now(), 30_000);
        return () => clearInterval(timer);
    });
</script>

<svelte:head><title>{t('surveys.heading')} · Anketa GYKAS</title></svelte:head>

        <main>
            <h1 class="sr-only">{t('surveys.heading')}</h1>
            {#if failed}
                <p class="load-error" role="alert">{t('surveys.loadFailed')}</p>
            {:else}
            <div class="survey-columns">
                {#each ['active', 'completed'] as kind}
                    {@const items = kind === 'active' ? active : closed}
                    <details class="survey-section" open>
                        <summary><h2>{t(kind === 'active' ? 'surveys.active' : 'surveys.completed')}</h2><svg class="chevron" viewBox="0 0 16 16" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true"><path d="m4 6 4 4 4-4"/></svg></summary>
                        <div class="empty-panel" class:has-items={items.length > 0} class:completed={kind === 'completed'}>
                            {#each items as poll (poll.id)}
                                <div class="survey-row">
                                    <span>{poll.name}</span>
                                    {#if kind === 'active'}<a href={`/fill/${poll.id}`}>{t('surveys.fill')} <span aria-hidden="true">→</span></a>{/if}
                                </div>
                            {:else}
                                <p>{t(kind === 'active' ? 'surveys.activeEmpty' : 'surveys.completedEmpty')}</p>
                            {/each}
                        </div>
                    </details>
                {/each}
            </div>
            {/if}
        </main>

<style>
    main { margin-top: 1.25rem; }
    .survey-columns { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); gap: 1.5rem; padding: 1.25rem; background: var(--surface); border-radius: .75rem; }
    summary { display: flex; align-items: center; justify-content: space-between; gap: .5rem; list-style: none; cursor: pointer; margin: 0 .4rem .65rem; }
    summary::-webkit-details-marker { display: none; }
    h2 { font-size: 1rem; text-transform: uppercase; font-weight: 600; line-height: 1.4; }
    .chevron { width: 1rem; height: 1rem; flex-shrink: 0; transition: transform .15s; }
    details[open] .chevron { transform: rotate(180deg); }
    .empty-panel { min-height: clamp(300px, 52svh, 520px); background: var(--panel); color: var(--panel-text); border: 1px solid transparent; border-radius: .65rem; display: flex; align-items: center; justify-content: center; text-align: center; padding: 1.5rem; }
    .has-items { flex-direction: column; align-items: stretch; justify-content: flex-start; gap: .75rem; text-align: left; }
    .survey-row { display: flex; align-items: center; justify-content: space-between; gap: 1rem; background: var(--row); border: 1px solid var(--row-border); border-radius: .65rem; padding: .65rem .8rem; font-size: .875rem; }
    .survey-row > span { overflow-wrap: anywhere; }
    .survey-row a { flex-shrink: 0; font-weight: 600; padding: .35rem .6rem; border-radius: .5rem; background: var(--panel); text-decoration: none; font-size: .75rem; }
    .survey-row a:hover { text-decoration: underline; text-underline-offset: 3px; }
    .load-error { padding: 2rem; text-align: center; }
    .completed { border-color: var(--completed-border); }
    .empty-panel p { max-width: 17rem; font-size: .875rem; line-height: 1.5; }
    @media (min-width: 701px) { .chevron { opacity: 0; } summary:hover .chevron, summary:focus-visible .chevron { opacity: 1; } }
    @media (max-width: 700px) {
        main { margin-top: 1.5rem; padding: 0 .75rem 1.5rem; }
        .survey-columns { grid-template-columns: 1fr; gap: 1.5rem; padding: 0; background: transparent; }
        summary { margin: 0; background: #fff; color: #294350; border: 1px solid #94a7b0; border-radius: .25rem; padding: .35rem .5rem; }
        h2 { font-size: .8rem; text-transform: none; }
        .empty-panel { min-height: 130px; border: 0; border-radius: 0 0 .35rem .35rem; padding: 1.25rem; }
        .completed { min-height: 150px; }
        .empty-panel p { font-size: .8rem; }
    }
</style>
