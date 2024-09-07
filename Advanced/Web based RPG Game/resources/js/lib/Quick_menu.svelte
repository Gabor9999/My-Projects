<script>
    import { getContext } from 'svelte';
    import Button from '../lib/Button.svelte';
    import Options from '../lib/Options.svelte';

    export let csrfToken,difficulty,text_size,pos_x,pos_y,chapter,itemIndex,item,helper,main_quest, main_items, items, chests, side_quests, exp, player, choices, consumables, AIdata, usedCraft;

    let options = false;
    var locked_fl,main_q,obj;
    var { locked } = getContext('flags');
    locked.subscribe((value) => {locked_fl = value});
    let charHP = player.HP;
    
    
    function setOptions() {
        options = !options;
    }

    //Handle key press
    function onKeyDown(e) {
        switch(e.keyCode) {
            case 27: //Esc
                locked.set(1);
                break;
            default:
                return;
        }
    }

    //Save ingame attributes
    const save = async () => {
        for(let i = 0; i < side_quests.length; i++) {
            side_quests[i] = side_quests[i].get_side_quest_data
        }
        main_q = main_quest.get_main_quest
        obj = main_quest.get_objective
        const response = await fetch('/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken,
            },
            body: JSON.stringify({ chapter, item, charHP, usedCraft, exp, choices, AIdata, consumables, pos_x, pos_y, itemIndex, helper, main_q, obj, main_items, items, chests, side_quests}),
        });

        if (response.ok) {
            window.location.href = "/Game"
        } else {
            alert("Failed to save the game!")
        }
    };
</script>
<!-- Menu options -->
{#if !options}
<div style="margin-top: 2%;">
    <form on:submit|preventDefault={save}>
    <Button type="submit" class="primary">
        Save
    </Button>
    </form>
    <Button class="primary" on:click={setOptions}>
        Options
    </Button>
    <a href="/">
    <Button class="primary">
        Back to Menu
    </Button>
    </a>
    <a href="/logout">
    <Button class="primary">
        Quit and Logout
    </Button>
    </a>
</div>
{:else}
<div class="options-container">
    <div>
        <Button class="back" on:click={setOptions}>
                        
        </Button><br>
        <Options bind:difficulty bind:text_size {csrfToken}/>
    </div>
</div>
{/if}
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<style>
    .options-container {
        margin: 0;
        padding: 30px;
        padding-bottom: 60px;
        background-color: white;
        border: 2px solid black;
        border-radius: 30px 30px;
        box-shadow: 5px 5px 5px 5px rgb(44, 44, 44);
    }
</style>