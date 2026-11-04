<script lang="ts">
	import type { Snippet } from 'svelte';

	type Props = {
		type?: 'button' | 'submit' | 'reset';
		disabled?: boolean;
		onclick?: () => void;
		children: Snippet;
		color?: string;
		textColor?: string;
		class?: string;
	};
	let { type = 'button', disabled = false, onclick, children, color = 'var(--ink)', textColor = '#fff', class: className = '' }: Props = $props();
</script>

<button
	{type}
	{disabled}
	{onclick}
	style:background-color={color}
	style:color={textColor}
	class={`inline-flex min-h-11 items-center justify-center gap-2 rounded-xl px-5 py-2.5 text-sm font-semibold transition-[filter,box-shadow] hover:brightness-110 focus-visible:outline-2 focus-visible:outline-offset-4 focus-visible:outline-current disabled:cursor-not-allowed disabled:opacity-50 ${className}`}
>
	{@render children()}
</button>
