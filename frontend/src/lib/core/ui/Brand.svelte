<script lang="ts">
	import bell from '$lib/assets/bell.svg';
	import wordmark from '$lib/assets/text.svg';
	let { compact = false, subtitleBelow = false }: { compact?: boolean; subtitleBelow?: boolean } = $props();
</script>

<span class:compact class:subtitle-below={subtitleBelow} class="brand" role="img" aria-label={compact ? 'GYKAS' : 'Anketa GYKAS'}>
	<span class="bell" style:mask-image={`url("${bell}")`} aria-hidden="true"></span>
	{#if !compact}
		<span class="wordmark" aria-hidden="true"><span class="brand-caption">Anketa</span><span class="brand-name" style:mask-image={`url("${wordmark}")`}></span></span>
	{/if}
</span>

<style>
	.brand { display: inline-flex; align-items: center; gap: 1rem; color: inherit; }
	.bell, .brand-name { display: block; background: currentColor; mask-size: contain; mask-position: center; mask-repeat: no-repeat; }
	.bell { width: 2.8rem; height: 4rem; order: 2; flex-shrink: 0; }
	.wordmark { display: flex; flex-direction: column; }
	.subtitle-below .wordmark { flex-direction: column-reverse; gap: .5rem; }
	.subtitle-below { align-items: flex-start; }
	.subtitle-below .brand-caption { font-family: Arial, Helvetica, sans-serif; }
	.brand-caption { font: 1.3rem/1.1 Georgia, serif; letter-spacing: .02em; }
	.brand-name { font-size: 3.8rem; width: 3.2em; aspect-ratio: 4.6; }
	.compact .bell { width: 1.65rem; height: 2rem; }
	@media (max-width: 700px) {
		.bell { order: 0; }
	}
</style>
