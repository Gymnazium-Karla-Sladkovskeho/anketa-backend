<script lang="ts">
    import { goto } from '$app/navigation';
    import { DashboardShell } from '$lib/core/ui';
    import { session, logout } from '$lib/features/auth';
    let { children } = $props();
    function handleLogout() {
        logout();
        void goto('/login');
    }
</script>

<DashboardShell homeHref="/surveys" userName={session.user?.name ?? ''} onLogout={handleLogout}>
    {@render children()}
</DashboardShell>
