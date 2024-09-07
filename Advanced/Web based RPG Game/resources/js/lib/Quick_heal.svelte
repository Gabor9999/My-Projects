<script>
    import { onMount, createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let canvas, ctx, drawHMenu, player, chief, companion, party, item, MCname;

    let width = 1920;
	let height = 1088;
    let opened = false;
    let selectH = 1;

    onMount(() => {
        ctx = canvas.getContext('2d');
        drawH()
    })

    function drawH() {
        opened = true;
		ctx.fillStyle = "white"
		ctx.fillRect(0,76/100 * canvas.height, 22/100*canvas.width, canvas.height)
		ctx.beginPath()
		ctx.moveTo(0, 76/100 * canvas.height - 1);
		ctx.lineTo(22/100*canvas.width + 2, 76/100 * canvas.height - 1);
		ctx.lineTo(22/100*canvas.width + 2, canvas.height)
		ctx.moveTo(0, 84/100 * canvas.height)
		ctx.lineTo(22/100*canvas.width, 84/100 * canvas.height)
		ctx.stroke()
		ctx.moveTo(0, 92/100 * canvas.height)
		ctx.lineTo(22/100*canvas.width, 92/100 * canvas.height)
		ctx.stroke()
		ctx.fillStyle = "#1FB31F";
		ctx.fillRect(canvas.width/50, 7*canvas.height/8, player.HP/player.maxHP * 4*canvas.width/25, 8);
        ctx.rect(canvas.width/50 - 1, 7*canvas.height/8 - 1, 4*canvas.width/25 + 1, 10);
		ctx.stroke();
        if(party == 1 || party == 3) {
            ctx.fillStyle = "#1FB31F";
            ctx.fillRect(canvas.width/50, 159*canvas.height/200, chief.HP/chief.maxHP * 4*canvas.width/25, 8);
            ctx.rect(canvas.width/50 - 1, 159*canvas.height/200 - 1, 4*canvas.width/25 + 1, 10);
            ctx.stroke();
        }
        if(party > 1) {
            ctx.fillStyle = "#1FB31F";
            ctx.fillRect(canvas.width/50, 191*canvas.height/200, companion.HP/companion.maxHP * 4*canvas.width/25, 8);
            ctx.lineWidth = 2
            ctx.rect(canvas.width/50 - 1, 191*canvas.height/200 - 1, 4*canvas.width/25 + 1, 10);
            ctx.stroke();
            ctx.lineWidth = 1
        }
    }

    function onKeyDown(e) {
        if(opened) {
            switch(e.keyCode) {
                case 38: // ^
					if((selectH == 1 && (party == 1 || party == 3)) || (selectH == 2 && party > 1)) {
						selectH--;
					}
					break;
				case 40: // ˇ
					if((selectH == 0 && (party == 1 || party == 3)) || (selectH == 1 && party > 1)) {
						selectH++;
					}
					break;
                case 27: //Esc
                    drawHMenu = false;
                    ctx.clearRect(0,0,canvas.width,canvas.height); 
                    opened = false;
                    break;
                case 13: //Enter
					if(drawHMenu) {
						drawHMenu = false
						if(item > 0) {
							dispatch('heal', {bench: false, char: selectH});
						}
					}
					break;
                default:
                    return;
            }
        }
    }
</script>

<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<canvas bind:this={canvas} {width} {height}>
	<slot />
</canvas>
<div class="potion"><div><img src="potion.png" alt="Potion" width="32px" height="32px"></div><div class="amount">{item}</div></div>
<table class="HP">
    <tr><th>{#if selectH == 0}+{Math.floor(chief.maxHP/4)}{:else}<br/>{/if}</th></tr>
    <tr><th>{#if selectH == 1}+{player.maxHP/4}{:else}<br/>{/if}</th></tr>
    <tr><th>{#if selectH == 2}+{companion.maxHP/4}{:else}<br/>{/if}</th></tr>
</table>
{#if party == 1 || party == 3}
<table class="character-names" style="top: 76.3%">
    <tr><td>Chief</td></tr>
    <tr><td><br></td></tr>
    <tr><td>{chief.HP}/{chief.maxHP}</td></tr>
</table>
{/if}
<table class="character-names" style="top: 84.3%">
    <tr><td>{MCname}</td></tr>
    <tr><td><br></td></tr>
    <tr><td>{player.HP}/{player.maxHP}</td></tr>
</table>
{#if party > 1}
<table class="character-names" style="top: 92.3%">
    <tr><td>Trainee</td></tr>
    <tr><td><br></td></tr>
    <tr><td>{companion.HP}/{companion.maxHP}</td></tr>
</table>
{/if}

<style>
    canvas {
		width: 100%;
		height: 100%;
		left: 0;
        right: 0;
        bottom: 0;
        top: 0;
        position: absolute;
	}
    /* Hősök nevei */
    .character-names {
        z-index: 2;
        position: absolute;
        left: 0;
        width: 20%;
        text-align: center;
    }
    /* Hozzáadott HP a heal ablakban */
    .HP {
        z-index: 2;
        position: absolute;
        top: 76%;
        width: 22%;
        height: 24%;
        text-align: right; color: rgb(31, 179, 31)
    }
    .potion {
        z-index: 2;
        position: absolute;
        top: 70%;
        width: 60px;
        background-color: white;
        border: 1px solid black;
        border-radius: 25px 25px;
        padding-left: 1%;
        padding: 5px;
        font-size: 20px;
        display: inline-flex;
    }
    .amount{
        text-align: end;
        margin-top: 6px;
        margin-left: 12%
    }
</style>