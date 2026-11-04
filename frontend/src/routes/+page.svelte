<script lang="ts">
    import { goto } from '$app/navigation';
    import { Brand, Button } from '$lib/core/ui';
    import { session, homeFor } from '$lib/features/auth';
    import { i18n, t } from '$lib/core/i18n/index.svelte';
</script>

<svelte:head><title>Anketa GYKAS</title></svelte:head>

<main class="flex min-h-svh flex-col items-center justify-center bg-[#fafbf9] px-6 py-24 text-center">
    <button class="absolute right-6 top-6 p-2 text-xs font-semibold" aria-label={t('nav.language')} onclick={() => i18n.locale = i18n.locale === 'cs' ? 'en' : 'cs'}>{i18n.locale === 'cs' ? 'EN' : 'CS'} ↗</button>
    <Brand />
    <h1 class="mt-10 font-[Georgia,serif] text-2xl">{t('brand.tagline')}</h1>
    <p class="mt-3 mb-9 text-sm text-[#71818a]">{t('landing.description')}</p>
    <Button class="min-w-48" onclick={() => goto(session.user ? homeFor(session.user.role) : '/login')}>{t('landing.start')} <span aria-hidden="true">→</span></Button>
    <p class="mt-20 text-xs text-[#71818a]">{t('brand.school')}</p>
</main>
