<script>
    import { getContext, onMount, createEventDispatcher } from 'svelte';
	import { collisions, battleground } from '../../../resources/js/collisions/collisions_ch2';

    const dispatch = createEventDispatcher();

	let collisionsPos = [];
	let battlegroundPos = [];
	const chest_a = [[912,1350,0],[900,4370,2]]

	var map;
	for (let i = 0; i < collisions.length; i += 81) {
			map = collisions.slice(i,81 + i);
			map.forEach((symbol, j) => {
				if(symbol != 0) {
					const coor = [j*64, (i/81)*64]
					collisionsPos.push(coor);
				}
			})

			map = battleground.slice(i,81 + i);
			map.forEach((symbol, j) => {
				if(symbol != 0) {
					const coor = [j*64, (i/81)*64]
					battlegroundPos.push(coor);
				}
			})
		}

    let width = 1920;
	let height = 1088;

	export let canvas, ctx, fightEvent,drawInv, helper, drawHMenu, showSide,pos_x,pos_y,chests, chapter,wear_t,showShader;

    var bg,foreground,chara,hair,wear,chief,rocks,op1,op2,op3,drawn,halfHeight,halfWidth,middleOfScreen, contains,chest_img;

	let moveLock = false;

    let dir = 2;
	let animationStep = 0;
	let place_w = -384;
	let place_h = -384;
    var { locked } = getContext('flags');
	var locked_fl;
    locked.subscribe((value) => {locked_fl = value});

    onMount(() => {
		const canvas = document.querySelector('canvas');
		bg = new Image();
		foreground = new Image();
		chest_img = new Image();
        chara = new Image();
		hair = new Image();
		wear = new Image();
        chief = new Image();
		rocks = new Image();
        op1 = new Image();
		op2 = new Image();
		op3 = new Image();
		ctx = canvas.getContext('2d');
		if (pos_x != 999) {
			place_w = pos_x;
			place_h = pos_y;
		}
		if(chests.length < 2) {
			chests.push([1,1])
		}
		if(helper < 22) {
			for(let i = 0; i < 9; i++) {
				collisionsPos.push([2240 + i * 64,4352])
			}
		}
        bg.onload = () => {
			halfWidth = canvas.width/2;
			halfHeight = canvas.height/2;
			middleOfScreen = [(halfWidth - 96), (halfHeight)];
			draw()
			showShader = false;
        };
		if(helper < 16) {
			for(let i = 0; i < 5; i++) {
				collisionsPos.push([4160,2560 + i*64])
			}
		}
		if(helper == 12) {
			for(let i = 0; i < 5; i++) {
				collisionsPos.push([2880 + i * 64,1920])
			}
		} else if(helper == 13) {
			for(let i = 0; i < 6; i++) {
				collisionsPos.push([3776 + i * 64,576])
			}
		}
		bg.src = 'road.png';
		chest_img.src = 'RGW_Chests.png';
		chara.src = 'player/player.png';
		hair.src = 'player/hair.png';
		if(wear_t < 2) {
			wear.src = 'player/underwear.png';
		} else if (wear_t < 4) {
			wear.src = 'player/armor1.png';
		} else {
			wear.src = 'player/armor2.png';
		}
		foreground.src = 'foreground_road.png'
		chief.src = 'chief.png';
		rocks.src = 'rocks_road.png';
		op1.src = 'woman_f1.png';
		op2.src = 'man_f2.png';
		op3.src = 'knight_f3.png';
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

	function drawChief(x,y) {
		ctx.drawImage(chief,
			0, 0,
			chief.width/4, chief.height,
			x, y,
			64,64);
	}

	function drawChests() {
		for (let i = 0; i < chest_a.length; i++) {
			let chest = chest_a[i];
			ctx.drawImage(chest_img,
				chests[1][i]*32, chest[2]*32,
				chest_img.width/12, chest_img.height/8,
				chest[0]+place_w, chest[1]+place_h,
				40, 40
			);
		}
	}

	const sleep = ms => new Promise(r => setTimeout(r, ms));

    function drawOpponent() {
		switch(fightEvent) {
			case 1:
				drawn = op1;
				break;
			case 2:
				drawn = op2;
				break;
			case 3:
				drawn = op3
				break;
        }
		ctx.drawImage(drawn,
			0, 0,
			drawn.width, drawn.height,
			1504+place_w, 856+place_h,
			60,60);
    }

    function draw() {
		ctx.clearRect(0,0,canvas.width,canvas.height);
		ctx.drawImage(bg, place_w, place_h);
		if(helper < 22) {
			ctx.drawImage(rocks, place_w, place_h);
		}
		drawChests();
        if(fightEvent > 0) {
            drawChief(1334+place_w,770+place_h)
            drawOpponent()
			drawCharacter(1152+place_w, 824+place_h);
        } else {
			drawChests()
			drawCharacter(middleOfScreen[0], middleOfScreen[1]);
		}
		ctx.drawImage(foreground,place_w,place_h);
	};

    function basicMove(move, d) {
		if(d) {
			place_w += move;
		} else { place_h += move;}
		switch(helper) {
			case 12:
				if ((middleOfScreen[0] - place_w >= 3744 && middleOfScreen[0] - place_w <= 4064) && middleOfScreen[1] - place_h == 608) {
					collisionsPos.splice(collisionsPos.length - 5,5)
					for(let i = 0; i < 6; i++) {
						collisionsPos.push([3776 + i * 64,576])
					}
					locked.set(0);
					helper++;
				}
				break;
			case 13:
				if ((middleOfScreen[0] - place_w >= 2848 && middleOfScreen[0] - place_w <= 3104) && middleOfScreen[1] - place_h == 1888) {
					locked.set(0);
					helper++;
					collisionsPos.splice(collisionsPos.length - 6,6)
					for(let i = 0; i < 5; i++) {
						collisionsPos.push([4128,2528 + i *64])
					}
				}
				break;
			case 24:
				if ((middleOfScreen[0] - place_w >= 2208 && middleOfScreen[0] - place_w <= 2720) && middleOfScreen[1] - place_h == 4320) {
					locked.set(0);
					helper++;
				}
				break;
			default:
				break;
		}
		if ((middleOfScreen[0] - place_w >= 3744 && middleOfScreen[0] - place_w <= 4064) && middleOfScreen[1] - place_h == 544) {
			(async() => {
				locked.set(7);
				await sleep(500);
				pos_x = -3072;
				pos_y = -2560;
				chapter = 1;
				await sleep(500);
				locked.set(1)
			})()
		} else if (middleOfScreen[0] - place_w == 4128 && (middleOfScreen[1] - place_h >= 2528 && middleOfScreen[1] - place_h <= 2784)) {
			(async() => {
				locked.set(7);
				await sleep(500);
				chapter = 3;
				pos_x = -64;
				pos_y = -1984;
				await sleep(500);
				locked.set(1)
			})()
		} else if ((middleOfScreen[0] - place_w >= 2208 && middleOfScreen[0] - place_w <= 2720) && middleOfScreen[1] - place_h == 4448){
			(async() => {
				locked.set(7);
				await sleep(500);
				chapter = 4;
				await sleep(500);
				locked.set(0)
			})()
		}
		onBattleground();
    }

    function animNextStep() {
		if(animationStep == 5) {
			animationStep = 0
		} else {animationStep++}
	}

	function checkInteractable() {
		if (dir == 5) {
			if((middleOfScreen[0] - place_w == 1952 || middleOfScreen[0] - place_w == 2016) && middleOfScreen[1] - place_h == 864) {
				dispatch('heal', {bench: true});
				return;
			}
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h - 26 == chest_a[i][1] && halfWidth - place_w - 48 == chest_a[i][0] && chests[1][i] == 1) {
					dispatch('item')
					chests[1][i] = 0;
					return
				}
			}
		}
		else if (dir == 7) {
			for(let i = 0; i < chest_a.length; i++) {
				if(halfHeight - place_h + 50 == chest_a[i][1] && halfWidth - place_w - 124 == chest_a[i][0] && chests[1][i] == 1) {
					dispatch('item')
					chests[1][i] = 0;
					return;
				}
			}
		}
	}

	function onBattleground() {
		contains = battlegroundPos.some(elem => {
			return JSON.stringify([middleOfScreen[0] - place_w + 32, middleOfScreen[1] - place_h + 32]) === JSON.stringify(elem);
		});
		if(contains) {
			const n = Math.random()
			if(n*100 < 15) {
				if(helper == 21) {
					dispatch('fight', {"fm": 0});
				} else {
					dispatch('fight', {"fm": 1});
					moveLock = true;
				}
			}
		}
	}

	$: if(locked_fl == 4) {
		moveLock = false;
	}

    function onKeyDown(e) {
		if((locked_fl === 1 || locked_fl === 2) && !drawInv && !drawHMenu && !moveLock) {
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
						basicMove(64, true)
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

	$: if(fightEvent != 1 && middleOfScreen != undefined) {draw()}
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