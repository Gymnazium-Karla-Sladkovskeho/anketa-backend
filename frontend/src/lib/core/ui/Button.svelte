<script lang="ts">
	import type { Snippet } from 'svelte';

	type Props = {
		type?: 'button' | 'submit' | 'reset';
		disabled?: boolean;
		onclick?: () => void;
		children: Snippet;
	};
	let { type = 'button', disabled = false, onclick, children }: Props = $props();
</script>

<button
	{type}
	{disabled}
	{onclick}
	class="rounded-xl bg-blue-600 px-6 py-3 text-white transition duration-200 hover:bg-blue-700 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-blue-600 disabled:opacity-50 motion-safe:hover:scale-105"
>
	{@render children()}
</button>
