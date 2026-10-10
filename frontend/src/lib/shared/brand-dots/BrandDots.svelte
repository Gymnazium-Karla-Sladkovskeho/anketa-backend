<script lang="ts">
	import { PRESETS } from './presets';
	import { brandDots } from './state.svelte';

	const COLORS = ['bg-brand-orange', 'bg-brand-gray', 'bg-brand-gold', 'bg-brand-purple'];
	let dots = $derived(PRESETS[brandDots.preset]);
</script>

<!-- Overlay in document coordinates, so it scrolls together with the page. -->
<div
	aria-hidden="true"
	class="pointer-events-none absolute top-0 left-0 z-50 transition-transform duration-700 ease-spring motion-reduce:transition-none"
	style:transform="translate({brandDots.x}px, {brandDots.y}px)"
>
	{#each dots as dot, i (i)}
		<div
			class="absolute top-0 left-0 size-25 origin-top-left rounded-full transition-transform duration-700 ease-spring motion-reduce:transition-none {COLORS[i]}"
			style:transform="translate({dot.x}px, {dot.y}px) scale({dot.size / 100})"
			style:transition-delay="{i * 40}ms"
		></div>
	{/each}
</div>
