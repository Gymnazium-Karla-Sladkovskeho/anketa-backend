<script lang="ts">
    import { SurveyFillingPage } from '$lib/features/survey-filling';
    let { data } = $props();
</script>

{#key data.survey?.id}
    <SurveyFillingPage survey={data.survey} failed={data.failed} />
{/key}
