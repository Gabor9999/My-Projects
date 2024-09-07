<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let helper,chiefMenu,usedCraft,consumables;

    var selector;

    let trade = false;
    let craft = false;

    var { locked, select_item} = getContext('flags');
    select_item.subscribe((value) => {selector = value});

    //Check if the 'Advance the Story' option is needed
    onMount(() => {
        select_item.set((helper == 10) ? 0:1)
    })

    //Handle key press
    function onKeyDown(e) {
        switch(e.keyCode) {
            case 27: //Esc
				chiefMenu = false; 
				break;
            case 13: //Enter
				if(selector == 0) {
					chiefMenu = false
					locked.set(0);
				} else if(selector == 1) {
                    trade = true;
                } else {
                    craft = true;
                }
				break;
			case 38: // ^
				if(selector == 2 || (selector == 1 && helper == 10)) {
                    select_item.update((n) => (n-1))
				}
				break;
			case 40: // ˇ
				if(selector != 2) {
					select_item.update((n) => (n+1))
				}
				break;
        }
    }

    //Add new items if requirements are met
    function addItem(item) {
        if(item == 0) {
            if(consumables[0] >= 3) {
                consumables[0] -= 3;
                dispatch('item')
            }
        } else {
            if (item == 1 && consumables[1] >= 3) {
                consumables[1] -= 3;
                usedCraft += 1
                dispatch('invitem', {item: item})
            } else if (item == 2 && consumables[2] >= 2) {
                consumables[2] -= 2;
                usedCraft += 10
                dispatch('invitem', {item: item})
            }
        }
    }
</script>
<!-- Initial Menu Window-->
{#if !trade && !craft}
<table class="chiefMenu">
    {#if helper == 10}<tr><th class="normalBorder" class:selectActive={ selector == 0 }>Advance the Story</th></tr>{/if}
    <tr><th class="normalBorder" class:selectActive={ selector == 1}>Trade</th></tr>
    <tr><th class="normalBorder" class:selectActive={ selector == 2}>Craft</th></tr>
</table>
{/if}
<!-- Trade Menu -->
{#if trade}
<div class="menu_label">Trade</div>
<div class="menu">
    <div class="menu_item">
    <table>
        <tr><td><img src="monster_essence.png" alt="Monster Essence" width=64px height=64px></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">Monster Essence {consumables[0]}/3</td></tr>
        <tr><td><button class="card-button" on:click={() => addItem(0)}>Trade</button></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">Potion</td></tr>
        <tr><td><img src="potion.png" alt="Potion" width=64px height=64px></td></tr>
    </table>
    </div>
</div>
{:else if craft}
<!-- Craft Menu -->
<div class="menu_label">Craft</div>
<div class="menu">
    <div class="menu_item">
    <table>
        <tr><td><img src="leather.png" alt="Leather"></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">Leather {consumables[1]}/3</td></tr>
        <tr><td><button class="card-button" class:card-button-disabled={usedCraft % 10 == 1} on:click={() => addItem(1)} disabled={usedCraft % 10 == 1}>Craft</button></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">Common Sandals</td></tr>
        <tr><td><img src="armors/common_sandals.png" alt="Sandals"></td></tr>
    </table>
    </div>
    <div class="menu_item">
    <table>
        <tr><td><img src="string.png" alt="String" width=64px height=64px></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">String {consumables[2]}/2</td></tr>
        <tr><td><button class="card-button" class:card-button-disabled={usedCraft > 9} on:click={() => addItem(2)} disabled={usedCraft > 9}>Craft</button></td></tr>
        <tr><td style="font-family: Papyrus, fantasy;">Merchant's Hood</td></tr>
        <tr><td><img src="armors/merchants_hood.png" alt="Hood"></td></tr>
    </table>
    </div>
</div>
{/if}
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<style>
    .menu {
        text-align: center;
        z-index: 2;
        position: absolute;
        left: 20%;
        width: 60%;
        top: 25%;
        height: 43%;
        background-color: azure;
        border: 2px solid black;
    }
    .menu_item {
        display: inline-table;
        padding: 5%;
        border: 2px solid black;
        border-radius: 35px;
        width: 12%;
        margin-top: 8px;
        margin-right: 5px;
    }
    .menu_label {
        text-align: center;
        z-index: 2;
        position: absolute;
        font-size: 140%;
        font-weight: bold;
        left: 20%;
        width: 12%;
        top: 21%;
        height: 4%;
        background-color: azure;
        border: 2px solid black;
    }
    .chiefMenu {
        z-index: 2;
        position: absolute;
        top: 76%;
        width: 22%;
        height: 24%;
        background-color: aliceblue;
    }
    .normalBorder {
        border: 1px solid black;
        font-family: Papyrus, fantasy;
    }
    .selectActive {
        border: 4px solid green;
    }
    .card-button {
        background-color: #0db136;
        color: azure;
        border-radius: 35px;
        padding-top: 5%;
        padding-bottom: 5%;
        padding-left: 10%;
        padding-right: 10%;
        margin-top: 5%;
        margin-bottom: 5%;
        font-size: 120%;
    }

    .card-button-disabled {
        background-color: black;
        color: azure;
        border-radius: 35px;
        padding-top: 5%;
        padding-bottom: 5%;
        padding-left: 10%;
        padding-right: 10%;
        margin-top: 5%;
        margin-bottom: 5%;
        font-size: 120%;
    }
</style>