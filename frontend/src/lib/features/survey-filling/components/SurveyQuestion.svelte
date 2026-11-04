<script lang="ts">
    import { t } from '$lib/core/i18n/index.svelte';
    import type { FillingQuestion } from '../model/types';
    let { question, index }: { question: FillingQuestion; index: number } = $props();
    let rating = $state(0);
</script>

<fieldset class="question-card">
    <legend>{question.name}</legend>
    <div class="question-content" class:text-content={question.type === 'OPEN'}>
        {#if question.type === 'OPEN'}
            <label class="sr-only" for={`answer-${index}`}>{question.name}</label>
            <textarea id={`answer-${index}`} name={`answer-${index}`} placeholder={t('filling.textPlaceholder')} rows="5"></textarea>
        {:else if question.type === 'CLOSE'}
            {#each question.choices as choice, choiceIndex}
                <label class="choice">
                    <input type={question.multiple ? 'checkbox' : 'radio'} name={`answer-${index}`} value={choiceIndex} />
                    <span>{choice.name}</span>
                </label>
            {/each}
        {:else if question.min !== undefined && question.max !== undefined}
            <div class="rating">
                <label class="sr-only" for={`answer-${index}`}>{question.name}</label>
                <output for={`answer-${index}`}>{rating}</output>
                <input id={`answer-${index}`} name={`answer-${index}`} type="range" min={question.min} max={question.max} step="1" bind:value={rating} />
                <div class="ticks" aria-hidden="true">
                    {#each Array.from({ length: question.max - question.min + 1 }, (_, i) => question.min! + i) as tick}<span>{tick}</span>{/each}
                </div>
            </div>
        {:else}
            <label for={`answer-${index}`} class="sr-only">{question.name}</label>
            <input id={`answer-${index}`} name={`answer-${index}`} type="number" step="any" class="number-input" />
        {/if}
    </div>
</fieldset>

<style>
    .question-card { min-width: 0; margin: 0; padding: 0; border: 1px solid #8297a2; border-radius: .45rem; background: white; color: #294350; overflow: hidden; }
    legend { float: left; width: 100%; padding: .45rem .7rem; border-bottom: 2px solid #8297a2; border-radius: .4rem; font-size: .85rem; font-weight: 600; line-height: 1.4; }
    .question-content { clear: both; min-height: 140px; padding: .8rem 1rem; }
    .text-content { padding: 0; }
    textarea { display: block; width: 100%; min-height: 145px; padding: .8rem; resize: vertical; font-size: .85rem; border-radius: 0 0 .4rem .4rem; }
    textarea::placeholder { color: #7a858b; }
    .choice { display: flex; align-items: flex-start; gap: .55rem; font-size: .85rem; line-height: 1.5; padding: .3rem 0; cursor: pointer; }
    .choice input { flex-shrink: 0; width: 1rem; height: 1rem; margin-top: .15rem; accent-color: #294350; }
    .rating { position: relative; padding: 2.5rem 0 .5rem; }
    .rating output { position: absolute; right: 0; top: 0; font-size: .75rem; font-weight: 600; }
    input[type='range'] { display: block; width: 100%; accent-color: #294350; cursor: pointer; }
    .ticks { display: flex; justify-content: space-between; gap: .1rem; margin-top: .4rem; color: #536571; font-size: .7rem; }
    .number-input { width: 100%; border: 1px solid #8297a2; border-radius: .3rem; padding: .65rem; }
    @media (max-width: 700px) {
        legend { font-size: .75rem; padding: .4rem .5rem; }
        .question-content { min-height: 135px; padding: .6rem; }
        .text-content { padding: 0; }
        .choice, textarea { font-size: .8rem; }
        textarea { min-height: 145px; padding: .6rem; }
        .ticks { font-size: .65rem; }
    }
</style>
