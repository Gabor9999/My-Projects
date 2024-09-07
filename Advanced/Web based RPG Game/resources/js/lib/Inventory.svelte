<script>
    import { onMount } from 'svelte';
    import { getContext } from 'svelte';
    import { newItem } from '../lib/Item';

    export let canvas, ctx;
    export let drawInv,main_items,items,main_atk,wear_t, player, chief, companion, companionType, consumables, chapter;

    let width = 1920;
	let height = 1088;

    var main_inv, inv, selected_x, selected_y, locked_fl;

    let icons = [newItem('helmet.png',1,true),newItem('chestplate.png',2,true),newItem('pants.png',3,true),newItem('leg-armor.png',4,true), newItem('gladius.png',5,true)];

    let selector_offset_x = 0;
    let selector_offset_y = 0;

    let selected_main = false;
    let selected_non_main = false;
    let opened = false;

    var { locked, helper_inv_heal } = getContext('flags');
    locked.subscribe((value) => {locked_fl = value});

    //Set the items in the inventory
    async function mountNec() {
        main_inv = main_items;
        inv = items;
        for(let i = 0; i < 7; i++) {
            for(let j = 0; j < 9; j++) {
                if(inv.length > i*9 + j) {
                    if(inv[i*9 + j].length != 0) {
                        inv[i*9 + j] = newItem(inv[i*9 + j][1],inv[i*9 + j][2],false,inv[i*9 + j][3],inv[i*9 + j][4],inv[i*9 + j][5],inv[i*9 + j][6]);
                        inv[i*9 + j].img.onload = () => { ctx.drawImage(inv[i*9 + j].img,0,0,
                        inv[i*9 + j].img.width,inv[i*9 + j].img.height, 
                        900 + j * 95, 200 + i * 95,
                        85,85)}
                    }
                } else {
                    break;
                }
            }
        }
        for(let i = 0; i < 5; i++) {
            if(main_inv[i] == 'i' || main_inv[i].length == 0) {
                main_inv[i] = icons[i];
            } else if(main_items != null) {
                main_inv[i] = newItem(main_inv[i][1],main_inv[i][2],false,main_inv[i][3],main_inv[i][4],main_inv[i][5],main_inv[i][6]);
            }
            if(i != 4) {
                main_inv[i].img.onload = () => { 
                    ctx.drawImage(main_inv[i].img,
                    0,0,
                    main_inv[i].img.width,main_inv[i].img.height, 
                    100, 277.5 + i * 125,
                    85,85)}
            }
        }
        main_inv[4].img.onload = () => {
            ctx.drawImage(main_inv[4].img,
            0,0,
            main_inv[4].img.width,main_inv[4].img.height, 
            725, 485,
            85,85)
        }
    }

    //Set the inventory up and draw the neccesary items and selector
    onMount(async() => {
        ctx = canvas.getContext('2d');
        const a = await mountNec();
        drawSelector(0,0)
    })

    function drawInventory() {
        if(locked_fl != 0) {
            opened = true;
        }
        ctx.beginPath()
        ctx.fillStyle = "white"
        ctx.fillRect(50, 100, canvas.width - 100, canvas.height - 150)
        for(let i = 0; i < 4; i++) {
            ctx.fillStyle = "grey"
            ctx.fillRect(100, 277.5 + i * 125, 85, 85)
            ctx.lineWidth = 3
            ctx.rect(99,276.5 + i * 125,86,86)
            ctx.stroke()
            ctx.drawImage(main_inv[i].img,0,0,
            main_inv[i].img.width,main_inv[i].img.height, 
            100, 277.5 + i * 125,
            85,85)
        }
        for(let i = 0; i < 7; i++) {
            for(let j = 0; j < 9; j++) {
                ctx.fillRect(900 + j * 95, 200 + i * 95, 85, 85)
                if(inv.length > i*9 + j) {
                    if(inv[i*9 + j].length != 0) {
                        ctx.drawImage(inv[i*9 + j].img,0,0,
                        inv[i*9 + j].img.width,inv[i*9 + j].img.height, 
                        900 + j * 95, 200 + i * 95,
                        85,85)
                    }
                }
        }
        }
        ctx.fillStyle = "grey"
        ctx.fillRect(725, 485, 85, 85)
        ctx.lineWidth = 3
        ctx.rect(724,484,86,86)
        ctx.stroke()
        ctx.drawImage(main_inv[4].img,0,0,
        main_inv[4].img.width,main_inv[4].img.height, 
            725, 485,
            85,85)
        ctx.fillStyle = "black"
        ctx.rect(49, 99, canvas.width - 99, canvas.height - 149)
        ctx.lineWidth = 2
        ctx.stroke()
        ctx.lineWidth = 1
        ctx.closePath();
        if(selected_main || selected_non_main) {
            drawSelected();
        }
    }

    function drawSelected() {
        ctx.beginPath();
        ctx.lineWidth = 4;
        ctx.strokeStyle = 'blue';
        ctx.rect(899 + selected_x, 199 + selected_y, 86, 86);
        ctx.stroke()
        ctx.strokeStyle = 'black';
        ctx.closePath();
    }

    function drawSelector(offset_x, offset_y) {
        drawInventory();
        if(offset_x != 0) {
            selector_offset_x += offset_x;
            if(selector_offset_x < 0) {
                if(!selected_main) {
                    selector_offset_x = -175;
                    selector_offset_y = 285;
                } else {
                    selector_offset_x = 760;
                }
            }
        } else {
            if(selector_offset_x != -175) {
                selector_offset_y += offset_y;
            }
            if(selector_offset_y < 0) {
                if(!selected_main) {
                    selector_offset_x = -800;
                    selector_offset_y = 452.5;
                } else {
                    selector_offset_y = 570;
                }
            } else if (selector_offset_y == 577.5) {
                selector_offset_x = -800;
                selector_offset_y = 77.5;
            }
        }
        ctx.beginPath();
        ctx.lineWidth = 4;
        ctx.strokeStyle = 'lightgreen';
        ctx.rect(899 + selector_offset_x, 199 + selector_offset_y, 86, 86);
        ctx.stroke()
        ctx.strokeStyle = 'black';
        ctx.closePath();
        if(selector_offset_x == -175 || selector_offset_x == -800) {
            let ind = (selector_offset_x == -175) ? 4 : (selector_offset_y-77.5)/125
            ctx.font = "25px serif";
            if(selected_non_main) {
                let i = 9*selected_y/95;
                let m = (selected_x/95);
                if(inv[i+m] != undefined) {
                    if(main_inv[ind].itemtype == inv[i+m].itemtype) {
                        ctx.fillText(((inv[i+m].atk - main_inv[ind].atk) > 0 ? "+" : "") + (inv[i+m].atk - main_inv[ind].atk), 680, 475)
                        ctx.fillText(((inv[i+m].def - main_inv[ind].def) > 0 ? "+" : "") + (inv[i+m].def - main_inv[ind].def), 680, 511)
                        ctx.fillText(((inv[i+m].acc - main_inv[ind].acc) > 0 ? "+" : "") + (inv[i+m].acc - main_inv[ind].acc), 680, 547)
                        ctx.fillText(((inv[i+m].eva - main_inv[ind].eva) > 0 ? "+" : "") + (inv[i+m].eva - main_inv[ind].eva), 680, 583)
                    }
                }
            } else if((ind == 4 && main_inv[4] != icons[4]) || (ind != 4 && main_inv[(selector_offset_y-77.5)/125] != icons[(selector_offset_y-77.5)/125])) {
                ctx.fillText("+" + main_inv[ind].atk, 680, 475)
                ctx.fillText("+" + main_inv[ind].def, 680, 511)
                ctx.fillText("+" + main_inv[ind].acc, 680, 547)
                ctx.fillText("+" + main_inv[ind].eva, 680, 583)
            }
        }
    }

    //Modify stats according to change of items
    function statChange(natk,ndef,nacc,neva) {
        player.atk += natk;
        player.def += ndef;
        player.acc += nacc;
        player.eva += neva;
    }

    //Handle key press
    function onKeyDown(e) {
        if(opened) {
            switch(e.keyCode) {
                case 87: //W
                    if(selector_offset_x == -800) {
                        drawSelector(0, -125)
                    } else {
                        drawSelector(0, -95)
                    }	
                    break;
                case 65: //A
                    if(selector_offset_x == -175) {
                        selector_offset_x = -800;
                        drawSelector(0, -207.5)
                    } else {
                        drawSelector(-95, 0)
                    }	
                    break;
                case 83: //S
                    if(selector_offset_x == -800) {
                        drawSelector(0, 125)
                    } else {
                        if(selector_offset_y != 570) {
                            drawSelector(0, 95)
                        }
                    }	
                    break;
                case 68: //D
                    if(selector_offset_x == -175 && !selected_non_main) {
                        selector_offset_x = 0;
                        drawSelector(0, 0)
                    } else {
                        if(selector_offset_x != 760) {
                            drawSelector(95, 0)
                        }
                    }
                    break;
                case 27: //Esc
                    closeInv()
                    break;
                case 13: //Enter
                    if(!(selected_main || selected_non_main)) {
                        if(selector_offset_x == -175 || selector_offset_x == -800) {
                            selected_main = true;
                            selected_x = selector_offset_x;
                            selected_y = selector_offset_y;
                            selector_offset_x = 0;
                            selector_offset_y = 0;
  
                        } else {
                            selected_non_main = true;
                            selected_x = selector_offset_x;
                            selected_y = selector_offset_y;
                            selector_offset_x = -800;
                            selector_offset_y = 77.5;
                        }
                    } else {
                        var n;
                        var m;
                        var i;
                        if(selected_main) {
                            if(selected_x == -175) {
                                n = 4;
                            } else {
                                n = (selected_y - 77.5)/125
                            }
                            if(main_inv[n] != icons[n]) {
                                i = 9*selector_offset_y/95;
                                m = (selector_offset_x/95)+1;
                                while(inv.length < i+m) {
                                    inv.push([]);
                                }
                                if(inv[i+m-1].length == 0) {
                                    statChange(-main_inv[n].atk,-main_inv[n].def,-main_inv[n].acc,-main_inv[n].eva)
                                    inv[i+m-1] = main_inv[n];
                                    main_inv[n] = icons[n];
                                    
                                } else if (inv[i+m-1].itemtype == main_inv[n].itemtype) {
                                    statChange(inv[i+m-1].atk-main_inv[n].atk,inv[i+m-1].def-main_inv[n].def,inv[i+m-1].acc-main_inv[n].acc,inv[i+m-1].eva-main_inv[n].eva)
                                    let temp = inv[i+m-1];
                                    inv[i+m-1] = main_inv[n];
                                    main_inv[n] = temp;
                                }
                            }
                            selected_main = false;
                        } else {
                            if(selector_offset_x == -175) {
                                n = 4;
                            } else {
                                n = (selector_offset_y - 77.5)/125
                            }
                            if(main_inv[n] != icons[n]) {
                                i = 9*selected_y/95;
                                m = (selected_x/95)+1;
                                while(inv.length < i+m) {
                                    inv.push([]);
                                }
                                let temp = inv[i+m-1];
                                if(temp.length == 0) {
                                    statChange(-main_inv[n].atk,-main_inv[n].def,-main_inv[n].acc,-main_inv[n].eva)
                                    inv[i+m-1] = main_inv[n];
                                    main_inv[n] = icons[n];
                                } else if (temp.itemtype == main_inv[n].itemtype){
                                    statChange(inv[i+m-1].atk-main_inv[n].atk,inv[i+m-1].def-main_inv[n].def,inv[i+m-1].acc-main_inv[n].acc,inv[i+m-1].eva-main_inv[n].eva)
                                    inv[i+m-1] = main_inv[n];
                                    main_inv[n] = temp;
                                }
                            } else {
                                i = 9*selected_y/95;
                                m = (selected_x/95)+1;
                                if(inv.length < i+m) {}
                                else if(inv[i+m-1] != []) {
                                    if(inv[i+m-1].itemtype == main_inv[n].itemtype) {
                                        statChange(inv[i+m-1].atk,inv[i+m-1].def,inv[i+m-1].acc,inv[i+m-1].eva)
                                        main_inv[n] = inv[i+m-1];
                                        inv[i+m-1] = [];
                                    }
                                }
                            }
                            selected_non_main = false;
                        }
                        selector_offset_x = 0;
                        selector_offset_y = 0;
                    }
                    drawSelector(0,0);
					break;
                default:
                    return;
            }
        }
    }

    function closeInv() {
        ctx.clearRect(0,0,canvas.width,canvas.height);
        let c = 0;
        for(let i = 0; i < 5;i++) {
            if(main_inv[i] == icons[i]) {
                main_inv[i] = 'i';
            } else {
                main_inv[i] = main_inv[i].get_data
                if(i != 4) {
                    c++;
                }
            }
        }
        wear_t = c;
        for(let i = 0; i < inv.length;i++) { 
            if(inv[i].length != 0) {
                inv[i] = inv[i].get_data;
            }
        }
        chief.atk = player.atk*1.2
        chief.def = player.def*1.2
        chief.acc = player.acc*0.8
        chief.eva = player.eva*0.6
        if(companionType == 1) {
            companion.atk = player.atk*0.7
            companion.def = player.def
            companion.acc = player.acc*1.2
            companion.eva = player.eva*1.5
        } else if (companionType == 2) {
            companion.atk = player.atk*0.8
            companion.def = player.def*1.2
            companion.acc = player.acc
            companion.eva = player.eva*0.75
        }
        main_items = main_inv;
        main_atk = (main_items[4] == 'i') ? 0 : main_items[4][3]
        items = inv;
        opened = false;
        drawInv = false;
        if(chapter == 0) {
            helper_inv_heal.set(2)
            locked.set(0);
        }
    }

    const sleep = ms => new Promise(r => setTimeout(r, ms));
    $: if(locked_fl != 0) {(async() => {await sleep(500); opened = true;})()}
