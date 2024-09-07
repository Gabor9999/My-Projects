<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';
	import { collisions } from '../../../resources/js/collisions/collisions_road_end'

    const dispatch = createEventDispatcher();

	let collisionsPos = [];

	var map;
	for (let i = 0; i < collisions.length; i += 38) {
		map = collisions.slice(i,38 + i);
		map.forEach((symbol, j) => {
			if(symbol != 0) {
				const coor = [j*64, (i/38)*64]
				collisionsPos.push(coor);
			}
		})
	}

    let width = 1920;
	let height = 1088;

	export let canvas, ctx, drawInv, drawHMenu, showSide, pos_x, pos_y, helper, chapter,wear_t,chiefMenu;
	var bg,chief,chara,hair,wear,halfHeight,halfWidth,middleOfScreen, contains,chest_img, locked_fl;

    let dir = 4;
	let animationStep = 0;
	let place_w = -256;
	let place_h = -64;
	let moveLock = false;

    var { locked } = getContext('flags');
    locked.subscribe((value) => {locked_fl = value});

	onMount(async() => {
		ctx = canvas.getContext('2d');
		bg = new Image();
		chief = new Image();
		chara = new Image();
		hair = new Image();
		wear = new Image();
		chest_img = new Image();
        bg.onload = () => {
			halfHeight = canvas.height/2
			halfWidth = canvas.width/2;
			middleOfScreen = [(halfWidth - 96), halfHeight];
			draw()
        };
		bg.src = 'road_end.png';
		chief.src = 'chief.png'
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png';
		if(wear_t < 2) {
			wear.src = 'player/underwear.png';
		} else if (wear_t < 4) {
			wear.src = 'player/armor1.png';
		} else {
			wear.src = 'player/armor2.png';
		}
		chest_img.src = 'RGW_Chests.png';
	});

	function drawCharacter(x,y) {
		ctx.drawImage(chara,
			animationStep * 64, dir * 64,
			64,64, 
			x, y,
			128,128)
		ctx.drawImage(hair,
			animationStep * 64,dir * 64,64,64,
			x + 8, y + 8, 112,112
		)
		ctx.drawImage(wear,
			animationStep * 64, dir * 64,64,64, 
			x, y,
			128,128)
	}

	function drawChief() {
		ctx.drawImage(chief,
			0, 0,
			chief.width/4, chief.height,
			896+place_w, 4280+place_h,
			64,64);
	}

	const sleep = ms => new Promise(r => setTimeout(r, ms));

	function draw() {
		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.drawImage(bg, place_w, place_h);
		drawCharacter(middleOfScreen[0], middleOfScreen[1]);
		drawChief()
	};

	function animNextStep() {
		if(animationStep == 5) {
			animationStep = 0
		} else {animationStep++}
	}

	function basicMove(move, d) {
		if(d) {
			place_w += move;
		} else { place_h += move;}
		if(middleOfScreen[1] - place_h == 1312 || middleOfScreen[1] - place_h == 2336 || middleOfScreen[1] - place_h == 3360) {
			moveLock = true;
			dispatch('fight', {"fm": 0});
		} else if (middleOfScreen[1] - place_h == 4448) {
			(async() => {
				locked.set(7);
				await sleep(500);
				chapter = 5;
				await sleep(500);
				locked.set(0)
			})()
		}
	}

	$: if(locked_fl == 4) {
		moveLock = false;
	}

	//Check if an object the player facing can be interacted with
    function checkInteractable() {
		if (dir == 7) {
			if((middleOfScreen[1] - place_h == 4128 || middleOfScreen[1] - place_h == 4192) && middleOfScreen[0] - place_w == 928) {
				dispatch('heal', {bench: true});
				return;
			} else if(middleOfScreen[1] - place_h == 4256 && middleOfScreen[0] - place_w == 928) {
				chiefMenu = true
			}
		}
	}

	//Handle key press
    function onKeyDown(e) {
		if((locked_fl === 1 || locked_fl === 2) && !drawInv && !drawHMenu && !chiefMenu && !moveLock) {
			switch(e.keyCode) {
				case 87: //W
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h - 32]) === JSON.stringify(elem);
					});
					dir = 5;
					if(!contains) {
						basicMove(64, false);
					}
					animNextStep()
					break;
				case 65: //A
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w - 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
					});
					dir = 7;
					if(!contains) {
						basicMove(64, true);
					}
					animNextStep()
					break;
				case 83: //S
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 96]) === JSON.stringify(elem);
					});
					dir = 4;
					if(!contains) {
						basicMove(-64, false);
					}
					animNextStep()
					break;
				case 68: //D
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w + 96, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
					});
					dir = 6;
					if(!contains) {
						basicMove(-64, true);
					}
					animNextStep()
					break;
				case 69: //E
					checkInteractable();
					break;
				case 73: //I
					drawInv = true;
					break;
				case 81: //Q
					drawHMenu = true;
					break;
				case 89: //Y
					showSide = !showSide;
					break;
				case 27: //Esc
					pos_x = place_w;
					pos_y = place_h;
					locked.set(6)
					break;
				default:
					return;
			}
			draw();
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