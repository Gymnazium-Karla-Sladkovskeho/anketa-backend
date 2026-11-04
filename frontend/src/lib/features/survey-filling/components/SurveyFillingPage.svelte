<script lang="ts">
    import { Button } from '$lib/core/ui';
    import { t } from '$lib/core/i18n/index.svelte';
    import SurveyQuestion from './SurveyQuestion.svelte';
    import type { FillingSurvey } from '../model/types';

    let { survey, failed = false }: { survey: FillingSurvey | null; failed?: boolean } = $props();
    let showNotice = $state(false);
    function handleSubmit(event: SubmitEvent) {
        event.preventDefault();
        // No answers leave this form until the submission/anonymity DTO is agreed.
        showNotice = true;
    }
</script>

<svelte:head><title>{survey?.name ?? t('surveys.heading')} · Anketa GYKAS</title></svelte:head>

<main>
    <a class="back" href="/fill">← {t('filling.back')}</a>
    {#if failed || !survey}
        <p class="error" role="alert">{t('filling.loadFailed')}</p>
    {:else}
        <form onsubmit={handleSubmit}>
            <div class="form-panel">
                <h1>{survey.name}</h1>
                <div class="questions">
                    {#each survey.questions as question, index}
                        <SurveyQuestion {question} {index} />
                    {/each}
                </div>
            </div>
            <p class="demo-note">{t('filling.preview')}</p>
            <Button type="submit" color="#208530" textColor="#fff" class="submit-button">{t('filling.submit')}</Button>
            {#if showNotice}<p class="notice" role="status">{t('filling.notSubmitted')}</p>{/if}
        </form>
    {/if}
</main>

<style>
    main { width: 100%; max-width: 760px; margin: 1.75rem auto 0; padding-bottom: 1rem; }
    .back { display: inline-block; font-size: .75rem; margin: 0 0 1rem .25rem; text-underline-offset: 3px; }
    .back:hover { text-decoration: underline; }
    .form-panel { padding: 2rem; background: var(--surface); border-radius: .85rem; }
    h1 { margin-bottom: 1.75rem; font-size: 1.65rem; line-height: 1.3; text-transform: uppercase; font-weight: 400; overflow-wrap: anywhere; }
    .questions { display: flex; flex-direction: column; gap: 1.75rem; }
    form :global(.submit-button) { width: 100%; min-height: 3.25rem; border-radius: 1.2rem; font-size: .85rem; letter-spacing: .02em; }
    .demo-note { font-size: .75rem; line-height: 1.5; margin: 1rem .25rem; }
    .notice, .error { margin-top: 1rem; padding: 1rem; background: var(--surface); border: 1px solid currentColor; border-radius: .5rem; font-size: .85rem; line-height: 1.6; }
    @media (max-width: 700px) {
        main { margin-top: 1rem; padding: 0 .75rem 1.5rem; }
        .form-panel { padding: 1rem .5rem; }
        h1 { font-size: 1.3rem; margin: 0 0 1.5rem; }
        .questions { gap: 1.25rem; }
        form :global(.submit-button) { min-height: 2.75rem; width: calc(100% - 2rem); margin: 0 1rem; }
    }
</style>