</script>
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>
<canvas bind:this={canvas} {width} {height}>
	<slot />
</canvas>
<div class="close-inv">
    <button on:click={ closeInv }></button>
</div>
<div class="inv-sign">Inventory</div>
<table class="inv-stat">
    <tr>
        <th>Attack</th>
        <th>{player.atk}</th>
    </tr>
    <tr>
        <th>Defence</th>
        <th>{player.def}</th>
    </tr>
    <tr>
        <th>Accuracy</th>
        <th>{player.acc}</th>
    </tr>
    <tr>
        <th>Evasion</th>
        <th>{player.eva}</th>
    </tr>
</table>
<table class="consumables">
    <tr>
        <th>Monster essense <span style="text-align: right; margin-left: 100px">{consumables[0]}</span></th>
        <th>Leather <span style="text-align: right; margin-left: 100px">{consumables[1]}</span></th>
        <th>Strings <span style="text-align: right; margin-left: 100px">{consumables[2]}</span></th>
    </tr>
</table>
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
    .close-inv {
        z-index: 2;
        position: absolute;
        top: 5%;
        left: 95%;
    }
    .close-inv button {
        border: none;
        background-image: url(../../../public/xbutton.png);
        width: 32px;
        height: 32px;
        background-color: transparent;
    }
    .inv-stat {
        z-index: 2;
        position: absolute;
        top: 41%;
        left: 11%;
        font-size: 150%;
        width: 26%;
    }
    .inv-sign {
        z-index: 2;
        top: 11%;
        font-size: 180%;
        font-weight: bold;
        position: absolute;
        text-align: center;
        width: 100%;
    }
    .consumables {
        z-index: 2;
        position: absolute;
        top: 84%;
        left: 15%;
        width: 70%;
        font-size: 130%;
        font-weight: bold;
    }

    .consumables th{
        display: inline-block;
        border: 2px solid black;
        border-radius: 35px;
        padding: 10px;
        margin-right: 8%;
    }
</style>