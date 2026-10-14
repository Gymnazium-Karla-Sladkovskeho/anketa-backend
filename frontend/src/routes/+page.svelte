<script lang="ts">
	import { goto } from '$app/navigation';
	import { Button } from '$lib/core/ui';
	import { session, homeFor } from '$lib/features/auth';
	import { t } from '$lib/core/i18n/index.svelte';

	function handleStart() {
		goto(session.user ? homeFor(session.user.role) : '/login');
	}
</script>

<main class="flex min-h-screen flex-col items-center justify-center gap-12">
	<h1 class="text-5xl font-bold">{t('app.title')}</h1>
	<Button onclick={handleStart}>{t('landing.start')}</Button>
</main>
