<div class="page">
    <!-- Logout -->
    <a class="out" href="/logout"> </a>
    <!-- Menu -->
    {#if visible}
    <div class="buttons-container">
        <div style="margin-bottom: 5%;text-align: center"><img src="Logo.png" alt="Game Logo"></div>
        <div style="width: 28%;position: absolute; left: 36%;">
            {#if hasSavedGame}
            <a href="/Game">
            <Button class="primary">
                Continue
            </Button>
            </a><br>
            {/if}
            {#if Button2visible}
            <Button class="primary" on:click={alertUser}>
                Start New Journey
            </Button>
            <br>
            {/if}
            {#if Button3visible}
            <Button class="primary" on:click={toggleVisible}>
                Options
            </Button>
            {/if} 
        </div>
    </div>
    {:else}
    <!-- Options -->
    <div class="options-container">
        <div>
        <Button class="back" on:click={toggleVisible}>
             
        </Button><br>
        <Options bind:difficulty bind:text_size {csrfToken} />
        </div>
    </div>
    {/if}
    <footer class="foot">Made by Gábor Markó - ELTE IK 2024</footer>
</div>

<style>
    .page {
        height: 100%;
        width: 100%;
        position: absolute;
        overflow: hidden;
        background-image: url("../../../public/menu_bg.png");
        background-position: 50% 30%;
        background-size: 1920px auto;
    }
    .options-container {
        margin: 0;
        position: absolute;
        top: 48%;
        left: 50%;
        -ms-transform: translate(-50%, -50%);
        transform: translate(-50%, -50%);
        padding: 30px;
        padding-bottom: 60px;
        background-color: white;
        border: 2px solid black;
        border-radius: 30px 30px;
        box-shadow: 5px 5px 5px 5px rgb(44, 44, 44);
    }
    .buttons-container {
        margin: 0;
        position: absolute;
        height: 45%;
        top: 25%;
        width: 100%;
    }

    .foot {
        position: fixed;
        left: 0px;
        bottom: 0px;
        opacity: 0.3;
        color: aliceblue;
    }

    a.out {
        background-image: url(../../../public/logout.png);
        text-decoration: none;
        float: right;
        height: 32px;
        width: 32px;
        margin-top: 10px;
        margin-right: 10px;
        }
</style>

<script>
    import { onMount } from "svelte"
    import Button from "../lib/Button.svelte"
    import Options from "../lib/Options.svelte"

    var difficulty,text_size,userData,playerData;
    
    let csrfToken = '';

    let visible = true
    let hasSavedGame = false
    let Button2visible = false
    let Button3visible = false

    function toggleVisible() {
        visible = !visible
    }

    const sleep = ms => new Promise(r => setTimeout(r, ms));

    onMount(async () => {
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

        if(playerData.position_x != 999) {
            hasSavedGame = true;
            await sleep(500);
            Button2visible = true;
            await sleep(500);
            Button3visible = true;
        } else {
            Button2visible = true;
            await sleep(500);
            Button3visible = true;
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
        difficulty = userData.difficulty
        text_size = userData.text_size

        //Getting user's CSRF-token
        try {
            const response = await fetch('/csrf-token');
            const data = await response.json();
            csrfToken = data.csrfToken;
        } catch (error) {
            alert("Error: Failed to fetch CSRF token")
            window.location.href='/logout'
        }
    });

    function alertUser() {
        if(hasSavedGame) {
            if(confirm("Saving in a new game will overwrite the previous save. Do you wish to continue?")) {
                window.location.href = "/GameNew"
            }
        } else {
            window.location.href = "/GameNew"
        }
    }
</script>