<script lang="ts">
	import { goto } from '$app/navigation';
	import { Button } from '$lib/shared/ui';
	import { dotsAnchor } from '$lib/shared/brand-dots';
	import { t } from '$lib/shared/i18n/index.svelte';
	import { login as loginRequest } from '../api/auth.api';
	import { session } from '../model/session.svelte';
	import { homeFor } from '../model/routes';

	let login = $state('');
	let password = $state('');
	let status = $state<'idle' | 'loading' | 'error'>('idle');
	let errorText = $state('');

	async function handleSubmit(event: SubmitEvent) {
		event.preventDefault();
		if (!login.trim() || !password) {
			status = 'error';
			errorText = t('login.empty');
			return;
		}
		status = 'loading';
		try {
			const user = await loginRequest({ login: login.trim(), password });
			session.user = user;
			await goto(homeFor(user.role));
		} catch {
			status = 'error';
			errorText = t('login.failed');
		}
	}
</script>

<main class="flex min-h-screen flex-col items-center justify-center gap-10">
	<!-- The dots live here: a fixed-size slot in the normal flow, so the layout never shifts. -->
	<div class="size-30" use:dotsAnchor={{ preset: status === 'loading' ? 'loading' : 'logo' }}></div>

	<form
		onsubmit={handleSubmit}
		inert={status === 'loading'}
		class="flex flex-col items-center gap-4"
		class:invisible={status === 'loading'}
	>
		<label class="sr-only" for="login">{t('login.login')}</label>
		<input
			id="login"
			type="text"
			autocomplete="username"
			placeholder={t('login.login')}
			bind:value={login}
			class="w-80 rounded-lg border border-slate-300 px-4 py-2"
		/>
		<label class="sr-only" for="password">{t('login.password')}</label>
		<input
			id="password"
			type="password"
			autocomplete="current-password"
			placeholder={t('login.password')}
			bind:value={password}
			class="w-80 rounded-lg border border-slate-300 px-4 py-2"
		/>
		<p class="min-h-6 text-sm text-red-600" role="alert">{status === 'error' ? errorText : ''}</p>
		<Button type="submit">{t('login.submit')}</Button>
	</form>
</main>
