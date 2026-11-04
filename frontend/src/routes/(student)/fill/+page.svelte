<script lang="ts">
    import { StudentHomePage } from '$lib/features/survey-filling';
    let { data } = $props();
</script>

<StudentHomePage polls={data.polls} failed={data.failed} />
