<script lang="ts">
	import { goto } from '$app/navigation';
	import schoolPattern from '$lib/assets/school-pattern.svg';
	import { Brand, Button } from '$lib/core/ui';
	import { i18n, t } from '$lib/core/i18n/index.svelte';
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

<svelte:head><title>{t('login.submit')} · Anketa GYKAS</title></svelte:head>

<main class="login-layout" style:--school-pattern={`url("${schoolPattern}")`}>
    <section class="identity" aria-label={t('brand.school')}>
        <span class="school-name">{t('brand.school')}</span>
        <div class="identity-center">
            <Brand subtitleBelow />
        </div>
        <p class="identity-footer">{t('brand.description')}</p>
    </section>
    <section class="form-panel">
        <button class="language" aria-label={t('nav.language')} onclick={() => i18n.locale = i18n.locale === 'cs' ? 'en' : 'cs'}>
            {i18n.locale === 'cs' ? 'EN' : 'CS'} <span aria-hidden="true">↗</span>
        </button>
        <div class="login-content">
            <div class="mobile-brand"><Brand /></div>
            <h1>{t('login.heading')}</h1>
            <p class="intro">{t('login.description')}</p>
            <form onsubmit={handleSubmit} aria-busy={status === 'loading'}>
                <fieldset disabled={status === 'loading'}>
                    <label for="login">{t('login.login')}</label>
                    <input id="login" type="text" autocomplete="username" bind:value={login} aria-describedby={status === 'error' ? 'login-error' : undefined} />
                    <label for="password">{t('login.password')}</label>
                    <input id="password" type="password" autocomplete="current-password" bind:value={password} aria-describedby={status === 'error' ? 'login-error' : undefined} />
                    <p id="login-error" class="error" role="alert">{status === 'error' ? errorText : ''}</p>
                    <Button type="submit" class="w-full" disabled={status === 'loading'}>
                        {status === 'loading' ? t('login.loading') : t('login.submit')}
                        <span aria-hidden="true">→</span>
                    </Button>
                </fieldset>
            </form>
            <p class="help">{t('login.help')}</p>
        </div>
        <p class="mobile-school">{t('brand.school')}</p>
    </section>
</main>

<style>
    .login-layout { min-height: 100svh; display: grid; grid-template-columns: 56% 44%; }
    .identity { position: relative; isolation: isolate; overflow: hidden; background: var(--ink); color: #fff; padding: 2.5rem 3rem; display: flex; flex-direction: column; justify-content: space-between; }
    .identity::before { content: ''; position: absolute; inset: 0; z-index: -1; background-image: var(--school-pattern); background-size: 740px auto; background-position: center; opacity: .2; pointer-events: none; mask-image: radial-gradient(ellipse at 50% 50%, transparent 12%, #000 78%); }
    .identity::after { content: ''; position: absolute; inset: 0; z-index: -1; pointer-events: none; background: linear-gradient(180deg, #29435099, transparent 18%, transparent 82%, #29435099); }
    .school-name, .identity-footer { font-size: .75rem; letter-spacing: .04em; color: #c5d2d7; }
    .identity-center { align-self: center; padding: 5rem 0; }
    .identity-center :global(.brand-name) { font-size: clamp(4rem, 7vw, 7rem); }
    .identity-center :global(.brand-caption) { font-size: 1.8rem; }
    .identity-center :global(.bell) { width: 4.6rem; height: 5.5rem; }
    .form-panel { position: relative; display: flex; align-items: center; justify-content: center; padding: 6rem 2.5rem; }
    .language { position: absolute; top: 2rem; right: 2.5rem; font-size: .75rem; font-weight: 600; display: flex; gap: .6rem; padding: .5rem; }
    .login-content { width: 100%; max-width: 20rem; }
    h1 { font: 2rem/1.2 Georgia, serif; margin: 0 0 .7rem; }
    .intro { color: #71818a; font-size: .875rem; margin-bottom: 2.2rem; }
    fieldset { border: 0; padding: 0; margin: 0; min-width: 0; }
    label { display: block; font-size: .75rem; font-weight: 600; margin: 1.2rem 0 .5rem; }
    input { width: 100%; height: 2.8rem; border: 1px solid #9aaab2; border-radius: .35rem; padding: .6rem .8rem; background: #fff; color: var(--ink); }
    input:focus { outline: 2px solid #6b9696; border-color: transparent; }
    .error { min-height: 1.5rem; margin: .6rem 0; font-size: .75rem; color: #a12f39; }
    .help { margin: 1.2rem 0 0; font-size: .75rem; line-height: 1.7; color: #71818a; }
    .mobile-brand, .mobile-school { display: none; }
    @media (max-width: 700px) {
        .login-layout { display: block; }
        .identity { display: none; }
        .form-panel { min-height: 100svh; padding: 6rem 1.5rem; }
        .language { top: 1.3rem; right: 1.3rem; }
        .mobile-brand { display: block; margin-bottom: 3rem; }
        h1 { font-size: 1.65rem; }
        .mobile-school { display: block; position: absolute; bottom: 1.5rem; font-size: .65rem; color: #71818a; }
    }
</style>
