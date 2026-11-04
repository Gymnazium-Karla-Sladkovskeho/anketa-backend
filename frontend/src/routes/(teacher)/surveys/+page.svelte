<script lang="ts">
    import { TeacherHomePage } from '$lib/features/surveys';
    let { data } = $props();
</script>

<TeacherHomePage polls={data.polls} failed={data.failed} />
