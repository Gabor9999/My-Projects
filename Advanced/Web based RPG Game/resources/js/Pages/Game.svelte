<!-- Black shader-->
{#if locked_fl == 7 || showShader}<div class="dark-shader"></div>{/if}
<!-- Transparent to Black shader-->
{#if fightShader}<div class="dark-shader2"></div>{/if}
<!-- Choices -->
{#if locked_fl == 7}
    {#if choice}
    <div class="choices" style="z-index: 6; position: absolute; height: 100%; width: 100%; font-family: Papyrus, fantasy;">
        {#if helper == 11}
        <div class="choice-text">
            <h2>Choose a skill to acquire</h2>
        </div>
        <div class="card" style="left: 22.5%">
            <h2>Bash</h2>
            <br>
            <div>Higher chance of stun.</div>
            <div>Slightly lower damage.</div>
            <br>
            <button class="card-button" on:click={() => {unlock(1); choices += 1}}>Choose</button>
        </div>
        <div class="card" style="left: 42.5%">
            <h2>Poison Blade</h2>
            <br>
            <div>Poisonous attack.</div>
            <div>Enemy recieves damage at the start of its turn for 1-3 turns.</div>
            <div>Poison damage is always 10% of HP.</div>
            <br>
            <button class="card-button" on:click={() => {unlock(2); choices += 2}}>Choose</button>
        </div>
        <div class="card" style="right: 22.5%">
            <h2>Eye for an Eye</h2>
            <br>
            <div>Deals 2x damage if player was attacked by 2 enemies last turn.</div>
            <div>If the players HP is below 25%, deals 2x damage.</div>
            <div>Or if both...</div>
            <br>
            <button class="card-button" on:click={() => {unlock(3); choices += 3}}>Choose</button>
        </div>
        {:else if helper == 20}
        <div class="choice-text">
            <h2 style="margin: 0">Choose a companion</h2>
            <p>Your companion's strength is and will be dependent on your stats</p>
        </div>
        <div class="card" style="left: 30%">
            <h2>Rogue</h2>
            <br>
            <table style="text-align: center;width: 90%">
                <tr><td><b>ATK</b></td><td>{Math.round(player.atk * 0.7)}</td></tr>
                <tr><td><b>DEF</b></td><td>{player.def}</td></tr>
                <tr><td><b>ACC</b></td><td>{Math.round(player.acc * 1.2)}</td></tr>
                <tr><td><b>EVA</b></td><td>{Math.round(player.eva * 1.5)}</td></tr>
            </table>
            <br>
            <img src="rogue.png" alt="Rogue trainee" width="20%">
            <br>
            <button class="card-button" on:click={() => chooseComp(1)}>Choose</button>
        </div>
        <div class="card" style="right: 30%">
            <h2>Knight</h2>
            <br>
            <table style="text-align: center;width: 90%">
                <tr><td><b>ATK</b></td><td>{Math.round(player.atk * 0.8)}</td></tr>
                <tr><td><b>DEF</b></td><td>{Math.round(player.def * 1.2)}</td></tr>
                <tr><td><b>ACC</b></td><td>{player.acc}</td></tr>
                <tr><td><b>EVA</b></td><td>{Math.round(player.eva * 0.75)}</td></tr>
            </table>
            <br>
            <img src="knight.png" alt="Knight trainee" width="20%">
            <br>
            <button class="card-button" on:click={() => chooseComp(2)}>Choose</button>
        </div>
        {/if}
    </div>
    {/if}
    {#if helper == 31}
        <div class="end_msg">Thank you for playing!</div>
        <div class="end_btn">
        <a href="/">
            <Button class="primary">
                Back To Menu
            </Button>
        </a>
        </div>
    {/if}
{/if}
<!-- Dialogue -->
{#if locked_fl == 0}
<div class="chat-shader">

</div>
<Key on:inc={enterKey} {locked} />
<div class="page">
    <div class="chat">
        <div class="name" style="font-size: {text_size}pt; font-weight: bold; font-family: Papyrus, fantasy;">{characterSp}</div><br>
        <!-- svelte-ignore a11y-click-events-have-key-events -->
        <!-- svelte-ignore a11y-no-static-element-interactions -->
        <div class="chatbox" on:click={incItemIndex}>
            <p style="font-size: {text_size}pt; font-family: Papyrus, fantasy;">{activeText}</p>
        </div>
    </div>
</div>
{:else if locked_fl < 3}
<!-- Active Quest -->
{#if drawInv == false && chapter != 0}
<div class="quests">
    <div class="active-quest">
        <li style="list-style: inside; margin-bottom: 3%; font-family: Papyrus, fantasy;"><b>{main_quest.get_title}</b></li>
        <li class="liststyle">{main_quest.get_desc} {#if gathering}{progress}/6{/if}</li>
    </div>
    {#if main_quest.get_main_quest > 2 && showSide}
        <span style="background-color: black; color: white; width: 100%; text-align:center;display: block; font-family: Papyrus, fantasy;">Side Quests</span>
        {#each side_quests as sq}
        {#if sq.active}
        <div class="side-quest">
            <li style="list-style: inside; margin-bottom: 3%; font-family: Papyrus, fantasy;"><b>{sq.get_title}</b></li>
            <li class="liststyle">{sq.get_desc} {sq.get_progress}/{sq.get_amount}</li>
        </div>
        {/if}
        {/each}
    {/if}
</div>
{/if}
<!-- Status Bar -->
<div class="status-bar">
    <span style="font-weight: bold;font-family: Papyrus, fantasy;">HP: {player.HP}/{player.maxHP} | Level: {charLvl} | {exp}/{levelThreshold[charLvl]} 🚀</span>
    <hr style="border: 0.1px solid black">
</div>
{/if}
<!-- Map Board -->
<div style="z-index: 2">
    {#if chapter == 0}
    <table class="skipTo"><tr><td><img src="arrow.png" alt="Arrow to link" style="margin-top: 5px;" id="arrow-pic"></td><td><button class="skip-b" on:click={() => {itemIndex = 39; incItemIndex();}}>Skip to Chapter 1</button></td></tr></table>
    <Tutorial bind:drawInv bind:drawHMenu bind:showShader bind:helper on:heal={healChar} on:item={addItem} />
    {:else if chapter == 1}
    <Village bind:chapter bind:gathering bind:drawInv bind:showShader bind:drawHMenu bind:chiefMenu bind:pos_x bind:pos_y bind:helper bind:progress bind:loadAssets bind:showSide bind:chests on:heal={healChar} on:item={addItem} on:fight={fightMode} {animCount} {wear_t}/>
    {:else if chapter == 2}
    <Road bind:chapter bind:pos_x bind:chests bind:drawInv bind:showShader bind:drawHMenu bind:pos_y bind:helper bind:showSide on:heal={healChar} on:item={addItem} on:fight={fightMode} {fightEvent} {wear_t}/>
    {:else if chapter == 3}
    <Maze bind:chapter bind:drawInv bind:drawHMenu bind:showShader bind:pos_x bind:pos_y bind:helper bind:showSide bind:side_quests bind:chests on:invitem={addInventoryItem} on:heal={healChar} on:item={addItem} on:fight={fightMode} {wear_t}/>
    {:else if chapter == 4}
    <EndRoad bind:drawInv bind:drawHMenu bind:pos_x bind:pos_y bind:helper bind:showSide bind:chapter bind:chiefMenu on:heal={healChar} on:fight={fightMode} {wear_t}/>
    {:else if chapter == 5}
    <WorldBoss bind:drawInv bind:drawHMenu bind:pos_x bind:pos_y bind:showSide on:heal={healChar} on:fight={fightMode} {wear_t} bind:helper/>
    {/if}
    {#if drawInv}
    <div class="canvas">
        <Inventory bind:drawInv bind:main_items bind:items bind:chief bind:companion bind:main_atk bind:wear_t bind:player {companionType} {consumables} {chapter}/>
    </div>
    {/if}
</div>
<!-- Info panels -->
{#if infoFl}
<div class="info">
    {#if main_quest.get_main_quest == 3}
    <h1>Message To You</h1>
    <p>From this point on side quests are available.</p>
    <p>The first one is mandatory, otherwise it will be completely optional.</p>
    <p><b>Press Y to view.</b></p>
    {:else}
    <h1>Point of No Return</h1>
    <p>From this point on the game ending sequence will begin.</p>
    <p>You won't be able to return once you proceed.</p>
    <p>Please make sure you have the appropriate gear and items.</p>
    <p>If you wish, complete the remaining side quests as well.</p>
    <p><b>When you are ready, go through the passage once again.</b></p>
    {/if}
    <button on:click={closeMes}></button>
</div>
{/if}
<!-- Heal status update -->
{#if showHit == 3}<div class="result">Healed</div>{/if}
<!-- +Item -->
{#if recItem}
<div class="recieveItem">+ {itemRecieved} x1</div>
{/if}
<!-- Quick Heal -->
{#if drawHMenu}
    <Quick_heal on:heal={healChar} bind:drawHMenu {chief} {companion} {player} {party} {item} {MCname}/>
{/if}
<!-- Chief Menu -->
{#if chiefMenu}
    <ChiefMenu bind:helper bind:chiefMenu bind:usedCraft bind:consumables on:item={addItem} on:invitem={addInventoryItem}/>
{/if}
<!-- Fight-->
{#if locked_fl == 4 || locked_fl == 5}
    <Fight on:heal={healChar} on:invitem={addInventoryItem} bind:showShader bind:difficulty bind:AIdata bind:player bind:chief bind:companion bind:consumables bind:showHit bind:exp bind:helper bind:side_quests bind:fightEvent {wear_t} {MCname} {chapter} {main_atk} {charLvl} {companionType} {fightmode} {skills} {party} {item} {fightType} {multF}/>
{/if}
<!-- Quick Menu -->
{#if locked_fl == 6}
<div class="quick-menu">
    <Quick_menu {csrfToken} {items} {main_items} {main_quest} {itemIndex} {chapter} {item} {helper} {chests} {side_quests} {player} {choices} {exp} {AIdata} {usedCraft} {consumables} bind:difficulty bind:text_size bind:pos_x bind:pos_y/>
</div>
{/if}
<style>
    .choice-text {
        width: 40%;
        border-radius: 45px;
        height: 10%;
        background-color: azure;
        text-align: center;
        left: 30%;
        top: 2%;
        position: absolute;
    }
    .card {
        top: 20%;
        height: 60%;
        width: 15%;
        background-color: azure;
        position: absolute;
        text-align: center
    }
    .card-button {
        background-color: #0db136;
        color: azure;
        margin-top: 30%;
        border-radius: 35px;
        padding: 5%;
        font-size: 120%;
    }
    .page {
        left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        position: absolute;
        z-index: 3;
    }
    .quick-menu {
        z-index: 2;
        position: relative;
        left: 35%;
        right: 35%;
        width: 30%;
        text-align: center;
    }
    .status-bar {
        z-index: 1;
        position: relative;
        left: 35%;
        right: 35%;
        height: 5%;
        width: 30%;
        text-align: center;
        padding-top: 5px;
        background-color: rgb(190, 190, 190);
    }

    .recieveItem {
        z-index: 2;
        position: absolute;
        bottom: 1%;
        left: 1%;
        padding: 1%;
        font-size: 120%;
        font-weight: bold;
        background-color: rgb(190, 190, 190, 0.85);
        border-radius: 25px;
    }
    .result {
        z-index: 2;
        position: absolute;
        text-align: center;
        width: 20%;
        left: 40%;
        margin-top: 2%;
        font-size: 22px;
        color: rgb(41, 151, 41);
        background: rgb(25, 25, 25);
        border: 1px solid rgb(41, 151, 41);
    }

    /* Beszélő neve */
    .name {
        background-color: rgb(0, 204, 255);
        padding: 10px;
        bottom: 30%;
        padding-left: 50px;
        padding-right: 50px;
        position: absolute;
    }
    .chatbox {
        background-color: rgb(190, 190, 190);
        bottom: 0;
        width: 100%;
        position: absolute;
        height: 30%;
        opacity: 0.9;
    }
    .chat-shader {
        height: 100%;
        width: 100%;
        position: absolute;
        background-color: rgb(139, 139, 139);
        z-index: 1;
        opacity: 0.5;
    }
    .dark-shader {
        height: 100%;
        width: 100%;
        position: absolute;
        background-color: black;
        z-index: 5;
    }
    .dark-shader2 {
        height: 100%;
        width: 100%;
        position: absolute;
        background-color: black;
        z-index: 5;
        opacity: 0;
    }
    p {
        margin-left: 2%;
        margin-top: 2%;
    }
    .skipTo {
        z-index: 4;
        position: absolute;
        top: 1%;
        right: 1%;
        display: none;
        height: 52px;
    }
    .skip-b {
        height: 23px;
        width: 100%;
        background-color: burlywood;
        font-weight: bold;
    }

    .quests {
        z-index: 2;
        position: absolute;
        width: 26%; 
        font-size: 115%; 
    }
    .active-quest {
        padding-left: 5%; 
        padding-top: 5%;
        padding-bottom: 5%; 
        background-color: rgb(190, 190, 190, 0.85);
        border-radius: 0 0 55px 0;
    }
    .side-quest {
        padding-left: 2%;
        padding-top: 2%;
        padding-bottom: 2%;
        background-color: rgb(190, 190, 190, 0.85);  
    }
    .liststyle {
        list-style-type:circle;
        margin-left: 5%;
        font-family: Papyrus, fantasy;
    }
    .info {
        position: absolute;
        z-index: 2;
        width: 40%;
        top: 20%;
        height: 60%;
        left: 30%;
        background-color: rgb(16, 133, 16);
        border-radius: 35px;
        text-align: center;
        font-family: Papyrus, fantasy;
    }
    .info p {
        font-size: 120%;
    }
    .info button {
        border: none;
        background-image: url(../../../public/xbutton.png);
        width: 32px;
        height: 32px;
        background-color: transparent;
    }
    .end_msg {
        position: absolute;
        z-index: 6;
        font-size: 250%;
        font-weight: bold;
        text-align: center;
        width: 60%;
        top: 10%;
        left: 20%;
        color: azure;
        font-family: Papyrus, fantasy;
    }
    .end_btn {
        width: 40%;
        left: 30%;
        top: 50%;
        position: absolute;
        z-index: 6;
    }
</style>
<script>
    export let started;
    import Tutorial from '../lib/Tutorial.svelte';
    import Village from '../lib/Village.svelte';
    import Road from '../lib/Road.svelte';
    import Maze from '../lib/Maze.svelte';
    import EndRoad from '../lib/EndRoad.svelte';
    import WorldBoss from '../lib/WorldBoss.svelte';
    import Fight from '../lib/Fight.svelte';
    import Key from '../lib/Key.svelte';
    import Quick_menu from '../lib/Quick_menu.svelte';
    import Quick_heal from "../lib/Quick_heal.svelte";
    import Inventory from '../lib/Inventory.svelte';
    import ChiefMenu from "../lib/ChiefMenu.svelte";
    import Button from '../lib/Button.svelte';
    import { main_quest, newSideQuest } from '../lib/Quest';
    import { createChar } from '../lib/Character';
    import script from '../../../resources/content/Full_Script.txt?raw'
    import {onMount, setContext} from 'svelte'
    import { writable } from 'svelte/store';

    //Variables
    var itemIndex,chapter, helper, pos_y, items, main_items, side_quests, obj, main_q, choices, player, chief, companion, itemRecieved;
    var text_Array,activeText,locked_fl,inv_fl,selector,text_size,difficulty,fightType,userData, playerData;
    let chests = [];
    let AIdata = [];
    let pos_x = 0;
    let characterSp = "Creator";
    let animCount = "0";
    let progress = 0;
    let exp = 0;
    let charLvl = 1;
    let item = 1; // potion
    let fightEvent = 0;
    let showHit = 0;
    let party = 0;
    let companionType = 0;
    let wear_t = 0;
    let MCname = "Main Character";
    let main_atk = 0;
    let usedCraft = 0
    let consumables = [0,0,0]
    let csrfToken = '';

    //Flags
    let chiefMenu = false;
    let placing = true;
    let multF = false;
    let recItem = false;
    let gathering = false;
    let drawInv = false;
    let drawHMenu = false;
    let loadAssets = false;
    let showSide = false;
    let infoFl = false;
    let choice = false;
    let showShader = true;
    let fightShader = false;

    let locked = writable(7);
    // 0: Story mode
    // 1: Adventure mode
    // 2: Adventure mode w Heal menu
    // 3: Animation
    // 4: Fight transition
    // 5: Fight action
    let helper_inv_heal = writable(0);
    // 0: No action
    // 1: Don't display skip / Inventory screen
    // 2: Display skip / Close Inventory
    // 3: Heal menu
    let select_item = writable(0); // Selector
    let fightmode = writable(0);
    // 0: Story
    // 1: Random encounter

    setContext('flags', {
        locked, helper_inv_heal, select_item
    });
    locked.subscribe((value) => {locked_fl = value});
    helper_inv_heal.subscribe((value) => {inv_fl = value});
    select_item.subscribe((value) => {selector = value});

    let skills = [false,false,false,false];
    const levelThreshold = [250,500,1000,1500,2500,3500,5000,6500,8500,10500,14000,18500,25000]

    onMount(async () => {
        //Getting user's CSRF-token
        try {
            const response = await fetch('/csrf-token');
            const data = await response.json();
            csrfToken = data.csrfToken;
        } catch (error) {
            alert("Error: Failed to fetch CSRF token")
            window.location.href='/logout'
        }

        //Getting user data
        await fetch('/api/user_data')
        .then(response => {
            if (!response.ok) {
                alert("Error: Failed to fetch user data")
                window.location.href='/logout'
            }
            return response.json();
        })
        .then(data => {
            userData = data;
        })
        .catch(error => {
            alert("Error: Failed to fetch user data")
            if(confirm("Do you want to try again?")) {
                location.reload();
            } else {
                window.location.href='/logout'
            }
        });

        //Getting player data
        await fetch('/api/player_data')
        .then(response => {
            if (!response.ok) {
                alert("Error: Failed to fetch player data")
                window.location.href='/logout'
            }
            return response.json();
        })
        .then(data => {
            playerData = data;
        })
        .catch(error => {
            alert("Error: Failed to fetch player data")
            if(confirm("Do you want to try again?")) {
                location.reload();
            } else {
                window.location.href='/logout'
            }
        });
        difficulty = userData.difficulty
        text_size = userData.text_size
        //Continue saved game
        if(started == 1) {
            await ( async () => {
            player = createChar(0,playerData.HP,100,skills,[100,100,0,0]);
            let ind = 0
            exp = playerData.exp;
            while(exp > levelThreshold[ind]) {
                ind++;
            }
            charLvl = ind + 1;
            player.atk += 5 * ind;
            player.def += 5 * ind;
            player.acc = ind;
            player.eva = ind;
            itemIndex = playerData.storyProgress;
            chests = JSON.parse(playerData.chests).chests;
            consumables = JSON.parse(playerData.consumables).consumables;
            side_quests = (playerData.side_quest == null) ? [] : JSON.parse(playerData.side_quest).side_quests;
            usedCraft = playerData.usedCraft;
            for(let i = 0; i < side_quests.length; i++) {
                side_quests[i] = newSideQuest(side_quests[i][0],side_quests[i][1],side_quests[i][2])
            }
            helper = playerData.helper
            if(helper > 5) {
                unlock(0)
            }
            item = playerData.potions
            pos_x = playerData.position_x
            pos_y = playerData.position_y
            const m = JSON.parse(playerData.main_items);
            if(m != null) {
                main_items = m;
                for(let i = 0; i < 5; i++) {
                    if(main_items[i] != "i") {
                        player.atk += main_items[i][3];
                        player.def += main_items[i][4];
                        player.acc += main_items[i][5];
                        player.eva += main_items[i][6];
                        if(i != 4) {
                            wear_t++;
                        }
                    }
                }
                main_atk = (main_items[4] == 'i') ? 0 : main_items[4][3]
            }
            const it = JSON.parse(playerData.items)
            items = it
            obj = playerData.objective
            main_q = playerData.main_quest
            if(main_q == 1 && obj == 0) {
                loadAssets = true;
            } else if(main_q == 2 && obj == 0) {
                animCount = "3"
                gathering = true;
            }
            party = main_quest.setMission(main_q, obj)
            if(helper < 1) {
                characterSp = "Main Character";
            } else {
                MCname = "Skye"
            }
            }) ()
            chief = createChar(0,90,90,[],[Math.floor(player.atk*1.1),Math.floor(player.def*1.2),Math.floor(player.acc*0.8),Math.floor(player.eva*0.6)])
            choices = playerData.choices;
            if(choices != 0) {
                unlock(choices % 10);
                if(choices > 20) {
                    companionType = 2;
                    companion = createChar(0,80,80,[],[Math.floor(player.atk*0.8),Math.floor(player.def*1.2),player.acc,Math.floor(player.eva*0.75)])
                } else if (choices > 10) {
                    companionType = 1;
                    companion = createChar(0,80,80,[],[Math.floor(player.atk*0.7),player.def,Math.floor(player.acc*1.2),Math.floor(player.eva*1.5)])
                }
            }
            AIdata = JSON.parse(playerData.AIdata).AIdata;
            chapter = playerData.chapter
            locked.set(1)
        } else {
            //Starting new game
            itemIndex = 0
            helper = 0
            item = 1
            pos_x = 999
            pos_y = 999
            main_items = ["i","i","i","i",[null,'knife_01.png',5,0,0,0,0]]
            items = []
            chests = []
            side_quests = []
            player = createChar(0,75,100,skills,[100,100,0,0])
            chief = createChar(0,90,90,[],[player.atk*1.1,player.def*1.2,player.acc*0.8,player.eva*0.6])
            main_q = 0
            obj = 0;
            chapter = 0;
            choices = 0;
            locked.set(0)
        }
        initText(script)
    });

    //Unlocking skills
    function unlock(skillNum) {
        skills[skillNum] = true;
        if(skillNum > 0) {
            locked.set(0)
        }
    }

    //Screen fading animation
    async function animationScreenFade() {
        multF = false;
        (async () => {
            await sleep(800);
            incItemIndex();
            for(let i = 0.9; i > 0; i -= 0.1) {
                await sleep(100);
                document.getElementsByClassName("dark-shader")[0].style.opacity = i;
            }
            await sleep(200);
            locked.set(0)
        })();
    }

    //Story text switcher
    async function incItemIndex() {
        itemIndex++
        activeText = text_Array[itemIndex]
        switch (activeText[0]) {
            case "~":
                // Adventure mode/End of current story section
                locked.set(1);
                break;
            case "ß":
                // Start animation
                animCount = activeText[1]
                locked.set(3);
                if(animCount == "2") {
                    const a = await animationScreenFade()
                } else if (animCount == "3") {
                    gathering = true;
                }
                break;
            case "Đ":
                // Fight
                fightType = [parseInt(activeText[1]),parseInt(activeText[2]),parseInt(activeText[3])]
                fightMode(0);
                break;
            case "ł":
                // Skip
                if(chapter == 0) {
                    document.querySelector(".skipTo").style.display = "table";     
                } else {
                    // Adding new items to inventory
                    var newitem, newstats;
                    activeText = activeText.substr(1);
                    if(helper == 16) {
                        newitem = 'armors/common_leggings.png'
                        newstats = [3,0,2,0,2]
                        item++
                    } else if (helper == 10) {
                        newitem = 'armors/knights_chestplate.png'
                        newstats = [2,0,5,1,1]
                    } else {
                        newitem = 'sword_01.png'
                        newstats = [5,5,0,0,0]
                        if(items == null) {
                            items = [[null,newitem,newstats[0],newstats[1],newstats[2],newstats[3],newstats[4]]];
                            return;
                        }
                    }
                    for(let i = 0; i < items.length;i++) {
                        if(items[i].length == 0) {
                            items[i] = [null,newitem,newstats[0],newstats[1],newstats[2],newstats[3],newstats[4]]
                            incItemIndex();
                            return;
                        }
                    }
                    items.push([null,newitem,newstats[0],newstats[1],newstats[2],newstats[3],newstats[4]]);
                }
                break;
            case "@":
                // Next main quest
                exp += main_quest.quest.reward
                party = main_quest.next();
                obj = 0;
                break;
            case "ä":
                // Next quest objective
                main_quest.onumber++;
                break;
            case "¤":
                // Show info panel
                infoFl = true;
                locked.set(8)
                break;
            case "$":
                // Add new side quest
                side_quests.push(newSideQuest(Number(activeText[1]),0))
                break;
            case "#":
                // Unlock first skill
                unlock(0);
                break;
            case ".":
                // Hide arrow
                if(chapter == 0) {
                    document.querySelector("#arrow-pic").style.display = "none";
                } else if(chapter == 1) {
                    // Change MC name
                    MCname = "Skye";
                } else if(chapter == 2) {
                    // Start tournament
                    fightEvent = 0;
                    helper++;
                } else if(chapter == 5) {
                    // Ending helper
                    helper++;
                    locked.set(1)
                }
                break;
            case "*":
                // Show choices to make
                choice = true;
                helper++;
                locked.set(7)
                break;
            case ",":
                // Show screen animation
                locked.set(7)
                animationScreenFade();
                return;
            case "Ł":
                // Switch maps
                chapter++;
                if(chapter == 1) {
                    helper = 0;
                    player.HP = 100;
                    characterSp = "Main Character";
                    locked.set(0);
                } else {
                    fightEvent = 1;
                    pos_x = -384;
	                pos_y = -384;
                }
                break;
            default:
                // Decide speaker's name
                if(chapter > 0) {
                    if(activeText[0] == '-') {
                        activeText = activeText.substr(1);
                        characterSp = "Chief";
                    } else if(activeText[0] == '_'){
                        activeText = activeText.substr(1);
                        characterSp = "Guard";
                    } else {
                        characterSp = MCname 
                    }
                }
                return;
        }
        incItemIndex();
    }

    //Initialize text array
    function initText(f) {
        text_Array = f.split("\r\n")
        activeText = text_Array[itemIndex]
        if(activeText[0] == '-') {
            activeText = activeText.substr(1);
            characterSp = "Chief";
        } else if(activeText[0] == '_') {
            activeText = activeText.substr(1);
            characterSp = "Guard";
        } else if (chapter != 0) {
            characterSp = MCname;
        }
    }

    //Function to handle companion choice
    function chooseComp(c) {
        companionType = c;
        choices += c*10;
        if(companionType == 2) {
            companion = createChar(0,80,80,[],[Math.floor(player.atk*0.8),Math.floor(player.def*1.2),player.acc,Math.floor(player.eva*0.75)])
        } else {
            companion = createChar(0,80,80,[],[Math.floor(player.atk*0.7),player.def,Math.floor(player.acc*1.2),Math.floor(player.eva*1.5)])
        }
        helper++;
        locked.set(0);
    }

    //Close info panel
    function closeMes() {
        infoFl = false;
        locked.set(1)
    }

    //Handle Enter to advance text
    function enterKey() {
        locked.set(0);
        incItemIndex();
    }

    //Add potion
    async function addItem() {
        item++;
        itemRecieved = "Potion"
        displayItemRecieveMessage()
    }

    //Display new items name for 2 sec
    async function displayItemRecieveMessage() {
        recItem = true;
        await sleep(2000);
        recItem = false;
    }

    //Add new inventory item (from quest or craft)
    function addInventoryItem(event) {
        var newitem, newstats;
        if(event.detail.item == 1) {
            newitem = 'armors/common_sandals.png'
            newstats = [4,0,0,0,2]
            itemRecieved = "Common Sandals"
            displayItemRecieveMessage()
        } else if (event.detail.item == 2) {
            newitem = 'armors/merchants_hood.png'
            newstats = [1,0,2,0,1]
            itemRecieved = "Merchant's Hood"
            displayItemRecieveMessage()
        } else if (event.detail.item == 3) {
            newitem = 'armors/warriors_boots.png'
            newstats = [4,3,5,4,3]
            itemRecieved = "Warriors Boots"
            displayItemRecieveMessage()
        } else if (event.detail.item == 4) {
            newitem = 'armors/warriors_hood.png'
            newstats = [1,2,4,2,3]
            itemRecieved = "Warriors Hood"
            displayItemRecieveMessage()
        } else if (event.detail.item == 5) {
            newitem = 'armors/warriors_leggings.png'
            newstats = [3,4,7,3,5]
            itemRecieved = "Warriors Leggings"
            displayItemRecieveMessage()
        } else {
            newitem = 'armors/warriors_chestplate.png'
            newstats = [2,6,9,5,1]
            itemRecieved = "Warriors Chestplate"
            displayItemRecieveMessage()
        }
        for(let i = 0; i < items.length;i++) {
            if(items[i].length == 0) {
                items[i] = [null,newitem,newstats[0],newstats[1],newstats[2],newstats[3],newstats[4]]
                return;
            }
        }
        items.push([null,newitem,newstats[0],newstats[1],newstats[2],newstats[3],newstats[4]]);
    }

    //Set if player can or cannot escape, the enemies to be fought and start fight
    async function fightMode(m) {
        if(m == 0) {
            showShader = true;
            fightmode.set(m);
        } else {
            fightShader = true;
            for(let i = 0.125; i < 1.125; i += 0.125) {
                await sleep(50);
                document.getElementsByClassName("dark-shader2")[0].style.opacity = i;
            }
            showShader = true;
            document.getElementsByClassName("dark-shader2")[0].style.opacity = 0;
            fightShader = false;
            fightmode.set(m.detail.fm);
            if(m.detail.fm == 0) {
                switch(helper) {
                    case 2:
                        multF = true;
                        fightType = [0,1,0];
                        break;
                    case 21:
                        fightType = [1,1,1];
                        helper++;
                        break;
                    case 25:
                        fightType = [1,2,1];
                        helper++;
                        break;
                    case 26:
                        fightType = [1,3,1];
                        helper++;
                        break;
                    case 27:
                        fightType = [2,3,1];
                        helper++;
                        break;
                    case 28:
                        fightType = [2,3,2];
                        helper++;
                        break;
                    case 29:
                        fightType = [1,4,1];
                        helper++;
                        break;
                }                   
            } else {
                if(helper > 22) {
                    const n = Math.random()*100
                    const m = Math.random()*100
                    const l = Math.random()*100
                    fightType = [(n < 20) ? (n < 5 ? 3 : 2) : 1, (m < 20) ? (m < 5 ? 3 : 2) : 1, (l < 20) ? (l < 5 ? 3 : 2) : 1];
                }
                else if(helper > 10) {
                    fightType = [ (Math.random()*100 < 15) ? 2:1, (Math.random()*100 < 15) ? 2:1, 0];
                } else {
                    fightType = [0,1,0];
                }
            }
        }
        placing = false;
        locked.set(4);
    }

    //Healing
    function healChar(event) {
        if(event.detail.bench) {
            chief.HP = chief.maxHP;
            player.HP = player.maxHP;
            if(companionType > 0) {
                companion.HP = companion.maxHP;
            }
            player = player;
        } else {
            if(event.detail.char == 0) {
                chief.HP += Math.min(chief.maxHP-chief.HP, Math.floor(chief.maxHP/4));
                chief = chief;
            } else if(event.detail.char == 1) {
                player.HP += Math.min(player.maxHP-player.HP, Math.floor(player.maxHP/4));
                player = player;
            } else if(event.detail.char == 2) {
                companion.HP += Math.min(companion.maxHP-companion.HP, Math.floor(companion.maxHP/4));
                companion = companion;
            }
            if(item > 0) {
                item--;
                if (locked_fl == 5) {
                    helper_inv_heal.set(4);
                }
            }
        }
        showHit = 3;
        showHitRes();
    }

    //Display hit message
    async function showHitRes() {
        await sleep(2500);
        showHit = 0;
    }

    //Calculate new level when player gets experience points
    function calcLevel() {
        for(let i = charLvl - 1; i < levelThreshold.length; i++) {
            if(exp > levelThreshold[i]) {
                charLvl++
                player.atk += 5;
                player.def += 5;
                player.acc++;
                player.eva++;
                chief.atk = Math.floor(player.atk*1.1)
                chief.def = Math.floor(player.def*1.2)
                chief.acc = Math.floor(player.acc*0.8)
                chief.eva = Math.floor(player.eva*0.6)
                if(companionType == 1) {
                    companion.atk = Math.floor(player.atk*0.7)
                    companion.def = player.def
                    companion.acc = Math.floor(player.acc*1.2)
                    companion.eva = Math.floor(player.eva*1.5)
                } else if (companionType == 2) {
                    companion.atk = Math.floor(player.atk*0.8)
                    companion.def = Math.floor(player.def*1.2)
                    companion.acc = player.acc
                    companion.eva = Math.floor(player.eva*0.75)
                }
            } else {return;}
        }
    }

    //Timer
    const sleep = ms => new Promise(r => setTimeout(r, ms));

    $: if (inv_fl == 1 && chapter == 0) {document.querySelector(".skipTo").style.display = "none"; } //Hides skip
    $: if (inv_fl == 2 && chapter == 0) {document.querySelector(".skipTo").style.display = "table"; } //Displays skip
    $: if (exp > 0) {calcLevel()} //Calls calcLevel when player gets experience points
</script>
