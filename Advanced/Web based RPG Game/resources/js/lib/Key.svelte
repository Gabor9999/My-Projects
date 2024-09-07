<script>
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let locked;

    function keydown(e) {
        if (e.keyCode === 13 && $locked === 0) {
            dispatch('inc');
        }
    }
</script>
<svelte:window on:keydown|preventDefault={keydown}></svelte:window>