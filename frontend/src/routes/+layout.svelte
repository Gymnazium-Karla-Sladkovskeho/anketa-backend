<script lang="ts">
	import '$lib/shared/styles/app.css';
	import favicon from '$lib/assets/favicon.svg';
	import { BrandDots } from '$lib/shared/brand-dots';

	let { children } = $props();
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

<BrandDots />
{@render children()}
