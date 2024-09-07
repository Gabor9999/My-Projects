<!-- Options window -->
<h1 class="options">Options</h1>
<hr>
<h2 style="text-align: center">Difficulty</h2>
<div style="text-align: center">
<Button class="secondary-1" id="secondary-1" on:click={() => setDifficulty(1)}>
    {content1}
</Button>
<Button class="secondary-2" id="secondary-2" on:click={() => setDifficulty(2)}>
    {content2}
</Button>
<Button class="secondary-3" id="secondary-3" on:click={() => setDifficulty(3)}>
    {content3}
</Button>
</div>
<h2 style="text-align: center">Text Size</h2>
<div class="textsize-container" style="font-size: {text_size}pt; font-family: Papyrus, fantasy; margin-bottom: 1%">"Perhaps one did not want to be loved so much as to be understood"</div>
<div class="textsize-container">{text_size}</div>
<div class="textsize-container">
    <button disabled='{hasReachedUpperBound}' on:click={incTextSize}>+</button>
    <button disabled='{hasReachedLowerBound}' on:click={decTextSize}>-</button>
</div><br>
<form on:submit|preventDefault={saveOptions}  style="text-align: center">
    <button type="submit" class="btn btn-success">Apply changes</button>
</form>

<script>
    import Button from "../lib/Button.svelte"
    import { onMount } from "svelte"

    export let difficulty, text_size, csrfToken;

    //Display current difficulty with stars
    onMount(() => {
        switch(difficulty) {
            case 1:
                content1 = "⭐ Easy ⭐"
                break
            case 2:
                content2 = "⭐ Medium ⭐"
                break
            case 3:
                content3 = "⭐ Hard ⭐"
                break
        }
    })

    let hasReachedLowerBound = false
    let hasReachedUpperBound = false
    function incTextSize() {
        if(text_size === 10) {
            hasReachedLowerBound = false
        }
        text_size = text_size + 1
        if(text_size === 30) {
            hasReachedUpperBound = true
        }
    }
    function decTextSize() {
        if(text_size === 30) {
            hasReachedUpperBound = false
        }
        text_size = text_size - 1
        if(text_size === 10) {
            hasReachedLowerBound = true
        }
    }

    let content1 = "Easy"
    let content2 = "Medium"
    let content3 = "Hard"

    //Set chosen difficulty and text size locally
    function setDifficulty(param) {
        switch(difficulty) {
            case 1:
                content1 = "Easy"
                break
            case 2:
                content2 = "Medium"
                break
            case 3:
                content3 = "Hard"
                break
            default:
                break
        }
        switch(param) {
            case 1:
                content1 = "⭐ Easy ⭐"
                break
            case 2:
                content2 = "⭐ Medium ⭐"
                break
            case 3:
                content3 = "⭐ Hard ⭐"
                break
            default:
                break
        }
        difficulty = param
    }

    //Save chosen difficulty and text size
    const saveOptions = async () => {
        const response = await fetch('/save-options', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken,
            },
            body: JSON.stringify({ difficulty, text_size}),
        });

        if (response.ok) {
            alert("Options saved!")
        } else {
            alert("Failed to save the options!")
        }
    };
</script>
<style>
    .textsize-container {
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .options {
        text-align: center;
    }
    hr { 
        width: 50%;
        margin-bottom: 50px;
    }
</style>