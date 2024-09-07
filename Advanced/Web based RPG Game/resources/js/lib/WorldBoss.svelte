<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

	let collisionsPos = [];

    let width = 1920;
	let height = 1088;

	export let canvas, ctx, drawInv, drawHMenu, showSide,helper, pos_x, pos_y,wear_t;
	var bg,chara,hair,wear,wizard,halfHeight,halfWidth,middleOfScreen, contains,chest_img, locked_fl;

    let dir = 4;
	let animationStep = 0;
	let place_w = -1760;
	let place_h = -400;
	let moveLock = false;

    var { locked } = getContext('flags');
    locked.subscribe((value) => {locked_fl = value});

	onMount(async() => {
		ctx = canvas.getContext('2d');
		bg = new Image();
		wizard = new Image();
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
		for(let i = 0; i < 6; i++) {
			collisionsPos.push([2496 + i*64,816])
			collisionsPos.push([2432,880 + i*64])
			collisionsPos.push([2816,880 + i*64])
		}
		bg.src = 'world_boss.png';
		wizard.src = 'wizard.png';
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

	function drawWizard(y) {
		ctx.drawImage(wizard,
			0, 0,
			wizard.width/4,wizard.height, 
			1400,y,
			64,64)
	}

	const sleep = ms => new Promise(r => setTimeout(r, ms));

	function draw() {
		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.drawImage(bg, place_w, place_h);
		drawCharacter(middleOfScreen[0], middleOfScreen[1]);
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
		if(middleOfScreen[1] - place_h == 1072 && helper == 28) {
			moveLock = true;
			dispatch('fight', {"fm": 0});
		} else if (middleOfScreen[1] - place_h == 1328 && helper == 29) {
			moveLock = true;
			dispatch('fight', {"fm": 0});
		}
	}

	$: if(locked_fl == 4) {
		moveLock = false;
	}

	//Handle key press
    function onKeyDown(e) {
		if((locked_fl === 1 || locked_fl === 2) && !drawInv && !drawHMenu && helper != 31 && !moveLock) {
			switch(e.keyCode) {
				case 87: //W
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w, middleOfScreen[1] - place_h-64]) === JSON.stringify(elem);
					});
					dir = 5;
					if(!contains) {
						basicMove(64, false);
					}
					animNextStep()
					break;
				case 65: //A
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w -64 , middleOfScreen[1] - place_h]) === JSON.stringify(elem);
					});
					dir = 7;
					if(!contains) {
						basicMove(64, true);
					}
					animNextStep()
					break;
				case 83: //S
					contains = collisionsPos.some(elem =>{
						return JSON.stringify([middleOfScreen[0] - place_w, middleOfScreen[1] - place_h +64]) === JSON.stringify(elem);
					});
					dir = 4;
					if(!contains) {
						basicMove(-64, false);
					}
					animNextStep()
					break;
				case 68: //D
					contains = collisionsPos.some(elem => {
						return JSON.stringify([middleOfScreen[0] - place_w+64, middleOfScreen[1] - place_h]) === JSON.stringify(elem);
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

	//Draw ending animation
	$: if(helper == 31) {
		(async() => {
			drawWizard(1020);
			await sleep(500)
			draw()
			drawWizard(956);
			await sleep(500)
			draw()
			drawWizard(896);
			await sleep(500)
			draw()
			drawWizard(832);
			await sleep(2000);
            locked.set(7);
		})()
	} 
</script>
<svelte:window on:keydown={onKeyDown}></svelte:window>
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