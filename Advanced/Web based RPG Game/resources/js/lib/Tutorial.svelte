<script>
	import { getContext, onMount, createEventDispatcher } from 'svelte';
	import { collisions } from '../../../resources/js/collisions/collisions_tut'

	const dispatch = createEventDispatcher();

    var bg,foreground,chara,hair,wear,chest_img,orc_idle;

	const collisionsPos = []
	var collisionsMap;
	for (let i = 0; i < 840; i += 60) {
		collisionsMap = collisions.slice(i,60 + i);
		collisionsMap.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/60)*64]
				collisionsPos.push(coor);
			}
		})
	}

	let width = 1920;
	let height = 1088;

	export let canvas, ctx, drawInv, drawHMenu, helper,showShader;
	
	let dir = 0;
	let animationStep = 0;
	let place_w = 0;
	let place_h = 0;
	let stepCount = 0;
	let chestOpened = 32;

	var { locked, helper_inv_heal, select_item} = getContext('flags');
	var locked_fl, inv_fl, selector, middleOfScreen, contains, halfHeight, halfWidth;
	locked.subscribe((value) => {locked_fl = value});
	helper_inv_heal.subscribe((value) => {inv_fl = value});
	select_item.subscribe((value) => {selector = value});

	let invisibleWall = true;
	let closeToChest = false;

	onMount(() => {
		ctx = canvas.getContext('2d');
		bg = new Image();
		foreground = new Image();
		chara = new Image();
		hair = new Image();
		wear = new Image();
		chest_img = new Image();
		orc_idle = new Image();
        bg.onload = () => {
            ctx.drawImage(bg, 0, 0);
			halfWidth = canvas.width/2;
			halfHeight = canvas.height/2;
			middleOfScreen = [(halfWidth - 96), (halfHeight)];
			draw();
			showShader = false;
        };
		bg.src = 'map1.png';
		foreground.src = 'map1_foreground.png'
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png';
		wear.src = 'player/underwear.png';
		chest_img.src = 'RGW_Chests.png';
		orc_idle.src = 'Orc_Idle-Sheet.png';
	});

	function drawCharacter(x,y) {
		ctx.drawImage(chara,
			animationStep * 64, dir * 64,
			64,64, 
			x, y,
			128,128)
		ctx.drawImage(hair,
			animationStep * 64,dir * 64,64, 64,
			x + 8, y + 8, 112,112
		)
		ctx.drawImage(wear,
			animationStep * 64, dir * 64,64,64, 
			x, y,
			128,128)
	}

	function drawOrc(x,y) {
		ctx.drawImage(orc_idle,
			0, 0,
			orc_idle.width/4, orc_idle.height,
			x, y,
			64,64
		)
	}

	function draw() {
		ctx.clearRect(0, 0, canvas.width, canvas.height)
		ctx.drawImage(bg, place_w, place_h);
		ctx.drawImage(chest_img,
			chestOpened, 0,
			chest_img.width/12, chest_img.height/8,
			2320+place_w, 560+place_h,
			chest_img.width/10, chest_img.height/8
		);
		drawCharacter(middleOfScreen[0], middleOfScreen[1]);
		ctx.drawImage(foreground,place_w,place_h);
	};

	const sleep = ms => new Promise(r => setTimeout(r, ms));
	const drawOrcAnim = async () => {
		drawOrc(2300+place_w, 640+place_h);
		await sleep(500);
		draw();
		drawOrc(2300+place_w, 624+place_h);
		await sleep(500);
		draw();
		drawOrc(2300+place_w, 608+place_h);
		await sleep(500);
		locked.set(0);	
	};

 	$: if(locked_fl == 3) {
		drawOrcAnim()
	}

	function basicMove(move, d) {
		if(drawInv) {
			return
		}
		stepCount++;
		if(d) {
			place_w += move;
		} else { place_h += move;}
	}

	function animNextStep() {
		if(animationStep === 5) {
			animationStep = 0
		} else {animationStep++}
	}

	//Handle key press
	function onKeyDown(e) {
		if(locked_fl == 1 || locked_fl == 2) {
			switch(e.keyCode) {
				case 87: //W
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h - 96]) === JSON.stringify(elem);
					});
					dir = 5;
					if(!contains) {
						basicMove(64, false);
					}
					animNextStep();
					break;
				case 65: //A
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w - 32, middleOfScreen[1] - place_h - 32]) === JSON.stringify(elem);
					});
					dir = 7;
					if(!contains) {
						basicMove(64, true);
					}
					animNextStep();
					break;
				case 83: //S
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
					});
					dir = 4;
					if(!contains) {
						basicMove(-64, false);
					}
					animNextStep();
					break;
				case 68: //D
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w + 96, middleOfScreen[1] - place_h - 32]) === JSON.stringify(elem);
					});
					dir = 6;
					if(!contains) {
						if(!invisibleWall || place_w > -640) {
							basicMove(-64, true);
						}
						if(place_w === -704 && !closeToChest) {
							closeToChest = true
							locked.set(0)
						}
					}
					animNextStep();
					break;
				case 69: //E
					if(halfHeight - place_h - 32 - 4 == 508 && halfWidth - place_w - 48 == 2320 && chestOpened != 0 && dir == 5) {
						chestOpened = 0;
						dispatch('item')
						locked.set(0)
					}
				case 73: //I
					if(helper == 1 && !drawInv) {
						helper_inv_heal.set(1);
						drawInv = true;
						locked.set(0);
						helper++;
					}
					break;
				case 81: //Q
					if(!drawHMenu && helper == 2) {
						drawHMenu = true;
					}
					break;
				case 13: //Enter
					if(drawHMenu) {
						helper++;
						locked.set(0);
						invisibleWall = false;
					}
					break;
				default:
					return;
			}
			draw();
			if(stepCount === 5 && helper < 1) {
				locked.set(0);
				helper++;
			}
		}
	}
</script>
<svelte:window on:keydown|preventDefault={onKeyDown}></svelte:window>

<canvas bind:this={canvas} {width} {height}>
	<slot />
</canvas>

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
</style>