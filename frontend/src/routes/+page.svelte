<script lang="ts">
	import { goto } from '$app/navigation';
	import { Button } from '$lib/shared/ui';
	import { dotsAnchor } from '$lib/shared/brand-dots';
	import { session, homeFor } from '$lib/features/auth';
	import { t } from '$lib/shared/i18n/index.svelte';

	function handleStart() {
		goto(session.user ? homeFor(session.user.role) : '/login');
	}
</script>

<main class="flex min-h-screen flex-col items-center justify-center gap-12">
	<h1 class="text-5xl font-bold">{t('app.title')}</h1>
	<!-- Dots are hidden here but anchored to the button: on the next page they grow out of it. -->
	<div use:dotsAnchor={{ preset: 'hidden' }}>
		<Button onclick={handleStart}>{t('landing.start')}</Button>
	</div>
</main>
